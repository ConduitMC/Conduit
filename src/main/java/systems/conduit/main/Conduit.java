package systems.conduit.main;

import net.minecraft.network.chat.TextComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import systems.conduit.main.api.MinecraftServer;
import systems.conduit.main.commands.CommandManager;
import systems.conduit.main.console.MessageFactory;
import systems.conduit.main.events.EventManager;
import systems.conduit.main.plugin.PluginManager;
import systems.conduit.main.plugin.annotation.PluginMeta;

import java.io.PrintStream;
import java.util.Optional;

@PluginMeta(name = "Conduit", description = "", version = "0.0.1", author = "ConduitMC")
public class Conduit {

    private final static Logger LOGGER = LogManager.getLogger("Conduit", new MessageFactory());
    private static EventManager eventManager = new EventManager();
    private static PluginManager pluginManager = new PluginManager();
    private static CommandManager commandManager = new CommandManager();
    private static Optional<MinecraftServer> server = Optional.empty();
    // TODO: Replace with build number in future
    private static PluginMeta meta = Conduit.class.getAnnotation(PluginMeta.class);

    public static void setupLogger() {
        // Redirect print to logger
        System.setOut(createLoggingProxy(System.out));
        System.setErr(createLoggingProxy(System.err));
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

    public static Optional<MinecraftServer> getServer() {
        return server;
    }

    public static void setServer(MinecraftServer newServer) {
        server = Optional.ofNullable(newServer);
    }

    public static PluginMeta getMeta() {
        return meta;
    }
  
    public static Logger getLogger() {
        return LOGGER;
    }

    public static EventManager getEventManager() {
        return eventManager;
    }

    public static PluginManager getPluginManager() {
        return pluginManager;
    }

    public static CommandManager getCommandManager() {
        return commandManager;
    }
}
