package com.example.plugins.routes.subcategory.put

import com.example.dao.dao
import com.example.model.Category
import com.example.model.SubCategory
import com.example.plugins.routes.category.put.uploadFile
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

fun Route.updateSubCategoryRoute() {
    put("/updateSubCategory/{id}") {
        val id = call.parameters["id"]?.toIntOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest, "Invalid or missing ID.")
            return@put
        }

        val multiPart = call.receiveMultipart()

        var name: String? = null
        var imageUrl: String? = null
        var objectName: String? = null
        var categoryId: Int? = null
        var imageUpdated = false
        var price: Int? = null

        multiPart.forEachPart { part ->
            when (part) {
                is PartData.FormItem -> {
                    when (part.name) {
                        "name" -> name = part.value
                        "objectName" -> objectName = part.value
                        "categoryId" -> categoryId = part.value.toIntOrNull()
                        "price" -> price = part.value.toIntOrNull()
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

                else -> part.dispose()
            }
        }

        // If no new image was uploaded, fetch old imageUrl and objectName from the database
        if (!imageUpdated) {
            val currentSubCategory = dao.getSubCategory(id)
            imageUrl = currentSubCategory?.imageUrl
            objectName = currentSubCategory?.objectName
        }

        // If category is not updated, fetch old categoryId from the database
        if (categoryId == null) {
            val currentSubCategory = dao.getSubCategory(id)
            categoryId = currentSubCategory?.categoryId
        }

        // Construct the updated sub-category object.
        val updatedSubCategory = SubCategory(
            id = id,
            name = name ?: "",
            imageUrl = imageUrl ?: "",
            objectName = objectName ?: "",
            categoryId = categoryId ?: throw IllegalArgumentException("Category ID is missing or invalid."),
            price = price ?: 0
        )

        // Update the sub-category in the database.
        val result = dao.updateSubCategory(id, updatedSubCategory)

        if (result) {
            call.respond(HttpStatusCode.OK, "SubCategory updated successfully.")
        } else {
            call.respond(HttpStatusCode.NotFound, "SubCategory with ID: $id not found.")
        }
    }
}




