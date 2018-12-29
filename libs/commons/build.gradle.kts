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
}