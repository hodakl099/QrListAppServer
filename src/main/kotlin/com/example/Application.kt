package com.example

import com.example.plugins.*
import com.example.util.DatabaseFactory
import com.google.cloud.storage.Cors
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.*
import io.ktor.server.plugins.cors.CORS
import io.ktor.server.plugins.cors.routing.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    DatabaseFactory.init()
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
