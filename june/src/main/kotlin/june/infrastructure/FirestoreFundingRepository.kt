package june.infrastructure

import com.google.cloud.firestore.DocumentSnapshot
import com.google.cloud.firestore.Firestore
import june.domain.Funding
import june.domain.FundingRepository
import org.springframework.stereotype.Repository

@Repository
class FirestoreFundingRepository(private val firestore: Firestore) : FundingRepository {
    override fun find(id: Funding.Id): Funding? {
        val docRef = firestore.collection(Funding.COLLECTION).document(id.value)
        val snapshot = docRef.get().get()

        return if (snapshot.exists()) snapshot.toFunding() else null
    }

    override fun findAll(): List<Funding> {
        val snapshot = firestore.collection(Funding.COLLECTION).get().get()

        return snapshot.documents.mapNotNull(DocumentSnapshot::toFunding)
    }

    override fun save(funding: Funding): Funding.Id {
        val docRef = firestore.collection(Funding.COLLECTION).document()
        val id = Funding.Id(docRef.id)
        docRef.set(funding.copy(id = id).toMap()).get()

        return id
    }

    override fun update(funding: Funding): Funding.Id {
        val docRef = firestore.collection(Funding.COLLECTION).document(funding.id.value)
        docRef.set(funding.toMap()).get()

        return funding.id
    }

    override fun delete(id: Funding.Id) {
        firestore.collection(Funding.COLLECTION).document(id.value).delete().get()
    }
}

fun DocumentSnapshot.toFunding(): Funding? {
    val data = this.data ?: return null

    return Funding(
        id = Funding.Id(this.id),
        title = data["title"] as String,
        description = data["description"] as String,
        author = data["author"] as String,
        age = data["age"] as String,
        size = data["size"] as String,
        gender = data["gender"] as String,
        region = data["region"] as String,
        budget = (data["budget"] as Number).toInt(),
        participants = (data["participants"] as List<*>).filterIsInstance<String>(),
        imageUrl = data["imageUrl"] as String?,
        amount = (data["amount"] as Number).toInt()
    )
}

fun Funding.toMap(): Map<String, Any> = mapOf(
    "id" to id.value,
    "title" to title,
    "description" to description,
    "author" to author,
    "age" to age,
    "size" to size,
    "gender" to gender,
    "region" to region,
    "budget" to budget,
    "amount" to amount,
    "participants" to participants,
    "imageUrl" to (imageUrl ?: ""),
)
