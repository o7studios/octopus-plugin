package studio.o7.octopus.plugin.api;

import gentle.Error;
import lombok.NonNull;

public enum OctopusError implements Error {

    GET_REQUEST_FAILED(0, "While trying to get an entry, a gRPC-Error occurred"),
    QUERY_REQUEST_FAILED(1, "While trying to query an entry, a gRPC-Error occurred"),
    CALL_REQUEST_FAILED(1, "While trying to call an object, a gRPC-Error occurred"),

    ;

    private final int code;
    private final String message;

    OctopusError(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int code() {
        return this.code;
    }

    @Override
    public @NonNull String message() {
        return this.message;
    }
}
