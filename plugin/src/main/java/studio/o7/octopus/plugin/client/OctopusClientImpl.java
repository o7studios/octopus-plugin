package studio.o7.octopus.plugin.client;

import io.grpc.ManagedChannel;
import io.grpc.okhttp.OkHttpChannelBuilder;
import org.bukkit.plugin.Plugin;
import studio.o7.octopus.plugin.api.client.OctopusClient;
import studio.o7.octopus.sdk.gen.api.v1.EntryResponse;
import studio.o7.octopus.sdk.gen.api.v1.Event;

import java.util.List;

public class OctopusClientImpl implements OctopusClient {

    private final ServerClient serverClient;
    private final SubscriptionClient subscriptionClient;

    public OctopusClientImpl(String host, int port, Plugin plugin, String identifier) {

        System.out.println("DEBUG: Creating channel with host='" + host + "', port=" + port);
        System.out.println("DEBUG: host.getClass()=" + host.getClass().getName());
        System.out.println("DEBUG: Host equals localhost: " + "localhost".equals(host));

        try {
            ManagedChannel channel = OkHttpChannelBuilder.forAddress(host, port)
                    .usePlaintext()
                    .build();

            System.out.println("DEBUG: Channel created successfully");

            this.serverClient = new ServerClient(channel);
            this.subscriptionClient = new SubscriptionClient(plugin, channel,  identifier);
        } catch (Exception e) {
            System.err.println("DEBUG: Channel creation failed: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }

    }

    @Override
    public boolean emitEvent(Event event) {
        return serverClient.emitEvent(event);
    }

    @Override
    public EntryResponse getEntry(String key) {
        return serverClient.getEntry(key);
    }

    @Override
    public void initializeSubscription(List<String> subscriptions) {
        subscriptionClient.initializeSubscription(subscriptions);
    }

    @Override
    public void updateSubscriptions(List<String> subscriptions) {
        subscriptionClient.updateSubscriptions(subscriptions);
    }
}
