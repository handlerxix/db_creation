plugins {
    java
}

group = "org.example.db.creation"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val jettyVersion = "9.4.26.v20200117"

dependencies {
    implementation("org.eclipse.jetty:jetty-rewrite:$jettyVersion")
    implementation("org.eclipse.jetty:jetty-server:$jettyVersion")
    implementation("org.eclipse.jetty:jetty-servlet:$jettyVersion")
}