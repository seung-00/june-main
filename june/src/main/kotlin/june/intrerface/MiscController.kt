package june.intrerface

import com.google.genai.Client
import june.domain.Something
import june.domain.SomethingRepository
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*


@CrossOrigin(origins = ["*"])
@RestController
class MiscController(
    private val somethingRepository: SomethingRepository
) {

    private val client = Client.builder().apiKey("AIzaSyCEVR8Fqk9G7IfcyKdViag4BbslfYmSltI").build()

    private val logger = LoggerFactory.getLogger(MiscController::class.java)

    data class SomethingResponse(
        val id: String,
        val data: Map<Any, Any>
    ) {
        companion object {
            fun from(something: Something): SomethingResponse {
                return SomethingResponse(
                    id = something.id.value,
                    data = something.data
                )
            }
        }
    }

    @RequestMapping("/health")
    fun health(): MessageResponse {
        val envPort = System.getenv("PORT") ?: "NOT SET"

        return MessageResponse(
            message = "Hello World! $envPort"
        )
    }

    @PostMapping("api/something")
    fun postSomething(@RequestBody data: Map<Any, Any>): SomethingResponse {

        val something = somethingRepository.save(
            Something(
                id = Something.UNDEFINED_ID,
                data = data
            )
        )

        return SomethingResponse.from(something)
    }

    @GetMapping("api/something")
    fun getSomething(@RequestParam("id") id: String): SomethingResponse {
        val something = somethingRepository.find(Something.Id(id))
            ?: throw IllegalArgumentException("Something with id $id not found")

        return SomethingResponse.from(something)
    }

    @DeleteMapping("api/something")
    fun deleteSomething(@RequestParam("id") id: String): MessageResponse {
        somethingRepository.delete(Something.Id(id))

        return MessageResponse(message = "Deleted something with id $id")
    }
}