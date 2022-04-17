package com.athlink.model

import com.athlink.util.BSONTimestampSerializer
import io.ktor.util.date.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNull.content
import org.bson.BsonTimestamp

@Serializable
data class JSConnection(
      var fromEmail: String?,
      var toEmail: String?,
      var status: String?,
      var timeStamp: String?
) {
    fun toMongoConnection() = MongoConnection(
        fromEmail, toEmail, status, BsonTimestamp(timeStamp?.toLong() ?: getTimeMillis())
    )

}


@Serializable
data class MongoConnection(
    var fromEmail: String?,
    var toEmail: String?,
    var status: String?,
    @Serializable(with = BSONTimestampSerializer::class) var timeStamp: BsonTimestamp? = BsonTimestamp(getTimeMillis()),
) {
    fun toJSConnection() = JSConnection(
        fromEmail, toEmail,  status, timeStamp?.value.toString()
    )
}