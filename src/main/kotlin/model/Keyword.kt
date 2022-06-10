package com.athlink.model

import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.newId


@Serializable
data class JSKeyword(
    var keyword: String?,
    var indexes: ArrayList<String>?
) {
    fun toMongoKeyword() = MongoKeyword(
        keyword, indexes
    )
}


@Serializable
data class MongoKeyword(
    var keyword: String?,
    var indexes: ArrayList<String>?,

) {
    fun toJSKeyword() = JSKeyword(
        keyword, indexes
    )
}