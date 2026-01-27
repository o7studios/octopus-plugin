package studio.o7.octopus.plugin.api;

import gentle.Error;
import gentle.Result;
import studio.o7.octopus.plugin.Unsafe;
import studio.o7.octopus.sdk.v1.Entry;
import studio.o7.octopus.sdk.v1.QueryResponse;

import java.util.UUID;

public interface Octopus {

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
     * <h1>Call</h1>
     * <p>
     * Stores an object on a key with new revision in the database
     * and returns the stored version, including the new revision
     * and ID.
     *
     * <h2>Expired</h2>
     * If an entry is expired. For example a permission, set the expired_at field
     * if it's not set and the entry will be flagged as expired
     *
     * <h2>Deletion</h2>
     * If an entry should be deleted, just set the deleted_at field and it will be
     * flagged as deleted
     *
     * @param obj Object that should be saved inside the database
     * @return returns the created {@link studio.o7.octopus.sdk.v1.Entry}
     */
    Result<Entry, Error> call(studio.o7.octopus.sdk.v1.Object obj);

    /**
     * <h1>Call</h1>
     * <p>
     * Stores an object on a key with new revision in the database
     * and just forgets it. All listeners will be called
     * as usual without blocking this method.
     *
     * <h2>Expired</h2>
     * If an entry is expired. For example a permission, set the expired_at field
     * if it's not set and the entry will be flagged as expired
     *
     * <h2>Deletion</h2>
     * If an entry should be deleted, just set the deleted_at field and it will be
     * flagged as deleted
     *
     * @param obj Object that should be saved inside the database
     */
    void write(studio.o7.octopus.sdk.v1.Object obj);

    /**
     * <p>
     * A registration of a handler will subscribe to its given key pattern
     * and receive all updates on the given key pattern. The handlers `onCall`
     * method will be invoked on the incoming {@link studio.o7.octopus.sdk.v1.EventCall}
     * </p>
     *
     * <p>
     * When subscribing, be reminded that the key pattern really matches the requested
     * EventCalls, using symbols such as `*` and `<` will subscribe on multiple keys
     * There's no safeguard to prevent subscribing to the same topic. So please make
     * shure you're not handling a topic twice!
     * </p>
     *
     * @param eventHandler Handler that will be invoked on matching incoming event
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
