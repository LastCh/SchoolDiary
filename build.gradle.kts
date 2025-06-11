plugins {
    id("java")
    id("war")
}

group = "ru.bmstu"
version = "1.0"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    // Spring MVC
    implementation("org.springframework:spring-webmvc:5.3.33")
    implementation("org.springframework:spring-test:5.3.33")

    // Swagger (SpringFox)
    implementation("io.springfox:springfox-swagger2:3.0.0")
    implementation("io.springfox:springfox-swagger-ui:3.0.0")

    // JSON
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.5")

    // Servlet API
    implementation("javax.servlet:javax.servlet-api:4.0.1")

    // JUnit
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")
    testImplementation("org.springframework:spring-test:5.3.33")

    implementation("org.aspectj:aspectjrt:1.9.9.1")
    implementation("org.aspectj:aspectjweaver:1.9.9.1")

    testImplementation("org.mockito:mockito-core:5.14.2")
    testImplementation("org.mockito:mockito-junit-jupiter:5.14.2")
    testImplementation("org.hamcrest:hamcrest:2.2")
    implementation("org.slf4j:slf4j-simple:2.0.16")
}

tasks.test {
    useJUnitPlatform()
}
