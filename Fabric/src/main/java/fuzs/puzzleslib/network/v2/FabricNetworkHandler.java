package fuzs.puzzleslib.network.v2;

import com.google.common.collect.Maps;
import fuzs.puzzleslib.network.v2.serialization.MessageSerializers;
import fuzs.puzzleslib.proxy.FabricProxy;
import fuzs.puzzleslib.proxy.Proxy;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class FabricNetworkHandler implements NetworkHandler {
    private static final Map<String, FabricNetworkHandler> MOD_TO_NETWORK = Maps.newConcurrentMap();

    private final Map<Class<?>, ResourceLocation> messageChannelNames = Maps.newIdentityHashMap();
    private final String modId;
    private final AtomicInteger discriminator = new AtomicInteger();

    private FabricNetworkHandler(String modId) {
        this.modId = modId;
    }

    @SuppressWarnings("unchecked")
    private <T extends Record & ClientboundMessage<T>> void registerClientbound(Class<?> clazz) {
        this.register((Class<T>) clazz, ((FabricProxy) Proxy.INSTANCE)::registerClientReceiverV2);
    }

    @SuppressWarnings("unchecked")
    private <T extends Record & ServerboundMessage<T>> void registerServerbound(Class<?> clazz) {
        this.register((Class<T>) clazz, ((FabricProxy) Proxy.INSTANCE)::registerServerReceiverV2);
    }

    private <T> void register(Class<T> clazz, BiConsumer<ResourceLocation, Function<FriendlyByteBuf, T>> register) {
        ResourceLocation channelName = this.nextIdentifier();
        if (this.messageChannelNames.put(clazz, channelName) != null) throw new IllegalStateException("Duplicate message of type %s".formatted(clazz));
        register.accept(channelName, MessageSerializers.findByType(clazz)::read);
    }

    private ResourceLocation nextIdentifier() {
        return new ResourceLocation(this.modId, "play/" + this.discriminator.getAndIncrement());
    }

    @Override
    public <T extends Record & ClientboundMessage<T>> Packet<?> toClientboundPacket(T message) {
        return this.toPacket(ServerPlayNetworking::createS2CPacket, message);
    }

    @Override
    public <T extends Record & ServerboundMessage<T>> Packet<?> toServerboundPacket(T message) {
        return this.toPacket(ClientPlayNetworking::createC2SPacket, message);
    }

    @SuppressWarnings("unchecked")
    private <T extends Record> Packet<?> toPacket(BiFunction<ResourceLocation, FriendlyByteBuf, Packet<?>> packetFactory, T message) {
        Class<T> clazz = (Class<T>) message.getClass();
        if (!clazz.isRecord()) throw new IllegalArgumentException("Message of type %s is not a record".formatted(clazz));
        FriendlyByteBuf byteBuf = PacketByteBufs.create();
        MessageSerializers.findByType(clazz).write(byteBuf, message);
        ResourceLocation channelName = this.messageChannelNames.get(clazz);
        Objects.requireNonNull(channelName, "Unknown message of type %s".formatted(clazz));
        return packetFactory.apply(channelName, byteBuf);
    }

    public synchronized static NetworkHandler of(String modId) {
        return MOD_TO_NETWORK.computeIfAbsent(modId, FabricNetworkHandler::new);
    }

    public static class FabricBuilder extends Builder {

        public FabricBuilder(String modId) {
            super(modId);
        }

        @Override
        public NetworkHandler build() {
            FabricNetworkHandler networkHandler = MOD_TO_NETWORK.computeIfAbsent(this.modId, FabricNetworkHandler::new);
            for (Class<? extends ClientboundMessage<?>> message : this.clientboundMessages) {
                networkHandler.registerClientbound(message);
            }
            for (Class<? extends ServerboundMessage<?>> message : this.serverboundMessages) {
                networkHandler.registerServerbound(message);
            }
            return networkHandler;
        }
    }
}
