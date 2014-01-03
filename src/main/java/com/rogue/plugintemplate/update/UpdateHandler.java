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
package com.rogue.plugintemplate.update;

import com.rogue.plugintemplate.PluginTemplate;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import org.bukkit.plugin.Plugin;

/**
 * Handles the update process for {@link PluginTemplate}
 *
 * @since 1.0.0
 * @author 1Rogue
 * @version 1.0.0
 */
public class UpdateHandler {
    
    protected final PluginTemplate plugin;
    protected final Choice choice;
    protected byte debug = 0;
    
    /**
     * Constructor for {@link UpdateHandler}
     * 
     * @since 1.0.0
     * @version 1.0.0
     * 
     * @param plugin The main {@link PluginTemplate} instance
     * @param choice The downloading option to use
     */
    public UpdateHandler(PluginTemplate plugin, Choice choice) {
        this.plugin = plugin;
        this.choice = choice;
        this.plugin.getServer().getScheduler().runTaskLater(this.plugin,
                new UpdateRunnable(this.plugin, choice),
                10L);
    }
    
    protected void handleUpdate(Result result) {
        if (result == Result.UPDATE_AVAILABLE) {
            this.registerNewNotifier();
        } else {
            result.handleUpdate(this.plugin.getLogger());
        }
    }
    
    protected void registerNewNotifier() {
        this.plugin.getListenerManager().registerListener("update",
                new UpdateListener("A new update is available for "
                        + this.plugin.getDescription().getFullName()
                        + "!"));
    }
    
    public void setDebug(byte debug) {
        this.debug = debug;
    }

}

/**
 * Runs an update check
 * 
 * TODO: Move to DBO and Curse API
 *
 * @since 1.0.0
 * @author 1Rogue
 * @version 1.0.0
 */
class UpdateRunnable extends UpdateHandler implements Runnable {
    
    private final String VERSION_URL = "https://raw.github.com/1Rogue/PluginTemplate/master/VERSION";
    private Result result;
    
    /**
     * Constructor for {@link UpdateRunnable}
     * 
     * @since 1.0.0
     * @version 1.0.0
     * 
     * @param plugin The {@link PluginTemplate} instance
     * @param choice The {@link Choice} for downloading
     */
    public UpdateRunnable(PluginTemplate plugin, Choice choice) {
        super(plugin, choice);
    }

    /**
     * Runs the update process
     * 
     * @since 1.0.0
     * @version 1.0.0
     */
    public void run() {
        boolean current = false;
        if (this.choice.doCheck()) {
            current = this.checkVersion();
        }
        if (this.choice.doDownload() && !current) {
            this.handleUpdate(this.downloadJar());
        } else {
            if (current) {
                this.handleUpdate(Result.NO_UPDATE);
            } else {
                this.handleUpdate(Result.UPDATE_AVAILABLE);
            }
        }
        
    }
    
    /**
     * Downloads the latest jarfile for the {@link Plugin}
     * 
     * @since 1.0.0
     * @version 1.0.0
     * 
     * @return The download result
     */
    public Result downloadJar() {
        File updateFolder = this.plugin.getServer().getUpdateFolderFile();
        //TODO: Add support
        return Result.DISABLED;
    }
    
    /**
     * Checks the current {@link Plugin} version against the latest live version
     * 
     * @since 1.0.0
     * @version 1.0.0
     * 
     * @return True if plugin is latest version, false otherwise
     */
    public boolean checkVersion() {
        boolean current = true;
        String curVersion = this.plugin.getDescription().getVersion();
        InputStream stream = null;
        InputStreamReader isr = null;
        BufferedReader reader = null;
        try {
            URL call = new URL(this.VERSION_URL);
            stream = call.openStream();
            isr = new InputStreamReader(stream);
            reader = new BufferedReader(isr);
            String latest = reader.readLine();
            current = latest.equalsIgnoreCase(curVersion);
        } catch (MalformedURLException ex) {
            plugin.getLogger().log(Level.SEVERE,
                    "Error checking for an update",
                    this.debug >= 3 ? ex : "");
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE,
                    "Error checking for an update",
                    this.debug >= 3 ? ex : "");
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
                if (isr != null) {
                    isr.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ex) {
                this.plugin.getLogger().log(Level.SEVERE,
                        "Error closing updater streams!",
                        this.debug >= 3 ? ex : "");
            }
        }
        return current;
    }

}