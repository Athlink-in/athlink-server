package com.athlink.util

import com.athlink.model.*
import com.mongodb.client.MongoCollection

data class AthlinkDatabase(
    val profiles : MongoCollection<MongoProfile>,
    val posts : MongoCollection<MongoPost>,
    val comments : MongoCollection<MongoComment>,
    val connections: MongoCollection<MongoConnection>,
    val messages: MongoCollection<MongoMessage>
)