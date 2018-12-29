plugins {
  kotlin("jvm") version "1.3.20-eap-52"
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
  implementation(kotlin("stdlib"))
  compile("com.jvmlab.libs:jvmlab-commons")
}