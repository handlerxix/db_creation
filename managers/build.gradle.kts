dependencies {
    implementation("org.flywaydb:flyway-core:6.1.4")
    implementation("org.postgresql:postgresql:42.2.9")
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation(project(":jooq"))

    compileOnly("org.projectlombok:lombok:1.18.22")
    annotationProcessor("org.projectlombok:lombok:1.18.22")
}