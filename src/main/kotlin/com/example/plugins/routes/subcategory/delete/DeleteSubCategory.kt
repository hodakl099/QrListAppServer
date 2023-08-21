package com.example.plugins.routes.subcategory.delete

import com.example.dao.dao
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.deleteSubCategoryRoute() {

   delete("deleteSubCategory/{id}") {
       val id = call.parameters["id"]?.toIntOrNull()
       if (id != null) {
           val isDeleted = dao.deleteSubCategory(id)

           if (isDeleted) {
               call.respond(HttpStatusCode.OK,"Subcategory is deleted successfully!")
           } else {
               call.respond(HttpStatusCode.BadRequest,"missing or invalid sub category!")
           }
       } else {
           call.respond(HttpStatusCode.NotFound, "Missing sub category id!")
       }
   }
}