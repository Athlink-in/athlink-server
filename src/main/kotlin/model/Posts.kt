package com.athlink.model

import com.athlink.util.BSONTimestampSerializer
import io.ktor.util.date.*
import kotlinx.serialization.Serializable
import org.bson.BsonTimestamp

@Serializable
data class JSPost(
    var postContent: String?,
    var timePosted: String?,
    val likes: Int?,
    var linkUrl: String?,
    var tags: List<String>?,
    var userEmail: String?,
    var photoUrl: String? = null,
    var userName: String? = null
) {
    fun toMongoPost() = MongoPost(
        postContent, BsonTimestamp(timePosted?.toLong() ?: getTimeMillis()), likes, linkUrl, tags, userEmail
    )
}


@Serializable
data class MongoPost(
    var postContent: String?,
    @Serializable(with = BSONTimestampSerializer::class) var timePosted: BsonTimestamp? = BsonTimestamp(getTimeMillis()),
    val likes: Int? = 0,
    var linkUrl: String?,
    var tags: List<String>?,
    var userEmail: String?,
    ) {
    fun toJSPost() = JSPost(
        postContent, timePosted?.value.toString(), likes, linkUrl, tags, userEmail
    )
}

