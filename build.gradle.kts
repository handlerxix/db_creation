plugins {
    java
    kotlin("jvm") version "1.4.31"
}

group = "org.example.db.creation"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
val lombokVersion = "1.18.22"

subprojects {

    apply {
        plugin("kotlin")
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