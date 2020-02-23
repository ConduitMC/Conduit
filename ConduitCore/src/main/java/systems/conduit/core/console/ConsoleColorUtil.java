package systems.conduit.core.console;

import net.minecraft.ChatFormatting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class ConsoleColorUtil implements Function<String, String> {

    private static final List<String> colors = new ArrayList<>();

    static {
        colors.addAll(Arrays.asList("\u001B[0;30;22m", "\u001B[0;34;22m", "\u001B[0;32;22m", "\u001B[0;36;22m", "\u001B[0;31;22m",
                "\u001B[0;35;22m", "\u001B[0;33;22m", "\u001B[0;37;22m", "\u001B[0;30;1m", "\u001B[0;34;1m", "\u001B[0;32;1m",
                "\u001B[0;36;1m", "\u001B[0;31;1m", "\u001B[0;35;1m", "\u001B[0;33;1m", "\u001B[0;37;1m", "\u001B[5m", "\u001B[21m",
                "\u001B[9m", "\u001B[4m", "\u001B[3m", "\u001B[m"));
    }

    @Override
    public String apply(String s) {
        s = s + ChatFormatting.RESET;
        for (int i = 0; i < ChatFormatting.values().length; i++) {
            String color = ChatFormatting.values()[i].toString();
            if (s.contains(color)) s = s.replaceAll("(?i)" + color, (System.console() != null && System.getenv().get("TERM") != null) ? colors.get(i) : "");
        }
        return s;
    }
}
