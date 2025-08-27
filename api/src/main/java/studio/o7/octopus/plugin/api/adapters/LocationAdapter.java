package studio.o7.octopus.plugin.api.adapters;

import com.google.gson.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.lang.reflect.Type;
import java.util.Optional;
import java.util.UUID;

public final class LocationAdapter implements JsonSerializer<Location>, JsonDeserializer<Location> {
    @Override
    public Location deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        var object = json.getAsJsonObject();

        var worldId = object.has("world") ? UUID.fromString(object.get("world").getAsString()) : null;
        var world = worldId != null ? Bukkit.getWorld(worldId) : null;

        double x = object.has("x") ? object.get("x").getAsDouble() : 0d;
        double y = object.has("y") ? object.get("y").getAsDouble() : 0d;
        double z = object.has("z") ? object.get("z").getAsDouble() : 0d;

        float yaw = object.has("yaw") ? object.get("yaw").getAsFloat() : 0f;
        float pitch = object.has("pitch") ? object.get("pitch").getAsFloat() : 0f;

        return new Location(world, x, y, z, yaw, pitch);
    }

    @Override
    public JsonElement serialize(Location src, Type typeOfSrc, JsonSerializationContext context) {
        var object = new JsonObject();
        object.addProperty("world", Optional.ofNullable(src.getWorld()).map(World::getUID).map(UUID::toString).orElse(null));
        object.addProperty("x", src.getX());
        object.addProperty("y", src.getY());
        object.addProperty("z", src.getZ());
        object.addProperty("yaw", src.getYaw());
        object.addProperty("pitch", src.getPitch());

        return object;
    }
}
