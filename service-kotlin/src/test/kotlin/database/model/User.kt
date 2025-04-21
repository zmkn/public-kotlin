package database.model

import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.datetime.Clock.System
import kotlinx.datetime.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @Contextual
    @SerialName("_id")
    @param:JsonProperty("_id")
    val id: String = "",
    @Contextual
    val accountId: String,
    val nickName: String,
    val profilePictureUrl: String,
    val status: String,
    val phoneNumbers: List<String> = emptyList(),
    val identityCardNumber: String? = null,
    val birth: Instant? = null,
    val gender: String? = null,
    val age: Int? = null,
    val description: String? = null,
    val createdAt: Instant = System.now(),
    val updatedAt: Instant = createdAt,
)
