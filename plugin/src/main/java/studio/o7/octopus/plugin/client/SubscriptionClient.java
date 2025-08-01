package studio.o7.octopus.plugin.client;

import io.grpc.Channel;
import io.grpc.stub.StreamObserver;
import org.bukkit.plugin.Plugin;
import studio.o7.octopus.sdk.gen.api.v1.SubscriptionRequest;
import studio.o7.octopus.sdk.gen.api.v1.SubscriptionServiceGrpc;

import java.util.List;

public class SubscriptionClient {

    private final Plugin plugin;
    private final String identifier;
    private StreamObserver<SubscriptionRequest> subscriptionObserver;

    private final SubscriptionServiceGrpc.SubscriptionServiceStub asyncStub;

    public SubscriptionClient(Plugin plugin, Channel channel, String identifier) {
        this.plugin = plugin;
        this.identifier = identifier;

        this.asyncStub = SubscriptionServiceGrpc.newStub(channel);
    }

    public void initializeSubscription(List<String> subscriptions) {
        this.subscriptionObserver =  asyncStub.subscription(new SubscriptionObserver(plugin));
        var request = SubscriptionRequest.newBuilder()
                .setIdentifier(identifier)
                .addAllKeys(subscriptions)
                .build();

        subscriptionObserver.onNext(request);
    }

    public void updateSubscriptions(List<String> subscriptions) {
        if (subscriptionObserver == null) return;
        var request = SubscriptionRequest.newBuilder()
                .setIdentifier(identifier)
                .addAllKeys(subscriptions)
                .build();

        subscriptionObserver.onNext(request);
    }
}
