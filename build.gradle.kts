import systems.conduit.stream.gradle.ConduitExtension

// TODO: Add to stream somehow?
plugins {
    kotlin("jvm") version "1.3.72"
}

buildscript {
    repositories {
        mavenLocal()
        jcenter()
        maven {
            url = uri("https://repo.conduit.systems/repository/releases/")
        }
    }
    dependencies {
        classpath("systems.conduit:Stream:1.0.1")
    }
}
apply(plugin = "Stream")

dependencies {
    implementation("org.reflections:reflections:0.9.11")
    implementation("org.jline:jline:3.13.0")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.11.2")
    implementation("com.zaxxer:HikariCP:3.4.1")
    implementation("redis.clients:jedis:2.8.1")
}

group = "systems.conduit"
version = "0.0.5"

configure<ConduitExtension> {
    minecraft = "1.15.2"
}
