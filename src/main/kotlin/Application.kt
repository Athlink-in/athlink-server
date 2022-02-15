package com.athlink

import com.athlink.api.userManagementRoutes
import com.athlink.model.MongoProfile
import com.mongodb.client.model.Filters
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.litote.kmongo.KMongo
import org.litote.kmongo.*

fun main() {
    val client = KMongo.createClient(System.getenv("MONGO_URI"))
    val database = client.getDatabase("athlink")
    val profiles = database.getCollection<MongoProfile>()

    embeddedServer(Netty, port = System.getenv("PORT").toInt()) {
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
            route("/") {
                get {
                    call.respond("Athlink says hello!")
                }
            }
            userManagementRoutes(profiles)
        }
    }.start(wait = true)
}
