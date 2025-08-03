package studio.o7.octopus.plugin.api;

import lombok.NonNull;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;
import studio.o7.octopus.plugin.Unsafe;
import studio.o7.octopus.sdk.gen.api.v1.Entry;
import studio.o7.octopus.sdk.gen.api.v1.Event;

import java.util.Collection;
import java.util.List;

@NullMarked
public interface Octopus {

    static Octopus get() {
        return Unsafe.getInstance().get();
    }

    /**
     * Returns true if event has been published successfully.
     */
    boolean publishEvent(@NonNull Event event);

    /**
     * Get entries by key, optionally filtered.
     */
    @NotNull List<Entry> getEntry(@NonNull String key);

    /**
     * Adds list of keys that should be subscribed.
     */
    void addSubscriptions(@NonNull Collection<String> subscriptions);


    /**
     * Removes list of keys that shouldn't be subscribed.
     */
    void removeSubscriptions(@NonNull Collection<String> subscriptions);

    /**
     * Resets list of keys that should be subscribed completely.
     * @apiNote Also resets all subscribed/unsubscribed keys
     * which have been added by {@link Octopus#addSubscriptions}
     * or removed by {@link Octopus#removeSubscriptions}
     */
    @ApiStatus.Experimental
    void setSubscriptions(@NonNull Collection<String> subscriptions);

    /**
     * Returns the identifier of this service (e.g. service-name)
     */
    String getIdentifier();
}
