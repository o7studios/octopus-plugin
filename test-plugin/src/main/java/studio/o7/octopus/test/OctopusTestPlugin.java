package studio.o7.octopus.test;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import studio.o7.octopus.plugin.api.Octopus;
import studio.o7.octopus.plugin.api.client.OctopusClient;
import studio.o7.octopus.test.command.MessageCommand;
import studio.o7.octopus.test.listeners.OctopusListener;

import java.util.List;

/*
 * This is a little example plugin implementing a direct message system.
 *
 * Use:
 * /msg <player_name> <message>
 * To send a player a direct message.
 *
 * It uses the Octopus Action
 */
public final class OctopusTestPlugin extends JavaPlugin {

    @Override
    public void onEnable() {

        // Access Octopus when it's loaded
        // Remember to depend on Octopus
        Octopus octopus = Octopus.get();

        // Access the client
        OctopusClient client = octopus.getClient();

        PluginManager pluginManager = Bukkit.getPluginManager();

        // Here is the action event (when a server emits an action)
        pluginManager.registerEvents(new OctopusListener(client), this);

        // The action gets emitted in this command
        getCommand("msg").setExecutor(new MessageCommand(client));

        // When using subscriptions. You need to initialize it first
        client.initializeSubscription(List.of("test"));
    }

}
