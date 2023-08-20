package com.example.plugins.routes.subcategory.post

import com.example.dao.dao
import com.example.model.Category
import com.example.model.SubCategory
import com.example.plugins.routes.category.put.uploadFile
import com.example.util.BasicApiResponse
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.postSubCategoryRoute() {
    post("/AddSubCategory/{categoryId}") {

        val categoryId = call.parameters["categoryId"]?.toIntOrNull()
        val multipart = call.receiveMultipart()
        var name: String? = null
        var imageUrl: String? = null
        var objectName: String? = null

        if (categoryId == null) {
            call.respond(HttpStatusCode.BadRequest, BasicApiResponse(false,"Category might be deleted or invalid!"))
        }


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
                            imageUrl = uploadFile(part)
                        } catch (e: Exception) {
                            call.respond(HttpStatusCode.InternalServerError, "Something went wrong while uploading the file.")
                            return@forEachPart
                        }
                    }
                }
                else -> part.dispose()
            }
            part.dispose()
        }

        val parentCategory = dao.getCategory(categoryId!!)
        if (parentCategory == null) {
            call.respond(HttpStatusCode.BadRequest,BasicApiResponse(false,"Category might be deleted or its not valid!")  )
            return@post
        }

        if (imageUrl.isNullOrEmpty()) {
            call.respond(HttpStatusCode.BadRequest, BasicApiResponse(false,"Image URL is missing."))
            return@post
        }

        val subCategory = SubCategory(
            id = 0,
            name = name ?: throw IllegalArgumentException("Name is missing."),
            imageUrl = imageUrl ?: "",
            objectName = objectName ?: "",
            categoryId = categoryId ?: 0
        )

        dao.addSubCategoryToCategory(categoryId, subCategory)
        call.respond(HttpStatusCode.Created, BasicApiResponse(false,"SubCategory added successfully."))
    }
}


