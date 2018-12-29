import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


buildscript {

  repositories {
    maven(url = "https://dl.bintray.com/kotlin/kotlin-eap")
    mavenCentral()
  }

  dependencies {
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.20-eap-52")
  }
}

apply(plugin = "kotlin")

group = "com.jvmlab.libs"


repositories {
  maven(url = "https://dl.bintray.com/kotlin/kotlin-eap")
  mavenCentral()
}

dependencies {
  "implementation"("org.jetbrains.kotlin:kotlin-stdlib")
  "implementation"("org.snakeyaml:snakeyaml-engine:1.+")
}