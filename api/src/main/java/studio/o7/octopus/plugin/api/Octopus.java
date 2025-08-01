package studio.o7.octopus.plugin.api;

import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;
import studio.o7.octopus.plugin.api.client.OctopusClient;
import studio.o7.octopus.plugin.Unsafe;

@NullMarked
public interface Octopus {

    static Octopus get() {
        return Unsafe.getInstance().get();
    }

    OctopusClient getClient();

    JavaPlugin getLibraryPlugin();

    String getIdentifier();

    String getHost();

    int getPort();
}
