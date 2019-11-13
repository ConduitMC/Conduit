package systems.conduit.main.mixin;

import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.connect.IMixinConnector;

public class MixinConnector implements IMixinConnector {

    @Override
    public void connect() {
        MixinBootstrap.init();
    }
}
