package systems.conduit.main.api;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.TextComponent;

public interface ITextComponent {

    TextComponent getColoredString(CommandSourceStack sourceStack);

}
