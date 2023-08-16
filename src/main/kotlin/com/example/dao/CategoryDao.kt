package com.example.dao

import com.example.model.Category
import com.example.model.SubCategory

interface CategoryDao {


    suspend fun addCategory(category : Category)

    suspend fun deleteCategory(id : Int) : Boolean

    suspend fun getAllCategories() : List<Category>

    suspend fun getCategory(id: Int) : Category?

    suspend fun updateCategory(id : Int, category: Category) : Boolean



    suspend fun addSubCategoryToCategory(categoryId: Int, subCategory: SubCategory): Boolean
    suspend fun updateSubCategory(subCategoryId: Int, subCategory: SubCategory): Boolean
    suspend fun deleteSubCategory(subCategoryId: Int): Boolean

    suspend fun getSubCategoriesForCategory(categoryId: Int): List<SubCategory>
    suspend fun getSubCategory(id: Int): SubCategory?



}