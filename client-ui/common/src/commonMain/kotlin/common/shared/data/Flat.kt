package data

const val loadingPlaceholderString = "..."
const val loadingPlaceholderNumber = -999_999

data class Flat(
    val id: Long = loadingPlaceholderNumber.toLong(),
    val name: String = loadingPlaceholderString,
    val age: Double = loadingPlaceholderNumber.toDouble(),
    val rooms: Int = loadingPlaceholderNumber
)
