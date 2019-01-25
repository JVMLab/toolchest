import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.20"
}

group = "com.jvmlab.libs"


repositories {
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.jvmlab.libs:jvmlab-commons")
}

