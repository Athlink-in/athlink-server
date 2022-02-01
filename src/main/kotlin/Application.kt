package com.athlink

import com.athlink.model.Profile
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
    val profiles = database.getCollection<Profile>()

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
            route("/user") {
                get("/{email}") {
                    val email = call.parameters["email"]
                    val filter = Filters.eq("email", email)
                    call.respond(profiles.find(filter).toList())
                }
                post {
                    val newProfile = call.receive<Profile>()
                    newProfile.memberSince = System.currentTimeMillis().toString()
                    val id = profiles.insertOne(newProfile)
                    call.respond(id)
                }
            }
        }
    }.start(wait = true)
}
