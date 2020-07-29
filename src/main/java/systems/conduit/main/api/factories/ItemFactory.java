package systems.conduit.main.api.factories;

import lombok.Builder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.List;
import java.util.Map;

/**
 * Handles the creation of new itemstacks.
 *
 * @author Innectic
 * @since 11/28/2019
 */
@Builder
public class ItemFactory {

    private TextComponent name;
    private List<String> meta;
    private Item item;
    private Map<Enchantment, Integer> enchantments;
    private CompoundTag tag;
    private int count;
    private int repairCost;
    private int damageValue;

    public ItemStack item() {
        ItemStack stack = new ItemStack(item);

        if (name != null) stack.setHoverName(name);
        if (enchantments != null && !enchantments.isEmpty()) enchantments.forEach(stack::enchant);
        if (tag != null) stack.setTag(tag);
        if (count > 0) stack.setCount(count);
        if (repairCost > 0) stack.setRepairCost(repairCost);
        if (damageValue > 0) stack.setDamageValue(damageValue);

        return stack;
    }
}
