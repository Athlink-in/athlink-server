package com.athlink.model

import com.athlink.util.BSONTimestampSerializer
import io.ktor.util.date.*
import kotlinx.serialization.Serializable
import org.bson.BsonTimestamp

@Serializable
data class JSPosts(
    var email: String?,
    var postContent: String?,
    var timePosted: String?,
    val likes: Int?,
    var linkUrl: String?,
    var photoUrl: String?
)


@Serializable
data class Posts(
    var email: String?,
    var postContent: String?,
    @Serializable(with = BSONTimestampSerializer::class) var timePosted: BsonTimestamp? = BsonTimestamp(getTimeMillis()),
    val likes: Int? = 0,
    var linkUrl: String?,
    var photoUrl: String?
) {
    fun toJSPost() = JSPosts(
        email, postContent, timePosted?.value.toString(), likes, linkUrl, photoUrl
    )
}

