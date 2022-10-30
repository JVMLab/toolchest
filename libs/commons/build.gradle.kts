import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version "1.7.20"
}


group = "com.jvmlab.libs"

repositories {
  mavenCentral()
}

dependencies {
  implementation(group = "org.snakeyaml", name = "snakeyaml-engine", version = "2.+")
  testImplementation(group = "org.junit.jupiter", name = "junit-jupiter", version = "5.+")
}



tasks.withType<KotlinCompile>().configureEach {
  kotlinOptions.jvmTarget = "17"
}

tasks.withType<Test> {
  useJUnitPlatform()
  testLogging {
    events("passed", "skipped", "failed")
  }
}
