plugins {
    id("java")
    id("me.champeau.jmh") version "0.7.2" // JMH 플러그인 추가
}

group = "io.devchw"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}
