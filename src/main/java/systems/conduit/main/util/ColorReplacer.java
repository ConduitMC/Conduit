package systems.conduit.main.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextComponent;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;
import systems.conduit.main.Conduit;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class ColorReplacer {

    private final static List<String> colors = new ArrayList<>();

    static {
        colors.add(getColor(true, null, Ansi.Color.BLACK, Ansi.Attribute.INTENSITY_BOLD_OFF));
        colors.add(getColor(true, null, Ansi.Color.BLUE, Ansi.Attribute.INTENSITY_BOLD_OFF));
        colors.add(getColor(true, null, Ansi.Color.GREEN, Ansi.Attribute.INTENSITY_BOLD_OFF));
        colors.add(getColor(true, null, Ansi.Color.CYAN, Ansi.Attribute.INTENSITY_BOLD_OFF));
        colors.add(getColor(true, null, Ansi.Color.RED, Ansi.Attribute.INTENSITY_BOLD_OFF));
        colors.add(getColor(true, null, Ansi.Color.MAGENTA, Ansi.Attribute.INTENSITY_BOLD_OFF));
        colors.add(getColor(true, null, Ansi.Color.YELLOW, Ansi.Attribute.INTENSITY_BOLD_OFF));
        colors.add(getColor(true, null, Ansi.Color.WHITE, Ansi.Attribute.INTENSITY_BOLD_OFF));

        colors.add(getColor(true, null, Ansi.Color.BLACK, Ansi.Attribute.INTENSITY_BOLD));
        colors.add(getColor(true, null, Ansi.Color.BLUE, Ansi.Attribute.INTENSITY_BOLD));
        colors.add(getColor(true, null, Ansi.Color.GREEN, Ansi.Attribute.INTENSITY_BOLD));
        colors.add(getColor(true, null, Ansi.Color.CYAN, Ansi.Attribute.INTENSITY_BOLD));
        colors.add(getColor(true, null, Ansi.Color.RED, Ansi.Attribute.INTENSITY_BOLD));
        colors.add(getColor(true, null, Ansi.Color.MAGENTA, Ansi.Attribute.INTENSITY_BOLD));
        colors.add(getColor(true, null, Ansi.Color.YELLOW, Ansi.Attribute.INTENSITY_BOLD));
        colors.add(getColor(true, null, Ansi.Color.WHITE, Ansi.Attribute.INTENSITY_BOLD));

        colors.add(getColor(false, Ansi.Attribute.BLINK_SLOW, null, null));
        colors.add(getColor(false, Ansi.Attribute.UNDERLINE_DOUBLE, null, null));
        colors.add(getColor(false, Ansi.Attribute.STRIKETHROUGH_ON, null, null));
        colors.add(getColor(false, Ansi.Attribute.UNDERLINE, null, null));
        colors.add(getColor(false, Ansi.Attribute.ITALIC, null, null));
        colors.add(getColor(false, Ansi.Attribute.RESET, null, null));
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
        for (int i = 0; i < ChatFormatting.values().length; i++) {
            String color = ChatFormatting.values()[i].toString();
            if (string.contains(color)) string = string.replaceAll("(?i)" + color,(System.console() != null && System.getenv().get("TERM") != null) ? colors.get(i) : "");
        }
        return string;
    }

    private static String getColor(boolean reset, Ansi.Attribute attribute, Ansi.Color fg, Ansi.Attribute bold) {
        Ansi ansi = Ansi.ansi();
        if (reset) ansi = ansi.a(Ansi.Attribute.RESET);
        if (attribute != null) ansi = ansi.a(attribute);
        if (fg != null) ansi = ansi.fg(fg);
        if (bold != null) ansi = ansi.a(bold);
        return ansi.toString();
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
