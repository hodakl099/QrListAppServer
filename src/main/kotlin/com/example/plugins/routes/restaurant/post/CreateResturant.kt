package com.example.plugins.routes.restaurant.post

import com.example.dao.dao
import com.example.model.Restaurant
import com.example.util.BasicApiResponse
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.createNewRestaurant() {

    post("/add") {
        val multiPart = call.receiveMultipart()
        var id : Int? = null
        var name : String? = null
        var email : String? = null

        multiPart.forEachPart {part ->
            when(part) {
                is PartData.FormItem ->  {
                    when(part.name) {
                        "id" -> id = part.value.toIntOrNull()
                        "name" -> name = part.value
                        "email" -> email = part.value
                    }
                }
                else -> return@forEachPart
            }
            part.dispose
        }
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest,BasicApiResponse(false,"Invalid or missing ID.."))
            return@post
        }

        val restaurant = Restaurant(
            id = id  ?: return@post,
            name = name ?: "",
            email = email ?: "",
        )

        dao.addRestaurant(restaurant)

        call.respond(HttpStatusCode.OK,BasicApiResponse(true,"Restaurant menu is created successfully!"))

    }
}
