package com.athlink.model

import com.athlink.util.BSONTimestampSerializer
import kotlinx.serialization.Serializable
import org.bson.BsonTimestamp
import org.litote.kmongo.Id
import org.litote.kmongo.newId

@Serializable
data class Profile(
    var firstName: String?,
    var lastName: String?,
    @Serializable(with = BSONTimestampSerializer::class) var memberSince: BsonTimestamp?,
    val email: String?,
    var _id: Id<Profile> = newId(),
)