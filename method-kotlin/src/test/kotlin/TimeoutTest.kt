import com.zmkn.method.setTimeout
import java.util.concurrent.CountDownLatch
import kotlin.test.Test

class TimeoutTest {
    @Test
    fun testSetTimeout() {
        println("testSetTimeout")
        val latch = CountDownLatch(1)

        setTimeout(3000) {
            println("延迟3秒执行了")
            latch.countDown() // 任务执行完毕后减少计数器
        }

        latch.await() // 等待计数器变为0
        assert(true) // 这里可以进行其他断言
    }
}
