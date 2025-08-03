package studio.o7.octopus.plugin.api.events;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import studio.o7.octopus.sdk.gen.api.v1.PlayerNotification;

@Getter
public class OctopusPlayerNotificationEvent extends Event {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final String key;
    private final PlayerNotification playerNotification;

    public OctopusPlayerNotificationEvent(@NonNull String key, @NonNull PlayerNotification playerNotification) {
        super(true);
        this.key = key;
        this.playerNotification = playerNotification;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}