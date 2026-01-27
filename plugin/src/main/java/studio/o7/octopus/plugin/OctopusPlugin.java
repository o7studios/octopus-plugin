package studio.o7.octopus.plugin;

import lombok.Getter;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import studio.o7.octopus.plugin.api.Octopus;
import studio.o7.octopus.plugin.channel.OctopusChannelFactory;

@Getter
public final class OctopusPlugin extends JavaPlugin implements PluginInstance {

    private OctopusImpl octopus;
    private String token;

    public OctopusPlugin() {
        Unsafe.setInstance(this);
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();

        String token = getConfig().getString("octopus.token");
        String host = getConfig().getString("octopus.host");
        int port = getConfig().getInt("octopus.port");

        octopus = new OctopusImpl(token, host, port);
    }

    @Override
    public void onDisable() {
        OctopusChannelFactory.shutdown();
        HandlerList.unregisterAll(this);
    }

    @Override
    public Octopus get() {
        return this.octopus;
    }
}