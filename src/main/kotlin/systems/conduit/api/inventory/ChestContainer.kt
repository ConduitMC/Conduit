package systems.conduit.api.inventory

import net.minecraft.world.ContainerListener
import net.minecraft.world.SimpleContainer
import net.minecraft.world.inventory.ChestMenu
import net.minecraft.world.inventory.MenuType

class ChestContainer private constructor(size: Int): SimpleContainer(size) {

    var type: MenuType<ChestMenu>? = null


    var title = ""
        private set

    // TODO: Use?
    override fun addListener(var1: ContainerListener) {
        throw UnsupportedOperationException("Container listener not implemented!")
    }

    companion object {
        fun create(size: MenuType<ChestMenu>, title: String): ChestContainer {
            val chestContainer = ChestContainer(getSizeFromEnum(size))
            chestContainer.type = size
            chestContainer.title = title
            return chestContainer
        }

        private fun getSizeFromEnum(size: MenuType<ChestMenu>): Int {
            if (MenuType.GENERIC_9x2 == size) {
                return 18
            } else if (MenuType.GENERIC_9x3 == size) {
                return 27
            } else if (MenuType.GENERIC_9x4 == size) {
                return 36
            } else if (MenuType.GENERIC_9x5 == size) {
                return 45
            } else if (MenuType.GENERIC_9x6 == size) {
                return 54
            }
            return 9
        }
    }
}