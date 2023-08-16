package com.example.dao

import com.example.model.Categories
import com.example.model.Category
import com.example.model.SubCategories
import com.example.model.SubCategory
import com.example.util.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class CategoryDaoImpl : CategoryDao {


    private fun toCategory(row: ResultRow): Category =
        Category(
            id = row[Categories.id],
            name = row[Categories.name],
            imageUrl = row[Categories.imageUrl],
            objectName = row[Categories.objectName],
            subCategories = listOf() // Empty list here, can be populated separately
        )


    private fun toSubCategory(row: ResultRow): SubCategory =
        SubCategory(
            id = row[SubCategories.id],
            name = row[SubCategories.name],
            imageUrl = row[SubCategories.imageUrl],
            objectName = row[SubCategories.objectName],
            categoryId = row[SubCategories.categoryId]
        )
    override suspend fun addCategory(category: Category) = dbQuery {
        Categories.insert {
            it[name] = category.name
            it[imageUrl] = category.imageUrl
            it[objectName] = category.objectName
        }
        Unit
    }

    override suspend fun deleteCategory(id: Int): Boolean = dbQuery {
        val deleted = Categories.deleteWhere { Categories.id eq Categories.id }
        deleted > 0
    }

    override suspend fun getAllCategories(): List<Category> = dbQuery {
        Categories.selectAll().map { categoryRow ->
            val category = toCategory(categoryRow)
            val subCategoriesForThisCategory = SubCategories.select { SubCategories.categoryId eq category.id }
                .map { toSubCategory(it) }
            category.copy(subCategories = subCategoriesForThisCategory)
        }
    }

    override suspend fun getCategory(id: Int): Category?  = dbQuery {
        Categories.select { Categories.id eq id }
            .mapNotNull { toCategory(it) }
            .singleOrNull()
    }
    override suspend fun updateCategory(id: Int, category: Category): Boolean = dbQuery {
        val updated = Categories.update({ Categories.id eq id }) {
            it[name] = category.name
            it[imageUrl] = category.imageUrl
            it[objectName] = category.objectName
        }
        updated > 0
    }

    override suspend fun addSubCategoryToCategory(catId: Int, subCategory: SubCategory): Boolean = dbQuery {
        SubCategories.insert {
            it[name] = subCategory.name
            it[imageUrl] = subCategory.imageUrl
            it[objectName] = subCategory.objectName
            it[categoryId] = catId
        }
        true
    }
    override suspend fun updateSubCategory(subCategoryId: Int, subCategory: SubCategory): Boolean = dbQuery {
        val updated = SubCategories.update({ SubCategories.id eq subCategoryId }) {
            it[name] = subCategory.name
            it[imageUrl] = subCategory.imageUrl
            it[objectName] = subCategory.objectName
        }
        updated > 0
    }

    override suspend fun deleteSubCategory(subCategoryId: Int): Boolean = dbQuery {
        val deleted = SubCategories.deleteWhere { SubCategories.id eq subCategoryId }
        deleted > 0
    }
    override suspend fun getSubCategoriesForCategory(categoryId: Int): List<SubCategory> = dbQuery {
        SubCategories.select { SubCategories.categoryId eq categoryId }
            .map { toSubCategory(it) }
    }
}