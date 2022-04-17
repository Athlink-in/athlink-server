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
import org.litote.kmongo.MongoOperator
import org.litote.kmongo.and
import org.litote.kmongo.findOne
import org.litote.kmongo.or


fun Application.userManagementRoutes(db: AthlinkDatabase){
    var slice = 0;
    routing {
        route("/user") {
            get("/{email}") {
                val email = call.parameters["email"]
                val userEmail = call.parameters["user"]
                val filter = Filters.eq("email", email)
                val connection = db.connections.findOne { and(or(Filters.eq("toEmail", userEmail), Filters.eq("fromEmail", userEmail))
                    ,or(Filters.eq("toEmail", email), Filters.eq("fromEmail", email))) }
                val numConnections = db.connections.countDocuments(or(Filters.eq("toEmail", email), Filters.eq("fromEmail", email)))
                call.respond(db.profiles.find(filter).map { it.toJSProfile().also {
                    if(connection == null)
                        it.connection = 0;
                    else
                        it.connection = 2
                    it.numConnections = numConnections
                } }.toList())
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
            get("/connections/{email}"){
                val email = call.parameters["email"]
                val connections = db.connections.find(or(Filters.eq("toEmail", email), Filters.eq("fromEmail", email))).map {
                    val key = if(email == it.fromEmail) it.toEmail else it.fromEmail
                    db.profiles.findOne(Filters.eq("email", key))
                }
                call.respond(connections.toList())
            }
        }
    }
}