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
        var name : String? = null
        var email : String? = null
        var firebaseUID : String? = null

        multiPart.forEachPart {part ->
            when(part) {
                is PartData.FormItem ->  {
                    when(part.name) {
                        "name" -> name = part.value
                        "email" -> email = part.value
                        "firebaseUID" -> firebaseUID = part.value
                    }
                }
                else -> return@forEachPart
            }
            part.dispose
        }

        val restaurant = Restaurant(
            name = name ?: "",
            email = email ?: "",
            firebaseUID = firebaseUID ?: ""
        )

        dao.addRestaurant(restaurant)

        call.respond(HttpStatusCode.OK,BasicApiResponse(true,"Restaurant menu is created successfully!"))

    }
}
