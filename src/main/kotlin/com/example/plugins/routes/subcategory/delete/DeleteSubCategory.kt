package com.example.plugins.routes.subcategory.delete

import com.example.dao.dao
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.deleteSubCategoryRoute() {

   delete("deleteCategory/{id}") {

        }
}