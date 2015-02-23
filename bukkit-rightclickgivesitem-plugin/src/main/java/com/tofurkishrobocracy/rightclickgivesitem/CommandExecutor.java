/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tofurkishrobocracy.rightclickgivesitem;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Cody
 */
public class CommandExecutor implements org.bukkit.command.CommandExecutor {

    private final RightClickGivesItemPlugin plugin;

    public CommandExecutor(RightClickGivesItemPlugin aThis) {
        plugin = aThis;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.isOp()) {
            if (command.getName().equalsIgnoreCase("rightclickgivesitem")) {
                if (args.length > 0) {
                    String arg = args[0].toLowerCase().trim();
                    if (arg.equals("help")) {
                        sender.sendMessage("do '/rightclickgivesitem setdrop <ITEMNAME>' to change the item dropped. \n"
                                + "do '/rightclickgivesitem setblock <BLOCKNAME>' to set the clickable block. \n"
                                + "do '/rightclickgivesitem destroy [true/false]' to set block destruction on or off. \n"
                                + "do '/rightclickgivesitem cancel [true/false]' to set right-click normal event cancellation on or off. \n"
                                + "do '/rightclickgivesitem delay <time in seconds>' to set default delay (in seconds) between right-clicks giving items. \n"
                                + "do '/rightclickgivesitem delay <playername> <time in seconds>' to set delay for a specific player. \n"
                                + "do '/rightclickgivesitem cleardelay <playername>' to remove custom delay for specific player. ");
                        return true;
                    } else if (arg.equals("setdrop")) {
                        if (args.length == 1) {
                            return false;
                        } else {
                            String arg2 = args[1].trim();
                            Material drop = Material.getMaterial(arg2);
                            if (drop != null) {
                                plugin.drop = drop;
                                plugin.getConfig().set("item_to_give", arg2);
                                plugin.saveConfig();
                                return true;
                            } else {
                                sender.sendMessage("Item '" + arg2 + "' is invalid.");
                                return true;
                            }
                        }
                    } else if (arg.equals("setblock")) {
                        if (args.length == 1) {
                            return false;
                        } else {
                            String arg2 = args[1].trim();
                            Material block = Material.getMaterial(arg2);
                            if (block != null) {
                                plugin.block = block;
                                plugin.getConfig().set("block_type_that_gives_items", arg2);
                                plugin.saveConfig();
                                return true;
                            } else {
                                sender.sendMessage("Item '" + arg2 + "' is invalid.");
                                return true;
                            }
                        }
                    } else if (arg.equals("destroy")) {
                        if (args.length == 1) {
                            return false;
                        } else {
                            String arg2 = args[1].trim().toLowerCase();
                            if (arg2.equals("true") || arg2.equals("false")) {
                                plugin.destroy = Boolean.parseBoolean(arg2);
                                plugin.getConfig().set("destroy_block_on_right_click", arg2);
                                plugin.saveConfig();
                            } else {
                                sender.sendMessage("'" + arg2 + "' must be 'true' or 'false'");
                            }
                            return true;
                        }
                    } else if (arg.equals("cancel")) {
                        if (args.length == 1) {
                            return false;
                        } else {
                            String arg2 = args[1].trim().toLowerCase();
                            if (arg2.equals("true") || arg2.equals("false")) {
                                plugin.cancel = Boolean.parseBoolean(arg2);
                                plugin.getConfig().set("cancel_normal_right_click_event", arg2);
                                plugin.saveConfig();
                            } else {
                                sender.sendMessage("'" + arg2 + "' must be 'true' or 'false'");
                            }
                            return true;
                        }
                    } else if (arg.equals("delay")) {
                        if (args.length == 1) {
                            return false;
                        } else if (args.length == 2) {
                            String arg2 = args[1].trim().toLowerCase();
                            try {
                                int delay = Math.max(Integer.parseInt(arg2), 0);
                                plugin.delayInSeconds = delay;
                                plugin.getConfig().set("delay_between_clicks_in_seconds", arg2);
                                plugin.saveConfig();
                            } catch (NumberFormatException ex) {
                                sender.sendMessage("'" + arg2 + "' must be a number");
                            }
                            return true;
                        } else if (args.length == 3) {
                            String arg2 = args[1].trim();
                            String arg3 = args[2].trim();
                            try {
                                Player targetPlayer = plugin.getServer().getPlayer(arg2);
                                int delay = Math.max(Integer.parseInt(arg3), 0);
                                plugin.persistence.savePlayer(targetPlayer, delay);
                            } catch (NumberFormatException ex) {
                                sender.sendMessage("'" + arg3 + "' must be a number");
                            }
                            return true;
                        }
                    } else if (arg.equals("cleardelay")) {
                        if (args.length == 1) {
                            return false;
                        } else if (args.length == 2) {
                            String arg2 = args[1].trim();
                            try {
                                Player targetPlayer = plugin.getServer().getPlayer(arg2);
                                if (!plugin.persistence.deletePlayer(targetPlayer)) {
                                    sender.sendMessage("No such player '" + arg2 + "' in database");
                                }
                            } catch (NumberFormatException ex) {
                                sender.sendMessage("'" + arg2 + "' must be a number");
                            }
                            return true;
                        }
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
