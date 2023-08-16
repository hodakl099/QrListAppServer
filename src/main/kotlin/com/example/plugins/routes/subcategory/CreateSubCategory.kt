package com.example.plugins.routes.subcategory

import com.example.plugins.routes.subcategory.post.postSubCategoryRoute
import io.ktor.server.routing.*


fun Route.createSubCategoryRoute() {
    route("/subcategory") {
        postSubCategoryRoute()
    }
}