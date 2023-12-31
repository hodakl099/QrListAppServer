package com.example.plugins.routes.category

import com.example.plugins.routes.category.delete.deleteCategoryRoute
import com.example.plugins.routes.category.get.getAllCategoriesRoute
import com.example.plugins.routes.category.get.getCategoryRoute
import com.example.plugins.routes.category.get.getSubCategoriesForCategoryRoute
import com.example.plugins.routes.category.post.postCategoryRoute
import com.example.plugins.routes.category.put.updateCategoryRoute
import io.ktor.server.routing.*

fun Route.createCategoryRoute() {
    route("/category") {
        postCategoryRoute()
        getCategoryRoute()
        getSubCategoriesForCategoryRoute()
        getAllCategoriesRoute()
        deleteCategoryRoute()
        updateCategoryRoute()
    }
}