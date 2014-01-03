/*
 * Copyright (C) 2013 Spencer Alderman
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.rogue.plugintemplate.config;

import com.rogue.plugintemplate.PluginTemplate;
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
