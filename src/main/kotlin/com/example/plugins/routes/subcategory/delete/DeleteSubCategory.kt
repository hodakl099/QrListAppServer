package com.example.plugins.routes.subcategory.delete

import com.example.dao.dao
import com.google.cloud.storage.BlobId
import com.google.cloud.storage.StorageOptions
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.deleteSubCategoryRoute() {

   delete("deleteSubCategory/{id}") {
       val id = call.parameters["id"]?.toIntOrNull()
       val storage = StorageOptions.getDefaultInstance().service
       if (id != null) {
           val subCategory = dao.getSubCategory(id)
           subCategory?.let {curCategory ->
               val blobId = BlobId.of("qrlist",curCategory.objectName)
               storage.delete(blobId)
           }

           val isDeleted = dao.deleteSubCategory(id)
           if (isDeleted) {
               call.respond(HttpStatusCode.OK,"Subcategory is deleted successfully! ")
           } else {
               call.respond(HttpStatusCode.BadRequest,"missing or invalid sub category!")
           }
       } else {
           call.respond(HttpStatusCode.NotFound, "Missing sub category id!")
       }
   }
}