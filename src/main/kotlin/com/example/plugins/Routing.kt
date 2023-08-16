package com.example.plugins

import com.example.plugins.routes.category.createCategoryRoute
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        route("/QrList") {
            createCategoryRoute()
        }
    }
}
