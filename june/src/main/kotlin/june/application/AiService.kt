package june.application

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import okhttp3.OkHttpClient
import okhttp3.Request
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.concurrent.Executors

const val AI_API_URL = "https://flask-hello-world-service-698010238719.us-central1.run.app/ai"

@Service
class AiService(
    private val aiApiClient: OkHttpClient,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    private val mapper = jacksonObjectMapper()

    fun draft(query: String): DraftSucceed {
        val request = Request.Builder()
            .url("$AI_API_URL/draft?query=$query")
            .build()

        aiApiClient.newCall(request).execute().use { response ->
            check(response.isSuccessful) { "Draft API failed: ${response.code}" }
            val body = response.body?.string() ?: throw IllegalStateException("Empty response")
            return mapper.readValue(body)
        }
    }

    fun image(query: String): ImageSucceed {
        val request = Request.Builder()
            .url("$AI_API_URL/image?query=$query")
            .build()

        aiApiClient.newCall(request).execute().use { response ->
            check(response.isSuccessful) { "Image API failed: ${response.code}" }
            val body = response.body?.string() ?: throw IllegalStateException("Empty response")
            return mapper.readValue(body)
        }
    }

    fun hashtag(query: String): HashTagSucceed {
        val request = Request.Builder()
            .url("$AI_API_URL/hashtag?query=$query")
            .build()

        aiApiClient.newCall(request).execute().use { response ->
            check(response.isSuccessful) { "Hashtag API failed: ${response.code}" }
            val body = response.body?.string() ?: throw IllegalStateException("Empty response")
            return mapper.readValue(body)
        }
    }

    fun guessFunding(query: String): FundingItem {
        Executors.newVirtualThreadPerTaskExecutor().use { executor ->
            val draftFuture = executor.submit<DraftSucceed> { draft(query).also { logger.info("Draft API succeeded: $it")  } }
            val imageFuture = executor.submit<ImageSucceed> { image(query).also { logger.info("Image API succeeded: $it") } }
            val hashtagFuture = executor.submit<HashTagSucceed> { hashtag(query).also { logger.info("HashTag API succeeded: $it") } }

            return FundingItem(
                draft = draftFuture.get(),
                image = imageFuture.get(),
                hashtag = hashtagFuture.get().hashtags,
            )
        }
    }
}
