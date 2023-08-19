package com.example

import com.example.dao.dao
import com.example.plugins.*
import com.example.util.DatabaseFactory
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.CORS
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}


fun Application.module() {

    DatabaseFactory.init()

    install(ContentNegotiation) {
        json()
    }

    GlobalScope.launch {
        println(dao.getAllCategories())
    }

    install(CORS) {
        anyHost()
        allowHeader(HttpHeaders.ContentType)
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.XForwardedProto)
        allowCredentials = true
        allowNonSimpleContentTypes = true
    }

    configureSockets()

    configureSerialization()

    configureMonitoring()

    configureRouting()
}
