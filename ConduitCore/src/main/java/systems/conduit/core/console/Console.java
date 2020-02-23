package systems.conduit.core.console;

import net.minecraft.DefaultUncaughtExceptionHandler;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import systems.conduit.core.Conduit;

import java.io.IOException;

public class Console {

    private static boolean consoleStarted = false;

    // TODO: Add tab complete?
    public static void createConsole() {
        if (consoleStarted) return;
        consoleStarted = true;
        try {
            Terminal terminal = TerminalBuilder.builder().dumb(true).build();
            LineReader reader = LineReaderBuilder.builder().terminal(terminal).parser(new DefaultParser()).appName(Conduit.getMeta().name()).build();
            Thread consoleThread = new Thread("Server console handler") {
                public void run() {
                    try {
                        String line;
                        while (Conduit.getServer().isPresent() && Conduit.getServer().get().isRunning() && !Conduit.getServer().get().isStopped()) {
                            try {
                                line = reader.readLine("> ", null);
                            } catch (EndOfFileException e) {
                                e.printStackTrace();
                                continue;
                            }
                            if (line == null) continue;
                            if (line.trim().length() <= 0) continue;
                            Conduit.getServer().get().getCommands().performCommand(Conduit.getServer().get().createCommandSourceStack(), line);
                        }
                    } catch (UserInterruptException e) {
                        Conduit.getServer().get().close();
                    }
                }
            };
            consoleThread.setDaemon(true);
            consoleThread.setUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler(Conduit.getLogger()));
            consoleThread.start();
        } catch (IOException e) {
            Conduit.getLogger().error("Error building terminal");
            e.printStackTrace();
        }
    }
}
