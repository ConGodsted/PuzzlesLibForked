package fuzs.puzzleslib.api.client.event.v1;

import fuzs.puzzleslib.api.event.v1.core.FabricEventFactory;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public final class ModelEvents {
    public static final Event<ModifyBakingResult> MODIFY_BAKING_RESULT = FabricEventFactory.create(ModifyBakingResult.class);
    public static final Event<BakingCompleted> BAKING_COMPLETED = FabricEventFactory.create(BakingCompleted.class);

    @FunctionalInterface
    public interface ModifyBakingResult {

        /**
         * Fired when the resource manager is reloading models and models have been baked, but before they are passed on for caching.
         *
         * @param models       all baked models for modifying
         * @param modelBakery  the bakery
         */
        void onModifyBakingResult(Map<ResourceLocation, BakedModel> models, ModelBakery modelBakery);
    }

    @FunctionalInterface
    public interface BakingCompleted {

        /**
         * Fired after the resource manager has reloaded models. Does not allow for modifying the models map, for that use {@link ModifyBakingResult}.
         *
         * @param modelManager model manager instance
         * @param models       all baked models, the collection is read-only
         * @param modelBakery  the bakery
         */
        void onBakingCompleted(ModelManager modelManager, Map<ResourceLocation, BakedModel> models, ModelBakery modelBakery);
    }
}