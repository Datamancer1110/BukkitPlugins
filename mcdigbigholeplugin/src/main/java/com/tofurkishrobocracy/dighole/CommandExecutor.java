/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tofurkishrobocracy.dighole;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Cody
 */
public class CommandExecutor implements org.bukkit.command.CommandExecutor {

    private final DigHolePlugin plugin;

    public CommandExecutor(DigHolePlugin aThis) {
        plugin = aThis;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("dighole")) {
            if (args.length > 0) {
                String arg = args[0].toLowerCase().trim();
                if (arg.equals("help")) {
                    sender.sendMessage("do '/dighole start' to get started. If you want to cancel, do '/dighole cancel'");
                    return true;
                } else if (arg.equals("start")) {
                    if ((sender instanceof Player) && !sender.hasPermission("hole.dig")) {
                        sender.sendMessage("You can't do that!");
                        return true;
                    } else {
                        Player player = plugin.getServer().getPlayer(sender.getName());
                        plugin.startDigHole(player);
                        sender.sendMessage("Break a block to select the first corner, or '/dighole cancel' to cancel");
                        return true;
                    }
                } else if (arg.equals("depth")) {
                    if ((sender instanceof Player) && !sender.hasPermission("hole.dig")) {
                        sender.sendMessage("You can't do that!");
                        return true;
                    } else {
                        if (args.length > 1) {
                            int depth;
                            try {
                                depth = Integer.parseInt(args[1].trim());
                                Player player = plugin.getServer().getPlayer(sender.getName());
                                plugin.digHole(player, depth);
                                sender.sendMessage("Diggin");
                                return true;
                            } catch (NumberFormatException e) {
                                sender.sendMessage("Do '/dighole depth 10' to go 10 deep");
                                return true;
                            }
                        } else {
                            sender.sendMessage("Do '/dighole depth 10' to go 10 deep");
                            return true;
                        }
                    }
                } else if (arg.equals("cancel")) {
                    plugin.cancelDigHole(plugin.getServer().getPlayer(sender.getName()));
                    sender.sendMessage("Cancelled");
                    return true;
                }
            }
        }
        return false;
    }
}
