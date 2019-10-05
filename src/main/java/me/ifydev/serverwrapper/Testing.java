package me.ifydev.serverwrapper;

import me.ifydev.serverwrapper.events.EventListener;
import me.ifydev.serverwrapper.events.EventType;
import me.ifydev.serverwrapper.events.annotations.EventHandler;
import net.minecraft.network.chat.TextComponent;

/**
 * @author Innectic
 * @since 10/5/2019
 */
public class Testing implements EventListener {

    @EventHandler(EventType.PlayerJoinEvent.class)
    public void onPlayerJoin(EventType.PlayerJoinEvent event) {
        event.getPlayer().sendMessage(new TextComponent("Testing hello"));
    }
}
