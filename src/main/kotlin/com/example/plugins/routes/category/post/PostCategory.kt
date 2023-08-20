package com.example.plugins.routes.category.post

import com.example.dao.dao
import com.example.model.Category
import com.example.plugins.routes.category.put.uploadFile
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
import java.io.FileInputStream


fun Route.postCategoryRoute() {
    post("/AddCategory") {
        val multipart = call.receiveMultipart()
        var name: String? = null
        var objectName: String? = null
        var imageUrl: String? = null

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
                            call.respond(HttpStatusCode.BadRequest, BasicApiResponse(false,"Image is required."))
                            return@forEachPart
                        }
                        try {
                            imageUrl = uploadFile(part)
                        } catch (e: Exception) {
                            print("Logeffesfksmd dsfijsdf " + e.message)
                            call.respond(HttpStatusCode.InternalServerError, BasicApiResponse(true,"Category is Added Successfully"))
                            return@forEachPart
                        }
                    }
                }
                else -> part.dispose()
            }
            part.dispose()
        }

        if (imageUrl.isNullOrEmpty()) {
            call.respond(HttpStatusCode.BadRequest, BasicApiResponse(true,"Image URL is missing."))
            return@post
        }

        val category = Category(
            id = 0,
            name = name ?: throw IllegalArgumentException("Name is missing."),
            imageUrl = imageUrl ?: "",
            objectName = objectName ?: "",
            subCategories = emptyList()
        )

        dao.addCategory(category)
        call.respond(HttpStatusCode.Created, BasicApiResponse(true,"Category."))
    }
}



