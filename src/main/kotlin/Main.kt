import com.google.gson.GsonBuilder
import java.io.File
import java.io.FileReader
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class GroupData(
    val name: String? = null,
    val type: String? = null,
    val quantity: Int? = null,
    val fileName: String? = null
) {

    private fun generateNameAndType(): List<List<String>> {
        val result = mutableListOf<List<String>>()
        if (quantity != null && quantity != 0) {
            for (i in 1..quantity) {
                val internalList = mutableListOf<String>().apply {
                    add(name + 1)
                    add(type + 1)
                    add(i.toString())
                }
                result.add(internalList)
            }
        }
        return result
    }

    private fun groupTo(): Data {
        return Data(
            name = name,
            type = type,
            quantity = quantity,
            testNames = generateNameAndType()
        )
    }

    private fun createFile(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm")
        val dateFormat = LocalDateTime.now().format(formatter)
        return "$dateFormat.csv"

    }

    fun writeToFile() {
        val newFilename = createFile()
        val data = listOf(groupTo())
        val header = "Name_Suite\tType_Suite\tQuantity\n"
        val columns = data.joinToString("\n") { it ->
            "${it.name}\t${it.type}\t${it.quantity}\n${
                it.testNames.joinToString(separator = "\n") { row ->
                    row.joinToString("\t")
                }
            }"
        }
        WriteTests.writeToFile(header, columns, newFilename)
    }

    fun readFromFile() {
        val lines = FileReader("$fileName.csv")
            .readLines().drop(1)
        val dataList = lines
            .map { row ->
                val result = row.split(Regex("\\s+"))
                GroupData(
                    name = result.first().trim(),
                    type = result.get(1).trim(),
                    quantity = result.last().toInt()
                )
            }

        val gson = GsonBuilder().setPrettyPrinting().create()
        println(gson.toJson(dataList))

    }

    data class Data(
        val name: String?,
        val type: String?,
        val quantity: Int?,
        val testNames: List<List<String>>
    )

    object WriteTests {
        fun writeToFile(header: String, data: String, newFilename: String) {
            File(newFilename).apply {
                writeText(header)
                appendText(data)
                println("File $newFilename will be created")
            }
        }
    }
}

fun main() {
    println("Input name test")
    val name = readLine().toString()
    println("Input type test")
    val type = readLine().toString()
    println("Input quantity")
    val quantity = readLine()?.toInt()
    val groupData = GroupData(name = name, type = type, quantity = quantity)
    groupData.writeToFile()

    println("Input name of the file in format YYYY-MM-dd-HH/mm")
    val fileName = readLine().toString()
    val groupData1 = GroupData(fileName = fileName)
    groupData1.readFromFile()
}