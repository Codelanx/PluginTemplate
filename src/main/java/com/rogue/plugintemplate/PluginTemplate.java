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
package com.rogue.plugintemplate;

import com.rogue.plugintemplate.command.CommandHandler;
import com.rogue.plugintemplate.metrics.Metrics;
import java.io.IOException;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main {@link JavaPlugin} class
 *
 * @since 1.0.0
 * @author 1Rogue
 * @version 1.0.0
 */
public class PluginTemplate extends JavaPlugin {
    
    private static String NAME;
    private CommandHandler chandle;

    /**
     * Loads informational and configurable aspects of {@link PluginTemplate}
     *
     * @since 1.0.0
     * @version 1.0.0
     */
    @Override
    public void onLoad() {
        PluginTemplate.NAME = this.getDescription().getFullName();
    }

    /**
     * Enables the separate modules and managers of {@link PluginTemplate}
     *
     * @since 1.0.0
     * @version 1.0.0
     */
    @Override
    public void onEnable() {
        try {
            Metrics metrics = new Metrics(this);
            this.getLogger().log(Level.INFO, "Enabling Metrics...");
            metrics.start();
        } catch (IOException ex) {
            this.getLogger().log(Level.SEVERE, "Error enabling metrics!", ex);
        }
        
        this.getLogger().log(Level.INFO, "Enabling command handler...");
        this.chandle = new CommandHandler(this);
    }

    /**
     * Cleans up plugin resources
     *
     * @since 1.0.0
     * @version 1.0.0
     */
    @Override
    public void onDisable() {
    }

    /**
     * Gets the instance of the plugin in its entirety.
     *
     * @since 1.0.0
     * @version 1.0.0
     *
     * @return The plugin instance
     */
    public static PluginTemplate getPlugin() {
        return (PluginTemplate) Bukkit.getServer().getPluginManager().getPlugin(PluginTemplate.NAME);
    }
    
    /**
     * Converts pre-made strings to have chat colors in them
     * 
     * @param encoded String with unconverted color codes
     * @return string with correct chat colors included
     */
    public static String __(String encoded) {
        return ChatColor.translateAlternateColorCodes('&', encoded);
    }
    
    public CommandHandler getCommandHandler() {
        return this.chandle;
    }
    
}
