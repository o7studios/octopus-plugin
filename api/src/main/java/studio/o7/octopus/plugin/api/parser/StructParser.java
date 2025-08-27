package studio.o7.octopus.plugin.api.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.protobuf.Struct;
import com.google.protobuf.util.JsonFormat;
import lombok.NonNull;
import net.kyori.adventure.resource.ResourcePackInfo;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import studio.o7.octopus.plugin.api.adapters.*;

import java.io.IOException;
import java.net.InetSocketAddress;

public final class StructParser<T> {
    private static final JsonFormat.Parser PARSER = JsonFormat.parser();
    private static final JsonFormat.Printer PRINTER = JsonFormat.printer();

    private static final ComponentAdapter COMPONENT_ADAPTER = new ComponentAdapter();
    private static final InetSocketAddressAdapter INET_SOCKET_ADDRESS_ADAPTER = new InetSocketAddressAdapter();
    private static final ItemStackAdapter ITEM_STACK_ADAPTER = new ItemStackAdapter();
    private static final LocationAdapter LOCATION_ADAPTER = new LocationAdapter();
    private static final OfflinePlayerAdapter OFFLINE_PLAYER_ADAPTER = new OfflinePlayerAdapter();
    private static final PlayerAdapter PLAYER_ADAPTER = new PlayerAdapter();
    private static final ResourcePackInfoAdapter RESOURCE_PACK_INFO_ADAPTER = new ResourcePackInfoAdapter();

    private final Gson gson;

    public StructParser(@NonNull GsonBuilder builder) {
        builder.disableHtmlEscaping();
        builder.registerTypeAdapter(Component.class, COMPONENT_ADAPTER);
        builder.registerTypeAdapter(InetSocketAddress.class, INET_SOCKET_ADDRESS_ADAPTER);
        builder.registerTypeAdapter(ItemStack.class, ITEM_STACK_ADAPTER);
        builder.registerTypeAdapter(Location.class, LOCATION_ADAPTER);
        builder.registerTypeAdapter(OfflinePlayer.class, OFFLINE_PLAYER_ADAPTER);
        builder.registerTypeAdapter(Player.class, PLAYER_ADAPTER);
        builder.registerTypeAdapter(ResourcePackInfo.class, RESOURCE_PACK_INFO_ADAPTER);
        this.gson = builder.create();
    }

    public T toObject(@NonNull Struct struct, @NonNull Class<T> tClass) throws IOException {
        var json = PRINTER.print(struct);
        return gson.fromJson(json, tClass);
    }

    public Struct toStruct(@NonNull T object) throws IOException {
        var builder = Struct.newBuilder();
        var json = gson.toJson(object);
        PARSER.merge(json, builder);
        return builder.build();
    }
}
