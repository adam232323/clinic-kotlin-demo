import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.FileInputStream
import java.util.*

group = "adms"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
	mavenCentral()
}

plugins {
	id("org.springframework.boot") version "2.3.1.RELEASE"
	id("io.spring.dependency-management") version "1.0.9.RELEASE"
	id("org.liquibase.gradle") version "2.0.4"

	kotlin("jvm") version "1.3.72"
	kotlin("plugin.spring") version "1.3.72"
	kotlin("plugin.jpa") version "1.3.72"
}

var liquibaseProperties = Properties()
try{
	val fis = FileInputStream("liquibase/liquibase.properties")
	liquibaseProperties.load(fis)
}catch (ex:Exception){}

liquibase {
	activities.register("main") {
		this.arguments = mapOf(
				"logLevel" to "info",
				"changeLogFile" to "src/main/resources/db/changelog/db.changelog-master.yaml",
				"classpath" to liquibaseProperties["db.driver.classpath"],
				"driver" to liquibaseProperties["db.driver"],
				"url" to liquibaseProperties["db.url"],
				"username" to liquibaseProperties["db.username"],
				"password" to liquibaseProperties["db.password"])
	}
}

val liquibaseCore = "org.liquibase:liquibase-core:3.10.0"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation(liquibaseCore)

	implementation("io.springfox:springfox-swagger-ui:2.9.2")
	implementation("io.springfox:springfox-swagger2:2.9.2")

	liquibaseRuntime(liquibaseCore)
	liquibaseRuntime("org.liquibase:liquibase-groovy-dsl:2.1.1")
	liquibaseRuntime("ch.qos.logback:logback-core:1.2.3")
	liquibaseRuntime("ch.qos.logback:logback-classic:1.2.3")
	liquibaseRuntime("org.yaml:snakeyaml:1.15")

	runtimeOnly("com.h2database:h2")
	runtimeOnly("mysql:mysql-connector-java:8.0.18")

	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}
	testImplementation("io.rest-assured:spring-mock-mvc:4.3.0")
	testImplementation("org.json:json:20200518")
}


tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}

tasks.test {
	useJUnitPlatform()
}
