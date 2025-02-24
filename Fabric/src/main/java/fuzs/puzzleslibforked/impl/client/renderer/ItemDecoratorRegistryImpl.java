package fuzs.puzzleslibforked.impl.client.renderer;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.blaze3d.systems.RenderSystem;
import fuzs.puzzleslibforked.api.client.renderer.ItemDecoratorRegistry;
import fuzs.puzzleslibforked.client.renderer.entity.DynamicItemDecorator;
import net.minecraft.client.gui.Font;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.Objects;

public final class ItemDecoratorRegistryImpl implements ItemDecoratorRegistry {
    private static final Multimap<Item, DynamicItemDecorator> DECORATORS = HashMultimap.create();

    @Override
    public void register(ItemLike item, DynamicItemDecorator itemDecorator) {
        Objects.requireNonNull(item, "item is null");
        Objects.requireNonNull(item.asItem(), "item is null");
        Objects.requireNonNull(itemDecorator, "decorator is null");
        DECORATORS.put(item.asItem(), itemDecorator);
    }

    public static void render(Font font, ItemStack stack, int itemPosX, int itemPosY, float blitOffset) {
        resetRenderState();
        for (DynamicItemDecorator itemDecorator : DECORATORS.get(stack.getItem())) {
            if (itemDecorator.renderItemDecorations(font, stack, itemPosX, itemPosY, blitOffset)) {
                resetRenderState();
            }
        }
    }

    private static void resetRenderState() {
        RenderSystem.enableTexture();
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
    }
}
