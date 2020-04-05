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
  testImplementation("org.junit.jupiter:junit-jupiter:5.6.1")
  testImplementation("org.assertj:assertj-core:3.15.0")
}


tasks.withType<Test> {
  useJUnitPlatform()
  testLogging {
    events("passed", "skipped", "failed")
  }
}
