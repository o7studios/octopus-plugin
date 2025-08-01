package studio.o7.octopus.plugin.api.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import studio.o7.octopus.sdk.gen.api.v1.EntryEdit;

@RequiredArgsConstructor
@Getter
public class OctopusEntryChangeEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final EntryEdit entry;
    private final String key;

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

}
