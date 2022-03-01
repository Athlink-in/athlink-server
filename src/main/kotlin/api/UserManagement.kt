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
import com.mongodb.client.model.Sorts.descending
import org.litote.kmongo.findOne
import kotlin.math.min


fun Application.userManagementRoutes(profiles: MongoCollection<MongoProfile>, posts: MongoCollection<Posts>){
    var slice = 0;
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
            get{
                val totalDocuments = posts.countDocuments()
                println("HIHIHHIHIHIHIHI")
                println(posts.countDocuments())
                var limit = call.parameters["limit"]?.toInt() ?: 10;
                limit = min(limit, totalDocuments.toInt())
                slice = call.parameters["slice"]?.toInt() ?: slice;
//                val result = posts.find().sort(descending("timePosted")).limit(limit).toList();
                val result = posts.find().sort(descending("timePosted")).toList().subList(slice, slice + limit)
                slice += limit;
                call.respond(result.map{ it.toJSPost() })
            }
            post{
                val newPost = call.receive<Posts>()
                newPost.timePosted = BsonTimestamp(System.currentTimeMillis())
                posts.insertOne(newPost)
                call.respond(HttpStatusCode.OK,"hi there")
            }
        }
    }
}