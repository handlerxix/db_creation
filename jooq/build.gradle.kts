plugins {
    id("nu.studer.jooq") version "6.0.1"
}

val jooqVersion = "3.15.4"

dependencies {
    compile("org.jooq:jooq:$jooqVersion")
    api("org.jooq:jooq:$jooqVersion")
    implementation("org.jooq:jooq-codegen:$jooqVersion")
    implementation("org.jooq:jooq-meta:$jooqVersion")

    jooqGenerator("org.postgresql:postgresql:42.2.9")
}

jooq {
    version.set(jooqVersion)
    edition.set(nu.studer.gradle.jooq.JooqEdition.OSS)
    configurations {
        create("main") {
            generateSchemaSourceOnCompilation.set(false)

            jooqConfiguration.apply {
                logging = org.jooq.meta.jaxb.Logging.INFO
                jdbc.apply {
                    driver = "org.postgresql.Driver"
                    url = "jdbc:postgresql://127.0.0.1:5432/local_db"
                    user = "postgres"
                    password = "postgres"
                }

                generator.apply {
                    name = "org.jooq.codegen.DefaultGenerator"
                    database.apply {
                        name = "org.jooq.meta.postgres.PostgresDatabase"
                        inputSchema = "public"
                        excludes = "flyway_schema_history"
                    }

                    generate.apply {
                        isRelations = true
                        isDeprecated = false
                        isRecords = true
                        isImmutableInterfaces = false
                        isFluentSetters = true
                    }

                    target.apply {
                        packageName = "generated"
                        directory = "src/main/java"
                    }

                    strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
                }
            }
        }
    }
}