package studio.o7.octopus.plugin.api.adapters;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.net.InetSocketAddress;

public final class InetSocketAddressAdapter implements JsonSerializer<InetSocketAddress>, JsonDeserializer<InetSocketAddress> {
    @Override
    public InetSocketAddress deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        String[] parts = json.getAsString().split(":", 2);
        if (parts.length != 2) throw new JsonParseException("Invalid InetSocketAddress format: " + json.getAsString());
        String host = parts[0];
        int port = Integer.parseInt(parts[1]);
        return InetSocketAddress.createUnresolved(host, port);
    }

    @Override
    public JsonElement serialize(InetSocketAddress add, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(add.getHostString() + ":" + add.getPort());
    }
}
