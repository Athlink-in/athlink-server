package com.athlink.model
import com.athlink.util.BSONTimestampSerializer
import io.ktor.util.date.*
import kotlinx.serialization.Serializable
import org.bson.BsonTimestamp
import org.bson.types.ObjectId
import org.litote.kmongo.Id
import org.litote.kmongo.id.toId
import org.litote.kmongo.newId

@Serializable
data class JSComment(
    var commentContent: String?,
    var timePosted: String?,
    var userEmail: String?,
    var postId: String?,
    var photoUrl: String? = null,
    var userName: String? = null
) {
    fun toMongoComment() = MongoComment(
        commentContent, BsonTimestamp(timePosted?.toLong() ?: getTimeMillis()), userEmail, postId
    )
}


@Serializable
data class MongoComment(
    var postContent: String?,
    @Serializable(with = BSONTimestampSerializer::class) var timePosted: BsonTimestamp? = BsonTimestamp(getTimeMillis()),
    var userEmail: String?,
    var postId: String?
) {
    fun toJSComment() = JSComment(
        postContent, timePosted?.value.toString(), userEmail, postId
    )
}

