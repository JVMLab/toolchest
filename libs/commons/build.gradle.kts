plugins {
  kotlin("jvm") version "1.3.20-eap-52"
}


group = "com.jvmlab.libs"

repositories {
  maven(url = "https://dl.bintray.com/kotlin/kotlin-eap")
  mavenCentral()
}

dependencies {
  implementation(kotlin("stdlib"))
  implementation(group = "org.snakeyaml", name = "snakeyaml-engine", version = "1.+")
  testCompile("org.junit.jupiter:junit-jupiter-api:5.3.2")
  testCompile("org.junit.jupiter:junit-jupiter-params:5.3.2")
  testRuntime("org.junit.jupiter:junit-jupiter-engine:5.3.2")
  testCompile("org.assertj:assertj-core:3.11.1")
}


tasks.withType<Test> {
  useJUnitPlatform()
  testLogging {
    events("passed", "skipped", "failed")
  }
}