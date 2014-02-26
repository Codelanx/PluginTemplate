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
package com.codelanx.plugintemplate.command;

import static com.codelanx.plugintemplate.PluginTemplate.__;
import com.codelanx.plugintemplate.command.commands.*;
import com.codelanx.plugintemplate.command.commands.SubCommand;
import com.codelanx.plugintemplate.PluginTemplate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * Manages commands abstractly for the plugin
 *
 * @since 1.3.0
 * @author 1Rogue
 * @version 1.4.2
 */
public class CommandHandler implements CommandExecutor {

    /** Private {$link PluginTemplate} instance */
    private final PluginTemplate plugin;
    /** Private {@link HashMap} of subcommands */
    private final Map<String, SubCommand> commands = new HashMap<String, SubCommand>();

    /**
     * {@link CommandHandler} constructor
     * 
     * @since 1.0.0
     * @version 1.0.0
     * 
     * @param plugin The main {@link PluginTemplate} instance
     */
    public CommandHandler(PluginTemplate plugin) {
        this.plugin = plugin;
        
        SubCommand[] cmds = new SubCommand[] {
            new HelpCommand(this.plugin)
        };
        
        final CommandHandler chand = this;
        for (SubCommand cmd : cmds) {
            this.commands.put(cmd.getName(), cmd);
            this.plugin.getCommand(cmd.getName()).setExecutor(chand);
        }
    }

    /**
     * Executes the proper {@link SubCommand}
     *
     * @since 1.3.0
     * @version 1.4.2
     *
     * @param sender The command executor
     * @param cmd The command instance
     * @param commandLabel The command name
     * @param args The command arguments
     *
     * @return Success of command, false if no command is found
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        SubCommand command = this.getCommand(cmd.getName());
        if (command != null) {
            String[] newArgs = new String[args.length - 1];
            for (int i = 0; i < newArgs.length; i++) {
                newArgs[i] = args[i + 1];
            }
            if (command.execute(sender, newArgs)) {
                return true;
            } else {
                sender.sendMessage(__("Usage: " + command.helpInfo()[0]));
                sender.sendMessage(__(command.helpInfo()[1]));
            }
        }
        return false;
    }
    
    /**
     * Returns a subcommand, or <code>null</code> if none exists.
     * 
     * @since 1.0.0
     * @version 1.0.0
     * 
     * @param name The name of the subcommand
     * @return A relevant {@link Succommand}, or null if it does not exist
     */
    public SubCommand getCommand(String name) {
        return this.commands.get(name);
    }
    
    /**
     * Returns all subcommands as a {@link Collection}.
     * 
     * @since 1.0.0
     * @version 1.0.0
     * 
     * @return A {@link Collection} of all registered {@link SubCommand}
     */
    public Collection<SubCommand> getCommands() {
        return this.commands.values();
    }
    
}
