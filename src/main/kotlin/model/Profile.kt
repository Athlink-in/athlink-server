package com.athlink.model

import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    var firstName: String?,
    var lastName: String?,
    var memberSince: String?,
    val email: String?
)