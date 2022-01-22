package com.example

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    infix fun String.ate(a: String) {
        println("${this} just ate ${a}")
    }
    embeddedServer(Netty, port = 8080) {
        install(CORS) {
            method(HttpMethod.Options)
            method(HttpMethod.Put)
            method(HttpMethod.Delete)
            method(HttpMethod.Patch)
            header(HttpHeaders.Authorization)
            header(HttpHeaders.AccessControlAllowOrigin)
            allowNonSimpleContentTypes = true
            allowSameOrigin = true
            host("*", listOf("http", "https"))
            install(ContentNegotiation){
                json()
            }
        }
        routing {
            get("/") {
                println("GET Request")
                call.respond(mapOf("user" to "test user"))
            }
        }
    }.start(wait = true)
}
