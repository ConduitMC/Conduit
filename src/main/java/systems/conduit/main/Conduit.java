package systems.conduit.main;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.chat.TextComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import systems.conduit.main.api.managers.BossBarManager;
import systems.conduit.main.api.managers.LevelManager;
import systems.conduit.main.api.managers.PlayerManager;
import systems.conduit.main.api.mixins.MinecraftServer;
import systems.conduit.main.console.MessageFactory;
import systems.conduit.main.core.RunnableManager;
import systems.conduit.main.core.commands.CommandManager;
import systems.conduit.main.core.events.EventManager;
import systems.conduit.main.core.plugin.PluginManager;
import systems.conduit.main.core.plugin.config.ConfigurationLoader;
import systems.conduit.main.core.plugin.config.ConfigurationTypes;
import systems.conduit.main.core.scoreboard.ScoreboardManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

public class Conduit {

    @Getter private static final Logger logger = LogManager.getLogger("Conduit", new MessageFactory());
    @Getter private static final EventManager eventManager = new EventManager();
    @Getter private static final PluginManager pluginManager = new PluginManager();
    @Getter private static final CommandManager commandManager = new CommandManager();
    @Getter private static final PlayerManager playerManager = new PlayerManager();
    @Getter private static final LevelManager levelManager = new LevelManager();
    @Getter private static final BossBarManager bossBarManager = new BossBarManager();
    @Getter private static final RunnableManager runnableManager = new RunnableManager();
    @Getter private static final ScoreboardManager scoreboardManager = new ScoreboardManager();

    @Setter private static MinecraftServer server = null;
    @Getter private static String version = "@VERSION@";
    @Getter private static ConduitConfiguration configuration;

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
                if (stream != null) Files.copy(stream, Paths.get("conduit.json"), StandardCopyOption.REPLACE_EXISTING);
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
        if (file.exists()) Conduit.configuration = (ConduitConfiguration) loader.get().load(file, ConduitConfiguration.class).orElse(null);
    }

    public static Optional<MinecraftServer> getServer() {
        return Optional.of(server);
    }
}
