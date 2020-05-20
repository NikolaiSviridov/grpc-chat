import com.google.protobuf.gradle.*

plugins {
    java
    kotlin("jvm") version "1.3.72"
    id("com.google.protobuf") version "0.8.8"
    application
}

group = "com.firsttimeinforever.chat"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
}

application {
    mainClassName = "com.firsttimeinforever.chat.MainKt"
}

val protobufVersion = "3.11.0"
val grpcVersion = "1.29.0"

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.grpc:grpc-protobuf:${grpcVersion}")
    implementation("io.grpc:grpc-stub:${grpcVersion}")
    runtimeOnly("io.grpc:grpc-netty-shaded:${grpcVersion}")
    testImplementation("io.grpc:grpc-testing:${grpcVersion}")
    compileOnly("org.apache.tomcat:annotations-api:6.0.53")
    compileOnly("org.jetbrains:annotations:16.0.2")
    implementation("com.rabbitmq", "amqp-client", "5.7.0")
    implementation("org.slf4j", "slf4j-api", "1.7.30")
    implementation("org.slf4j", "slf4j-simple", "1.7.30")
    testCompile("junit", "junit", "4.12")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

sourceSets {
    main {
        java {
            srcDirs("src/main/grpc", "src/main/java")
        }
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${protobufVersion}"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${grpcVersion}"
        }
    }
    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins {
                id("grpc")
            }
        }
    }
    generatedFilesBaseDir = "$projectDir/src"
}
