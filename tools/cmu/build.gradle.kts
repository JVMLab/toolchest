plugins {
  kotlin("jvm") version "1.7.20"
  kotlin("plugin.serialization") version "1.7.10"
  application
}


buildscript {
  repositories { mavenCentral() }

  dependencies {
    val kotlinVersion = "1.7.20"
    classpath(kotlin("gradle-plugin", version = kotlinVersion))
    classpath(kotlin("serialization", version = kotlinVersion))
  }
}


group = "com.jvmlab.tools"
version = "0.0.1"
application {
  mainClass.set("com.jvmlab.tools.cmu.ApplicationKt")
}

repositories {
  mavenCentral()
}

dependencies {
  implementation("com.jvmlab.libs:jvmlab-commons")

  implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.5")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
}