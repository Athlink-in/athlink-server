package com.athlink.api

import com.athlink.model.JSPost
import com.athlink.model.MongoPost
import com.athlink.util.AthlinkDatabase
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.bson.BsonTimestamp
import io.ktor.util.date.*
import org.litote.kmongo.*
import kotlin.math.min


fun Application.postManagementRoutes(db: AthlinkDatabase){
    routing {
        route("/post"){
            get{
                val totalDocuments = db.posts.countDocuments()
                println(db.posts.countDocuments())
                var limit = call.parameters["limit"]?.toInt() ?: 10;
                limit = min(limit, totalDocuments.toInt())
                val start = call.parameters["last_time"]?.toInt() ?: getTimeMillis();
                val tag = call.parameters["tags"]
                val posts = if (tag == null || tag.isBlank()) {
                    db.posts.find(MongoPost::timePosted.lt(BsonTimestamp(start.toInt(), 1)))
                        .take(limit)
                        .map{ it.toJSPost() }
                        .toList()
                } else {
                    db.posts.find(and(MongoPost::timePosted.lt(BsonTimestamp(start.toInt(), 1)), MongoPost::tags.contains(tag)))
                        .take(limit)
                        .map{ it.toJSPost() }
                        .toList()
                }
                for (p in posts) {
                    val user = db.profiles.findOne(MongoPost::userEmail.eq(p.userEmail))
                    p.photoUrl = user?.photoURL
                    p.userName = user?.email
                    println(p)
                }
//                posts.forEach {
//                    val user = db.profiles.findOne(MongoPost::userEmail.eq(it.userEmail))
//                    it.photoUrl = user?.photoURL
//                    it.userName = user?.email
//                    println(it)
//                }
                call.respond(posts)
            }
            post{
                val newPost = call.receive<JSPost>().toMongoPost()
                newPost.timePosted = BsonTimestamp(System.currentTimeMillis().toInt(), 1)
                db.posts.insertOne(newPost)
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}