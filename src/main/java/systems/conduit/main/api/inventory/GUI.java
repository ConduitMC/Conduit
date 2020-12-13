package systems.conduit.main.api.inventory;

import com.google.common.collect.ImmutableList;
import systems.conduit.main.api.mixins.ServerPlayer;

/**
 * @author Innectic
 * @since 12/11/2020
 */
public interface GUI {

    void open(ServerPlayer player);
    void open(Iterable<ServerPlayer> player);

    void close(ServerPlayer player);
    void close(Iterable<ServerPlayer> player);

    ImmutableList<ServerPlayer> getActiveViewers();
}
