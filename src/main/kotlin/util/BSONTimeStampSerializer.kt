package com.athlink.util

import io.ktor.util.date.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bson.BsonTimestamp

object BSONTimestampSerializer: KSerializer<BsonTimestamp?> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("BSONTimestamp", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: BsonTimestamp?) {
        val string = value?.toString() ?: getTimeMillis().toString()
        encoder.encodeString(string)
    }

    override fun deserialize(decoder: Decoder): BsonTimestamp {
        val timeString = decoder.decodeString()
        if(timeString == "null"){
            return BsonTimestamp(getTimeMillis())
        }
        return BsonTimestamp(timeString.toLong())
    }
}