package systems.conduit.core.console

import net.minecraft.ChatFormatting
import java.util.*
import java.util.function.Function

class ConsoleColorUtil: Function<String, String> {
    companion object {
        private val colors: MutableList<String> = ArrayList()

        init {
            colors.addAll(listOf("\u001B[0;30;22m", "\u001B[0;34;22m", "\u001B[0;32;22m", "\u001B[0;36;22m", "\u001B[0;31;22m",
                    "\u001B[0;35;22m", "\u001B[0;33;22m", "\u001B[0;37;22m", "\u001B[0;30;1m", "\u001B[0;34;1m", "\u001B[0;32;1m",
                    "\u001B[0;36;1m", "\u001B[0;31;1m", "\u001B[0;35;1m", "\u001B[0;33;1m", "\u001B[0;37;1m", "\u001B[5m", "\u001B[21m",
                    "\u001B[9m", "\u001B[4m", "\u001B[3m", "\u001B[m"))
        }
    }

    override fun apply(s: String): String {
        val shouldUseColor = System.console() != null && System.getenv()["TERM"] != null
        var colorS = s
        if (shouldUseColor) colorS += ChatFormatting.RESET
        for (i in ChatFormatting.values().indices) {
            val color = ChatFormatting.values()[i].toString()
            if (colorS.contains(color)) colorS = colorS.replace(color, if (shouldUseColor) colors[i] else "")
        }
        return colorS
    }
}
