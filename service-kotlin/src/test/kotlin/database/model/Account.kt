package database.model

import kotlinx.serialization.Serializable

@Serializable
data class Account(
    val accountId: String? = null,
    val userId: String? = null,
    val mobilePhone: String? = null,
    val nickName: String? = null,
)
