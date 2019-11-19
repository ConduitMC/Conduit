package systems.conduit.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.chat.TextComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import systems.conduit.main.api.MinecraftServer;
import systems.conduit.main.api.managers.LevelManager;
import systems.conduit.main.api.managers.PlayerManager;
import systems.conduit.main.commands.CommandManager;
import systems.conduit.main.console.MessageFactory;
import systems.conduit.main.events.EventManager;
import systems.conduit.main.plugin.PluginManager;
import systems.conduit.main.plugin.annotation.PluginMeta;

import java.io.PrintStream;
import java.util.Optional;

@PluginMeta(name = "Conduit", description = "", version = "0.0.2", author = "ConduitMC")
public class Conduit {

    @Getter(AccessLevel.PUBLIC) private static final Logger logger = LogManager.getLogger("Conduit", new MessageFactory());
    @Getter(AccessLevel.PUBLIC) private static EventManager eventManager = new EventManager();
    @Getter(AccessLevel.PUBLIC) private static PluginManager pluginManager = new PluginManager();
    @Getter(AccessLevel.PUBLIC) private static CommandManager commandManager = new CommandManager();
    @Getter(AccessLevel.PUBLIC) private static PlayerManager playerManager = new PlayerManager();
    @Getter(AccessLevel.PUBLIC) private static LevelManager levelManager = new LevelManager();
    @Getter(AccessLevel.PUBLIC) private static ObjectMapper objectMapper = new ObjectMapper();

    @Setter(AccessLevel.PUBLIC) private static MinecraftServer server = null;

    // TODO: Replace with build number in future
    @Getter(AccessLevel.PUBLIC) private static PluginMeta meta = Conduit.class.getAnnotation(PluginMeta.class);

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
        return Optional.of(server);
    }
}
