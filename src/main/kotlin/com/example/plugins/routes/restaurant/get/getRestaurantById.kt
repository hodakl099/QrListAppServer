package com.example.plugins.routes.restaurant.get

import com.example.dao.dao
import com.example.util.BasicApiResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getRestaurantById() {

    get("/getRestaurant/{restaurantId}") {
        val id = call.parameters["restaurantId"]?.toIntOrNull()
        if (id != null) {
            val categories = dao.getRestaurantCategories(id)
            call.respond(HttpStatusCode.OK,categories)
        } else {
            call.respond(HttpStatusCode.BadRequest,BasicApiResponse(false,"restaurant ID doesn't exist"))
        }
    }
}