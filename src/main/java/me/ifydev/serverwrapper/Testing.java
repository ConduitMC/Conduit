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
        event.setMessage(new TextComponent("Wow ").append(event.getPlayer().getDisplayName()).append(" joined the game!"));
    }

    @EventHandler(EventType.PlayerDamageByEntityEvent.class)
    public void onPlayerDamageByEntity(EventType.PlayerDamageByEntityEvent event) {
        event.getDamaged().sendMessage(new TextComponent("Ow! " + event.getDamager().getType() + " is the type of the thing that harmed you!"));
    }

    @EventHandler(EventType.PlayerDamageByPlayerEvent.class)
    public void onPlayerDamageByPlayer(EventType.PlayerDamageByPlayerEvent event) {
        event.getDamaged().sendMessage(new TextComponent("Ow!"));
        event.getDamager().sendMessage(new TextComponent("Pain causer!"));
    }

    @EventHandler(EventType.PlayerDamageByArrowEvent.class)
    public void onPlayerDamageByArrow(EventType.PlayerDamageByArrowEvent event) {
        event.getDamaged().sendMessage(new TextComponent("Ow! " + event.getShooter().getType().getDescription() + " shot you!"));
    }
}
