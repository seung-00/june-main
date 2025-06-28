package june.application
import com.google.cloud.firestore.Firestore
import june.domain.Task
import org.springframework.stereotype.Service

@Service
class TaskService(
    private val firestore: Firestore
) {

    fun create(task: Task): String {
        val docRef = firestore.collection(Task.COLLECTION).document()
        docRef.set(task).get()  // blocking
        return docRef.id
    }

    fun findById(id: String): Task? {
        val docRef = firestore.collection(Task.COLLECTION).document(id)
        val snapshot = docRef.get().get()
        return if (snapshot.exists()) snapshot.toObject(Task::class.java) else null
    }

    fun findAll(): List<Pair<String, Task>> {
        val snapshot = firestore.collection(Task.COLLECTION).get().get()
        return snapshot.documents.mapNotNull { doc ->
            val task = doc.toObject(Task::class.java)
            if (task != null) doc.id to task else null
        }
    }

    fun markDone(id: String): Boolean {
        val docRef = firestore.collection(Task.COLLECTION).document(id)
        val updateResult = docRef.update("done", true).get()
        return updateResult.updateTime != null
    }

    fun delete(id: String) {
        firestore.collection(Task.COLLECTION).document(id).delete().get()
    }
}
