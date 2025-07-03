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
data class Account(
    @Contextual
    @SerialName("_id")
    @param:JsonProperty("_id")
    val id: ObjectId = ObjectId(),
    @Contextual
    val notificationGroupId: ObjectId,
    val account: String,
    val phoneNumber: String,
    val status: String,
    @Contextual
    val userId: ObjectId,
    val passwordStatus: String,
    val accountUpdatedAt: Instant? = null,
    val password: String? = null,
    val passwordUpdatedAt: Instant? = null,
    val passwordUpdatedVersion: Int? = null,
    val createdAt: Instant = Clock.System.now(),
    val updatedAt: Instant = createdAt,
)
