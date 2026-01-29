import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import studio.o7.remora.plugin.ApiVersion
import studio.o7.remora.plugin.Load

plugins {
    id("xyz.jpenilla.run-paper") version "3.0.2"
}

dependencies {
    implementation(project(":api"))

    implementation("io.grpc:grpc-okhttp:1.78.0")
    implementation("io.grpc:grpc-protobuf:1.78.0")
    implementation("io.grpc:grpc-stub:1.78.0")
}

information {
    artifactId = "octopus"
    description = "Octopus-API paper plugin implementation"
    url = "https://github.com/o7studios/octopus-plugin"
}

plugin {
    enabled = true
    main = "studio.o7.octopus.plugin.OctopusPlugin"
    apiVersion = ApiVersion.PAPER_1_21_8
    load.set(Load.STARTUP)
}

tasks.named<ShadowJar>("shadowJar") {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    mergeServiceFiles()

    filesMatching("META-INF/services/**") {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }
}

tasks {
    runServer {
        minecraftVersion("1.21.11")
        args("--port", "25565")
    }
}
