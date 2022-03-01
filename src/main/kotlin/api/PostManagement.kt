package com.athlink.api

import com.athlink.model.JSPost
import com.athlink.model.MongoPost
import com.athlink.model.MongoProfile
import com.athlink.util.AthlinkDatabase
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.bson.BsonTimestamp
import io.ktor.util.date.*
import org.bson.conversions.Bson
import org.litote.kmongo.*


fun Application.postManagementRoutes(db: AthlinkDatabase){
    routing {
        route ("/post") {
            get {
                var limit = call.parameters["limit"]?.toInt() ?: 10;
                val start = call.parameters["last_time"]?.toLong() ?: getTimeMillis();
                val tag = call.parameters["tag"]
                val user = call.parameters["user"]
                val filterList = emptyList<Bson>().toMutableList()

                filterList += MongoPost::timePosted.lt(BsonTimestamp(start))

                tag?.let { filterList += MongoPost::tags.contains(it) }
                user?.let { filterList += MongoPost::userEmail.eq(it) }

                val posts = db.posts.find(and(filterList)).sort(descending(MongoPost::timePosted))
                        .take(limit)
                        .map{ it.toJSPost() }
                        .toList()
                posts.forEach {
                    val user = db.profiles.findOne(MongoProfile::email.eq(it.userEmail))
                    it.photoUrl = user?.photoURL
                    it.userName = user?.firstname + " " + user?.lastname
                }
                call.respond(posts)
            }
            post {
                val newPost = call.receive<JSPost>().toMongoPost()
                newPost.timePosted = BsonTimestamp(System.currentTimeMillis())
                db.posts.insertOne(newPost)
                call.respond(HttpStatusCode.OK)
            }
            get ("/trending") {
                val postTags = db.posts.find()
                    .toList()
                    .map { it.tags ?: emptyList() }
                    .flatMap { it }
                    .groupBy { it }
                val frequencies = postTags
                    .mapValues { it -> it.value.size }
                    .toList()
                    .sortedBy { (_, value) -> -value }
                    .map { it.first }
                    .take(10)
                call.respond(frequencies)
            }
        }
    }
}