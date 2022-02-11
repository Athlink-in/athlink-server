package com.athlink.model

import com.athlink.util.BSONTimestampSerializer
import io.ktor.util.date.*
import kotlinx.serialization.Serializable
import org.bson.BsonTimestamp
import org.litote.kmongo.Id
import org.litote.kmongo.newId

@Serializable
data class Profile(
    var firstname: String?,
    var lastname: String?,
    val height: Int?,
    val age: Int?,
    val school: String?,
    val weight: Int?,
    val gradyear: Int?,
    val sex: String?,
    @Serializable(with = BSONTimestampSerializer::class) var memberSince: BsonTimestamp? = BsonTimestamp(getTimeMillis()),
    val email: String?,
    var _id: Id<Profile> = newId(),
)