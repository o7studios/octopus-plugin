package studio.o7.octopus.test.command;

import com.google.protobuf.Value;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import studio.o7.octopus.plugin.api.client.OctopusClient;
import studio.o7.octopus.sdk.gen.api.v1.Action;
import studio.o7.octopus.sdk.gen.api.v1.Event;

public class MessageCommand implements CommandExecutor {

    private final OctopusClient client;

    public MessageCommand(OctopusClient client) {
        this.client = client;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (!(commandSender instanceof Player player)) return true;
        if (strings.length < 2) return false;

        var playerName = strings[0];
        var offlinePlayer = Bukkit.getOfflinePlayer(playerName);

        var message = Value.newBuilder().setStringValue(String.join(" ", strings)).build();
        var receiver = Value.newBuilder().setStringValue(offlinePlayer.getUniqueId().toString()).build();
        var action = Action
                .newBuilder()
                .setActionName("PRIVATE_MESSAGE")
                .putMetadata("receiver", receiver)
                .putMetadata("message", message)
                .build();

        var key = "communication::private::" + player.getUniqueId();

        var event = Event.newBuilder().setKey(key).setAction(action).build();
        player.sendMessage("Sent private message to " + playerName);
        client.emitEvent(event);

        return false;
    }
}