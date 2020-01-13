import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.allopen.gradle.AllOpenExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "optionalrelation"
version = "0.1"

val assertjVersion = "3.14.0"
val jacksonVersion = "2.10.1"
val junitVersion = "5.5.1"
val kotlinVersion = "1.3.61"
val logbackVersion = "1.2.3"
val mariaDbVersion = "2.5.1"
val micronautVersion = "1.2.8"
val testContainersVersion = "1.12.3"

plugins {
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
    id("com.github.johnrengelman.shadow") version "5.2.0"

    application

    kotlin("jvm") version "1.3.61"
    kotlin("kapt") version "1.3.61"
    kotlin("plugin.allopen") version "1.3.61"
}

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
    maven("https://oss.jfrog.org/oss-snapshot-local")
}

dependencyManagement {
    imports {
        mavenBom("io.micronaut:micronaut-bom:$micronautVersion")
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")

    implementation("io.micronaut:micronaut-runtime")
    implementation("io.micronaut.data:micronaut-data-jdbc:1.0.0.BUILD-SNAPSHOT")
    implementation("io.micronaut.configuration:micronaut-jdbc-hikari")
    implementation("org.mariadb.jdbc:mariadb-java-client:$mariaDbVersion")

    kapt("io.micronaut:micronaut-inject-java")
    kapt("io.micronaut.data:micronaut-data-processor:1.0.0.BUILD-SNAPSHOT")

    runtimeOnly("ch.qos.logback:logback-classic:$logbackVersion")
    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    runtimeOnly("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")

    // Test dependencies
    testImplementation("io.micronaut.test:micronaut-test-junit5")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testImplementation("org.assertj:assertj-core:$assertjVersion")
    testImplementation("org.testcontainers:testcontainers:$testContainersVersion")
    testImplementation("org.testcontainers:junit-jupiter:$testContainersVersion")
    testImplementation("org.testcontainers:mariadb:$testContainersVersion")

    kaptTest("io.micronaut:micronaut-inject-java")
    kaptTest("io.micronaut.data:micronaut-data-processor:1.0.0.BUILD-SNAPSHOT")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}

configure<AllOpenExtension> {
    annotation("io.micronaut.aop.Around")
}

application {
    mainClassName = "com.equisoft.accountservice.Application"
}

tasks {
    withType<ShadowJar> {
        mergeServiceFiles()
    }

    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            javaParameters = true
        }
    }

    test {
        useJUnitPlatform()
    }
}
