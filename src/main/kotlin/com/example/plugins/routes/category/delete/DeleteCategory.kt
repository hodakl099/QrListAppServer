package com.example.plugins.routes.category.delete

import com.example.dao.dao
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.deleteCategoryRoute() {

   delete("deleteCategory/{id}") {
       val id = call.parameters["id"]?.toIntOrNull()
       if (id != null) {
           val isDeleted = dao.deleteCategory(id)
           if (isDeleted) {
               call.respond(HttpStatusCode.OK,"Category is deleted successfully!")
           } else {
               call.respond(HttpStatusCode.BadRequest,"Something went wrong!")
           }
       } else {
           call.respond(HttpStatusCode.BadRequest,"Invalid or missing id!")
       }
   }
}