package studio.o7.octopus.plugin.api.adapters;

import com.google.gson.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;

import java.lang.reflect.Type;

public final class ComponentAdapter implements JsonSerializer<Component>, JsonDeserializer<Component> {
    @Override
    public JsonElement serialize(Component component, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(JSONComponentSerializer.json().serialize(component));
    }

    @Override
    public Component deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return JSONComponentSerializer.json().deserialize(jsonElement.getAsString());
    }
}
