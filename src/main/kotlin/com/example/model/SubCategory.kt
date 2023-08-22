package com.example.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table


@Serializable
data class SubCategory(
    val id : Int,
    val name : String,
    val imageUrl : String,
    val objectName : String,
    val categoryId : Int
)

object SubCategories  : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 1024)
    val imageUrl = varchar("imageUrl",2048)
    val objectName = varchar("objectName", 256)
    val categoryId = reference("category_id", Categories.id, onDelete = ReferenceOption.CASCADE)
}