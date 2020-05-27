package systems.conduit.core.extensions

import net.minecraft.ChatFormatting

operator fun ChatFormatting.plus(s: String): String = this.toString() + s

operator fun ChatFormatting.plus(color: ChatFormatting): String = this.toString() + color
