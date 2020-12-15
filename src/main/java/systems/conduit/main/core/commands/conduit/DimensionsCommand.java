package systems.conduit.main.core.commands.conduit;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import systems.conduit.main.Conduit;
import systems.conduit.main.api.mixins.MinecraftServer;
import systems.conduit.main.api.mixins.ServerLevel;
import systems.conduit.main.core.utils.PermissionUtils;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Innectic
 * @since 10/25/2020
 */
public class DimensionsCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> baseCommand() {
        return Commands.literal("dimensions").executes(c -> {
            c.getSource().sendFailure(new TextComponent("Please provide a valid subcommand. /conduit dimensions <teleport|list>"));
            return 1;
        });
    }

    public static LiteralArgumentBuilder<CommandSourceStack> teleportCommand() {
        return Commands.literal("teleport").requires(ctx -> PermissionUtils.checkPermissions(ctx, "conduit.dimensions.teleport", false, true)).then(Commands.argument("dimensionName", StringArgumentType.word()).executes(c -> {
            String dimensionName = StringArgumentType.getString(c, "dimensionName");
            Optional<ServerLevel> destination = Conduit.getLevelManager().getLevel(dimensionName);
            if (!destination.isPresent()) {
                c.getSource().sendFailure(new TextComponent("Invalid dimension!"));
                return -1;
            }

            if (c.getSource().getEntity() != null && c.getSource().getEntity() instanceof net.minecraft.server.level.ServerPlayer) {
                net.minecraft.server.level.ServerPlayer player = (net.minecraft.server.level.ServerPlayer) c.getSource().getEntity();
                player.teleportTo((net.minecraft.server.level.ServerLevel) destination.get(), destination.get().getServerLevelData().getXSpawn(),
                        destination.get().getServerLevelData().getYSpawn(), destination.get().getServerLevelData().getZSpawn(), 0, 0);
                c.getSource().sendSuccess(new TextComponent("Woosh!"), false);
                return 1;
            }

            c.getSource().sendFailure(new TextComponent("You aren't a player!"));

            return 1;
        }));
    }

    public static LiteralArgumentBuilder<CommandSourceStack> listCommand() {
        return Commands.literal("list").requires(ctx -> PermissionUtils.checkPermissions(ctx, "conduit.dimensions.list", false, true)).executes(c -> {
            Optional<MinecraftServer> server = Conduit.getServer();
            if (!server.isPresent()) {
                c.getSource().sendFailure(new TextComponent("An internal error has occurred."));
                return -1;
            }
            Map<ResourceKey<Level>, net.minecraft.server.level.ServerLevel> levels = server.get().getLevels();
            String levelNames = levels.values().stream().map(entry -> (ServerLevel) entry).map(level -> {
                String name = level.getServerLevelData().getLevelName();
                String suffix = "";

                Registry<DimensionType> dimensionTypeRegistry = server.get().getRegistryHolder().registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY);

                if (level.dimensionType().equals(dimensionTypeRegistry.getOrThrow(DimensionType.NETHER_LOCATION))) suffix = " (Nether)";
                else if (level.dimensionType().equals(dimensionTypeRegistry.getOrThrow(DimensionType.END_LOCATION))) suffix = " (End)";

                return name + suffix;
            }).collect(Collectors.joining(", "));

            c.getSource().sendSuccess(new TextComponent("Loaded dimensions: " + levelNames), false);

            return 1;
        });
    }
}
