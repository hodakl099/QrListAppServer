package com.example.plugins.routes.category.get

import com.example.dao.dao
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getCategoryRoute() {

    get("getCategory/{id}") {
        val id = call.parameters["id"]?.toIntOrNull()
        if (id != null) {
            val category = dao.getCategory(id)
            if (category != null) {
                call.respond(HttpStatusCode.OK,category)
            } else {
                call.respond(HttpStatusCode.BadRequest,"Invalid or missing ID.")
            }
        }
    }
}