package com.jagsnet.minecraft.plugins.quests.commands;

import com.jagsnet.minecraft.plugins.quests.otherStuff.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class Completer implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission("quests.admin")) {
                Utils.sendMessage(player, "You are missing quests admin permissions. Speak to Melon for more info");
                return true;
            }
        }

        if (args.length < 1) {
            if (sender instanceof Player) {
                Utils.sendMessage((Player) sender, "/cquest txt <playerName> <matchString1> <matchString2>");
                Utils.sendMessage((Player) sender, "/cquest num <playerName> <matchString1> <matchString2> <num>");
                Utils.sendMessage((Player) sender, "/cquest unlock <playerName> <matchString1> <matchString2>");
            } else {
                Utils.log("Invalid quests command");
            }
            return true;
        }

        Player player = null;
        if (args[1].equalsIgnoreCase("@p")) {

            BlockCommandSender bcs = (BlockCommandSender) sender;
            Location l = bcs.getBlock().getLocation();
            ArrayList<Entity> entities = (ArrayList<Entity>) l.getWorld().getNearbyEntities(l, 10, 10, 10);

            double lowestDistanceSoFar = Double.MAX_VALUE;
            Entity closestEntity = null;

            for (Entity entity : entities) { // This loops through all the entities, setting the variable "entity" to each element.
                double distance = entity.getLocation().distance(l);
                if (distance < lowestDistanceSoFar) {
                    lowestDistanceSoFar = distance;
                    if (entity instanceof Player) {
                        closestEntity = entity;
                    }
                }
            }

            if (closestEntity != null) {
                player = (Player) closestEntity;
            }

        } else {
            player = Bukkit.getPlayer(args[1]);
            if (!player.isOnline()) {
                if (sender instanceof Player) {
                    Utils.sendMessage((Player) sender, "Please specify a player that is currently online");
                } else {
                    Utils.log("Please specify a player that is currently online");
                }
                return true;
            }
        }

        // ------------------------------------------------------
        // --------------- Complete a txt based line ------------
        // ------------------------------------------------------
        if (args[0].equalsIgnoreCase("txt")) {
            if (args.length < 4) {
                if (sender instanceof Player) {
                    Utils.sendMessage((Player) sender, "Not enough arguments specified.");
                } else {
                    Utils.log("Not enough arguments specified.");
                }
                return true;
            }

            if (Utils.isScrollOff(player)) {
                ItemMeta offHandItem = player.getInventory().getItemInOffHand().getItemMeta();
                String entity = args[2].toLowerCase();
                String entity2 = args[3].toLowerCase();
                int loreLength = offHandItem.getLore().size();
                ArrayList<String> lore = (ArrayList<String>) player.getInventory().getItemInOffHand().getItemMeta().getLore();

                for (int i = 0; i < loreLength; i++) {
                    if (offHandItem.getLore().get(i).toLowerCase().contains(entity) &&
                            offHandItem.getLore().get(i).toLowerCase().contains(entity2) &&
                            !offHandItem.getLore().get(i).contains("Complete")) {
                        if (Utils.updateTxtLine(lore, player, i, true)) {
                            return true;
                        }
                    }
                }
            }
            return true;
        }

        // ------------------------------------------------------
        // --------------- Complete a num based line ------------
        // ------------------------------------------------------
        if (args[0].equalsIgnoreCase("num")) {
            if (args.length < 4) {
                if (sender instanceof Player) {
                    Utils.sendMessage((Player) sender, "Not enough arguments specified.");
                } else {
                    Utils.log("Not enough arguments specified.");
                }
                return true;
            }

            int amount = 9999999;
            if (args.length > 4) {
                try {
                    amount =  parseInt(args[4]);
                } catch (Exception e) {}
            }

            if (Utils.isScrollOff(player)) {
                ItemMeta offHandItem = player.getInventory().getItemInOffHand().getItemMeta();
                String entity = args[2].toLowerCase();
                String entity2 = args[3].toLowerCase();
                int loreLength = offHandItem.getLore().size();
                ArrayList<String> lore = (ArrayList<String>) player.getInventory().getItemInOffHand().getItemMeta().getLore();

                for (int i = 0; i < loreLength; i++) {
                    if (offHandItem.getLore().get(i).toLowerCase().contains(entity) &&
                            offHandItem.getLore().get(i).toLowerCase().contains(entity2) &&
                            !offHandItem.getLore().get(i).contains("Complete")) {
                        if (Utils.updateNumLine(lore, player, amount, i)) {
                            return true;
                        }
                    }
                }
            }
            return true;
        }

        // ------------------------------------------------------
        // --------------- Unlock a locked line -----------------
        // ------------------------------------------------------
        if (args[0].equalsIgnoreCase("unlock")) {
            if (args.length < 4) {
                if (sender instanceof Player) {
                    Utils.sendMessage((Player) sender, "Not enough arguments specified.");
                } else {
                    Utils.log("Not enough arguments specified.");
                }
                return true;
            }

            if (Utils.isScrollOff(player)) {
                ItemMeta offHandItem = player.getInventory().getItemInOffHand().getItemMeta();
                String entity = args[2].replace("_", " ").toLowerCase();
                String entity2 = args[3].replace("_", " ").toLowerCase();
                int loreLength = offHandItem.getLore().size();
                ArrayList<String> lore = (ArrayList<String>) player.getInventory().getItemInOffHand().getItemMeta().getLore();

                for (int i = 0; i < loreLength; i++) {
                    if (offHandItem.getLore().get(i).toLowerCase().contains(entity) &&
                            offHandItem.getLore().get(i).toLowerCase().contains(entity2) &&
                            !offHandItem.getLore().get(i).contains("Complete")) {
                        String strike = ChatColor.STRIKETHROUGH + "";
                        lore.set(i, lore.get(i).replace(strike, ""));
                        ItemStack itemStack = player.getInventory().getItemInOffHand();
                        ItemMeta meta = itemStack.getItemMeta();
                        meta.setLore(lore);
                        itemStack.setItemMeta(meta);
                    }
                }
            }
            return true;
        }

        // ------------------------------------------------------
        // ------------------------ Trade -----------------------
        // ------------------------------------------------------
        if (args[0].equalsIgnoreCase("trade")) {
            if (args.length < 4) {
                if (sender instanceof Player) {
                    Utils.sendMessage((Player) sender, "Not enough arguments specified.");
                } else {
                    Utils.log("Not enough arguments specified.");
                }
                return true;
            }

            if (Utils.isScrollOff(player)) {
                ItemMeta offHandItem = player.getInventory().getItemInOffHand().getItemMeta();
                String entity = args[2].replace("_", " ").toLowerCase();
                String entity2 = args[3].replace("_", " ").toLowerCase();
                int loreLength = offHandItem.getLore().size();
                ArrayList<String> lore = (ArrayList<String>) player.getInventory().getItemInOffHand().getItemMeta().getLore();

                for (int i = 0; i < loreLength; i++) {
                    if (offHandItem.getLore().get(i).toLowerCase().contains(entity) &&
                            offHandItem.getLore().get(i).toLowerCase().contains(entity2) &&
                            !offHandItem.getLore().get(i).contains("Complete")) {
                        String strike = ChatColor.STRIKETHROUGH + "";
                        lore.set(i, lore.get(i).replace(strike, ""));
                        ItemStack itemStack = player.getInventory().getItemInOffHand();
                        ItemMeta meta = itemStack.getItemMeta();
                        meta.setLore(lore);
                        itemStack.setItemMeta(meta);
                    }
                }
            }
            return true;
        }

        if (sender instanceof Player) {
            Utils.sendMessage((Player) sender, "Please use a valid quests command");
        }
        return true;
    }
}
