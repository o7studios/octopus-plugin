package studio.o7.octopus.plugin.channel;

import io.grpc.ManagedChannel;
import io.grpc.okhttp.OkHttpChannelBuilder;

import java.util.concurrent.TimeUnit;

/**
 * Creates and holds the shared Octopus gRPC channel.
 *
 * <p>Defaults:
 * host = 127.0.0.1
 * port = 50051
 * plaintext</p>
 */
public final class OctopusChannelFactory {

    private static volatile ManagedChannel channel;

    private OctopusChannelFactory() {}

    public static ManagedChannel getOrCreate(String host, int port) {
        var c = channel;
        if (c != null) return c;

        synchronized (OctopusChannelFactory.class) {
            if (channel != null) return channel;

            channel = OkHttpChannelBuilder
                    .forAddress(host, port)
                    .usePlaintext()
                    .keepAliveTime(30, TimeUnit.SECONDS)
                    .keepAliveWithoutCalls(true)
                    .build();

            return channel;
        }
    }

    public static void shutdown() {
        var c = channel;
        if (c == null) return;
        c.shutdown();
        channel = null;
    }
}