package com.athlink

import com.athlink.api.postManagementRoutes
import com.athlink.api.userManagementRoutes
import com.athlink.api.ChatManagementRoutes
import com.athlink.model.MongoComment
import com.athlink.model.MongoConnection
import com.athlink.model.MongoPost
import com.athlink.model.MongoProfile
import com.athlink.util.AthlinkDatabase
import com.mongodb.client.MongoCollection
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.websocket.*
import org.litote.kmongo.KMongo
import org.litote.kmongo.*

fun main() {
    val client = KMongo.createClient(System.getenv("MONGO_URI"))
    val database = client.getDatabase("athlink")

    val db = AthlinkDatabase(
        profiles = database.getCollection<MongoProfile>("profile"),
        posts = database.getCollection<MongoPost>("post"),
        comments = database.getCollection<MongoComment>("comment"),
        connections = database.getCollection<MongoConnection>("connection")
    )

    embeddedServer(Netty, port = System.getenv("PORT").toInt()) {
        install(WebSockets)
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
            userManagementRoutes(db)
            postManagementRoutes(db)
            ChatManagementRoutes(db)
        }
    }.start(wait = true)
}

class Database()
