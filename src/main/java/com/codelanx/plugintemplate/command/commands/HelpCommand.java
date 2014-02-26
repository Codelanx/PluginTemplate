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

import com.codelanx.plugintemplate.PluginTemplate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import org.bukkit.command.CommandSender;

/**
 * Displays help information
 *
 * @since 1.0.0
 * @author 1Rogue
 * @version 1.0.0
 */
public class HelpCommand implements SubCommand {
    
    private final PluginTemplate plugin;
    private final String usagePrefix = "";
    private final String infoPrefix = "";
    
    public HelpCommand(PluginTemplate plugin) {
        this.plugin = plugin;
    }

    public boolean execute(CommandSender sender, String[] args) {
        
        int factor = 5;
        int page = 1;
        
        if (args.length > 0) {
            try {
                page = Integer.parseInt(args[1]);
            } catch (NumberFormatException ex) {
                return false;
            }
        }
        
        Collection<SubCommand> subs = this.plugin.getCommandHandler().getCommands();
        SubCommand[] cmds = subs.toArray(new SubCommand[subs.size()]);
        Arrays.sort(cmds, new Comparator<SubCommand>() {

            public int compare(SubCommand o1, SubCommand o2) {
                return o1.getName().compareTo(o2.getName());
            }
            
        });
        
        if (page * factor > cmds.length) {
            
        }
        
        StringBuilder sb = new StringBuilder();
        for (SubCommand cmd : this.plugin.getCommandHandler().getCommands()) {
            sb.append(cmd.helpInfo()[0]).append(" - ").append(cmd.helpInfo()[1]).append('\n');
        }
        
        return true;
    }

    public String getName() {
        return "help";
    }

    public String[] helpInfo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
