plugins {
    kotlin("jvm") version "1.7.10"
    `maven-publish`
    application
}

group = "me.theseems"
version = parent!!.version

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    implementation(project(":tinybench-api"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
