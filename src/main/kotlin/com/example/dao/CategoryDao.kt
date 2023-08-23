package com.example.dao

import com.example.model.Category
import com.example.model.Restaurant
import com.example.model.SubCategory

interface CategoryDao {


    suspend fun addCategory(category : Category)

    suspend fun deleteCategory(id : Int) : Boolean

    suspend fun getAllCategories() : List<Category>

    suspend fun getCategory(id: Int) : Category?

    suspend fun updateCategory(id : Int, category: Category) : Boolean
    suspend fun deleteSubCategoriesForCategory(categoryId: Int) : Boolean



    suspend fun addSubCategoryToCategory(categoryId: Int, subCategory: SubCategory): Boolean
    suspend fun updateSubCategory(subCategoryId: Int, subCategory: SubCategory): Boolean
    suspend fun deleteSubCategory(subCategoryId: Int): Boolean

    suspend fun getSubCategoriesForCategory(categoryId: Int): List<SubCategory>?
    suspend fun getSubCategory(id: Int): SubCategory?

    // Onboarding to setup resturant name and user UID when its first time launched

    suspend fun addRestaurant(restaurant: Restaurant)

    suspend fun getRestaurantCategories(restaurantId: Int):List<Category>



}