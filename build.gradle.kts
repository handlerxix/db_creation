plugins {
    java
    kotlin("jvm") version "1.3.+" apply false
    application
    `kotlin-dsl`
    `java-library`
}

group = "org.example.db.creation"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
val lombokVersion = "1.18.22"

subprojects {

    apply {
        plugin("org.jetbrains.kotlin.jvm")
    }

    group = "org.example.db.creation"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    dependencies {

        compileOnly("com.intellij:annotations:12.0")

        testImplementation("junit:junit:4.12")
        testCompileOnly("com.intellij:annotations:12.0")
    }
}