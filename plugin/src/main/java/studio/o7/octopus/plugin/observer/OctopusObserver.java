package studio.o7.octopus.plugin.observer;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import studio.o7.octopus.plugin.api.EventHandler;
import studio.o7.octopus.sdk.v1.EventCall;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@Slf4j(topic = "OctopusObserver")
public class OctopusObserver implements StreamObserver<EventCall> {

    private final HashMap<UUID, EventHandler> handlers;

    public OctopusObserver() {
        this.handlers = new HashMap<>();
    }

    public void addHandler(EventHandler handler) {
        handlers.put(handler.getListenerUniqueId(), handler);
    }

    public void removeHandler(UUID id) {
        this.handlers.remove(id);
    }

    public Iterable<String> getKeys() {
        return handlers.values().stream().map(EventHandler::getKeyPattern).toList();
    }

    public Optional<EventHandler> findMatchingHandler(String key) {
        String[] keyTokens = key.split("\\.");

        for (EventHandler handler : this.handlers.values()) {
            if (matches(handler.getKeyPattern(), keyTokens)) {
                return Optional.of(handler);
            }
        }
        return Optional.empty();
    }

    private static boolean matches(String pattern, String[] keyTokens) {
        String[] patternTokens = pattern.split("\\.");

        int i = 0; // pattern index
        int j = 0; // key index

        while (i < patternTokens.length && j < keyTokens.length) {
            String p = patternTokens[i];

            if (p.equals(">")) {
                // '>' matches everything remaining (including nothing)
                return true;
            }

            if (!p.equals("*") && !p.equals(keyTokens[j])) {
                return false;
            }

            i++;
            j++;
        }

        // If pattern has remaining tokens
        if (i < patternTokens.length) {
            // Only valid if the remaining token is a single '>'
            return i == patternTokens.length - 1 && patternTokens[i].equals(">");
        }

        // Match only if key is fully consumed
        return j == keyTokens.length;
    }

    private String currentCallID;

    @Override
    public void onNext(EventCall eventCall) {
        this.currentCallID = eventCall.getCallId();

        var object = eventCall.getObject();
        var optionalHandler = findMatchingHandler(object.getKey());

        if (optionalHandler.isEmpty()) {
            log.error("Failed to find handler for callID: {}", currentCallID);
            return;
        }

        var handler = optionalHandler.get();
        handler.onCall(object);
        log.info("Successfully handled callID: {} with pattern: {}", currentCallID, object.getKey());
        this.currentCallID = "";
    }

    @Override
    public void onError(Throwable throwable) {
        log.error("Failed listener {}, callID: {}", throwable.getMessage(), currentCallID);
        this.currentCallID = "";
    }

    @Override
    public void onCompleted() {
        log.debug("Completed listener, callID: {}", currentCallID);
        this.currentCallID = "";

    }
}
