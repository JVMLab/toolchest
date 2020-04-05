import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version "1.3.71"
}

group = "com.jvmlab.libs"


repositories {
  maven(url = "https://dl.bintray.com/kotlin/kotlin-eap")
  mavenCentral()
}

dependencies {
  implementation(kotlin("stdlib"))
  implementation("com.jvmlab.libs:jvmlab-commons")
}

