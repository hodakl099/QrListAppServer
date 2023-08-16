package com.example.plugins.routes.category

import com.example.plugins.routes.category.post.postCategory
import io.ktor.server.routing.*

fun Route.createCategoryRoute() {

    route("/category") {
        postCategory()
    }

}