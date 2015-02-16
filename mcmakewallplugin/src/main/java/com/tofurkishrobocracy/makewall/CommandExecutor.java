/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tofurkishrobocracy.makewall;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Cody
 */
public class CommandExecutor implements org.bukkit.command.CommandExecutor {

    private final MakeWallPlugin plugin;

    public CommandExecutor(MakeWallPlugin aThis) {
        plugin = aThis;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("makewall")) {
            if (args.length > 0) {
                String arg = args[0].toLowerCase().trim();
                if (arg.equals("help")) {
                    sender.sendMessage("do '/makewall start' to get started. If you want to cancel, do '/makewall cancel'");
                    return true;
                } else if (arg.equals("start")) {
                    if ((sender instanceof Player) && !sender.hasPermission("wall.make")) {
                        sender.sendMessage("You can't do that!");
                        return true;
                    } else {
                        Player player = plugin.getServer().getPlayer(sender.getName());
                        plugin.startMakeWall(player);
                        sender.sendMessage("Place a block to select the first corner, or '/makewall cancel' to cancel");
                        return true;
                    }
                } else if (arg.equals("height")) {
                    if ((sender instanceof Player) && !sender.hasPermission("hole.dig")) {
                        sender.sendMessage("You can't do that!");
                        return true;
                    } else {
                        if (args.length > 1) {
                            int depth;
                            try {
                                depth = Integer.parseInt(args[1].trim());
                                Player player = plugin.getServer().getPlayer(sender.getName());
                                plugin.makeWall(player, depth);
                                sender.sendMessage("Wallin");
                                return true;
                            } catch (NumberFormatException e) {
                                sender.sendMessage("Do '/makewall height 10' to go 10 high");
                                return true;
                            }
                        } else {
                            sender.sendMessage("Do '/makewall height 10' to go 10 high");
                            return true;
                        }
                    }
                } else if (arg.equals("cancel")) {
                    plugin.cancelMakeWall(plugin.getServer().getPlayer(sender.getName()));
                    sender.sendMessage("Cancelled");
                    return true;
                }
            }
        }
        return false;
    }
}
