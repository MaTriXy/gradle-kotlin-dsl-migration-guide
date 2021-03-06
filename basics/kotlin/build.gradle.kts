import org.springframework.boot.gradle.tasks.bundling.BootJar
import org.springframework.boot.gradle.tasks.run.BootRun

buildscript {
    repositories {
        gradlePluginPortal()
    }
    dependencies {
        classpath("gradle.plugin.com.boxfuse.client:gradle-plugin-publishing:5.0.3")
    }
}

plugins {
    java
    id("jacoco")
    id("org.springframework.boot") version "2.0.1.RELEASE"
    kotlin("jvm") version "1.2.41"
}

apply(plugin = "checkstyle")
apply(plugin = "org.flywaydb.flyway")
apply(plugin = "io.spring.dependency-management")

repositories {
    mavenCentral()
}

val jar: Jar by tasks
jar.archiveName = "foo.jar"

jar.apply {
    archiveName = "foo.jar"
    into("META-INF") {
        from("bar")
    }
}

val test by tasks
test.doLast { println("test completed") }

tasks {
    "jar"(Jar::class) {
        archiveName = "foo.jar"
        into("META-INF") {
            from("bar")
        }
    }

    "test" {
        doLast { println("test completed") }
    }
}

tasks {
    "bootJar"(BootJar::class) {
        archiveName = "app.jar"
        mainClassName = "com.ninja_squad.demo.Demo"
    }

    "bootRun"(BootRun::class) {
        main = "com.ninja_squad.demo.Demo"
        args("--spring.profiles.active=demo")
    }
}

val greeting by tasks.creating {
    println("always printed: configuration phase")
    doLast {
        println("only printed if executed: execution phase")
    }
}

val docZip by tasks.creating(Zip::class) {
    archiveName = "doc.zip"
    from("doc")
}

tasks {
    "greeting2" {
        println("always printed: configuration phase")
        doLast {
            println("only printed if executed: execution phase")
        }
    }

    "docZip2"(Zip::class) {
        archiveName = "doc2.zip"
        from("doc")
    }
}


tasks {
    // get and customize the existing task named test. Fails if there is no test task.
    val test by getting {
        doLast { println("test completed") }
    }

    // create a new docZip3 task. Fails if a task docZip3 already exists.
    val docZip3 by creating(Zip::class) {
        archiveName = "doc3.zip"
        from("doc")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("io.jsonwebtoken:jjwt:0.9.0")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(module = "junit")
    }
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

val db by configurations.creating
val integTestImplementation by configurations.creating {
    extendsFrom(configurations["testImplementation"])
}

dependencies {
    db("org.postgresql:postgresql")
    integTestImplementation("com.ninja-squad:DbSetup:2.1.0")
}

// get the existing testRuntimeOnly configuration
val testRuntimeOnly by configurations

dependencies {
    testRuntimeOnly("org.postgresql:postgresql")
    "db"("org.postgresql:postgresql")
    "integTestImplementation"("com.ninja-squad:DbSetup:2.1.0")
}

jacoco {
    toolVersion = "0.8.1"
}

springBoot {
    buildInfo {
        properties {
            time = null
        }
    }
}

configure<CheckstyleExtension> {
    maxErrors = 10
}
