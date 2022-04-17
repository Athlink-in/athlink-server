package com.athlink.api

import com.athlink.model.JSConnection
import com.athlink.model.JSProfile
import com.athlink.util.AthlinkDatabase
import com.mongodb.client.model.Filters
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.bson.BsonTimestamp


fun Application.userManagementRoutes(db: AthlinkDatabase){
    var slice = 0;
    routing {
        route("/user") {
            get("/{email}") {
                val email = call.parameters["email"]
                val filter = Filters.eq("email", email)
                call.respond(db.profiles.find(filter).map { it.toJSProfile() }.toList())
            }
            post {
                val newProfile = call.receive<JSProfile>().toMongoProfile()
                newProfile.memberSince = BsonTimestamp(System.currentTimeMillis())
                val existingProfiles = db.profiles.find(Filters.eq("email", newProfile.email)).toList()
                if(existingProfiles.isEmpty()) {
                    db.profiles.insertOne(newProfile)
                    call.respond(HttpStatusCode.OK, newProfile.email.toString())

                } else {
                    val existingProfile = existingProfiles.first()
                    newProfile.memberSince = existingProfile.memberSince
                    db.profiles.replaceOne(Filters.eq("email", newProfile.email), newProfile)
                    call.respond(HttpStatusCode.OK, newProfile.email.toString())
                }
            }
            post("/add"){
                val newConnection = call.receive<JSConnection>().toMongoConnection()
                newConnection.timeStamp = BsonTimestamp(System.currentTimeMillis())
                db.connections.insertOne(newConnection)
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}