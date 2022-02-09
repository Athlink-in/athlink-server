package com.athlink.model

import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import org.litote.kmongo.Id
import org.litote.kmongo.newId

@Serializable
data class Profile(
    var firstName: String?,
    var lastName: String?,
    var memberSince: String?,
    val email: String?,
    var _id: Id<Profile> = newId(),
)