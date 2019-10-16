package systems.conduit.main.mixin;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.Entity;
import org.fusesource.jansi.Ansi;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import systems.conduit.main.Conduit;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

@Mixin(value = CommandSourceStack.class, remap = false)
public abstract class ColorMessageMixin {

    @Shadow @Nullable public abstract Entity getEntity();

    @Shadow @Final private CommandSource source;
    private final static Map<ChatFormatting, String> colorReplacements = new HashMap<>();

    static {
        colorReplacements.put(ChatFormatting.BLACK, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLACK).boldOff().toString());
        colorReplacements.put(ChatFormatting.DARK_BLUE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLUE).boldOff().toString());
        colorReplacements.put(ChatFormatting.DARK_GREEN, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.GREEN).boldOff().toString());
        colorReplacements.put(ChatFormatting.DARK_AQUA, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.CYAN).boldOff().toString());
        colorReplacements.put(ChatFormatting.DARK_RED, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.RED).boldOff().toString());
        colorReplacements.put(ChatFormatting.DARK_PURPLE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.MAGENTA).boldOff().toString());
        colorReplacements.put(ChatFormatting.GOLD, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.YELLOW).boldOff().toString());
        colorReplacements.put(ChatFormatting.GRAY, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.WHITE).boldOff().toString());
        colorReplacements.put(ChatFormatting.DARK_GRAY, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLACK).bold().toString());
        colorReplacements.put(ChatFormatting.BLUE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLUE).bold().toString());
        colorReplacements.put(ChatFormatting.GREEN, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.GREEN).bold().toString());
        colorReplacements.put(ChatFormatting.AQUA, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.CYAN).bold().toString());
        colorReplacements.put(ChatFormatting.RED, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.RED).bold().toString());
        colorReplacements.put(ChatFormatting.LIGHT_PURPLE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.MAGENTA).bold().toString());
        colorReplacements.put(ChatFormatting.YELLOW, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.YELLOW).bold().toString());
        colorReplacements.put(ChatFormatting.WHITE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.WHITE).bold().toString());
        colorReplacements.put(ChatFormatting.OBFUSCATED, Ansi.ansi().a(Ansi.Attribute.BLINK_SLOW).toString());
        colorReplacements.put(ChatFormatting.BOLD, Ansi.ansi().a(Ansi.Attribute.UNDERLINE_DOUBLE).toString());
        colorReplacements.put(ChatFormatting.STRIKETHROUGH, Ansi.ansi().a(Ansi.Attribute.STRIKETHROUGH_ON).toString());
        colorReplacements.put(ChatFormatting.UNDERLINE,Ansi.ansi().a(Ansi.Attribute.UNDERLINE).toString());
        colorReplacements.put(ChatFormatting.ITALIC, Ansi.ansi().a(Ansi.Attribute.ITALIC).toString());
        colorReplacements.put(ChatFormatting.RESET, Ansi.ansi().a(Ansi.Attribute.RESET).toString());
    }

    @Redirect(method = "sendSuccess", at = @At(value = "INVOKE", target = "Lnet/minecraft/commands/CommandSource;sendMessage(Lnet/minecraft/network/chat/Component;)V"))
    private void sendSuccess(CommandSource source, Component component) {
        sendColoredString(component);
    }

    @Redirect(method = "sendFailure", at = @At(value = "INVOKE", target = "Lnet/minecraft/commands/CommandSource;sendMessage(Lnet/minecraft/network/chat/Component;)V"))
    private void sendFailure(CommandSource source, Component component) {
        sendColoredString((new TextComponent("")).append(component).withStyle(ChatFormatting.RED));
    }

    private void sendColoredString(Component component) {
        String coloredString = component.getColoredString();
        if (this.getEntity() == null) {
            for (ChatFormatting color : ChatFormatting.values()) {
                if (coloredString.contains(color.toString())) {
                    if (System.console() != null && System.getenv().get("TERM") != null) {
                        coloredString = coloredString.replaceAll("(?i)" + color.toString(), colorReplacements.get(color));
                    } else {
                        coloredString = coloredString.replaceAll("(?i)" + color.toString(), "");
                    }
                }
            }
            Conduit.getLogger().info(new TextComponent(coloredString).getText());
            return;
        }
        this.source.sendMessage(new TextComponent(coloredString));
    }
}
