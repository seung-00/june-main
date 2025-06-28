package june

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class JuneApplication

fun main(args: Array<String>) {
    System.setProperty("GOOGLE_APPLICATION_CREDENTIALS", "src/main/resources/service-account.json")

    runApplication<JuneApplication>(*args)
}
