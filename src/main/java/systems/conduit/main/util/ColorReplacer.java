package systems.conduit.main.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextComponent;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;
import systems.conduit.main.Conduit;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class ColorReplacer {

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

    public static void init() {
        // Logger color
        AnsiConsole.systemInstall();
        // Redirect print to logger
        System.setOut(createLoggingProxy(System.out));
        System.setErr(createLoggingProxy(System.err));
    }

    static String getColoredMessage(String string) {
        string = string + ChatFormatting.RESET;
        for (ChatFormatting color : ChatFormatting.values()) {
            if (string.contains(color.toString())) {
                if (System.console() != null && System.getenv().get("TERM") != null) {
                    string = string.replaceAll("(?i)" + color.toString(), colorReplacements.get(color));
                } else {
                    string = string.replaceAll("(?i)" + color.toString(), "");
                }
            }
        }
        return string;
    }

    private static PrintStream createLoggingProxy(final PrintStream realPrintStream) {
        return new PrintStream(realPrintStream) {
            public void print(final String string) {
                Conduit.getLogger().info(new TextComponent(string).getText());
            }
            public void println(final String string) {
                Conduit.getLogger().info(new TextComponent(string).getText());
            }
        };
    }
}
