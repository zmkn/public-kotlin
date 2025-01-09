import com.zmkn.service.PemService
import java.io.File
import java.io.IOException
import kotlin.test.Test

class PemServiceTest {
    private val publicPemResourcePath = "/secret/public-es512.pem"
    private val privatePemResourcePath = "/secret/private-es512-pkcs1.pem"

    @Test
    fun testGetPublicKey() {
        println("testGetPublicKey")
        val publicPemInputStream = javaClass.getResourceAsStream(publicPemResourcePath) ?: throw IOException("PEM file not found in path: $publicPemResourcePath")
        val publicPemService = PemService(publicPemInputStream)
        println(publicPemService.getPublicKey())
    }

    @Test
    fun testGetPrivateKey() {
        println("testGetPrivateKey")
        val privatePemAbsolutePath = javaClass.getResource(privatePemResourcePath)?.run {
            File(toURI()).absolutePath
        } ?: throw IOException("PEM file not found in path: $privatePemResourcePath")
        val privatePemService = PemService(privatePemAbsolutePath)
        println(privatePemService.getPrivateKey())
    }
}
