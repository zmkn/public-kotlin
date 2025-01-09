package database.entity

import dev.morphia.annotations.*
import dev.morphia.mapping.IndexType
import org.bson.types.ObjectId

@Entity
@Indexes(
    Index(fields = [Field(value = "updatedAt", type = IndexType.ASC)])
)
data class User(
    @Id var id: ObjectId? = null,
    var accountId: String? = null,
    var userId: String? = null,
    var mobilePhone: String? = null,
    var nickName: String? = null,
    var avatarImagePath: String? = null,
    var status: String? = null,
    var createdAt: Double? = null,
    var updatedAt: Double? = null,
)
