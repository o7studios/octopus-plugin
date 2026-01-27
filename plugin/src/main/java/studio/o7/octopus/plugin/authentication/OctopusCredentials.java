package studio.o7.octopus.plugin.authentication;

import io.grpc.CallCredentials;
import io.grpc.Metadata;

import java.util.concurrent.Executor;

public class OctopusCredentials extends CallCredentials {

    private final String token;

    public OctopusCredentials(String token) {
        this.token = token;
    }

    @Override
    public void applyRequestMetadata(RequestInfo requestInfo, Executor executor, MetadataApplier metadataApplier) {
        Metadata headers = new Metadata();
        Metadata.Key<String> authKey = Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER);
        headers.put(authKey, this.token);
        metadataApplier.apply(headers);
    }
}
