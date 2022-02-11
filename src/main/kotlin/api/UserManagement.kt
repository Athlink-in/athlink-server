package com.athlink.api

import com.athlink.model.Profile
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.bson.BsonTimestamp
import org.bson.types.ObjectId
import org.litote.kmongo.MongoOperator

fun Application.userManagementRoutes(profiles: MongoCollection<Profile>){
    routing {
        route("/user") {
            get("/{email}") {
                val email = call.parameters["email"]
                val filter = Filters.eq("email", email)
                call.respond(profiles.find(filter).toList())
            }
            post {
                val newProfile = call.receive<Profile>()
                newProfile.memberSince = BsonTimestamp(System.currentTimeMillis())
                val existingProfiles = profiles.find(Filters.eq("email", newProfile.email)).toList()
                if(existingProfiles.isEmpty()) {
                    profiles.insertOne(newProfile)
                    call.respond(HttpStatusCode.OK, newProfile._id.toString())

                } else {
                    val existingProfile = existingProfiles.first()
                    newProfile.memberSince = existingProfile.memberSince
                    newProfile._id = existingProfile._id
                    profiles.replaceOne(Filters.eq("_id", newProfile._id), newProfile)
                    call.respond(HttpStatusCode.OK, newProfile._id.toString())
                }
            }
        }
    }
}

