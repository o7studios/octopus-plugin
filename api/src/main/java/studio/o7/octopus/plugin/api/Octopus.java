package studio.o7.octopus.plugin.api;

import org.jspecify.annotations.NullMarked;
import studio.o7.octopus.plugin.Unsafe;
import studio.o7.octopus.plugin.api.listener.Listener;
import studio.o7.octopus.sdk.gen.api.v1.Entry;
import studio.o7.octopus.sdk.gen.api.v1.Object;

import javax.annotation.Nullable;
import java.time.Instant;
import java.util.Collection;
import java.util.UUID;

@NullMarked
public interface Octopus {

    static Octopus get() {
        return Unsafe.getInstance().get();
    }

    /**
     * Retrieves existing entries from the database matching a
     * key pattern.
     */
    default Collection<Entry> get(String keyPattern) {
        return get(keyPattern, false);
    }

    /**
     * Retrieves existing entries from the database matching a
     * key pattern. Can optionally include expired objects.
     */
    default Collection<Entry> get(String keyPattern, boolean includeExpired) {
        return get(keyPattern, includeExpired, null, null);
    }

    /**
     * Retrieves existing entries from the database matching a
     * key pattern. Can optionally include expired objects and
     * filter by revision creation time.
     */
    Collection<Entry> get(String keyPattern, boolean includeExpired, @Nullable Instant createdRangeStart, @Nullable Instant createdRangeEnd);


    void registerListener(Listener listener);

    default void unregisterListener(Listener listener) {
        unregisterListener(listener.getListenerUniqueId());
    }

    void unregisterListener(UUID listenerUniqueId);

    /**
     * Stores an object on a key with new revision in the database
     * and returns the stored version, including the new revision
     * and ID.
     */
    Entry call(Object obj);

    /**
     * Stores an object on a key with new revision in the database
     * and just forgets it. All listeners will be called
     * as usual without blocking this method.
     */
    void callAndForget(Object obj);

}
