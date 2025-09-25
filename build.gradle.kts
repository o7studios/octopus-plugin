import studio.o7.remora.RemoraPlugin

plugins {
    id("studio.o7.remora") version "0.3.6"
}

allprojects {
    apply<RemoraPlugin>()

    group = "studio.o7"
}
