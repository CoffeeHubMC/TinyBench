import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val nexusURL: String by project
val devNexusURL: String by project
val coffeehubUsername: String by project
val coffeehubPassword: String by project

plugins {
    kotlin("jvm") version "1.7.10"
    `maven-publish`
    application
}

fun getVersionName(): String {
    val stdout = `java.io`.ByteArrayOutputStream()
    exec {
        commandLine("git", "describe", "--tags")
        standardOutput = stdout
    }
    return stdout.toString().trim()
}
ext {
    set("devBuild", true)
}

group = "me.theseems"
version = if (ext["devBuild"] as Boolean) getVersionName() else "1.0.2"

repositories {
    mavenCentral()
    maven {
        name = "coffeehub"
        url = uri(nexusURL)
    }
}

dependencies {
    testImplementation(kotlin("test"))
}

val devBuildEnabled = ext["devBuild"] as Boolean
subprojects {
    afterEvaluate {
        repositories {
            mavenCentral()
            maven {
                name = "coffeehub"
                url = uri(nexusURL)
            }
        }
        publishing {
            publications {
                create<MavenPublication>(project.name) {
                    groupId = "me.theseems"
                    artifactId = project.name
                    version = project.version.toString()

                    from(components["java"])
                }
            }

            repositories {
                maven {
                    name = "coffeehub"
                    url = uri(if (devBuildEnabled) devNexusURL else nexusURL)
                    credentials {
                        username = coffeehubUsername
                        password = coffeehubPassword
                    }
                }
            }
        }
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
