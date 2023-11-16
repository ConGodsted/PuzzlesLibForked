package fuzs.puzzleslibforked.client.core;

import fuzs.puzzleslibforked.client.gui.screens.CommonScreens;
import fuzs.puzzleslibforked.core.CoreServices;

import static fuzs.puzzleslibforked.util.PuzzlesUtil.loadServiceProvider;

/**
 * services which may only be loaded on the client side
 *
 * @deprecated remove in favor of decentralized access to prevent loading all SPIs at the same time
 */
@Deprecated(forRemoval = true)
public class ClientCoreServices extends CoreServices {
    /**
     * important client exclusive factories
     */
    public static final ClientFactories FACTORIES = ClientFactories.INSTANCE;
    /**
     * a helper class for dealing with instances of {@link net.minecraft.client.gui.screens.Screen}
     */
    public static final CommonScreens SCREENS = CommonScreens.INSTANCE;
}
