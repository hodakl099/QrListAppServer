package com.example.plugins.routes.category.get

import com.example.dao.dao
import com.example.util.BasicApiResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getAllCategoriesRoute() {

    get("getAllCategories") {
        val categories = dao.getAllCategories()
        if (categories.isNotEmpty()) {
            call.respond(HttpStatusCode.OK,categories)
        }
        else {
            call.respond(HttpStatusCode.BadRequest, BasicApiResponse(false,"There is no categories available"))
        }
    }
}