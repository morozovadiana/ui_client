import com.google.gson.Gson
import java.io.File
import java.io.FileReader

class WorkWithJson() {
    data class ApiResponse(
        val data: DataContent
    )

    data class DataContent(
        val tests: List<Content>,
        val total: Int
    )

    data class Content(
        val name: String,
        val type: String,
        val status: String
    )

    fun openFile() = File("response.txt").bufferedReader()
    fun parseJson() {
        val gson = Gson()
        val result = gson.fromJson(openFile(), ApiResponse::class.java)
        result.data.tests.forEach { println("name: ${it.name} type: ${it.type}  status: ${it.status}") }
        println(" total: ${result.data.total}")
    }
}

fun main() {
    val workWithJson = WorkWithJson()
    workWithJson.parseJson()
}