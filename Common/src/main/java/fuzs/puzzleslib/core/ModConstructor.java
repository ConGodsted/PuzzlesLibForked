package fuzs.puzzleslib.core;

import com.mojang.brigadier.CommandDispatcher;
import fuzs.puzzleslib.api.biome.v1.BiomeLoadingContext;
import fuzs.puzzleslib.api.biome.v1.BiomeLoadingPhase;
import fuzs.puzzleslib.util.CreativeModeTabConfigurator;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * a base class for a mods main common class, contains a bunch of methods for registering various things
 */
public interface ModConstructor {

    /**
     * this is very much unnecessary as the method is only ever called from loader specific code anyway which does have
     * access to the specific mod constructor, but for simplifying things and having this method in a common place we keep it here
     *
     * @param modId                the mod id for registering events on Forge to the correct mod event bus
     * @param modConstructor       the main mod instance for mod setup
     * @param contentRegistrations specific content this mod uses that needs to be additionally registered
     */
    static void construct(String modId, Supplier<ModConstructor> modConstructor, ContentRegistrationFlags... contentRegistrations) {
        CommonFactories.INSTANCE.constructMod(modId, modConstructor, contentRegistrations);
    }

    /**
     * runs when the mod is first constructed, mainly used for registering game content, configs, network packages, and event callbacks
     */
    default void onConstructMod() {

    }

    /**
     * runs after content has been registered, so it's safe to use here
     * used to set various values and settings for already registered content
     *
     * @param context enqueue work to be run sequentially for all mods as the setup phase runs in parallel on Forge
     */
    default void onCommonSetup(final ModLifecycleContext context) {

    }

    /**
     * provides a place for registering spawn placements for entities
     *
     * @param context add to spawn placement register
     */
    default void onRegisterSpawnPlacements(final SpawnPlacementsContext context) {

    }

    /**
     * allows for registering default attributes for our own entities
     * anything related to already existing entities (vanilla and modded) needs to be done in {@link #onEntityAttributeModification}
     *
     * @param context add to entity attribute map
     */
    default void onEntityAttributeCreation(final EntityAttributesCreateContext context) {

    }

    /**
     * allows for modifying the attributes of an already existing entity, attributes are modified individually
     *
     * @param context replace/add attribute to entity attribute map
     */
    default void onEntityAttributeModification(final EntityAttributesModifyContext context) {

    }

    /**
     * allows for setting burn times for fuel items, e.g. in a furnace
     *
     * @param context add fuel burn time for items/blocks
     */
    default void onRegisterFuelBurnTimes(final FuelBurnTimesContext context) {

    }

    /**
     * register a new command, also natively supports replacing existing commands
     *
     * @param context context with helper objects for registering commands
     */
    default void onRegisterCommands(final RegisterCommandsContext context) {

    }

    /**
     * allows for replacing built-in {@link LootTable}s on loading
     *
     * @param context replace a whole {@link LootTable}
     */
    default void onLootTableReplacement(final LootTablesReplaceContext context) {

    }

    /**
     * allows changing of {@link LootPool}s in a {@link LootTable}
     *
     * @param context add or remove a {@link LootPool}
     */
    default void onLootTableModification(final LootTablesModifyContext context) {

    }

    /**
     * @param context allows for registering modifications (including additions and removals) to biomes loaded from the current data pack
     */
    default void onRegisterBiomeModifications(final BiomeModificationsContext context) {

    }

    /**
     * @param context register blocks that {@link net.minecraft.world.level.block.FireBlock} can spread to
     */
    default void onRegisterFlammableBlocks(final FlammableBlocksContext context) {

    }

    /**
     * @param context register new creative mode tabs via the respective builder
     */
    default void onRegisterCreativeModeTabs(final CreativeModeTabContext context) {

    }

    /**
     * enqueue work to be run sequentially for all mods as the construct/setup phase runs in parallel on Forge
     */
    @FunctionalInterface
    interface ModLifecycleContext {

        /**
         * enqueue work to be run sequentially for all mods as the construct/setup phase runs in parallel on Forge
         *
         * @param runnable the work
         */
        void enqueueWork(Runnable runnable);
    }

    /**
     * register a default spawn placement for entities
     */
    @FunctionalInterface
    interface SpawnPlacementsContext {

        /**
         * registers a spawning behavior for an <code>entityType</code>
         *
         * @param entityType        the entity type
         * @param location          type of spawn placement, probably {@link SpawnPlacements.Type#ON_GROUND}
         * @param heightmap         heightmap type, probably {@link Heightmap.Types#MOTION_BLOCKING_NO_LEAVES}
         * @param spawnPredicate    custom spawn predicate for mob
         * @param <T>               type of entity
         */
        <T extends Mob> void registerSpawnPlacement(EntityType<T> entityType, SpawnPlacements.Type location, Heightmap.Types heightmap, SpawnPlacements.SpawnPredicate<T> spawnPredicate);
    }

    /**
     * register default attributes for our own entities
     */
    @FunctionalInterface
    interface EntityAttributesCreateContext {

        /**
         * register attributes for our own entities, modifying attributes for any other entity (vanilla or modded) should be done using {@link EntityAttributesModifyContext}
         *
         * @param type type of entity
         * @param builder the attribute supplier builder
         */
        void registerEntityAttributes(EntityType<? extends LivingEntity> type, AttributeSupplier.Builder builder);
    }

    /**
     * registers modifications to attributes of already existing entities (not from our mod)
     */
    @FunctionalInterface
    interface EntityAttributesModifyContext {

        /**
         * use this method for modifying attributes of existing entity types
         * this overrides/adds attributes individually as this is what is done on Forge
         *
         * @param type                  type of entity
         * @param attribute             attribute to override/add
         */
        default void registerAttributeModification(EntityType<? extends LivingEntity> type, Attribute attribute) {
            this.registerAttributeModification(type, attribute, attribute.getDefaultValue());
        }

        /**
         * use this method for modifying attributes of existing entity types
         * this overrides/adds attributes individually as this is what is done on Forge
         *
         * @param type                  type of entity
         * @param attribute             attribute to override/add
         * @param attributeValue        new value, possibly {@link Attribute#getDefaultValue()}
         */
        void registerAttributeModification(EntityType<? extends LivingEntity> type, Attribute attribute, double attributeValue);
    }

    /**
     * Applies fuel burn times instead of implementing this on the item side.
     */
    @FunctionalInterface
    interface FuelBurnTimesContext {

        /**
         * Registers an <code>item</code> as a fuel with the given <code>burnTime</code>.
         *
         * @param items items to add
         * @param burnTime burn time in ticks
         */
        void registerFuel(int burnTime, ItemLike item, ItemLike... items);
    }

    /**
     * register a new command, also supports replacing existing commands by default
     *
     * @param dispatcher    the dispatcher used for registering commands
     * @param environment   command selection environment
     * @param context       registry access context
     */
    record RegisterCommandsContext(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context, Commands.CommandSelection environment) {

    }

    /**
     * basic context for loot tables
     */
    abstract class LootTablesContext {
        /**
         * access to all loot tables
         */
        private final LootTables lootManager;
        /**
         * the loot table id
         */
        private final ResourceLocation id;

        /**
         * @param lootManager   access to all loot tables
         * @param id            the loot table id
         */
        protected LootTablesContext(LootTables lootManager, ResourceLocation id) {
            this.lootManager = lootManager;
            this.id = id;
        }

        /**
         * @return  access to all loot tables
         */
        public final LootTables getLootManager() {
            return this.lootManager;
        }

        /**
         * @return  the loot table id
         */
        public final ResourceLocation getId() {
            return this.id;
        }
    }

    /**
     * allows for replacing built-in {@link LootTable}s on loading
     */
    abstract class LootTablesReplaceContext extends LootTablesContext {
        /**
         * the original loot table that will be replaced
         */
        private final LootTable original;

        /**
         * @param lootManager   access to all loot tables
         * @param id            the loot table id
         * @param original      the original loot table that will be replaced
         */
        public LootTablesReplaceContext(LootTables lootManager, ResourceLocation id, LootTable original) {
            super(lootManager, id);
            this.original = original;
        }

        /**
         * @return the original loot table that will be replaced
         */
        public LootTable getLootTable() {
            return this.original;
        }

        /**
         * @param table     replacement for <code>original</code>
         */
        public abstract void setLootTable(LootTable table);
    }

    /**
     * allows changing of {@link LootPool}s in a {@link LootTable}
     */
    abstract class LootTablesModifyContext extends LootTablesContext {

        /**
         * @param lootManager   access to all loot tables
         * @param id            the loot table id
         */
        public LootTablesModifyContext(LootTables lootManager, ResourceLocation id) {
            super(lootManager, id);
        }

        /**
         * @param pool  add a {@link LootPool}
         */
        public abstract void addLootPool(LootPool pool);

        /**
         * @param index     pool to remove at index, pools are indexed starting from 0
         * @return          was removing the pool successful (has a pool at <code>index</code> been found)
         */
        public abstract boolean removeLootPool(int index);
    }

    /**
     * Allows for registering modifications (including additions and removals) to biomes loaded from the current data pack.
     */
    @FunctionalInterface
    interface BiomeModificationsContext {

        /**
         * Add a modification to this context.
         *
         * @param phase the loading phase, mainly to separate additions and removals
         * @param selector selection context for current biome
         * @param modifier modification context
         */
        void register(BiomeLoadingPhase phase, Predicate<BiomeLoadingContext> selector, Consumer<fuzs.puzzleslib.api.biome.v1.BiomeModificationContext> modifier);
    }

    /**
     * Register blocks that {@link net.minecraft.world.level.block.FireBlock} can spread to.
     */
    @FunctionalInterface
    interface FlammableBlocksContext {

        /**
         * Register blocks that {@link net.minecraft.world.level.block.FireBlock} can spread to.
         *
         * @param encouragement a value determining how fast this block will spread fire to other nearby flammable blocks
         * @param flammability a value determining how easily this block catches on fire from nearby fires
         * @param blocks blocks to register <code>encouragement</code> and <code>flammability</code> for
         */
        void registerFlammable(int encouragement, int flammability, Block... blocks);
    }

    /**
     * Register new creative mode tabs via the respective builder.
     */
    @FunctionalInterface
    interface CreativeModeTabContext {

        /**
         * Register a {@link CreativeModeTabConfigurator} which is used to configure a {@link net.minecraft.world.item.CreativeModeTab.Builder}
         *
         * @param configurator the configurator instance
         */
        void registerCreativeModeTab(CreativeModeTabConfigurator configurator);
    }
}
