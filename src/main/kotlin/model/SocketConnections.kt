package com.athlink.model

import io.ktor.http.cio.websocket.*
import io.ktor.websocket.*
import java.util.concurrent.atomic.*

class SocketConnection(val session: DefaultWebSocketSession, private val email : String) {
    val name = email
    companion object {
        var lastId = AtomicInteger(0)
    }

}