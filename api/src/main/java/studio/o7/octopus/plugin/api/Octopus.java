package studio.o7.octopus.plugin.api;

import gentle.Error;
import gentle.Result;
import studio.o7.octopus.plugin.Unsafe;
import studio.o7.octopus.sdk.v1.Entry;
import studio.o7.octopus.sdk.v1.QueryResponse;

import java.util.UUID;

/**
 * Main entry point for interacting with Octopus.
 *
 * <p>
 * Octopus provides operations to publish ("call") objects to a key, retrieve entries, and
 * subscribe listeners to key patterns.
 * </p>
 *
 * <p><b>Key format</b></p>
 * <ul>
 *   <li>Keys are dot-separated tokens, e.g. {@code foo.bar.baz}.</li>
 *   <li>Patterns may include wildcards (see subscription methods / {@link EventHandler}).</li>
 * </ul>
 *
 * <p><b>Wildcard patterns</b></p>
 * <ul>
 *   <li>{@code *} matches exactly one token (between dots).</li>
 *   <li>{@code &gt;} matches zero or more tokens until the end of the key.</li>
 * </ul>
 *
 * <p>
 * Example: {@code foo.*.bar} matches {@code foo.x.bar} but not {@code foo.x.y.bar}.<br>
 * Example: {@code foo.&gt;} matches {@code foo}, {@code foo.bar}, {@code foo.bar.baz}, etc.
 * </p>
 */

public interface Octopus {

    /**
     * Returns the active {@link Octopus} implementation.
     *
     * <p>
     * The returned instance is the entry point for calling objects, retrieving entries,
     * and registering listeners.
     * </p>
     *
     * @return the active {@link Octopus} instance
     */
    static Octopus instance() {
        return Unsafe.getInstance().get();
    }

    /**
     * Gets a unique entry of the given key
     *
     * @param key exact key pattern that will match between entries until one is found
     * @return Returns the first {@link studio.o7.octopus.sdk.v1.Entry} that matches the key
     */
    Result<studio.o7.octopus.sdk.v1.Object, Error> get(String key);

    /**
     * Query multiple Entries
     *
     * @param queryParameter Query parameter to build the query request
     * @return Returns a collection of matches for this query request
     */
    Result<QueryResponse, Error> query(QueryParameter queryParameter);

    /**
     * Publishes an object to the given key.
     *
     * <p><b>Behavior</b></p>
     * <ul>
     *   <li>Listeners are matched by their {@code keyPattern} (see {@link EventHandler}).</li>
     *   <li>Wildcard patterns are supported:
     *     {@code *} matches exactly one token; {@code &gt;} matches zero or more tokens until the end.</li>
     * </ul>
     *
     * <p><b>Errors</b></p>
     * <p>
     * Implementations may signal failures via an {@code OctopusError} (or another mechanism used by this API).
     * </p>
     *
     * @param obj the affected object to publish
     * @return the stored {@link Entry} on success, otherwise an {@link Error}
     */
    Result<Entry, Error> call(studio.o7.octopus.sdk.v1.Object obj);

    /**
     * Publishes an object to the given key.
     *
     * <p>
     * This operation is fire-and-forget: listeners are triggered asynchronously and this method
     * does not block until they complete.
     * </p>
     *
     * <p><b>Expired entries</b></p>
     * <p>
     * If an entry should expire (for example, a permission), set its {@code expired_at} field.
     * If it is not set, the entry will be flagged as expired.
     * </p>
     *
     * <p><b>Deletion</b></p>
     * <p>
     * To delete an entry, set its {@code deleted_at} field. The entry will then be flagged as deleted.
     * </p>
     *
     * @param obj object that should be saved inside the database
     */
    void write(studio.o7.octopus.sdk.v1.Object obj);

    /**
     * Registers an {@link EventHandler} (listener) for a key pattern.
     *
     * <p><b>Pattern rules</b></p>
     * <ul>
     *   <li>Tokens are dot-separated.</li>
     *   <li>{@code *} matches exactly one token.</li>
     *   <li>{@code &gt;} matches zero or more tokens until the end of the key.</li>
     * </ul>
     *
     * <p>
     * Example: {@code foo.*.bar} matches {@code foo.x.bar}.<br>
     * Example: {@code foo.&gt;} matches {@code foo}, {@code foo.bar}, and {@code foo.bar.baz}.
     * </p>
     *
     * @param eventHandler the handler to register
     */
    void registerHandler(EventHandler eventHandler);

    /**
     * Unregister a handler
     *
     * @param eventHandler Handler to unregister
     */
    default void unregisterHandler(EventHandler eventHandler) {
        unregisterHandler(eventHandler.getListenerUniqueId());
    }

    /**
     * Unregister a handler
     *
     * @param listenerUniqueId ID of the handler to unregister
     */
    void unregisterHandler(UUID listenerUniqueId);
}
