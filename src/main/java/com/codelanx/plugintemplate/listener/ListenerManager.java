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
package com.codelanx.plugintemplate.listener;

import com.codelanx.plugintemplate.listener.listeners.*;
import com.codelanx.plugintemplate.PluginTemplate;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

/**
 * Handles listeners for {@link PluginTemplate}
 *
 * @since 1.0.0
 * @author 1Rogue
 * @version 1.0.0
 */
public class ListenerManager {
    
    private final PluginTemplate plugin;
    private final Map<String, Listener> listeners = new HashMap<String, Listener>();
    
    /**
     * {@link ListenerManager} constructor
     * 
     * @since 1.0.0
     * @version 1.0.0
     * 
     * @param plugin The main {@link PluginTemplate} instance
     */
    public ListenerManager(PluginTemplate plugin) {
        
        this.plugin = plugin;
        
        this.listeners.put("example", new SomeListener());
        
        for (Listener l : this.listeners.values()) {
            this.plugin.getServer().getPluginManager().registerEvents(l, this.plugin);
        }
    }
    
    /**
     * Gets a listener by its string name. Returns null if the listener is
     * disabled or not registered.
     * 
     * @since 1.0.0
     * @version 1.0.0
     * 
     * @param name Name of the listener
     * @return The listener class, null if disabled or not registered
     */
    public Listener getListener(String name) {
        return this.listeners.get(name);
    }
    
    /**
     * Returns whether or not a listener is registered under the relevant listener key
     * 
     * @since 1.0.0
     * @version 1.0.0
     * 
     * @param name The key to look for
     * @return <code>true</code> if registered, <code>false</code> otherwise
     */
    public boolean isRegistered(String name) {
        return this.listeners.containsKey(name);
    }

    /**
     * Registers a listener through bukkit and {@link ListenerManager}
     * 
     * @since 1.0.0
     * @version 1.0.0
     * 
     * @param name The name to place the listener as, cannot be the same as a current listener
     * @param listener The listener to register
     * @throws ListenerReregisterException Attempted to register a Listener under a similar key
     */
    public void registerListener(String name, Listener listener) throws ListenerReregisterException {
        if (!this.listeners.containsKey(name)) {
            this.listeners.put(name, listener);
            this.plugin.getServer().getPluginManager().registerEvents(listener, this.plugin);
        } else {
            throw new ListenerReregisterException("Listener Map already contains key: " + name);
        }
    }
    
    /**
     * Unregisters all the listeners attached to {@link PluginTemplate}
     * 
     * @since 1.0.0
     * @version 1.0.0
     */
    public void cleanup() {
        HandlerList.unregisterAll(this.plugin);
    }
}
