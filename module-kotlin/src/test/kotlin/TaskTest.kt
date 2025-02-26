import com.zmkn.module.task.model.CreateParams
import com.zmkn.module.task.util.TaskUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.test.Test

class TaskTest {
    @Test
    fun testCreate() = runBlocking {
        println("testCreate-start")
        TaskUtils.create(
            CreateParams(
                name = "testCreate",
                delayMillis = 1000,
            )
        ) {
            println("create")
            "createbbbbbbbbbb"
        }
        println("bbbbbbbbbb")
        println(TaskUtils.jobs)
        delay(5000)
        println(TaskUtils.jobs)
        println("testCreate-end")
    }
}
