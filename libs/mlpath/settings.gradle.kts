pluginManagement {
  repositories {
    maven { url = uri("https://dl.bintray.com/kotlin/kotlin-eap") }
    mavenCentral()
    maven { url = uri("https://plugins.gradle.org/m2/") }
  }
}

rootProject.name = "jvmlab-mlpath"
includeBuild("../../libs/commons")