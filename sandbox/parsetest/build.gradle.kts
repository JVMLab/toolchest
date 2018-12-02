plugins {
  kotlin("jvm") version "1.3.10" 
  application
}

application {
  mainClassName = "com.jvmlab.sandbox.parsetest.ApplicationKt"
}

repositories {
  jcenter() 
}

dependencies {
  implementation(kotlin("stdlib"))
  compile("com.jvmlab.libs:commons")
}