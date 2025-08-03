package studio.o7.octopus.plugin.api.events;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import studio.o7.octopus.sdk.gen.api.v1.EntryMutation;

@Getter
public final class OctopusEntryMutationEvent extends Event {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final String key;
    private final EntryMutation entryMutation;

    public OctopusEntryMutationEvent(@NonNull String key, @NonNull EntryMutation entryMutation) {
        super(true);
        this.key = key;
        this.entryMutation = entryMutation;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
