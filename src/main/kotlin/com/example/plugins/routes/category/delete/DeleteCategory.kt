package com.example.plugins.routes.category.delete

import com.example.dao.dao
import com.example.util.BasicApiResponse
import com.google.cloud.storage.BlobId
import com.google.cloud.storage.StorageOptions
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.deleteCategoryRoute() {

   delete("deleteCategory/{id}") {

       val storage = StorageOptions.getDefaultInstance().service

       val id = call.parameters["id"]?.toIntOrNull()
       if (id != null) {
           val subcategories = dao.getSubCategoriesForCategory(id)
           val category = dao.getCategory(id)
           val isDeleted = dao.deleteCategory(id)

           category?.let {curCategory ->
               val blobId = BlobId.of("qrlist",curCategory.objectName)
               storage.delete(blobId)
           }


           if (isDeleted) {
               dao.deleteSubCategoriesForCategory(id)
               // Delete associated images from Google Cloud Storage
               if (subcategories.isNotEmpty()) {
                   subcategories.forEach { subcategory ->
                       val blobId = BlobId.of("qrlist", subcategory.objectName)
                       storage.delete(blobId)
                   }
               }

               call.respond(HttpStatusCode.OK, BasicApiResponse(true,"Category is deleted successfully!"))
           } else {
               call.respond(HttpStatusCode.BadRequest,BasicApiResponse(false,"Something went wrong!"))
           }
       } else {
           call.respond(HttpStatusCode.BadRequest,BasicApiResponse(false,"Invalid or missing id!"))
       }
   }
}