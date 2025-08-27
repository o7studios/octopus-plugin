package studio.o7.octopus.plugin.api.adapters;

import com.google.gson.*;
import net.kyori.adventure.resource.ResourcePackInfo;

import java.lang.reflect.Type;
import java.net.URI;
import java.util.UUID;

public final class ResourcePackInfoAdapter implements JsonSerializer<ResourcePackInfo>, JsonDeserializer<ResourcePackInfo> {
    @Override
    public ResourcePackInfo deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        var object = json.getAsJsonObject();
        var id = UUID.fromString(object.get("id").getAsString());
        var uri = URI.create(object.get("uri").getAsString());
        var hash = object.get("hash").getAsString();
        return ResourcePackInfo.resourcePackInfo(id, uri, hash);
    }

    @Override
    public JsonElement serialize(ResourcePackInfo info, Type type, JsonSerializationContext jsonSerializationContext) {
        var object = new JsonObject();
        object.addProperty("id", info.id().toString());
        object.addProperty("uri", info.uri().toString());
        object.addProperty("hash", info.hash());
        return object;
    }
}
