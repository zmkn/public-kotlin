package model

import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id

@Serializable
data class Name(
    @Contextual
    @SerialName("_id")
    @JsonProperty("_id")
    val id: Id<Name>,
    val name: String,
)
