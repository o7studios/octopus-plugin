package studio.o7.octopus.plugin;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import studio.o7.octopus.plugin.api.Octopus;
import studio.o7.octopus.plugin.api.client.OctopusClient;
import studio.o7.octopus.plugin.client.OctopusClientImpl;

public final class OctopusImpl implements Octopus {

    private final OctopusClient client;
    private final String identifier;
    private final String host;
    private final int port;

    public OctopusImpl(Plugin plugin) {
        this.identifier = "test-minecraft-plugin";
        this.host = "127.0.0.1";
        this.port = 50051;
        this.client = new OctopusClientImpl(host, port, plugin, identifier);
    }

    @Override
    public @NotNull OctopusClient getClient() {
        return client;
    }

    @Override
    public @NotNull JavaPlugin getLibraryPlugin() {
        return JavaPlugin.getPlugin(OctopusPlugin.class);
    }

    @Override
    public @NotNull String getIdentifier() {
        return identifier;
    }

    @Override
    public @NotNull String getHost() {
        return host;
    }

    @Override
    public int getPort() {
        return port;
    }

}