import studio.o7.remora.plugin.ApiVersion
import studio.o7.remora.plugin.Load

dependencies {
    implementation(project(":api"))
}

information {
    artifactId = "octopus"
    description = "Octopus paper plugin"
}

plugin {
    enabled = true
    main = "studio.o7.octopus.plugin.OctopusPlugin"
    apiVersion = ApiVersion.PAPER_1_21_8
    load.set(Load.POST_WORLD)
}
