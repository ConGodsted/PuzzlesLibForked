package fuzs.puzzleslibforked.client.core;

import fuzs.puzzleslibforked.client.model.geom.ModelLayerRegistry;
import fuzs.puzzleslibforked.util.PuzzlesUtil;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * all sorts of instance factories that need to be created on a per-mod basis
 */
public interface ClientFactories {
    /**
     * instance of the client factories SPI
     */
    ClientFactories INSTANCE = PuzzlesUtil.loadServiceProvider(ClientFactories.class);

    /**
     * this is very much unnecessary as the method is only ever called from loader specific code anyway which does have
     * access to the specific mod constructor, but for simplifying things and having this method in a common place we keep it here
     *
     * @param modId         the mod id for registering events on Forge to the correct mod event bus
     * @return              provides a consumer for loading a mod being provided the base class
     */
    Consumer<ClientModConstructor> clientModConstructor(String modId);

    /**
     * helper for creating {@link ModelLayerRegistry} objects with a provided <code>modId</code>>
     *
     * @param modId         the mod to create registry for
     * @return              mod specific registry instance
     */
    default ModelLayerRegistry modelLayerRegistration(String modId) {
        return ModelLayerRegistry.of(modId);
    }

    /**
     * creates a new creative mode tab, handles adding to the creative screen
     * use this when one tab is enough for the mod, <code>tabId</code> defaults to "main"
     *
     * @param modId             the mod this tab is used by
     * @param stackSupplier     the display stack
     * @return                  the creative mode tab
     *
     * @deprecated              moved to common package in {@link fuzs.puzzleslibforked.core.CommonFactories#creativeTab}
     */
    @Deprecated(forRemoval = true)
    default CreativeModeTab creativeTab(String modId, Supplier<ItemStack> stackSupplier) {
        return this.creativeTab(modId, "main", stackSupplier);
    }

    /**
     * creates a new creative mode tab, handles adding to the creative screen
     *
     * @param modId             the mod this tab is used by
     * @param tabId             the key for this tab, useful when the mod has multiple
     * @param stackSupplier     the display stack
     * @return                  the creative mode tab
     *
     * @deprecated              moved to common package in {@link fuzs.puzzleslibforked.core.CommonFactories#creativeTab}
     */
    @Deprecated(forRemoval = true)
    CreativeModeTab creativeTab(String modId, String tabId, Supplier<ItemStack> stackSupplier);
}
