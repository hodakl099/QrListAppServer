package com.example.plugins.routes.category.put

import com.example.dao.dao
import com.example.model.Category
import com.example.util.BasicApiResponse
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
import java.io.ByteArrayInputStream
import java.io.FileInputStream

fun Route.updateCategoryRoute() {
    put("/updateCategory/{id}") {
        val id = call.parameters["id"]?.toIntOrNull()
        println(id)
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest, BasicApiResponse(false,"Invalid or missing ID."))
            return@put
        }

        val multiPart = call.receiveMultipart()


        var name: String? = null
        var imageUrl: String? = null
        var objectName: String? = null
        var imageUpdated = false

        multiPart.forEachPart { part ->
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
                        try {
                            imageUrl = uploadFile(part.originalFileName ?: "default.jpg", fileBytes)
                            objectName = part.originalFileName
                            imageUpdated = true
                        } catch (e: Exception) {
                            call.respond(HttpStatusCode.InternalServerError, "Failed to upload the image.")
                            return@forEachPart
                        }
                    }
                }
                else -> return@forEachPart
            }
            part.dispose()
        }

        // If no new image was uploaded, fetch old imageUrl and objectName from the database
        if (!imageUpdated) {
            val currentCategory = dao.getCategory(id)
            imageUrl = currentCategory?.imageUrl
            objectName = currentCategory?.objectName
        }

        // Construct the updated category object.
        val updatedCategory = Category(
            id = id,
            name = name ?: "",
            imageUrl = imageUrl ?: "",
            objectName = objectName ?: "",
            subCategories = emptyList(), // Modify this as per your requirements,
            restaurantId = 0
        )

        // Update the category in the database.
        val result = dao.updateCategory(id, updatedCategory)

        if (result) {
            call.respond(HttpStatusCode.OK, BasicApiResponse(true,"Category updated successfully."))
        } else {
            call.respond(HttpStatusCode.NotFound, BasicApiResponse(false,"Category with ID: $id not found."))
        }
    }
}


suspend fun uploadFile(fileName: String, fileBytes: ByteArray): String? {
    val creds = withContext(Dispatchers.IO) {
        GoogleCredentials.fromStream(FileInputStream("src/main/resources/verdant-option-390012-c9c70b72ef8f.json"))
    }
    val storage = StorageOptions.newBuilder().setCredentials(creds).build().service

    val bucketName = "qrlist"
    val blobId = BlobId.of(bucketName, fileName)
    val blobInfo = BlobInfo.newBuilder(blobId).setContentType("image/jpeg").build() // Adjust content type accordingly

    val blob = storage.create(blobInfo, fileBytes)

    if (blob == null || blob.size != fileBytes.size.toLong()) {
        throw Exception("Failed to upload blob correctly to Google Cloud Storage.")
    }

    return blob.mediaLink
}
