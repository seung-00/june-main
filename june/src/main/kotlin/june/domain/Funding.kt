package june.domain

data class Funding(
    val id: Id,
    val title: String,
    val description: String,
    val author: String,
    val age: String,
    val size: String,
    val gender: String,
    val region: String,
    val budget: Int,
    val amount: Int,
    val participants: List<String>,
    val imageUrl: String?,
) {
    @JvmInline
    value class Id(val value: String)

    companion object {
        const val COLLECTION = "funding"

        val UNDEFINED_ID = Id("")

        fun initialize(
            title: String,
            description: String,
            author: String,
            age: String,
            size: String,
            gender: String,
            region: String,
            budget: Int,
            imageUrl: String?,
        ): Funding = Funding(
            id = UNDEFINED_ID,
            title = title,
            description = description,
            author = author,
            age = age,
            size = size,
            gender = gender,
            region = region,
            budget = budget,
            participants = mutableListOf(author),
            imageUrl = imageUrl,
            amount = 0,
        )
    }
}

interface FundingRepository {
    fun find(id: Funding.Id): Funding?
    fun findAll(): List<Funding>
    fun findAllId(): List<String>
    fun save(funding: Funding): Funding.Id
    fun update(funding: Funding): Funding.Id
    fun delete(id: Funding.Id)
}