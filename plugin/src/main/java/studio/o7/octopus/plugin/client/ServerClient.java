package studio.o7.octopus.plugin.client;

import io.grpc.Channel;
import studio.o7.octopus.sdk.gen.api.v1.*;

public class ServerClient {

    //TODO: Switch To Async
    EventServiceGrpc.EventServiceBlockingStub blockingStub;

    public ServerClient(Channel channel) {
        blockingStub = EventServiceGrpc.newBlockingStub(channel);
    }

    public boolean emitEvent(Event event) {
        EmitEventResponse response = blockingStub.emitEvent(event);
        System.out.println("EmitEvent success: " + response.getSuccess());
        return response.getSuccess();
    }

    public EntryResponse getEntry(String key) {
        EntryRequest request = EntryRequest.newBuilder().setKey(key).build();
        return blockingStub.getEntry(request);
    }
}
