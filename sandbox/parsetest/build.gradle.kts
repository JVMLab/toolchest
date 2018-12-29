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

plugins {
  application
}


application {
  mainClassName = "com.jvmlab.sandbox.parsetest.ApplicationKt"
}

repositories {
  maven(url = "https://dl.bintray.com/kotlin/kotlin-eap")
  mavenCentral()
}

dependencies {
  "compile"("org.jetbrains.kotlin:kotlin-stdlib")
  "compile"("com.jvmlab.libs:jvmlab-commons")
}