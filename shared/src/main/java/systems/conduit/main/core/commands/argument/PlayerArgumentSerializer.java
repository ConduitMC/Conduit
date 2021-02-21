package systems.conduit.main.core.commands.argument;

import com.google.gson.JsonObject;
import net.minecraft.commands.synchronization.ArgumentSerializer;
import net.minecraft.network.FriendlyByteBuf;

/**
 * @author Innectic
 * @since 12/15/2020
 */
public class PlayerArgumentSerializer implements ArgumentSerializer<PlayerArgumentType> {

    @Override
    public void serializeToNetwork(PlayerArgumentType argumentType, FriendlyByteBuf buffer) {
    }

    @Override
    public PlayerArgumentType deserializeFromNetwork(FriendlyByteBuf friendlyByteBuf) {
        return new PlayerArgumentType();
    }

    @Override
    public void serializeToJson(PlayerArgumentType playerArgumentType, JsonObject jsonObject) {
    }
}
