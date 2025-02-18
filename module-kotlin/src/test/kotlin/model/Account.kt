package model

import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.datetime.Clock.System
import kotlinx.datetime.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.newId

@Serializable
data class Account(
    @Contextual
    @SerialName("_id")
    @JsonProperty("_id")
    val id: Id<Account> = newId(),
    @Contextual
    val notificationGroupId: Id<String>,
    val account: String,
    val mobileNumber: String,
    val status: String,
    @Contextual
    val userId: Id<User>,
    val passwordStatus: String,
    val accountUpdatedAt: Instant? = null,
    val password: String? = null,
    val passwordUpdatedAt: Instant? = null,
    val passwordUpdatedVersion: Int? = null,
    val createdAt: Instant = System.now(),
    val updatedAt: Instant = createdAt,
)
