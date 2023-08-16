package com.example.plugins.routes.category.post

import com.example.dao.CategoryDao
import com.example.dao.dao
import com.example.model.Category
import com.example.model.SubCategory
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.storage.BlobId
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.StorageOptions
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.FileInputStream


fun Route.postCategory() {
    post("/AddCategory") {
        val multipart = call.receiveMultipart()
        var name: String? = null
        var imageUrl: String? = null
        var objectName: String? = null

        multipart.forEachPart { part ->
            when (part) {
                is PartData.FormItem -> {
                    when (part.name) {
                        "name" -> name = part.value
                        "objectName" -> objectName = part.value
                    }
                }
                is PartData.FileItem -> {
                    if (part.name == "image") {
                        val fileBytes = part.streamProvider().readBytes()
                        if (fileBytes.isEmpty()) {
                            call.respond(HttpStatusCode.BadRequest, "Image is required.")
                            return@forEachPart
                        }
                        try {
                            val creds = withContext(Dispatchers.IO) {
                                GoogleCredentials.fromStream(FileInputStream("src/main/resources/verdant-option-390012-c9c70b72ef8f.json"))
                            }
                            val storage = StorageOptions.newBuilder().setCredentials(creds).build().service
                            val bucketName = "qrlist"

                            val blobName = objectName ?: part.originalFileName ?: "unknown"
                            val blobId = BlobId.of(bucketName, blobName)
                            val blobInfo = BlobInfo.newBuilder(blobId).setContentType(part.contentType?.toString() ?: "image/jpeg").build()
                            storage.create(blobInfo, fileBytes)

                            imageUrl = "https://storage.googleapis.com/$bucketName/$blobName"

                        } catch (e: Exception) {
                            call.respond(HttpStatusCode.InternalServerError, "Something went wrong while uploading the file.")
                            return@forEachPart
                        } catch (e: IllegalArgumentException) {
                            call.respond(HttpStatusCode.BadRequest, e.message ?: "Invalid input.")
                            return@forEachPart
                        }
                    }
                }
                else -> part.dispose()
            }
            part.dispose()
        }

        if (imageUrl.isNullOrEmpty()) {
            call.respond(HttpStatusCode.BadRequest, "Image URL is missing.")
            return@post
        }

        val category = Category(
            id = 0,
            name = name ?: throw IllegalArgumentException("Name is missing."),
            imageUrl = imageUrl ?: throw IllegalArgumentException("Image Url Is missing"),
            objectName = objectName ?: "",
            subCategories = emptyList()
        )

        dao.addCategory(category)
        call.respond(HttpStatusCode.Created, "Category added successfully.")
    }
}



