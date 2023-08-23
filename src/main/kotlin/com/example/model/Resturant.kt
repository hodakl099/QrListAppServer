package com.example.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table


@Serializable
data class Restaurant(
    val id : Int,
    val name : String,
    val email : String,
    val firebaseUID : String
)


object Restaurants : Table() {

    val id = integer("id").autoIncrement()
    val name = varchar("name",1024)
    val email = varchar("email",1024)
    val firebaseUID = varchar("firebaseUID",2040)

    override val primaryKey = PrimaryKey(id, name = "PK_Resturants_id")

}