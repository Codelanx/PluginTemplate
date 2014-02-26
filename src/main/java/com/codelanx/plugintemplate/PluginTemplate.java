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
package com.codelanx.plugintemplate;

import com.codelanx.plugintemplate.command.CommandHandler;
import com.codelanx.plugintemplate.config.ConfigValues;
import com.codelanx.plugintemplate.config.ConfigurationLoader;
import com.codelanx.plugintemplate.listener.ListenerManager;
import com.codelanx.plugintemplate.metrics.Metrics;
import com.codelanx.plugintemplate.update.Choice;
import com.codelanx.plugintemplate.update.UpdateHandler;
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
    
    private final int ID = 0; // curse plugin id, needs to be set
    private static String NAME;
    private CommandHandler chandle;
    private ConfigurationLoader cloader;
    private ListenerManager listener;
    private UpdateHandler update;
    private final byte debug = 3; //TODO: load from config

    /**
     * Loads informational and configurable aspects of {@link PluginTemplate}
     *
     * @since 1.0.0
     * @version 1.0.0
     */
    @Override
    public void onLoad() {
        PluginTemplate.NAME = this.getDescription().getFullName();
        
        this.getLogger().log(Level.INFO, "Loading configuration...");
        this.cloader = new ConfigurationLoader(this);
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
        
        this.getLogger().log(Level.INFO, "Enabling listeners...");
        this.listener = new ListenerManager(this);
        
        this.getLogger().log(Level.INFO, "Enabling command handler...");
        this.chandle = new CommandHandler(this);
        
        this.getLogger().log(Level.INFO, "Evaluating update checks...");
        boolean check = this.cloader.getBoolean(ConfigValues.UPDATE_CHECK);
        boolean dl = this.cloader.getBoolean(ConfigValues.UPDATE_DOWNLOAD);
        this.update = new UpdateHandler(this, Choice.getChoice(check, dl), this.ID, this.getFile().getName());
        this.update.setDebug(this.debug);
        this.update.runCheck();
    }

    /**
     * Cleans up plugin resources
     *
     * @since 1.0.0
     * @version 1.0.0
     */
    @Override
    public void onDisable() {
        this.listener.cleanup();
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
    
    /**
     * Gets the {@link CommandHandler} for {@link PluginTemplate}
     * 
     * @since 1.0.0
     * @version 1.0.0
     * 
     * @return The {@link CommandHandler} instance
     */
    public CommandHandler getCommandHandler() {
        return this.chandle;
    }
    
    /**
     * Gets the {@link ConfigurationLoader} for {@link PluginTemplate}
     * 
     * @since 1.0.0
     * @version 1.0.0
     * 
     * @return The {@link ConfigurationLoader} instance
     */
    public ConfigurationLoader getConfiguration() {
        return this.cloader;
    }
    
    /**
     * Gets the {@link ListenerManager} for {@link PluginTemplate}
     * 
     * @since 1.0.0
     * @version 1.0.0
     * 
     * @return The {@link ListenerManager} instance
     */
    public ListenerManager getListenerManager() {
        return this.listener;
    }
    
    /**
     * Gets the {@link UpdateHandler} for {@link PluginTemplate}
     * 
     * @since 1.0.0
     * @version 1.0.0
     * 
     * @return The {@link UpdateHandler} instance
     */
    public UpdateHandler getUpdateHandler() {
        return this.update;
    }
    
}
