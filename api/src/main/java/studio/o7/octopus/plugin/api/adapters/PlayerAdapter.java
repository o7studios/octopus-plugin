package studio.o7.octopus.plugin.api.adapters;

import com.google.gson.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Type;
import java.util.UUID;

public final class PlayerAdapter implements JsonSerializer<Player>, JsonDeserializer<Player> {
    @Override
    public Player deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        var object = json.getAsJsonObject();

        if (object.has("uuid")) {
            var uuid = UUID.fromString(object.get("uuid").getAsString());
            var player = Bukkit.getPlayer(uuid);
            if (player != null) return player;

            var offlinePlayer = Bukkit.getOfflinePlayer(uuid);
            return offlinePlayer.isOnline() ? offlinePlayer.getPlayer() : null;
        }

        if (object.has("name")) {
            var name = object.get("name").getAsString();
            return Bukkit.getPlayer(name);
        }

        return null;
    }

    @Override
    public JsonElement serialize(Player player, Type typeOfSrc, JsonSerializationContext context) {
        var object = new JsonObject();
        object.addProperty("uuid", player.getUniqueId().toString());
        object.addProperty("name", player.getName());
        return object;
    }
}
