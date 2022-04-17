package com.athlink.model

import com.athlink.util.BSONTimestampSerializer
import io.ktor.util.date.*
import kotlinx.serialization.Serializable
import org.bson.BsonTimestamp

@Serializable
data class JSProfile(
    var firstname: String?,
    var lastname: String?,
    var height: Int?,
    var age: Int?,
    var school: String?,
    var weight: Int?,
    var gradYear: Int?,
    var sex: String?,
    var email: String?,
    var description: String?,
    var photoURL: String?,
    var memberSince: String?,
    var connection: Int?,
    var numConnections: Long?
) {
    fun toMongoProfile() = MongoProfile(
        firstname, lastname, height, age, school, weight, gradYear, sex, email, description, photoURL, BsonTimestamp(memberSince?.toLong() ?: getTimeMillis())
    )
}


@Serializable
data class MongoProfile(
    var firstname: String?,
    var lastname: String?,
    var height: Int?,
    var age: Int?,
    var school: String?,
    var weight: Int?,
    var gradYear: Int?,
    var sex: String?,
    var email: String?,
    var description: String?,
    var photoURL: String?,
    @Serializable(with = BSONTimestampSerializer::class) var memberSince: BsonTimestamp? = BsonTimestamp(getTimeMillis()),
) {
    fun toJSProfile() = JSProfile(
        firstname, lastname, height, age, school, weight, gradYear, sex, email, description, photoURL, memberSince?.value.toString(), 0, 0
    )
}