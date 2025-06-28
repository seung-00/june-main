plugins {
    kotlin("jvm") version "2.0.0"
    kotlin("plugin.spring")
}

repositories {
    mavenCentral()
}

dependencies {
    // logging
    implementation("org.slf4j:slf4j-api:2.0.9")
    implementation("ch.qos.logback:logback-classic:1.4.11")

    // gemini
    implementation("com.google.genai:google-genai:1.5.0")
    implementation("com.google.firebase:firebase-admin:9.5.0")
    implementation("com.google.cloud:google-cloud-firestore:3.31.6")

    testImplementation("com.h2database:h2")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.testcontainers:gcloud:1.20.1")
    testImplementation(kotlin("test"))

    // okhttp3
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    // jackson
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.2")

    // Firestore + 테스트용 NoCredentials
    implementation("com.google.cloud:google-cloud-firestore")

    // Testcontainers for Firestore Emulator
    testImplementation("org.testcontainers:testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")

    // JUnit 5 + AssertJ
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.assertj:assertj-core")
}

kotlin {
    jvmToolchain(21)
}

tasks {
    bootJar {
        enabled = true
    }
    jar {
        enabled = true
    }
}

tasks.test {
    useJUnitPlatform()
    jvmArgs = listOf("-Xmx2g") // JVM 힙 크기를 2GB로 설정
}

sourceSets {
    test {
        java.srcDirs("src/test/kotlin")
    }
}
