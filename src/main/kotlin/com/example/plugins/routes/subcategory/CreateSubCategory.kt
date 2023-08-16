package com.example.plugins.routes.subcategory

import com.example.plugins.routes.subcategory.delete.deleteSubCategoryRoute
import com.example.plugins.routes.subcategory.get.getSubCategoryRoute
import com.example.plugins.routes.subcategory.post.postSubCategoryRoute
import com.example.plugins.routes.subcategory.put.updateSubCategoryRoute
import io.ktor.server.routing.*


fun Route.createSubCategoryRoute() {
    route("/subcategory") {
        postSubCategoryRoute()
        updateSubCategoryRoute()
        getSubCategoryRoute()
        deleteSubCategoryRoute()
    }
}