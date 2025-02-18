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
data class User(
    @Contextual
    @SerialName("_id")
    @JsonProperty("_id")
    val id: Id<User> = newId(),
    @Contextual
    val accountId: Id<Account>,
    val nickName: String,
    val profilePictureUrl: String,
    val status: String,
    val mobileNumbers: List<String> = emptyList(),
    val identityCardNumber: String? = null,
    val birth: Instant? = null,
    val gender: String? = null,
    val age: Int? = null,
    val description: String? = null,
    val createdAt: Instant = System.now(),
    val updatedAt: Instant = createdAt,
)
