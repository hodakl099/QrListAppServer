package com.example.plugins

import com.example.plugins.routes.category.createCategoryRoute
import com.example.plugins.routes.restaurant.createRestaurantRoute
import com.example.plugins.routes.subcategory.createSubCategoryRoute
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        route("/QrList") {
            createCategoryRoute()
            createSubCategoryRoute()
            createRestaurantRoute()
        }
    }
}
