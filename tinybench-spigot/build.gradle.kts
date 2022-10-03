plugins {
    kotlin("jvm") version "1.7.10"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

val nexusURL: String by project
val coffeehubUsername: String by project
val coffeehubPassword: String by project

group = "me.theseems"
version = parent!!.version

repositories {
    mavenCentral()
    maven {
        name = "papermc-repo"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        name = "sonatype"
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }
    maven {
        name = "Lumine Releases"
        url = uri("https://mvn.lumine.io/repository/maven-public/")
    }
    maven {
        name = "coffeehub"
        url = uri(nexusURL)
        credentials {
            username = coffeehubUsername
            password = coffeehubPassword
        }
    }
}

dependencies {
    implementation(project(":tinybench-api"))
    implementation(project(":tinybench-common"))

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.4")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.13.4")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.4")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.13.4")
    compileOnly("io.lumine:Mythic-Dist:5.0.3-SNAPSHOT")

    compileOnly("me.theseems:toughwiki:1.0.1:all")
    compileOnly("io.papermc.paper:paper-api:1.19.2-R0.1-SNAPSHOT")

    testImplementation("me.theseems:toughwiki:1.0.1:all")
    testImplementation(kotlin("test"))
    testImplementation("com.github.seeseemelk:MockBukkit-v1.19:2.120.1")
}

var targetJavaVersion = 17
java {
    val javaVersion = JavaVersion.toVersion(targetJavaVersion)
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
    if (JavaVersion.current() < javaVersion) {
        toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
    }
}

tasks.withType<JavaCompile>().configureEach {
    if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible) {
        options.release.set(targetJavaVersion)
    }
}

tasks.processResources {
    val props = mapOf("version" to version)
    for (prop in props) {
        inputs.property(prop.key, prop.value)
    }
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }
}

val templateSource = file("src/main/templates")
val templateDest = layout.buildDirectory.dir("generated/sources/templates")
val generateTemplates = tasks.register<Copy>("generateTemplates") {
    val props = mapOf("version" to version)
    for (prop in props) {
        inputs.property(prop.key, prop.value)
    }

    from(templateSource)
    destinationDir = templateDest.get().asFile
    to(templateDest)
    expand(props)
}

sourceSets.main {
    java.srcDir(generateTemplates.map { it.outputs })
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
