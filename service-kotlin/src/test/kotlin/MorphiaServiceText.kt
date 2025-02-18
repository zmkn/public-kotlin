import com.zmkn.service.LoggerService
import com.zmkn.service.MorphiaService
import dev.morphia.query.FindOptions
import dev.morphia.query.Sort.descending
import dev.morphia.query.filters.Filters
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import database.entity.User as UserModel

class MorphiaServiceText {
    private val logger = LoggerService.getInstance()

    //    @Test
    fun test() = runBlocking {
        val user = ""
        val password = ""
        val hosts = listOf("39.106.11.144:27017", "39.106.11.144:27018", "39.106.11.144:27019", "39.106.11.144:27020")
        val database = "usercenter"
        val replicaName = "rs0"
        val connectionString = "mongodb://$user:$password@${hosts.joinToString(",")}/$database?authSource=$database&replicaSet=$replicaName"
        logger.error(connectionString)
        val morphiaService = MorphiaService(connectionString, database)
        val datastore = morphiaService.datastore
        val userList = morphiaService.async {
            logger.error("async1111")
            val userData = listOf(
                UserModel(nickName = "hz"),
                UserModel(nickName = "kz"),
                UserModel(nickName = "xz")
            )
//            datastore.save(userData)
            val userQuery = datastore.find(UserModel::class.java)
            logger.error(userQuery)
            val userList = userQuery.iterator().toList()
            logger.error(userList)
            val filterUserList = userQuery
                .filter(
                    Filters
                        .gte("mobilePhone", "134")
                )
                .iterator(
                    FindOptions()
                        .sort(descending("mobilePhone"))
                )
                .toList()
            logger.error(filterUserList)
        }

        morphiaService.launch {
            logger.error("async222")
            val userQuery = datastore.find(UserModel::class.java)
            logger.error(userQuery)
            val filterUserList = userQuery
                .filter(
                    Filters
                        .gte("mobilePhone", "138")
                )
                .toList()
            logger.error(filterUserList)
        }
        logger.error("bbbbbbbbb")
        delay(10000)
        logger.error("close")
        morphiaService.close()
    }
}
