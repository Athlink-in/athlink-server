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
    var memberSince: String?
) {
    fun toMongoProfile() = MongoProfile(
        firstname, lastname, height, age, school, weight, gradYear, sex, email, description, BsonTimestamp(memberSince?.toLong() ?: getTimeMillis())
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
    @Serializable(with = BSONTimestampSerializer::class) var memberSince: BsonTimestamp? = BsonTimestamp(getTimeMillis()),
) {
    fun toJSProfile() = JSProfile(
        firstname, lastname, height, age, school, weight, gradYear, sex, email, description, memberSince?.value.toString()
    )
}