package systems.conduit.api;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.LevelData;

/**
 * @author Innectic
 * @since 1/5/2020
 */
public interface LevelStorage {

    void saveLevelData(LevelData level, CompoundTag data);
}
