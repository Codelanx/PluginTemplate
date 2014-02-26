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
package com.codelanx.plugintemplate.update;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Actions to take if an update is available
 *
 * @since 1.4.1
 * @author 1Rogue
 * @version 1.4.1
 */
class UpdateListener implements Listener {

    private final String message;

    public UpdateListener(String message) {
        this.message = message;
    }

    /**
     * Sends a notification to ops/players with all of the plugin's permissions
     *
     * @since 1.2.0
     * @version 1.4.1
     *
     * @param e The join event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (e.getPlayer().hasPermission("playtime.updatenotice")) {
            e.getPlayer().sendMessage(this.message);
        }
    }

}
