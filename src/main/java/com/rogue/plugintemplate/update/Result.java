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

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Result return type for the update checker
 *
 * @since 1.0.0
 * @author Rogue
 * @version 1.0.0
 */
public enum Result {
    
    /** Successfully updated the jar file */
    UPDATED {
        @Override public void handleUpdate(Logger log) {
            log.log(Level.INFO, "Plugin updated for next restart!");
        }
    },
    /** Update available, but not downloaded */
    UPDATE_AVAILABLE {
        @Override public void handleUpdate(Logger log) {
            log.log(Level.INFO, "An update is available!");
        }
    },
    /** No update is available */
    NO_UPDATE {
        @Override public void handleUpdate(Logger log) {
            log.log(Level.INFO, "Plugin is up to date!");
        }
    },
    /** Error in updater: bad plugin id provided */
    ERROR_BADID {
        @Override public void handleUpdate(Logger log) {
            log.log(Level.SEVERE, "Invalid plugin ID provided!");
        }
    },
    /** No file found when attempting to download a new version */
    ERROR_FILENOTFOUND {
        @Override public void handleUpdate(Logger log) {
            log.log(Level.SEVERE, "Could not locate latest version!");
        }
    },
    /** Error in updater: bad plugin api key */
    ERROR_APIKEY {
        @Override public void handleUpdate(Logger log) {
            log.log(Level.SEVERE, "Bad plugin API key provided!");
        }
    },
    /** Updating disabled (shouldn't occur) */
    DISABLED {
        @Override public void handleUpdate(Logger log) {}
    };
    
    public abstract void handleUpdate(Logger log);
    
}