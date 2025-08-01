plugins {
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

dependencies {
    compileOnly(project(":api"))
}

val copyPluginJar by tasks.registering(Copy::class) {

    doFirst {
        println("Copying plugin JAR to plugins folder...")
    }

    dependsOn(":plugin:shadowJar")

    from(project.file("../plugin/build/libs/octopus-plugin.jar"))
    into(project.file("../test-plugin/run/plugins/"))
}


tasks {
    runServer {
        dependsOn(copyPluginJar)

        minecraftVersion("1.21.8")
    }
}