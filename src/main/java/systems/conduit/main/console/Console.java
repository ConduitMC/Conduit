package systems.conduit.main.console;

import net.minecraft.DefaultUncaughtExceptionHandler;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import systems.conduit.main.Conduit;
import systems.conduit.main.api.mixins.MinecraftServer;

import java.io.IOException;

public class Console {

    private static boolean consoleStarted = false;

    // TODO: Add tab complete?
    public static void createConsole() {
        if (consoleStarted) return;
        consoleStarted = true;

        try {
            Terminal terminal = TerminalBuilder.builder().dumb(true).build();
            LineReader reader = LineReaderBuilder.builder().terminal(terminal).parser(new DefaultParser()).appName(Conduit.getVersion()).build();
            Thread consoleThread = new Thread("Server console handler") {
                public void run() {
                    try {
                        String line;
                        while (Conduit.getServer().map(s -> s.isRunning() && !s.isStopped()).orElse(false)) {
                            try {
                                line = reader.readLine("> ", null);
                            } catch (EndOfFileException e) {
                                e.printStackTrace();
                                continue;
                            }
                            if (line == null || line.trim().length() <= 0) continue;
                            Conduit.getServer().get().getCommands().performCommand(Conduit.getServer().get().createCommandSourceStack(), line);
                        }
                    } catch (UserInterruptException e) {
                        // If the user sent a kill code to the process, stop the server.
                        Conduit.getServer().ifPresent(MinecraftServer::close);
                    }
                }
            };
            consoleThread.setDaemon(true);
            consoleThread.setUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler(Conduit.getLogger()));
            consoleThread.start();
        } catch (IOException e) {
            Conduit.getLogger().error("Failed to initialize terminal.");
            e.printStackTrace();
        }
    }
}
