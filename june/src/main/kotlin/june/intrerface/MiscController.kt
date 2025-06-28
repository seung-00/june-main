package june.intrerface

import june.application.AiService
import june.config.HttpClientConfig
import june.domain.Something
import june.domain.SomethingRepository
import okhttp3.OkHttpClient
import okhttp3.Request
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*


@CrossOrigin(origins = ["*"])
@RestController
class MiscController(
    private val somethingRepository: SomethingRepository,
    private val aiApiClient: OkHttpClient,
    private val aiService: AiService,
) {

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

    @GetMapping("ai/health")
    fun aiHealth(): MessageResponse {
        return try {
            val request = Request.Builder()
                .url("https://flask-hello-world-service-698010238719.us-central1.run.app/health")
                .build()

            val response = aiApiClient.newCall(request).execute()

            MessageResponse(message = response.body?.string() ?: "No response body").also {
                logger.info("AI service health check response: ${it.message}")
            }
        } catch (e: Exception) {
            MessageResponse(message = "AI service is not healthy")
        }
    }

    @GetMapping("ai/hashtag")
    fun hashtag(): MessageResponse {
        return try {
            val succeed = aiService.hashtag("실버 걷기 챌린지")
            MessageResponse(message = "Hashtags: ${succeed.hashtags.joinToString(", ")}").also {
                logger.info("AI service hashtag response: ${it.message}")
            }
        } catch (e: Exception) {
            logger.error("AI service hashtag request failed", e)
            MessageResponse(message = "Failed to get hashtags")
        }
    }
}