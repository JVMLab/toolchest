import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version "1.3.72"
}

group = "com.jvmlab.libs"


repositories {
  maven(url = "https://dl.bintray.com/kotlin/kotlin-eap")
  mavenCentral()
}

dependencies {
  implementation(kotlin("stdlib-jdk8"))
  implementation("com.jvmlab.libs:jvmlab-commons")
  testImplementation(group = "org.junit.jupiter", name = "junit-jupiter", version = "5.+")
}


tasks.withType<KotlinCompile>().configureEach {
  kotlinOptions.jvmTarget = "11"
}

tasks.withType<Test> {
  useJUnitPlatform()
  testLogging {
    events("passed", "skipped", "failed")
  }
}
