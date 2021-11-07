plugins {
    java
}

group = "org.example.db.creation"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.flywaydb:flyway-core:6.1.4")
    implementation("org.postgresql:postgresql:42.2.9")

    compileOnly("com.intellij:annotations:12.0")
}