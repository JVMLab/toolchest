plugins {
  kotlin("jvm") version "1.7.20"
  application
}


application {
  mainClassName = "com.jvmlab.sandbox.parsetest.ApplicationKt"
}

repositories {
  mavenCentral()
}

dependencies {
  implementation(kotlin("stdlib"))
  implementation("com.jvmlab.libs:jvmlab-commons")
}
