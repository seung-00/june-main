package june.domain

import java.util.Date

data class Task(
    val title: String = "",
    val done: Boolean = false,
    val createdAt: Date = Date()
) {
    companion object {
        const val COLLECTION = "tasks"
    }
}
