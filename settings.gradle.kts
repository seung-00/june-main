// https://docs.gradle.org/current/userguide/plugins.html#sec:plugin_version_management
pluginManagement {
  val kotlinVersion: String by settings
  val springVersion: String by settings

  plugins {
    id("org.springframework.boot") version springVersion
    id("io.spring.dependency-management") version "1.1.5"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    kotlin("plugin.jpa") version kotlinVersion
    kotlin("kapt") version kotlinVersion
  }
}
plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

rootProject.name = "june-main"
include("june")
