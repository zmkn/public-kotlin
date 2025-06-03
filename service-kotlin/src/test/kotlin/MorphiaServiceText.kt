import com.zmkn.service.MorphiaService
import dev.morphia.query.filters.Filters
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import database.entity.User as UserModel

class MorphiaServiceText {
    //    @Test
    fun test() = runBlocking {
        val user = ""
        val password = ""
        val hosts = listOf("39.106.11.144:27017", "39.106.11.144:27018", "39.106.11.144:27019", "39.106.11.144:27020")
        val database = "usercenter"
        val replicaName = "rs0"
        val connectionString = "mongodb://$user:$password@${hosts.joinToString(",")}/$database?authSource=$database&replicaSet=$replicaName"
        println(connectionString)
        val morphiaService = MorphiaService(connectionString, database)
        val datastore = morphiaService.datastore
        val userList = morphiaService.async {
            println("async1111")
            val userData = listOf(
                UserModel(nickName = "hz"),
                UserModel(nickName = "kz"),
                UserModel(nickName = "xz")
            )
//            datastore.save(userData)
            val userQuery = datastore.find(UserModel::class.java)
            println(userQuery)
            val userList = userQuery.iterator().toList()
            println(userList)
            val filterUserList = userQuery
                .filter(
                    Filters
                        .gte("mobilePhone", "134")
                )
                .iterator()
                .toList()
            println(filterUserList)
        }

        morphiaService.launch {
            println("async222")
            val userQuery = datastore.find(UserModel::class.java)
            println(userQuery)
            val filterUserList = userQuery
                .filter(
                    Filters
                        .gte("mobilePhone", "138")
                )
                .toList()
            println(filterUserList)
        }
        println("bbbbbbbbb")
        delay(10000)
        println("close")
        morphiaService.close()
    }
}
