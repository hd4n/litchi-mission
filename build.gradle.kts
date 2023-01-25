import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.21"
    application
    `maven-publish`
}

group = "org.hd4n"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}

publishing{
    publications{
        create<MavenPublication>("maven"){
            groupId="org.hd4n"
            artifactId="litchi-mission"
            version="1.0"

            from(components["java"])
        }
    }
}