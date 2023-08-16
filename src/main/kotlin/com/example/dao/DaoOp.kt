package com.example.dao

import kotlinx.coroutines.runBlocking

val dao : CategoryDaoImpl = CategoryDaoImpl().apply {
    runBlocking {
        if (getAllCategories().isNotEmpty()) {
            println(getAllCategories())
        }
    }
}