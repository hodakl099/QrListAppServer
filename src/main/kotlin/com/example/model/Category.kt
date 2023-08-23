package com.example.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ReferenceOption

import org.jetbrains.exposed.sql.Table
@Serializable
data class Category(
    val  id : Int,
    val name : String,
    val imageUrl : String,
    val objectName : String,
    val subCategories : List<SubCategory>,
    val firebaseUID : String
)

object Categories : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name",1024)
    val imageUrl = varchar("imageUrl",2048)
    val objectName = varchar("objectName",256)
    val firebaseUID = reference("restaurant_id",Restaurants.firebaseUID,onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(id, name = "PK_Categories_ID")
}
