package com.athlink.util

import com.athlink.model.MongoPost
import com.athlink.model.MongoProfile
import com.mongodb.client.MongoCollection

data class AthlinkDatabase(
    val profiles : MongoCollection<MongoProfile>,
    val posts : MongoCollection<MongoPost>
)