package fuzs.puzzleslibforked.core;

import fuzs.puzzleslibforked.capability.CapabilityController;
import fuzs.puzzleslibforked.capability.FabricCapabilityController;
import fuzs.puzzleslibforked.config.ConfigCore;
import fuzs.puzzleslibforked.config.ConfigHolder;
import fuzs.puzzleslibforked.config.FabricConfigHolderImpl;
import fuzs.puzzleslibforked.init.FabricRegistryManager;
import fuzs.puzzleslibforked.init.RegistryManager;
import fuzs.puzzleslibforked.network.FabricNetworkHandler;
import fuzs.puzzleslibforked.network.NetworkHandler;
import fuzs.puzzleslibforked.proxy.FabricClientProxy;
import fuzs.puzzleslibforked.proxy.FabricServerProxy;
import fuzs.puzzleslibforked.proxy.Proxy;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * factories for various utilities on Fabric
 */
public final class FabricFactories implements CommonFactories {

    @Override
    public Consumer<ModConstructor> modConstructor(String modId) {
        return constructor -> FabricModConstructor.construct(modId, constructor);
    }

    @Override
    public NetworkHandler network(String modId, boolean clientAcceptsVanillaOrMissing, boolean serverAcceptsVanillaOrMissing) {
        return FabricNetworkHandler.of(modId);
    }

    @SuppressWarnings("Convert2MethodRef")
    @Override
    public Supplier<Proxy> clientProxy() {
        return () -> new FabricClientProxy();
    }

    @SuppressWarnings("Convert2MethodRef")
    @Override
    public Supplier<Proxy> serverProxy() {
        return () -> new FabricServerProxy();
    }

    @Override
    public <T extends ConfigCore> ConfigHolder.Builder clientConfig(Class<T> clazz, Supplier<T> clientConfig) {
        return new FabricConfigHolderImpl().clientConfig(clazz, clientConfig);
    }

    @Override
    public <T extends ConfigCore> ConfigHolder.Builder commonConfig(Class<T> clazz, Supplier<T> commonConfig) {
        return new FabricConfigHolderImpl().commonConfig(clazz, commonConfig);
    }

    @Override
    public <T extends ConfigCore> ConfigHolder.Builder serverConfig(Class<T> clazz, Supplier<T> serverConfig) {
        return new FabricConfigHolderImpl().serverConfig(clazz, serverConfig);
    }

    @Override
    public RegistryManager registration(String modId, boolean deferred) {
        return FabricRegistryManager.of(modId, deferred);
    }

    @Override
    public CapabilityController capabilities(String modId) {
        return FabricCapabilityController.of(modId);
    }

    @Override
    public CreativeModeTab creativeTab(String modId, String tabId, Supplier<ItemStack> stackSupplier) {
        return FabricItemGroupBuilder.build(new ResourceLocation(modId, tabId), stackSupplier);
    }
}
