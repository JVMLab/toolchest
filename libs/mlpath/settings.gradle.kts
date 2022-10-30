pluginManagement {
  repositories {
    mavenCentral()
    maven { url = uri("https://plugins.gradle.org/m2/") }
  }
}

rootProject.name = "jvmlab-mlpath"
includeBuild("../../libs/commons")