package studio.o7.octopus.test.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import studio.o7.octopus.plugin.api.client.OctopusClient;
import studio.o7.octopus.plugin.api.events.OctopusActionEvent;

import java.util.ArrayList;
import java.util.List;

public class OctopusListener implements Listener {
    private final List<String> keys;
    private final OctopusClient client;

    public OctopusListener(OctopusClient client) {
        this.client = client;
        this.keys = new ArrayList<>();
    }

    /*
     * The OctopusActionEvent will get called when an Action is sent over a subscription
     */
    @EventHandler
    public void onAction(OctopusActionEvent event) {
        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(event.getKey()));

        // Checking if it's the right action
        if (event.getAction().getActionName().equals("PRIVATE_MESSAGE")) {

            // Reading the Actions body
            var playerUniqueId = event.getAction().getMetadataMap().get("receiver").getStringValue();
            var message = event.getAction().getMetadataMap().get("message").getStringValue();

            for (var player : Bukkit.getOnlinePlayers()) {
                if (player.getUniqueId().toString().equals(playerUniqueId)) {

                    // Performs the direct message
                    player.sendMessage(message);
                }
            }
        }
    }

    /*
     * Updates the subscription list when a user joins
     */
    @EventHandler
    public void onJoin(org.bukkit.event.player.PlayerJoinEvent event) {
        var key = "communication::private::" + event.getPlayer().getUniqueId();
        System.out.println("Subscribing to " + key);
        keys.add(key);

        client.updateSubscriptions(keys);
    }

    /*
     * Updates the subscription list when a user leaves
     */
    @EventHandler
    public void onQuit(org.bukkit.event.player.PlayerQuitEvent event) {
        var key = "communication::private::" + event.getPlayer().getUniqueId();
        System.out.println("Unsubscribing from " + key);
        keys.remove(key);

        client.updateSubscriptions(keys);
    }
}
