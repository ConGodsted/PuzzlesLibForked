package fuzs.puzzleslibforked.capability.data;

import net.minecraft.nbt.CompoundTag;

/**
 * same functionality as INBTSerializable (Forge) or ComponentV3 (Fabric) for providing common read and write methods
 */
public interface CapabilityComponent {

    /**
     * @param tag tag to write to
     */
    void write(CompoundTag tag);

    /**
     * @param tag tag to read from
     */
    void read(CompoundTag tag);

    /**
     * @return this capability serialized to {@link CompoundTag}
     */
    default CompoundTag toCompoundTag() {
        CompoundTag tag = new CompoundTag();
        this.write(tag);
        return tag;
    }
}
