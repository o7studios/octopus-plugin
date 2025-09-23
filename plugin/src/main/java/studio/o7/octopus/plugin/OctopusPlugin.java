package studio.o7.octopus.plugin;

import lombok.Getter;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import studio.o7.octopus.plugin.api.Octopus;

@Getter
public final class OctopusPlugin extends JavaPlugin implements PluginInstance {

    private OctopusImpl octopus;

    public OctopusPlugin() {
        Unsafe.setInstance(this);
    }

    @Override
    public void onEnable() {
        octopus = new OctopusImpl();
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public Octopus get() {
        return this.octopus;
    }
}