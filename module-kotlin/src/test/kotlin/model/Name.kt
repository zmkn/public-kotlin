package model

import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class Name(
    @Contextual
    @SerialName("_id")
    @param:JsonProperty("_id")
    val id: ObjectId = ObjectId(),
    val name: String,
    val createdAt: Instant = Clock.System.now(),
)
