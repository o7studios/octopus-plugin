package studio.o7.octopus.plugin;

import io.grpc.stub.StreamObserver;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import lombok.NonNull;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import studio.o7.octopus.plugin.api.Octopus;
import studio.o7.octopus.plugin.api.listener.Listener;
import studio.o7.octopus.plugin.observer.EmptyObserver;
import studio.o7.octopus.plugin.utils.ProtoUtils;
import studio.o7.octopus.sdk.OctopusSDK;
import studio.o7.octopus.sdk.gen.api.v1.*;
import studio.o7.octopus.sdk.gen.api.v1.Object;

import java.time.Instant;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public final class OctopusImpl implements Octopus {
    private static final EmptyObserver EMPTY_OBSERVER = new EmptyObserver();

    private final OctopusGrpc.OctopusStub stub = OctopusSDK.stub();
    private final OctopusGrpc.OctopusBlockingStub blockingStub = OctopusSDK.blockingStub();
    private final Object2ObjectMap<UUID, Pair<Listener, StreamObserver<ListenMessage>>> listeners = new Object2ObjectArrayMap<>();

    private final Logger logger;

    public OctopusImpl(Plugin plugin) {
        logger = plugin.getSLF4JLogger();
    }

    @Override
    public Collection<Entry> get(@NonNull String keyPattern, boolean includeExpired, @Nullable Instant createdRangeStart, @Nullable Instant createdRangeEnd) {
        var builder = GetRequest.newBuilder();

        builder.setKeyPattern(keyPattern);
        builder.setIncludeExpired(includeExpired);

        if (createdRangeStart != null)
            builder.setCreatedAtRangeStart(ProtoUtils.toProto(createdRangeStart));

        if (createdRangeEnd != null)
            builder.setCreatedAtRangeEnd(ProtoUtils.toProto(createdRangeEnd));

        return this.blockingStub.get(builder.build()).getEntriesList();
    }

    @Override
    public void registerListener(@NonNull Listener listener) {
        var requestRef = new AtomicReference<StreamObserver<ListenMessage>>();

        var observer = stub.listen(new StreamObserver<>() {
            @Override
            public void onNext(EventCall value) {
                listener.onCall(value.getObject());

                var msg = ListenMessage.newBuilder()
                        .setCallback(value)
                        .build();

                requestRef.get().onNext(msg);
            }

            @Override
            public void onError(Throwable t) {
                logger.error("Cannot call event on listener {} with key-pattern {}", listener.getListenerUniqueId(), listener.getKeyPattern(), t);
            }

            @Override
            public void onCompleted() {
                listeners.remove(listener.getListenerUniqueId());
            }
        });

        requestRef.set(observer);

        observer.onNext(ListenMessage.newBuilder()
                .setRegister(ListenRegister.newBuilder()
                        .setKeyPattern(listener.getKeyPattern())
                        .setPriority(listener.getPriority())
                        .build()).build());

        this.listeners.put(listener.getListenerUniqueId(), new Pair<>() {
            @Override
            public Listener left() {
                return listener;
            }

            @Override
            public StreamObserver<ListenMessage> right() {
                return observer;
            }
        });
    }

    @Override
    public void unregisterListener(@NonNull Listener listener) {
        unregisterListener(listener.getListenerUniqueId());
    }

    @Override
    public void unregisterListener(@NonNull UUID listenerUniqueId) {
        var pair = this.listeners.get(listenerUniqueId);
        pair.right().onCompleted();
        this.listeners.remove(listenerUniqueId);
    }

    @Override
    public @NotNull Entry call(@NonNull Object obj) {
        return blockingStub.call(obj);
    }

    @Override
    public void callAndForget(@NonNull Object obj) {
        stub.write(obj, EMPTY_OBSERVER);
    }
}