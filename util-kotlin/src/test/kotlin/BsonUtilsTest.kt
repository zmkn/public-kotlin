import com.mongodb.client.model.Sorts
import com.zmkn.util.BsonUtils
import org.junit.jupiter.api.Disabled
import kotlin.test.Test

class BsonUtilsTest {
    data class User(
        val name: String,
        val age: Int,
    )

    @Test
    @Disabled
    fun testReverseSort() {
        val user = User(
            name = "test",
            age = 42,
        )
        val sortBson = Sorts.orderBy(
            Sorts.ascending("name"),
            Sorts.ascending("age"),
        )
        val sortBson2 = Sorts.orderBy(
            Sorts.descending("size"),
            Sorts.descending("type"),
        )
        val sortBson3 = Sorts.orderBy(
            sortBson,
            sortBson2,
        )
        println(Sorts.ascending("name"))
        println(sortBson3)
        val newSort = BsonUtils.reverseSort(sortBson3)
        println(newSort)
    }
}
