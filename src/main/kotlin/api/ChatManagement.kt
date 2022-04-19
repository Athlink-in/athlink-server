package com.athlink.api

import com.athlink.model.Message
import com.athlink.util.AthlinkDatabase
import io.ktor.application.*
import io.ktor.http.cio.websocket.*
import io.ktor.network.sockets.*
import io.ktor.routing.*
import io.ktor.websocket.*
import java.util.*
import kotlin.collections.LinkedHashSet
import com.athlink.model.SocketConnection
import io.ktor.http.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json



fun Application.ChatManagementRoutes(db: AthlinkDatabase){
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
            connections += SocketConnection(this, receiverEmail)
            for(frame in incoming) {
                frame as? Frame.Text ?: continue
                val receivedText = frame.readText()
                print(receivedText)
                val message = Json.decodeFromString<Message>(receivedText)
                connections.find { it.name == message.toEmail }?.session?.send(message.content)
                println(message)
            }
        }
    }
}