package studio.o7.octopus.plugin.api.client;

import studio.o7.octopus.sdk.gen.api.v1.EntryResponse;
import studio.o7.octopus.sdk.gen.api.v1.Event;

import java.util.List;

public interface OctopusClient {

    boolean emitEvent(Event event);

    EntryResponse getEntry(String key);

    void initializeSubscription(List<String> subscriptions);

    void updateSubscriptions(List<String> subscriptions);
}
