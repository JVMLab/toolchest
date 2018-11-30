plugins {
  kotlin("jvm") version "1.3.10"
}

group = "com.jvmlab.libs"

tasks.named<Jar>("jar") {
  baseName = "jvmlab-commons"
}

repositories {
  jcenter()
}

dependencies {
  implementation(kotlin("stdlib"))
  implementation(group = "org.snakeyaml", name = "snakeyaml-engine", version = "1.+")
}