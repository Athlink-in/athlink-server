package com.athlink.model

import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val firstName: String?,
    val lastName: String?,
    val memberSince: String?,
    val email: String?
)