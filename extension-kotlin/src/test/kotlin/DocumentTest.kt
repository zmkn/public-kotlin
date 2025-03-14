import com.zmkn.extension.assign
import com.zmkn.extension.deepCopy
import com.zmkn.extension.filter
import org.bson.Document
import org.junit.jupiter.api.Test

class DocumentTest {
    @Test
    fun testDocumentDeepCopy() {
        val document = Document()
            .append("a", "aaaaaaaa")
            .append(
                "document", Document()
                    .append("a1", "a111")
                    .append("document", Document().append("aa1", "a1111111111"))
                    .append("list", listOf("l1", 2, "l3"))
            )
            .append("list", listOf(1, "2bb", 3))
        println(document)
        println(document.deepCopy())
        val document2 = Document()
            .append(
                "document", Document()
                    .append("a1", "aaaaa1")
                    .append("document", Document().append("aa2", "aaaaaa222"))
                    .append("list", listOf("l2", 4, "l8"))
            )
            .append("list", listOf(3, "4a", "5b"))
        println(document2)
        println(document2.get("list"))
        val document3 = document.assign(document2)
        println(document3)
        println(document.filter {
            it.key != "list"
        })
    }
}
