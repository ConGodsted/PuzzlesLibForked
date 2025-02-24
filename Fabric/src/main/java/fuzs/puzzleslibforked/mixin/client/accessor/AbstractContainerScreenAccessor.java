package fuzs.puzzleslibforked.mixin.client.accessor;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractContainerScreen.class)
public interface AbstractContainerScreenAccessor {

    @Accessor("imageWidth")
    int getXSize();

    @Accessor("imageHeight")
    int getYSize();

    @Accessor("leftPos")
    int getGuiLeft();

    @Accessor("topPos")
    int getGuiTop();

    @Accessor("hoveredSlot")
    Slot getSlotUnderMouse();
}
