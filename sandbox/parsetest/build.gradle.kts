plugins {
  kotlin("jvm") version "1.7.20"
  application
}


application {
  mainClass.set("com.jvmlab.sandbox.parsetest.ApplicationKt")
}

repositories {
  mavenCentral()
}

dependencies {
  implementation("com.jvmlab.libs:jvmlab-commons")
}
