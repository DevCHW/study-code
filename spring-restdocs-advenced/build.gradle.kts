plugins {
    kotlin("jvm")
    kotlin("kapt")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("com.epages.restdocs-api-spec")
    id("org.asciidoctor.jvm.convert")
}

val asciidoctorExt: Configuration by configurations.creating
val snippetsDir by extra { file("build/generated-snippets") }

group = "${property("projectGroup")}"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // Jackson
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // Spring
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Test - Spring Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("com.ninja-squad:springmockk:${property("springMockkVersion")}")

    // Test - restdocs
    asciidoctorExt("org.springframework.restdocs:spring-restdocs-asciidoctor")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    testImplementation("org.springframework.restdocs:spring-restdocs-restassured")
    testImplementation("io.rest-assured:spring-mock-mvc")
    testImplementation("com.epages:restdocs-api-spec-restassured:${property("restDocsApiSpecVersion")}")
    testImplementation("com.epages:restdocs-api-spec-mockmvc:${property("restDocsApiSpecVersion")}")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    outputs.dir(snippetsDir)
    useJUnitPlatform()
    systemProperty("user.timezone", "UTC")
}

tasks.asciidoctor {
    dependsOn(tasks.test)
    configurations(asciidoctorExt.name)
    baseDirFollowsSourceFile()
    inputs.dir(snippetsDir)
    outputs.file("build/docs/asciidoc")

    sources {
        include("**/index.adoc", "enums/*.adoc") // html로 만들 adoc 파일 설정.
    }
}

tasks.resolveMainClassName {
    dependsOn(tasks.getByName("copyDocument"))
}

tasks.register("copyDocument", Copy::class) {
    dependsOn(tasks.asciidoctor)
    from("build/docs/asciidoc")
    into("build/resources/main/static/docs")
    include("index.html", "enums/**")
    doFirst {
        delete(file("src/main/resources/static/docs"))
    }
}

tasks.build {
    dependsOn(tasks.getByName("copyDocument"))
}

tasks.bootJar {
    dependsOn(tasks.getByName("copyDocument"))
}

