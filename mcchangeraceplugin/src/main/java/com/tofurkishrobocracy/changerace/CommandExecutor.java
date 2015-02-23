/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tofurkishrobocracy.changerace;

import com.tofurkishrobocracy.changerace.asynch.StopWarTime;
import com.tofurkishrobocracy.changerace.races.Race;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Cody
 */
public class CommandExecutor implements org.bukkit.command.CommandExecutor {

    private final ChangeRacePlugin plugin;
    private String RACES_CSV;
    private static final long FIVE_MINUTES = 1000 * 60 * 5;

    public CommandExecutor(ChangeRacePlugin aThis) {
        plugin = aThis;
        Set<String> races = ChangeRacePlugin.stringToRaceMap.keySet();
        RACES_CSV = "";
        for (String race : races) {
            if (!race.equalsIgnoreCase("none") && !race.equalsIgnoreCase("ubermench")) {
                RACES_CSV += race + ", ";
            }
        }
        RACES_CSV = RACES_CSV.substring(0, RACES_CSV.length() - 2);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("race")) {
            if (args.length > 0) {
                String arg = args[0].toLowerCase().trim();
                //sender.sendMessage("arg[0]=" + arg);
                if (arg.equals("list")) {
                    Player p = (Player) sender;
                    Race r = plugin.getRace(p);
                    if (r != null && !r.getName().equalsIgnoreCase(ChangeRacePlugin.RACE_NONE.getName())) {
                        sender.sendMessage("You are a " + r.getName());
                    } else {
                        sender.sendMessage("You have not selected a race");
                    }
                    String s = RACES_CSV;
                    if (sender.isOp()) {
                        s += ", ubermench";
                    }
                    sender.sendMessage("All Races: " + RACES_CSV);
                    return true;
                } else if (arg.equals("set")) {
                    if (args.length > 1) {
                        String desiredRace = args[1].toLowerCase().trim();
                        //sender.sendMessage("arg[1]=" + desiredRace);
                        if (ChangeRacePlugin.stringToRaceMap.containsKey(desiredRace)) {
                            Player targetPlayer;
                            if (args.length > 2) {
                                String targetPlayerName = args[2].trim();
                                //sender.sendMessage("arg[2]=" + desiredRace);
                                if ((sender instanceof Player) && !sender.hasPermission("race.setOther")) {
                                    sender.sendMessage("You can't do that!");
                                    return false;
                                } else {
                                    targetPlayer = plugin.getServer().getPlayer(targetPlayerName);
                                    if (targetPlayer == null) {
                                        sender.sendMessage("Player " + targetPlayerName + " is not online");
                                        return true;
                                    }
                                }
                            } else {
                                if (!sender.hasPermission("race.setSelf")) {
                                    sender.sendMessage("You can't do that!");
                                    return false;
                                } else {
                                    targetPlayer = (Player) sender;
                                }
                            }
                            Race r = ChangeRacePlugin.stringToRaceMap.get(desiredRace);
                            if (r.requiresOp() && !sender.isOp()) {
                                sender.sendMessage("You can't do that!");
                                return true;
                            }
                            plugin.setRace(targetPlayer, r);
                            targetPlayer.sendMessage("Your race has been changed to " + r.getName());
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                } else if (arg.equals("unset")) {
                    Player targetPlayer;
                    if (args.length > 1) {
                        String targetPlayerName = args[1].trim();
                        //sender.sendMessage("arg[1]=" + targetPlayerName);
                        if ((sender instanceof Player) && !sender.hasPermission("race.unsetOther")) {
                            sender.sendMessage("You can't do that!");
                            return false;
                        } else {
                            targetPlayer = plugin.getServer().getPlayer(targetPlayerName);
                        }
                    } else {
                        if (!sender.hasPermission("race.unsetSelf")) {
                            sender.sendMessage("You can't do that!");
                            return false;
                        } else {
                            targetPlayer = (Player) sender;
                        }
                    }
                    plugin.unsetRace(targetPlayer);
                    targetPlayer.sendMessage("Your race has been unset");
                    return true;
                } else if (arg.equals("showdb")) {
                    if (sender instanceof Player) {
                        sender.sendMessage("You can't do that! Console only!");
                        return true;
                    }
                    plugin.getLogger().log(Level.INFO, "DB:");
                    List<String> strs = plugin.persistance.showAllPlayersAndRaces();
                    for (String s : strs) {
                        plugin.getLogger().log(Level.INFO, " - {0}", s);
                    }
                    return true;
                } else if (arg.equals("clanwar")) {
                    plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "Clan war is on!");
                    ChangeRacePlugin.isWarTime = true;
                    ChangeRacePlugin.instance.getServer().getScheduler().runTaskLaterAsynchronously(
                            plugin, new StopWarTime(), FIVE_MINUTES
                    );
                    return true;
                } else if (arg.equals("debug")) {
                    plugin.getLogger().log(Level.INFO, "In debug");
                    /*Player p = (Player) sender;
                     sender.sendMessage("Food=" + p.getFoodLevel());
                     p.setSaturation(0);
                     plugin.getLogger().log(Level.INFO, "Getting race");
                     Race r = plugin.getRace(p);
                     plugin.getLogger().log(Level.INFO, "Setting race to {0}", r.getName());
                     plugin.setRace(p, r);*/
                    plugin.persistance.deletePlayer((Player) sender);
                    return true;
                }
            }
        }
        return false;
    }
}
