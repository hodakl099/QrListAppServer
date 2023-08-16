package com.example.plugins.routes.subcategory.get

import com.example.dao.dao
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getSubCategoryRoute() {
    get("getSubCategory/{id}") {
        val id = call.parameters["id"]?.toIntOrNull()
        if (id != null) {
            val subCategory = dao.getSubCategory(id)
            call.respond(HttpStatusCode.OK,subCategory!!)
        } else {
            call.respond(HttpStatusCode.BadRequest,"sub category doesn't exist")
        }
    }
}