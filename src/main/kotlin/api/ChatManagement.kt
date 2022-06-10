package com.athlink.api

import com.athlink.model.JSMessage
import com.athlink.util.AthlinkDatabase
import io.ktor.application.*
import io.ktor.http.cio.websocket.*
import io.ktor.network.sockets.*
import io.ktor.routing.*
import io.ktor.websocket.*
import java.util.*
import kotlin.collections.LinkedHashSet
import com.athlink.model.SocketConnection
import com.google.gson.Gson
import com.mongodb.client.model.Filters
import io.ktor.http.*
import io.ktor.response.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.litote.kmongo.or
import org.litote.kmongo.and


fun Application.chatManagementRoutes(db: AthlinkDatabase){
    routing {
        val connections = Collections.synchronizedSet<SocketConnection?>(LinkedHashSet())
//        webSocket("/testWebsocket/{senderEmail}/{receiverEmail}"){
//            println("Adding user!")
//            val email = call.parameters["senderEmail"].toString()
//            val receiver = call.parameters["receiverEmail"].toString()
//            val thisConnection = SocketConnection(this, email)
//            connections += thisConnection
//            for(frame in incoming) {
//                frame as? Frame.Text ?: continue
//                val receivedText = frame.readText()
//                println()
//                val connection = connections.find { it.name == receiver}
//                println(connection?.name)
//                connection?.session?.send(receivedText)
//            }
//        }
        webSocket("/testWebsocket2/{email}"){
            val receiverEmail = call.parameters["email"].toString()
            println("Setting up server for $receiverEmail")
            val connection = SocketConnection(this, receiverEmail)
            println(connections)


            if(connections.contains(connection)){
                connections.remove(connection)
            }
            connections += connection
            println(connections.size)
            try {
                for (frame in incoming){
                    frame as? Frame.Text ?: continue
                    val receivedText = frame.readText()
                    if(receivedText == "ping"){
                        connection?.session?.send("pong")
                    }
                    else {
                        println(receivedText)
                        val message = Json.decodeFromString<JSMessage>(receivedText)
                        println(message)
                        val connection = connections.find { it.name == message.toEmail }
//                    connections.filter { it.name == message.toEmail }.forEach { it.session.send(Gson().toJson(message)) }
//                    connections.find { it.name == message.toEmail }?.session?.send(Gson().toJson(message)
                        if(connection != null){
                            println("Connection found")

                        }
                        else{
                            println("connection not found")
                        }
                        connection?.session?.send(Gson().toJson(message))

                        println("should add to db now")
                        print(message.toMongoMessage())
                        db.messages.insertOne(message.toMongoMessage())
                    }
                }
            } catch (e : Exception) {
                println("onClose $e")
                println("CLOSED")
                connections -= connection
            }
        }
        route("/messages") {
            get("/{fromEmail}/{toEmail}") {
                val fromEmail = call.parameters["fromEmail"].toString()
                val toEmail = call.parameters["toEmail"].toString()
                val messages = db.messages.find(and(or(Filters.eq("toEmail", fromEmail), Filters.eq("fromEmail", fromEmail)),
                    or(Filters.eq("toEmail", toEmail), Filters.eq("fromEmail", toEmail))))
                messages.forEach{ println(it.toJSMessage())}
                call.respond(messages.map { it.toJSMessage() }.toList())
            }
        }
    }
}