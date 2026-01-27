package studio.o7.octopus.plugin;

import gentle.Error;
import gentle.Result;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import studio.o7.octopus.plugin.api.Octopus;
import studio.o7.octopus.plugin.api.EventHandler;
import studio.o7.octopus.plugin.api.OctopusError;
import studio.o7.octopus.plugin.api.QueryParameter;
import studio.o7.octopus.plugin.authentication.OctopusCredentials;
import studio.o7.octopus.plugin.channel.OctopusChannelFactory;
import studio.o7.octopus.plugin.observer.OctopusObserver;
import studio.o7.octopus.sdk.v1.*;
import studio.o7.octopus.sdk.v1.Object;

import java.util.UUID;

@Slf4j(topic = "OctopusPlugin")
public final class OctopusImpl implements Octopus {

    private final OctopusObserver streamObserver;
    private StreamObserver<ListenMessage> responseObserver;

    private final OctopusGrpc.OctopusBlockingStub blocking;
    private final OctopusGrpc.OctopusStub async;

    public OctopusImpl(String token, String host, int port) {
        var channel = OctopusChannelFactory.getOrCreate(host, port);

        var blockingStub = OctopusGrpc.newBlockingStub(channel);
        this.blocking = blockingStub.withCallCredentials(new OctopusCredentials(token));

        var asyncStub = OctopusGrpc.newStub(channel);
        async = asyncStub.withCallCredentials(new OctopusCredentials(token));

        this.streamObserver = new OctopusObserver();
    }

    @Override
    public Result<Object, Error> get(String key) {
        var request = GetRequest.newBuilder().setKey(key).build();
        try {
            var response = blocking.get(request);
            return Result.ok(response.getObject());
        } catch (RuntimeException e) {
            log.error("Failed to send a get request: {}", e.getMessage());
            return Result.err(OctopusError.GET_REQUEST_FAILED);
        }
    }

    @Override
    public Result<QueryResponse, Error> query(QueryParameter queryParameter) {
        var request = QueryRequest.newBuilder();
        request.setKeyPattern(queryParameter.getKeyPattern());
        request.setDataFilter(queryParameter.getDataFilter());

        request.setIncludeExpired(queryParameter.isIncludeExpired());

        var paginator = Paginator.newBuilder().setPage(queryParameter.getPage()).setPageSize(queryParameter.getPageSize()).build();

        request.setPaginator(paginator);

        if (queryParameter.getCreatedAtStart() != null) {
            request.setCreatedAtRangeStart(queryParameter.getCreatedAtStart());
        }

        if (queryParameter.getCreatedAtEnd() != null) {
            request.setCreatedAtRangeEnd(queryParameter.getCreatedAtEnd());
        }

        try {
            var response = blocking.query(request.build());
            return Result.ok(response);
        } catch (RuntimeException e) {
            log.error("Failed to send a query request: {}", e.getMessage());
            return Result.err(OctopusError.QUERY_REQUEST_FAILED);
        }
    }

    @Override
    public Result<Entry, Error> call(studio.o7.octopus.sdk.v1.Object obj) {
        try {
            var response = blocking.call(obj);
            return Result.ok(response);
        } catch (RuntimeException e) {
            log.error("Failed to send a call request: {}", e.getMessage());
            return Result.err(OctopusError.CALL_REQUEST_FAILED);
        }
    }

    @Override
    public void write(Object obj) {
        try {
            blocking.write(obj);
        } catch (RuntimeException e) {
            log.error("Failed to send a write request: {}", e.getMessage());
        }
    }

    @Override
    public void registerHandler(EventHandler eventHandler) {
        log.debug("Adding a new handler to the pattern {}", eventHandler.getKeyPattern());
        streamObserver.addHandler(eventHandler);

        if (responseObserver == null) {
            log.debug("initializing stream to octopus");
            this.responseObserver = async.listen(streamObserver);
        }

        var req = ListenMessage.newBuilder().addAllKeyPattern(streamObserver.getKeys());
        responseObserver.onNext(req.build());
    }

    @Override
    public void unregisterHandler(@org.jspecify.annotations.NonNull EventHandler eventHandler) {
        this.unregisterHandler(eventHandler.getListenerUniqueId());
    }

    @Override
    public void unregisterHandler(UUID listenerUniqueId) {
        this.streamObserver.removeHandler(listenerUniqueId);
    }

}