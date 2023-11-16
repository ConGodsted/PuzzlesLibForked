package fuzs.puzzleslibforked.core;

import fuzs.puzzleslibforked.init.CommonGameRuleFactory;
import fuzs.puzzleslibforked.util.PuzzlesUtil;

import static fuzs.puzzleslibforked.util.PuzzlesUtil.loadServiceProvider;

/**
 * common core services
 *
 * @deprecated remove in favor of decentralized access to prevent loading all SPIs at the same time
 */
@Deprecated(forRemoval = true)
public class CoreServices {
    /**
     * helper class for mod loader environment
     */
    public static final ModLoaderEnvironment ENVIRONMENT = ModLoaderEnvironment.INSTANCE;
    /**
     * important factories, mainly for networking, registering content and configs
     */
    public static final CommonFactories FACTORIES = CommonFactories.INSTANCE;
    /**
     * useful methods for gameplay related things that require mod loader specific abstractions
     */
    public static final CommonAbstractions ABSTRACTIONS = CommonAbstractions.INSTANCE;
    /**
     * helper class for creating and registering new game rules
     */
    public static final CommonGameRuleFactory GAME_RULES = CommonGameRuleFactory.INSTANCE;

    /**
     * loads a service provider interface yay
     *
     * @param clazz         service class to load
     * @param <T>           service type
     *
     * @return loaded service
     *
     * @deprecated use {@link PuzzlesUtil#loadServiceProvider}
     */
    @Deprecated(forRemoval = true)
    public static <T> T load(Class<T> clazz) {
        return loadServiceProvider(clazz);
    }
}