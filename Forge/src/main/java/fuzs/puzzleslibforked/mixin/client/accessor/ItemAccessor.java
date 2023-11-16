package fuzs.puzzleslibforked.mixin.client.accessor;

import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Item.class)
public interface ItemAccessor {

    @Accessor(remap = false)
    Object getRenderProperties();

    @Accessor(remap = false)
    void setRenderProperties(Object renderProperties);
}
