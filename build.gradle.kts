plugins {
    java
}

group = "org.example.db.creation"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
val lombokVersion = "1.18.22"
dependencies {
    compileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")

    implementation("org.flywaydb:flyway-core:6.1.4")
    implementation("org.postgresql:postgresql:42.2.9")
    implementation("org.apache.commons:commons-lang3:3.12.0")

    compileOnly("com.intellij:annotations:12.0")
}