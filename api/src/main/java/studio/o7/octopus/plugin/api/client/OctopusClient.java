package studio.o7.octopus.plugin.api.client;

import lombok.NonNull;
import studio.o7.octopus.sdk.gen.api.v1.Entry;
import studio.o7.octopus.sdk.gen.api.v1.Event;

import java.util.Collection;
import java.util.List;

public interface OctopusClient {

    boolean publishEvent(@NonNull Event event);

    List<Entry> getEntry(@NonNull String key);

    void updateSubscriptions(@NonNull Collection<String> subscriptions);
}
