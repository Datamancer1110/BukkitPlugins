/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tofurkishrobocracy.rightclickgivesitem;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

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
                        sender.sendMessage("do '/rightclickgivesitem setdrop <ITEMNAME>' to change the item dropped. "
                                + "do '/rightclickgivesitem setblock <BLOCKNAME>' to set the clickable block. "
                                + "do '/rightclickgivesitem destroy [true/false]' to set block destruction on or off. "
                                + "do '/rightclickgivesitem cancel [true/false]' to set right-click normal event cancellation on or off. ");
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
