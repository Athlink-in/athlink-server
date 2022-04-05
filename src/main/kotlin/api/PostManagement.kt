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
import org.bson.types.ObjectId
import org.litote.kmongo.*
import org.litote.kmongo.id.toId


fun Application.postManagementRoutes(db: AthlinkDatabase){
    routing {
        route ("/post") {
            get {
                var postId = call.parameters["postId"]?.toString()
                if(postId != null) {
                    val post = db.posts.findOne(MongoPost::_id eq ObjectId(postId).toId())
                    call.respond(listOf(post?.likeCount.toString()))
                }
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
                newPost.likes = emptyList()
                newPost.likeCount = 0
                db.posts.insertOne(newPost)
                call.respond(newPost._id.toString())
            }
            get ("/trending") {
                val postTags = db.posts.find()
                    .toList()
                    .map { it.tags ?: emptyList() }
                    .flatMap { it }
                    .groupBy { it }
                val frequencies = postTags
                    .mapValues { it.value.size }
                    .toList()
                    .sortedBy { (_, value) -> -value }
                    .map { it.first }
                    .take(10)
                call.respond(frequencies)
            }
            post("/like") {
                val params = call.request.queryParameters
                println(params)
                val postId = ObjectId(params["postId"].toString())
                val email = params["email"].toString()

                val post = db.posts.findOne(MongoPost::_id eq postId.toId())
                if(post!!.likes!!.contains(email)) {
                    post.likes = post.likes!!.filter { it != email }
                    post.likeCount = (post.likeCount ?: 0) - 1
                } else {
                    post.likes = post.likes!! + email
                    post.likeCount = (post.likeCount ?: 0) + 1
                }
                db.posts.updateOne(post)
                call.respond(post.likeCount.toString())
            }
        }
    }
}