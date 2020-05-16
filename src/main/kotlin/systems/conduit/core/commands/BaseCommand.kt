package systems.conduit.core.commands

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.minecraft.commands.CommandSourceStack

abstract class BaseCommand {
    abstract val command: LiteralArgumentBuilder<CommandSourceStack>?
}