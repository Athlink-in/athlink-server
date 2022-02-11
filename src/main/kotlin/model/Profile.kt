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
    val height: Int?,
    val age: Int?,
    val school: String?,
    val weight: Int?,
    val gradYear: Int?,
    @Serializable(with = BSONTimestampSerializer::class) var memberSince: BsonTimestamp?,
    val email: String?,
    var _id: Id<Profile> = newId(),
)