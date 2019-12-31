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
import systems.conduit.main.plugin.config.ConfigurationLoader;
import systems.conduit.main.plugin.config.ConfigurationTypes;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

@PluginMeta(name = "Conduit", description = "", version = "@VERSION@", author = "ConduitMC")
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

    @Getter(AccessLevel.PUBLIC) private static ConduitConfiguration configuration = null;

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

    public static void loadConfiguration() {
        // Ensure that the path exists. If it doesn't, then we don't need to process this.
        File file = Paths.get("conduit.json").toFile();
        if (!file.exists()) {
            // If the file does not exist, then lets attempt to generate a default.
            try (InputStream stream = Conduit.class.getResourceAsStream("/conduit.json")) {  // TODO: Fix that separator some day
                Files.copy(stream, Paths.get("conduit.json"), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        Optional<ConfigurationLoader> loader = ConfigurationTypes.getLoaderForExtension("json");
        if (!loader.isPresent()) {
            Conduit.getLogger().error("Failed to find loader for JSON extension");
            return;
        }
        Conduit.configuration = (ConduitConfiguration) loader.get().load(file, ConduitConfiguration.class).orElse(null);
    }

    public static Optional<MinecraftServer> getServer() {
        return Optional.of(server);
    }
}
