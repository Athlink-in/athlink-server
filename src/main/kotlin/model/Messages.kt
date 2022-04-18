package com.athlink.model

import kotlinx.serialization.Serializable

@Serializable
data class Message(val fromEmail : String, val toEmail : String, val content : String){

}