import com.zmkn.util.FileUtils
import java.io.File
import kotlin.test.Test

class FileUtilsTest {
    @Test
    fun testCopy() {
        println("testCopy")
        println(File(".").canonicalPath)
        println(FileUtils.getProjectRootDirectory("/build", "resources/2.txt"))
        javaClass.getResource("/1.txt")?.let {
            println(it.path)
            FileUtils.copy(it.openStream(), FileUtils.getProjectRootDirectory("build", "resources/2.txt"), FileUtils.getProjectRootDirectory("build", "resources", "/3.txt"))
        }
    }
}
