package com.athlink.api

import com.athlink.model.JSProfile
import com.athlink.model.MongoProfile
import com.athlink.model.Posts
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.utils.io.*
import org.bson.BsonTimestamp


fun Application.userManagementRoutes(profiles: MongoCollection<MongoProfile>, posts: MongoCollection<Posts>){
    routing {
        route("/user") {
            get("/{email}") {
                val email = call.parameters["email"]
                val filter = Filters.eq("email", email)
                call.respond(profiles.find(filter).map { it.toJSProfile() }.toList())
            }
            post {
                val newProfile = call.receive<JSProfile>().toMongoProfile()
                newProfile.memberSince = BsonTimestamp(System.currentTimeMillis())
                val existingProfiles = profiles.find(Filters.eq("email", newProfile.email)).toList()
                if(existingProfiles.isEmpty()) {
                    profiles.insertOne(newProfile)
                    call.respond(HttpStatusCode.OK, newProfile.email.toString())

                } else {
                    val existingProfile = existingProfiles.first()
                    newProfile.memberSince = existingProfile.memberSince
                    profiles.replaceOne(Filters.eq("email", newProfile.email), newProfile)
                    call.respond(HttpStatusCode.OK, newProfile.email.toString())
                }
            }
        }
        route("/post"){
            post{
                println("hi")
                val newPost = call.receive<Posts>()
                newPost.timePosted = BsonTimestamp(System.currentTimeMillis())
                posts.insertOne(newPost)
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}