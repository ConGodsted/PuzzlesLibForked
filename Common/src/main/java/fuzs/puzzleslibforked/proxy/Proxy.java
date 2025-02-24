package fuzs.puzzleslibforked.proxy;

import fuzs.puzzleslibforked.core.CommonFactories;
import fuzs.puzzleslibforked.core.DistTypeExecutor;
import fuzs.puzzleslibforked.network.Message;
import fuzs.puzzleslibforked.network.NetworkHandler;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.function.Function;

/**
 * proxy base class for client and server implementations
 * mainly used for handling content not present on a physical server
 */
public interface Proxy {
    /**
     * sided proxy depending on physical side
     */
    @SuppressWarnings("Convert2MethodRef")
    Proxy INSTANCE = DistTypeExecutor.getForDistType(() -> CommonFactories.INSTANCE.clientProxy(), () -> CommonFactories.INSTANCE.serverProxy());

    /**
     * @return client player from Minecraft singleton when on physical client, otherwise null
     */
    Player getClientPlayer();

    /**
     * @return client level from Minecraft singleton when on physical client, otherwise null
     */
    Level getClientLevel();

    /**
     * @return Minecraft singleton instance on physical client, otherwise null
     */
    Object getClientInstance();

    /**
     * @return the connection to the server on physical client, otherwise null
     */
    Connection getClientConnection();

    /**
     * @return current game server, null when not in a world
     */
    MinecraftServer getGameServer();

    /**
     * only used by Fabric implementation of {@link NetworkHandler}
     * @param channelName channel name
     * @param factory message factory when received
     */
    default void registerClientReceiver(ResourceLocation channelName, Function<FriendlyByteBuf, Message<?>> factory) {

    }

    /**
     * only used by Fabric implementation of {@link NetworkHandler}
     * @param channelName channel name
     * @param factory message factory when received
     */
    default void registerServerReceiver(ResourceLocation channelName, Function<FriendlyByteBuf, Message<?>> factory) {

    }

    /**
     * useful for item tooltips
     * @return is the control key (command on mac) pressed
     */
    boolean hasControlDown();

    /**
     * useful for item tooltips
     * @return is the shift key pressed
     */
    boolean hasShiftDown();

    /**
     * useful for item tooltips
     * @return is the alt key pressed
     */
    boolean hasAltDown();
}
