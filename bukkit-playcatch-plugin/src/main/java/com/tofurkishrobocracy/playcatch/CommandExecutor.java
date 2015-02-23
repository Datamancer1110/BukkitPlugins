/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tofurkishrobocracy.playcatch;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Cody
 */
public class CommandExecutor implements org.bukkit.command.CommandExecutor {

    private final PlayCatchPlugin plugin;

    public CommandExecutor(PlayCatchPlugin aThis) {
        plugin = aThis;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.isOp()) {
            if (command.getName().equalsIgnoreCase("playcatch")) {
                if (args.length > 0) {
                    String arg = args[0].toLowerCase().trim();
                    if (arg.equals("help")) {
                        sender.sendMessage("do '/playcatch enable' to enable the plugin. \n"
                                + "do '/playcatch disable' to disable the plugin.");
                        return true;
                    } else if (arg.equals("enable")) {
                        plugin.enabled = true;
                        return true;
                    } else if (arg.equals("disable")) {
                        plugin.enabled = false;
                        return true;
                    }
                }
            }
            return false;
        } else {
            sender.sendMessage("You aren't allowed to do that!");
            return true;
        }
    }
}
