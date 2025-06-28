package june.infrastructure

import june.domain.Something
import june.domain.SomethingRepository
import org.springframework.stereotype.Repository
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Repository
class InMemorySomethingRepository : SomethingRepository {
    private val storage = ConcurrentHashMap<Something.Id, Something>()

    override fun find(id: Something.Id): Something? {
        return storage[id]
    }

    override fun findAll(): List<Something> {
        return storage.values.toList()
    }

    override fun save(someThing: Something): Something {
        if (someThing.id == Something.UNDEFINED_ID) {
            val id = UUID.randomUUID().let { Something.Id(it.toString()) }

            val newSomething = someThing.copy(id = id)
            storage[id] = newSomething
            return newSomething
        }

        storage[someThing.id] = someThing
        return someThing
    }

    override fun delete(id: Something.Id) {
        storage.remove(id)
    }
}