package studio.o7.octopus.plugin;

import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import studio.o7.octopus.plugin.api.Octopus;
import studio.o7.octopus.sdk.OctopusSDK;
import studio.o7.octopus.sdk.gen.api.v1.Entry;
import studio.o7.octopus.sdk.gen.api.v1.EntryRequest;
import studio.o7.octopus.sdk.gen.api.v1.Event;
import studio.o7.octopus.sdk.gen.api.v1.OctopusServiceGrpc;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class OctopusImpl implements Octopus {
    private final Plugin plugin;

    private final OctopusServiceGrpc.OctopusServiceStub stub = OctopusSDK.stub();
    private final OctopusServiceGrpc.OctopusServiceBlockingStub blockingStub = OctopusSDK.blockingStub();

    private final Logger logger;
    private final OctopusSubscriptions subscriptions;

    private String identifier;

    private final Collection<String> subscriptionList = Collections.synchronizedSet(new ObjectArraySet<>());

    public OctopusImpl(Plugin plugin) {
        this.plugin = plugin;
        logger = plugin.getSLF4JLogger();
        subscriptions = new OctopusSubscriptions(plugin, logger, stub);
    }

    @Override
    public boolean publishEvent(@NonNull Event event) {
        try {
            var publishEventResponse = blockingStub.publishEvent(event);
            logger.info("Published event on key {}", event.getKey());
            return publishEventResponse.getSuccess();
        } catch (Exception e) {
            logger.error("Failed to publish event on key {}", event.getKey(), e);
            return false;
        }
    }

    @Override
    public @NotNull List<Entry> getEntry(@NonNull String key) {
        try {
            return blockingStub.getEntry(EntryRequest.newBuilder().setKey(key).build()).getEntriesList();
        } catch (Exception e) {
            logger.error("Failed to retrieve entry on key {}", key, e);
            return Collections.emptyList();
        }
    }

    @Override
    public void addSubscriptions(@NonNull Collection<String> subscriptions) {
        this.subscriptionList.addAll(subscriptions);
        this.subscriptions.updateSubscriptions(subscriptionList);
    }

    @Override
    public void removeSubscriptions(@NonNull Collection<String> subscriptions) {
        this.subscriptionList.removeAll(subscriptions);
        this.subscriptions.updateSubscriptions(subscriptionList);
    }

    @Override
    public synchronized void setSubscriptions(@NonNull Collection<String> subscriptions) {
        this.subscriptionList.clear();
        this.subscriptionList.addAll(subscriptions);
        this.subscriptions.updateSubscriptions(subscriptionList);
    }

    @Override
    public @NotNull String getIdentifier() {
        if (this.identifier != null) return identifier;
        identifier = System.getProperty("octopus.identifier");
        if (identifier == null || identifier.isEmpty())
            identifier = System.getenv("OCTOPUS_IDENTIFIER");
        if (identifier == null || identifier.isEmpty())
            identifier = "default";
        return identifier;
    }
}