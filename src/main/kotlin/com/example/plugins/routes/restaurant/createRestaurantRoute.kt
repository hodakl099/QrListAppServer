package com.example.plugins.routes.restaurant

import com.example.plugins.routes.restaurant.get.getRestaurantById
import com.example.plugins.routes.restaurant.post.createNewRestaurant
import io.ktor.server.routing.*


fun Route.createRestaurantRoute() {

    route("restaurant") {
        createNewRestaurant()
        getRestaurantById()
    }


}