repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    api("studio.o7:octopus-sdk:0.5.8")
    compileOnly("io.papermc.paper:paper-api:1.21.8-R0.1-SNAPSHOT")
}

information {
    artifactId = "octopus-plugin-api"
    description = "Octopus Paper plugin api"
    url = "https://o7.studio"

    developers {
        developer {
            id = "julian-siebert"
            name = "Julian Siebert"
            email = "julian.siebert@o7.studio"
        }
        developer {
            id = "raphael-goetz"
            name = "Raphael Goetz"
            email = "raphael.goetz@o7.studio"
        }
    }

    scm {
        connection = "scm:git:git://github.com/o7studios/octopus-plugin.git"
        developerConnection = "scm:git:git@https://github.com/o7studios/octopus-plugin.git"
        url = "https://github.com/o7studios/octopus-plugin"
        tag = "HEAD"
    }

    ciManagement {
        system = "GitHub Actions"
        url = "https://github.com/o7studios/octopus-plugin/actions"
    }

    licenses {
        license {
            name = "GNU General Public License, Version 3"
            url = "https://www.gnu.org/licenses/gpl-3.0.txt"
        }
    }
}
