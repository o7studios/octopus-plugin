package studio.o7.octopus.plugin.observer;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;

public class EmptyObserver implements StreamObserver<Empty> {
    @Override
    public void onNext(Empty value) {

    }

    @Override
    public void onError(Throwable t) {

    }

    @Override
    public void onCompleted() {

    }
}
