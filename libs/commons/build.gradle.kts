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
  implementation(group = "org.snakeyaml", name = "snakeyaml-engine", version = "2.+")
  testImplementation(group = "org.junit.jupiter", name = "junit-jupiter", version = "5.+")
}


tasks.withType<Test> {
  useJUnitPlatform()
  testLogging {
    events("passed", "skipped", "failed")
  }
}
