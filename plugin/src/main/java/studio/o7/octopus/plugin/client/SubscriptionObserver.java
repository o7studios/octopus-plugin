package studio.o7.octopus.plugin.client;


import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import studio.o7.octopus.plugin.api.events.OctopusActionEvent;
import studio.o7.octopus.plugin.api.events.OctopusEntryChangeEvent;
import studio.o7.octopus.plugin.api.events.OctopusPlayerNotificationEvent;
import studio.o7.octopus.sdk.gen.api.v1.Event;
import studio.o7.octopus.sdk.gen.api.v1.SubscriptionResponse;

@RequiredArgsConstructor
public class SubscriptionObserver implements io.grpc.stub.StreamObserver<SubscriptionResponse> {
    private final Plugin plugin;

    @Override
    public void onNext(SubscriptionResponse response) {
        for (Event event : response.getEventsList()) {
            Bukkit.getScheduler().runTask(plugin, () -> {
                switch (event.getBodyCase()) {
                    case PLAYER_NOTIFICATION ->  Bukkit.getPluginManager().callEvent(
                            new OctopusPlayerNotificationEvent(event.getPlayerNotification(), event.getKey())
                    );
                    case BODY_NOT_SET -> Bukkit.getLogger().severe("No Body");
                    case ACTION -> Bukkit.getPluginManager().callEvent(
                            new OctopusActionEvent(event.getAction(), event.getKey())
                    );
                    case ENTRY ->  Bukkit.getPluginManager().callEvent(
                            new OctopusEntryChangeEvent(event.getEntry(), event.getKey())
                    );
                }
            });
        }
    }

    @Override
    public void onError(Throwable t) {
        Bukkit.getLogger().severe("Subscription error: " + t.getMessage());
    }

    @Override
    public void onCompleted() {
        Bukkit.getLogger().info("Subscription completed.");
    }
}