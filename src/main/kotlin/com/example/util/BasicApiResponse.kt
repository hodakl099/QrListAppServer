package com.example.util

import kotlinx.serialization.Serializable

@Serializable
data class BasicApiResponse(
    val isSuccess : Boolean = false,
    val message : String? = null
)
