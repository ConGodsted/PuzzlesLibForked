package fuzs.puzzleslib.api.event.v1.entity;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import org.jetbrains.annotations.Nullable;

public final class EntityLevelEvents {
    public static final EventInvoker<Load> LOAD = EventInvoker.lookup(Load.class);
    public static final EventInvoker<Unload> UNLOAD = EventInvoker.lookup(Unload.class);

    private EntityLevelEvents() {

    }

    @FunctionalInterface
    public interface Load {

        /**
         * Fired when an entity is added to the level on the server.
         *
         * <p>In case the entity was added because it has just been spawned in the level (as opposed to being loaded from saved data),
         * instances of {@link net.minecraft.world.entity.Mob} will additionally provide the {@link MobSpawnType},
         * which essentially allows for preventing certain types of spawns.
         *
         * @param entity    the entity that is being loaded
         * @param level     the level the entity is loaded in
         * @param spawnType if a {@link net.minecraft.world.entity.Mob} was just spawned this provides the spawn type
         * @return {@link EventResult#INTERRUPT} to prevent the entity from being added to the level (on Fabric the entity will instead just immediately be removed again),
         * {@link EventResult#PASS} for the entity to be added normally
         */
        EventResult onLoad(Entity entity, ServerLevel level, @Nullable MobSpawnType spawnType);
    }

    @FunctionalInterface
    public interface Unload {

        /**
         * Fired when an entity is removed from the level on the server.
         *
         * @param entity    the entity that is being unloaded
         * @param level     the level the entity is unloaded in
         */
        void onUnload(Entity entity, ServerLevel level);
    }
}
