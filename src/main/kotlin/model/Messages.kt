package com.athlink.model

import com.athlink.util.BSONTimestampSerializer
import io.ktor.util.date.*
import kotlinx.serialization.Serializable
import org.bson.BsonTimestamp
//
//@Serializable
//data class JSMessage(
//    var fromEmail : String?,
//    var toEmail : String?,
//    var content : String?,
//    var timeStamp: String?
//    ){
//    fun toMongoMessage() = MongoMessage(
//        fromEmail, toEmail, content, BsonTimestamp(timeStamp?.toLong() ?: getTimeMillis())
//    )
//}
//
//@Serializable
//data class MongoMessage(
//    var fromEmail : String?,
//    var toEmail : String?,
//    var content : String?,
//    @Serializable(with = BSONTimestampSerializer::class) var timeStamp: BsonTimestamp? = BsonTimestamp(getTimeMillis())
//){
//    fun toJSMessage() = JSMessage(fromEmail, toEmail, content, timeStamp?.value.toString())
//}

@Serializable
data class JSMessage(
    var fromEmail: String?,
    var toEmail: String?,
    var content: String?,
    var timeStamp: String? = null
) {
    fun toMongoMessage() = MongoMessage(
        fromEmail, toEmail, content, BsonTimestamp(timeStamp?.toLong() ?: getTimeMillis())
    )

}


@Serializable
data class MongoMessage(
    var fromEmail: String?,
    var toEmail: String?,
    var content: String?,
    @Serializable(with = BSONTimestampSerializer::class) var timeStamp: BsonTimestamp? = BsonTimestamp(getTimeMillis()),
) {
    fun toJSMessage() = JSMessage(
        fromEmail, toEmail,  content, timeStamp?.value.toString()
    )
}