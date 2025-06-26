package model

import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Serializable
data class Name(
    @Contextual
    @SerialName("_id")
    @param:JsonProperty("_id")
    val id: ObjectId = ObjectId(),
    val name: String,
    @Contextual
    val createdAt: Instant = Clock.System.now(),
)
