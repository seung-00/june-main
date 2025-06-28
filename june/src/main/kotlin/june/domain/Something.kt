package june.domain

data class Something(
    val id: Id,
    val data: Map<Any, Any>,
) {
    @JvmInline
    value class Id(val value: String)

    companion object {
        val UNDEFINED_ID = Id("")
    }
}

interface SomethingRepository {
    fun find(id: Something.Id): Something?
    fun findAll(): List<Something>
    fun save(someThing: Something): Something
    fun delete(id: Something.Id)
}