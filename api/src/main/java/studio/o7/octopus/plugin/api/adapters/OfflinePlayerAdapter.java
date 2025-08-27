package studio.o7.octopus.plugin.api.adapters;

import com.google.gson.*;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.lang.reflect.Type;
import java.util.UUID;

public final class OfflinePlayerAdapter implements JsonSerializer<OfflinePlayer>, JsonDeserializer<OfflinePlayer> {

    @Override
    public OfflinePlayer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        var object = json.getAsJsonObject();

        if (object.has("uuid")) {
            var uuid = UUID.fromString(object.get("uuid").getAsString());
            return Bukkit.getOfflinePlayer(uuid);
        }

        if (object.has("name")) {
            var name = object.get("name").getAsString();
            return Bukkit.getOfflinePlayer(name);
        }

        return null;
    }

    @Override
    public JsonElement serialize(OfflinePlayer player, Type typeOfSrc, JsonSerializationContext context) {
        var object = new JsonObject();
        object.addProperty("uuid", player.getUniqueId().toString());
        object.addProperty("name", player.getName());
        return object;
    }
}
