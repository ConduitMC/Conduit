package systems.conduit.main.core.api.managers;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.bossevents.CustomBossEvent;
import systems.conduit.main.Conduit;

import java.util.Optional;

public class BossBarManager {

    public Optional<CustomBossEvent> createBossBar(String id) {
        if (!Conduit.getServer().isPresent()) return Optional.empty();
        return Optional.of(Conduit.getServer().get().getCustomBossEvents().create(new ResourceLocation(id), new TextComponent("")));
    }

    public  Optional<CustomBossEvent> get(String id) {
        if (!Conduit.getServer().isPresent()) return Optional.empty();
        return Optional.ofNullable(Conduit.getServer().get().getCustomBossEvents().get(new ResourceLocation(id)));
    }

    public void remove(CustomBossEvent bossBar) {
        if (Conduit.getServer().isPresent()) Conduit.getServer().get().getCustomBossEvents().remove(bossBar);
    }
}
