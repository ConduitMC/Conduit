package systems.conduit.main.console;

import net.minecraft.ChatFormatting;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ColorReplacer implements Function<String, String> {

    private final static List<String> colors = new ArrayList<>();

    static {
        colors.add("\u001B[0;30;22m");
        colors.add("\u001B[0;34;22m");
        colors.add("\u001B[0;32;22m");
        colors.add("\u001B[0;36;22m");
        colors.add("\u001B[0;31;22m");
        colors.add("\u001B[0;35;22m");
        colors.add("\u001B[0;33;22m");
        colors.add("\u001B[0;37;22m");
        colors.add("\u001B[0;30;1m");
        colors.add("\u001B[0;34;1m");
        colors.add("\u001B[0;32;1m");
        colors.add("\u001B[0;36;1m");
        colors.add("\u001B[0;31;1m");
        colors.add("\u001B[0;35;1m");
        colors.add("\u001B[0;33;1m");
        colors.add("\u001B[0;37;1m");
        colors.add("\u001B[5m");
        colors.add("\u001B[21m");
        colors.add("\u001B[9m");
        colors.add("\u001B[4m");
        colors.add("\u001B[3m");
        colors.add("\u001B[m");
    }

    @Override
    public String apply(String string) {
        string = string + ChatFormatting.RESET;
        for (int i = 0; i < ChatFormatting.values().length; i++) {
            String color = ChatFormatting.values()[i].toString();
            if (string.contains(color)) string = string.replaceAll("(?i)" + color,(System.console() != null && System.getenv().get("TERM") != null) ? colors.get(i) : "");
        }
        return string;
    }
}
