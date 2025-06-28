package june.infrastructure

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
        val credentials = GoogleCredentials.fromStream(ClassPathResource("service-account.json").inputStream)

        val firestore = FirestoreOptions.newBuilder()
            .setProjectId("decent-destiny-463614-g6")
            .setDatabaseId("uscode22")
            .setCredentials(credentials)
            .build()
            .service

        return firestore
    }
}
