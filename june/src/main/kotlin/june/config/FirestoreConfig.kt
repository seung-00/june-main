package june.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.FirestoreOptions
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource

@Configuration
class FirestoreConfig {

    @Bean
    fun firestore(): Firestore {
        val credentials: GoogleCredentials = if (isCloudRun()) {
            // Cloud Run 환경: 자동 인증
            GoogleCredentials.getApplicationDefault()
        } else {
            // 로컬 개발환경: json 파일을 통한 인증
            val inputStream = ClassPathResource("service-account.json").inputStream
            GoogleCredentials.fromStream(inputStream)
        }

        return FirestoreOptions.newBuilder()
            .setProjectId("decent-destiny-463614-g6")
            .setDatabaseId("uscode22")
            .setCredentials(credentials)
            .build()
            .service
    }

    private fun isCloudRun(): Boolean {
        // Cloud Run에서는 이 환경변수가 항상 존재함
        return System.getenv("K_SERVICE") != null
    }
}