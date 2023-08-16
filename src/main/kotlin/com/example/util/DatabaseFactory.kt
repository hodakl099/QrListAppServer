package com.example.util

import com.example.model.Categories
import com.example.model.SubCategories
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory  {

    fun init() {
        val database =  Database.connect(
            url = "jdbc:postgresql://localhost:5432/postgres",
            driver = "org.postgresql.Driver",
            user = "mahmoud",
            password = "mahmoud99"
        )
        transaction(database) {
            SchemaUtils.create(
                Categories,
                SubCategories
            )
        }
    }
    suspend fun <T> dbQuery(block : suspend () -> T) : T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}