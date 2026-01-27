package studio.o7.octopus.plugin.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import studio.o7.octopus.sdk.v1.Object;

import java.util.UUID;

/**
 * Base class for event handlers that can be registered with {@link Octopus}.
 */
@AllArgsConstructor
@Getter
public abstract class EventHandler {
    /**
     * ID for identifying this listener.
     * (Made for internal purposes)
     */
    private final UUID listenerUniqueId = UUID.randomUUID();

    /**
     * Pattern to match keys. Supports wildcards:
     * <br>
     * *  — matches exactly one token (between dots), e.g., "foo.*.bar" matches "foo.x.bar" but not "foo.x.y.bar"
     * <br>
     * >  — matches one or more tokens until the end, e.g., "foo.>" matches "foo", "foo.bar", "foo.bar.baz", etc.
     * Multiple wildcards can be used in a single pattern. Tokens are dot-separated.
     */
    protected final String keyPattern;

    /**
     * @param obj The affected object.
     */
    public abstract void onCall(@NonNull Object obj);
}
