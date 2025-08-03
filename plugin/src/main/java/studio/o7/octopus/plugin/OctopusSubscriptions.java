package studio.o7.octopus.plugin;

import io.grpc.stub.StreamObserver;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.slf4j.Logger;
import studio.o7.octopus.plugin.api.Octopus;
import studio.o7.octopus.plugin.api.events.OctopusActionEvent;
import studio.o7.octopus.plugin.api.events.OctopusEntryMutationEvent;
import studio.o7.octopus.plugin.api.events.OctopusPlayerNotificationEvent;
import studio.o7.octopus.sdk.gen.api.v1.Event;
import studio.o7.octopus.sdk.gen.api.v1.EventPublication;
import studio.o7.octopus.sdk.gen.api.v1.EventSubscriptionUpdate;
import studio.o7.octopus.sdk.gen.api.v1.OctopusServiceGrpc;

import java.util.Collection;

@RequiredArgsConstructor
public final class OctopusSubscriptions {
    private final Plugin plugin;
    private final Logger logger;
    private final OctopusServiceGrpc.OctopusServiceStub stub;

    private StreamObserver<EventSubscriptionUpdate> subscriptionObserver;

    private synchronized void init() {
        logger.info("Initializing event subscription observer");
        subscriptionObserver = stub.subscription(new Observer(logger));
    }

    public void updateSubscriptions(@NonNull Collection<String> subscriptions) {
        try {
            if (subscriptionObserver == null) {
                init();
                updateSubscriptions(subscriptions);
                return;
            }

            var update = EventSubscriptionUpdate.newBuilder()
                    .setIdentifier(Octopus.get().getIdentifier())
                    .addAllKeys(subscriptions)
                    .build();

            subscriptionObserver.onNext(update);
            logger.debug("Updated event subscriptions");
        } catch (Exception exception) {
            logger.error("Failed to update event subscriptions", exception);
        }
    }

    @RequiredArgsConstructor
    static class Observer implements StreamObserver<EventPublication> {
        private final Logger logger;

        @Override
        public void onNext(EventPublication pub) {
            var pm = Bukkit.getPluginManager();

            for (Event event : pub.getEventsList()) {
                var key = event.getKey();
                logger.debug("Event publication on key {}", key);
                switch (event.getBodyCase()) {
                    case PLAYER_NOTIFICATION -> pm.callEvent(new OctopusPlayerNotificationEvent(key, event.getPlayerNotification()));
                    case ENTRY_MUTATION -> pm.callEvent(new OctopusEntryMutationEvent(key, event.getEntryMutation()));
                    case ACTION -> pm.callEvent(new OctopusActionEvent(key, event.getAction()));
                    case BODY_NOT_SET -> logger.error("Event subscription on key {} without body", key);
                }
            }
        }

        @Override
        public void onError(Throwable throwable) {
            logger.error("Event subscription error", throwable);
        }

        @Override
        public void onCompleted() {
            logger.info("Event subscription observer completed");
        }
    }
}
