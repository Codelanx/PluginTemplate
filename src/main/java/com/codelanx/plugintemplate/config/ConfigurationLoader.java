/*
 * Copyright (C) 2013 CodeLanx , All Rights Reserved
 *
 * This work is licensed under a Creative Commons
 * Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 *
 * This program is protected software: You are free to distrubute your
 * own use of this software under the terms of the Creative Commons BY-NC-ND
 * license as published by Creative Commons in the year 2014 or as published
 * by a later date. You may not provide the source files or provide a means
 * of running the software outside of those licensed to use it.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * You should have received a copy of the Creative Commons BY-NC-ND license
 * along with this program. If not, see <https://creativecommons.org/licenses/>.
 */
package com.codelanx.plugintemplate.config;

import com.codelanx.plugintemplate.PluginTemplate;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Loads and manages the main configuration file for {@link InventoryShop}
 *
 * @since 1.0.0
 * @author 1Rogue
 * @version 1.0.0
 */
public final class ConfigurationLoader {

    private final PluginTemplate plugin;
    private final FileConfiguration yaml;

    /**
     * Constructor for {@link ConfigurationLoader}
     *
     * @since 1.0.0
     * @version 1.0.0
     *
     * @param plugin The {@link PluginTemplate} instance
     */
    public ConfigurationLoader(PluginTemplate plugin) {
        this.plugin = plugin;
        this.plugin.saveDefaultConfig();
        this.yaml = this.plugin.getConfig();
        for (ConfigValues conf : ConfigValues.values()) {
            if (!this.yaml.isSet(conf.getPath())) {
                this.yaml.set(conf.getPath(), conf.getDefault());
            }
        }
        this.saveConfig();
    }

    /**
     * Saves the current configuration from memory
     *
     * @since 1.0.0
     * @version 1.0.0
     */
    public void saveConfig() {
        this.plugin.saveConfig();
    }

    /**
     * Gets the configuration file for {@link PluginTemplate}
     *
     * @since 1.3.0
     * @version 1.3.0
     *
     * @return YamlConfiguration file, null if verifyConfig() has not been run
     */
    public FileConfiguration getConfig() {
        return this.yaml;
    }

    /**
     * Gets a string value from the config
     *
     * @since 1.0.0
     * @version 1.0.0
     *
     * @param path Path to string value
     * @return String value
     */
    public synchronized String getString(ConfigValues path) {
        return this.yaml.getString(path.getPath());
    }

    /**
     * Gets an int value from the config
     *
     * @since 1.0.0
     * @version 1.0.0
     *
     * @param path Path to int value
     * @return int value
     */
    public synchronized int getInt(ConfigValues path) {
        return this.yaml.getInt(path.getPath());
    }

    /**
     * Gets a boolean value from the config
     *
     * @since 1.0.0
     * @version 1.0.0
     *
     * @param path Path to boolean value
     * @return boolean value
     */
    public synchronized boolean getBoolean(ConfigValues path) {
        return this.yaml.getBoolean(path.getPath());
    }

}
