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
package com.rogue.plugintemplate.listener;

import com.rogue.plugintemplate.PluginTemplate;
import com.rogue.plugintemplate.listener.listeners.*;
import java.util.HashMap;
import java.util.Map;
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
}
