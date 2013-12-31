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
package com.rogue.plugintemplate.command;

import static com.rogue.plugintemplate.PluginTemplate.__;
import com.rogue.plugintemplate.command.commands.SubCommand;
import com.rogue.plugintemplate.command.commands.*;
import com.rogue.plugintemplate.PluginTemplate;
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
