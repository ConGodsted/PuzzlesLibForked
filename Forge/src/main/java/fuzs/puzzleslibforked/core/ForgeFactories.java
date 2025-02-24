package fuzs.puzzleslibforked.core;

import fuzs.puzzleslibforked.capability.CapabilityController;
import fuzs.puzzleslibforked.capability.ForgeCapabilityController;
import fuzs.puzzleslibforked.config.ConfigCore;
import fuzs.puzzleslibforked.config.ConfigHolder;
import fuzs.puzzleslibforked.config.ForgeConfigHolderImpl;
import fuzs.puzzleslibforked.init.ForgeRegistryManager;
import fuzs.puzzleslibforked.init.RegistryManager;
import fuzs.puzzleslibforked.network.ForgeNetworkHandler;
import fuzs.puzzleslibforked.network.NetworkHandler;
import fuzs.puzzleslibforked.proxy.ForgeClientProxy;
import fuzs.puzzleslibforked.proxy.ForgeServerProxy;
import fuzs.puzzleslibforked.proxy.Proxy;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * factories for various utilities on Forge
 */
public final class ForgeFactories implements CommonFactories {

    @Override
    public Consumer<ModConstructor> modConstructor(String modId) {
        return constructor -> ForgeModConstructor.construct(modId, constructor);
    }

    @Override
    public NetworkHandler network(String modId, boolean clientAcceptsVanillaOrMissing, boolean serverAcceptsVanillaOrMissing) {
        return ForgeNetworkHandler.of(modId, clientAcceptsVanillaOrMissing, serverAcceptsVanillaOrMissing);
    }

    @SuppressWarnings("Convert2MethodRef")
    @Override
    public Supplier<Proxy> clientProxy() {
        return () -> new ForgeClientProxy();
    }

    @SuppressWarnings("Convert2MethodRef")
    @Override
    public Supplier<Proxy> serverProxy() {
        return () -> new ForgeServerProxy();
    }

    @Override
    public <T extends ConfigCore> ConfigHolder.Builder clientConfig(Class<T> clazz, Supplier<T> clientConfig) {
        return new ForgeConfigHolderImpl().clientConfig(clazz, clientConfig);
    }

    @Override
    public <T extends ConfigCore> ConfigHolder.Builder commonConfig(Class<T> clazz, Supplier<T> commonConfig) {
        return new ForgeConfigHolderImpl().commonConfig(clazz, commonConfig);
    }

    @Override
    public <T extends ConfigCore> ConfigHolder.Builder serverConfig(Class<T> clazz, Supplier<T> serverConfig) {
        return new ForgeConfigHolderImpl().serverConfig(clazz, serverConfig);
    }

    @Override
    public RegistryManager registration(String modId, boolean deferred) {
        return ForgeRegistryManager.of(modId);
    }

    @Override
    public CapabilityController capabilities(String modId) {
        return ForgeCapabilityController.of(modId);
    }

    @Override
    public CreativeModeTab creativeTab(String modId, String tabId, Supplier<ItemStack> stackSupplier) {
        return new CreativeModeTab(modId + "." + tabId) {

            @Override
            public ItemStack makeIcon() {
                return stackSupplier.get();
            }
        };
    }
}
