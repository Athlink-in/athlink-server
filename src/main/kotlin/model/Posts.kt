package com.athlink.model

import com.athlink.util.BSONTimestampSerializer
import io.ktor.util.date.*
import kotlinx.serialization.Serializable
import org.bson.BsonTimestamp
import org.bson.types.ObjectId
import org.litote.kmongo.Id
import org.litote.kmongo.MongoOperator
import org.litote.kmongo.id.toId
import org.litote.kmongo.newId

@Serializable
data class JSPost(
    var postContent: String?,
    var timePosted: String?,
    var likeCount: Int?,
    var linkUrl: String?,
    var tags: List<String>?,
    var userEmail: String?,
    var likes: List<String>?,
    val postId: String? = "test",
    var photoUrl: String? = null,
    var userName: String? = null,
) {
    fun toMongoPost() = MongoPost(
        postContent, BsonTimestamp(timePosted?.toLong() ?: getTimeMillis()), likeCount, linkUrl, tags, userEmail, likes
//        postContent, BsonTimestamp(timePosted?.toLong() ?: getTimeMillis()), likeCount, linkUrl, tags, userEmail, likes
    )
}


@Serializable
data class MongoPost(
    var postContent: String?,
    @Serializable(with = BSONTimestampSerializer::class) var timePosted: BsonTimestamp? = BsonTimestamp(getTimeMillis()),
    var likeCount: Int? = 0,
    var linkUrl: String?,
    var tags: List<String>?,
    var userEmail: String?,
    var likes: List<String>?,
    val _id: Id<MongoPost> = newId()
    ) {
    fun toJSPost() = JSPost(
        postContent, timePosted?.value.toString(), likeCount, linkUrl, tags, userEmail, likes, _id.toString()
//        postContent, timePosted?.value.toString(), likeCount, linkUrl, tags, userEmail, likes, _id.toString()
    )
}

