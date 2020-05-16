package systems.conduit.api.factories

import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.TextComponent
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import java.util.function.BiConsumer

/**
 * Handles the creation of new itemstacks.
 *
 * @author Innectic
 * @since 11/28/2019
 */
class ItemFactory {

    private val name: TextComponent? = null
    private val meta: List<String>? = null
    private val item: Item? = null
    private val enchantments: Map<Enchantment, Int>? = null
    private val tag: CompoundTag? = null
    private val count = 0
    private val repairCost = 0
    private val damageValue = 0

    fun item(): ItemStack {
        val stack = ItemStack(item)
        if (name != null) stack.hoverName = name
        if (enchantments != null && !enchantments.isEmpty()) enchantments.forEach(BiConsumer { enchantment: Enchantment?, i: Int? -> stack.enchant(enchantment, i!!) })
        if (tag != null) stack.tag = tag
        if (count > 0) stack.count = count
        if (repairCost > 0) stack.setRepairCost(repairCost)
        if (damageValue > 0) stack.damageValue = damageValue
        return stack
    }
}
