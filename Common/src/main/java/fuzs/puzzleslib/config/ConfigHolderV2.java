package fuzs.puzzleslib.config;

import java.nio.file.Paths;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * a config holder for holding mod configs
 * there are three different kinds depending on where the data shall be used: CLIENT, COMMON, SERVER
 * this implementation is not limited to three held configs though, as many configs as desired may be added (file names must be different!)
 * instead of retrieving configs via mod config type they are stored by class type
 */
public interface ConfigHolderV2 {

    /**
     * @param clazz config clazz type
     * @param <T>   config type
     * @return      the config holder
     */
    <T extends AbstractConfig> ConfigDataHolderV2<T> getHolder(Class<T> clazz);

    /**
     * @param clazz config clazz type
     * @param <T>   config type
     * @return      the actual config
     */
    default <T extends AbstractConfig> T get(Class<T> clazz) {
        return this.getHolder(clazz).config();
    }

    /**
     * register config event and configs themselves for <code>modId</code>
     *
     * @param modId modId to register for
     */
    void bakeConfigs(String modId);

    /**
     * @param modId mod id this config belongs to
     * @return config name
     */
    static String simpleName(String modId) {
        return String.format("%s.toml", modId);
    }

    /**
     * @param modId mod id this config belongs to
     * @param type type of config
     * @return config name as when generated by Forge
     */
    static String defaultName(String modId, String type) {
        return String.format("%s-%s.toml", modId, type);
    }

    /**
     * @param configDir dir to move config to
     * @param fileName config file name
     * @return path to config in dir
     */
    static String moveToDir(String configDir, String fileName) {
        return Paths.get(configDir, fileName).toString();
    }

    /**
     * builder interface for registering configs, not needed anymore after initial registration is complete,
     * but no new instance is created, so we only store the super type {@link ConfigHolderV2}
     */
    interface Builder extends ConfigHolderV2 {

        /**
         * register a new client config to the holder/builder
         *
         * @param clazz         client config main class
         * @param clientConfig  client config factory
         * @param <T>           client config type
         * @return              the builder we are working with
         */
        @Deprecated(forRemoval = true)
        default <T extends AbstractConfig> Builder client(Class<T> clazz, Supplier<T> clientConfig) {
            return this.clientConfig(clazz, clientConfig);
        }

        /**
         * register a new client config to the holder/builder
         *
         * @param clazz         common config main class
         * @param commonConfig  common config factory
         * @param <T>           common config type
         * @return              the builder we are working with
         */
        @Deprecated(forRemoval = true)
        default <T extends AbstractConfig> Builder common(Class<T> clazz, Supplier<T> commonConfig) {
            return this.commonConfig(clazz, commonConfig);
        }

        /**
         * register a new client config to the holder/builder
         *
         * @param clazz         server config main class
         * @param serverConfig  server config factory
         * @param <T>           server config type
         * @return              the builder we are working with
         */
        @Deprecated(forRemoval = true)
        default <T extends AbstractConfig> Builder server(Class<T> clazz, Supplier<T> serverConfig) {
            return this.serverConfig(clazz, serverConfig);
        }

        /**
         * register a new client config to the holder/builder
         *
         * @param clazz         client config main class
         * @param clientConfig  client config factory
         * @param <T>           client config type
         * @return              the builder we are working with
         */
        <T extends AbstractConfig> Builder clientConfig(Class<T> clazz, Supplier<T> clientConfig);

        /**
         * register a new client config to the holder/builder
         *
         * @param clazz         common config main class
         * @param commonConfig  common config factory
         * @param <T>           common config type
         * @return              the builder we are working with
         */
        <T extends AbstractConfig> Builder commonConfig(Class<T> clazz, Supplier<T> commonConfig);

        /**
         * register a new client config to the holder/builder
         *
         * @param clazz         server config main class
         * @param serverConfig  server config factory
         * @param <T>           server config type
         * @return              the builder we are working with
         */
        <T extends AbstractConfig> Builder serverConfig(Class<T> clazz, Supplier<T> serverConfig);

        /**
         * this sets the file name on {@link ConfigDataHolderV2}, it's only used for storing,
         * since actually it's only ever need in this class when calling {@link #bakeConfigs}
         *
         * by default this is set to {@link #defaultName}, otherwise {@link #simpleName} and {@link #moveToDir} exist for convenience
         *
         * @param clazz     config main class
         * @param fileName  file name operator, passed in is the modId
         * @param <T>       config type
         * @return          the builder we are working with
         */
        <T extends AbstractConfig> Builder setFileName(Class<T> clazz, UnaryOperator<String> fileName);
    }
}
