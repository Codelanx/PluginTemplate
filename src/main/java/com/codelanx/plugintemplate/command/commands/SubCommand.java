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
package com.codelanx.plugintemplate.command.commands;

import org.bukkit.command.CommandSender;

/**
 *
 * @since 1.3.0
 * @author 1Rogue
 * @version 1.3.0
 */
public interface SubCommand {

    /**
     * Executes a relevant command grabbed from the CommandHandler.
     * 
     * @since 1.3.0
     * @version 1.3.0
     * 
     * @param sender The command executor
     * @param args The command arguments, starting after the command name
     * 
     * @return true on success, false if failed
     */
    public abstract boolean execute(CommandSender sender, String[] args);
    
    /**
     * Returns the name of the command, used for storing a hashmap of the
     * commands
     * 
     * @since 1.3.0
     * @version 1.3.0
     * 
     * @return The command's name
     */
    public abstract String getName();
    
    /**
     * Represents data put out by the help menu, or incorrect usage
     * 
     * @since 1.0.0
     * @version 1.0.0
     * 
     * @return 
     */
    public abstract String[] helpInfo();
    
}
