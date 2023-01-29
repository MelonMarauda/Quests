package com.jagsnet.minecraft.plugins.quests.commands;

import com.jagsnet.minecraft.plugins.quests.Quests;
import com.jagsnet.minecraft.plugins.quests.otherStuff.Utils;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

public class Admin implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {

            Player player = (Player) sender;
            if (!player.hasPermission("quests.admin")) {
                Utils.sendMessage(player, "You are missing quests admin permissions. Speak to Melon for more info");
                return true;
            }
            if (args.length < 1) {
                Utils.sendMessage(player, "/aquest rewardList");
                Utils.sendMessage(player, "/aquest expiry <expiryInMillis>");
                Utils.sendMessage(player, "/aquest claim");
                Utils.sendMessage(player, "/aquest who");
                Utils.sendMessage(player, "/aquest setWho <UUID>");
                Utils.sendMessage(player, "/aquest reloadList");
                Utils.sendMessage(player, "/aquest version");
                Utils.sendMessage(player, "/aquest auto");
                Utils.sendMessage(player, "/aquest auto [true|false]");
                Utils.sendMessage(player, "/aquest list");
                Utils.sendMessage(player, "/aquest list <fileName>");
                return true;
            }
            // ------------------------------------------------------
            // ------------------ List Rewards ----------------------
            // ------------------------------------------------------
            if (args[0].equalsIgnoreCase("rewardList")) {
                if (Utils.isScrollMain(player) || Utils.isScrollOff(player)) {
                    ItemMeta mainHandItem = null;
                    if (Utils.isScrollMain(player)) {
                        mainHandItem = player.getInventory().getItemInMainHand().getItemMeta();
                    }
                    if (Utils.isScrollOff(player)) {
                        mainHandItem = player.getInventory().getItemInOffHand().getItemMeta();
                    }
                    NamespacedKey key = new NamespacedKey(Quests.getInstance(), "rewardLocation");
                    if (mainHandItem.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
                        String location = mainHandItem.getPersistentDataContainer().get(key, PersistentDataType.STRING);
                        Utils.load(location.split("_")[0]);
                        List rewards = Utils.get().getStringList( location.split("_")[1] + ".rewards");
                        int rewardCount = rewards.size();
                        Utils.sendMessage(player, rewardCount + " Rewards ------------------------");
                        for (int i = 0; i < rewardCount; i++) {
                            Utils.sendMessage(player, "/" + rewards.get(i).toString());
                        }
                    } else {
                        Utils.sendMessage(player, "No rewards set for this item");
                    }
                } else {
                    Utils.sendMessage(player, "Please hold a valid quest scroll");
                }
                return true;
            }
            // ------------------------------------------------------
            // ------------------ Set expiry ------------------------
            // ------------------------------------------------------
            if (args[0].equalsIgnoreCase("expiry")) {
                if (args.length < 2) {
                    Utils.sendMessage(player, "Insufficient arguments. Please enter the command like /aQuest expiry <timeInMilliseconds>");
                    return true;
                }
                long expiry;
                try {
                    expiry = parseLong(args[1]);
                } catch (Exception e) {
                    Utils.sendMessage(player, "Please enter a valid time in milliseconds");
                    return true;
                }
                expiry = expiry + System.currentTimeMillis();
                if (Utils.isScrollMain(player)) {
                    ItemMeta mainHandItem = player.getInventory().getItemInMainHand().getItemMeta();
                    NamespacedKey key = new NamespacedKey(Quests.getInstance(), "expiry");
                    mainHandItem.getPersistentDataContainer().set(key, PersistentDataType.LONG, expiry);
                    Utils.sendMessage(player, "Expiry set for " + expiry + " milliseconds from now");
                    ItemStack itemStack = player.getInventory().getItemInMainHand();
                    itemStack.setItemMeta(mainHandItem);
                }
                return true;
            }
            // ------------------------------------------------------
            // --------- Set config for scroll generation -----------
            // ------------------------------------------------------
            if (args[0].equalsIgnoreCase("monthly")) {
                if (args.length < 4) {
                    Utils.sendMessage(player, "Insufficient arguments.");
                    return true;
                }
                String out = "";
                for (int i = 4; i <= args.length; i++) {
                    if (args.length == i) {
                        out = out + args[i - 1];
                    } else {
                        out = out + args[i - 1] + " ";
                    }
                }
                Utils.get().set(args[1] + "." + args[2], out);
                Utils.save();
                return true;
            }
            // ------------------------------------------------------
            // ----- Claim a quest scroll without completing it -----
            // ------------------------------------------------------
            if (args[0].equalsIgnoreCase("claim")) {
                if (args.length > 1) {
                    if (args[1].equalsIgnoreCase("force")) {
                        if (Utils.isScrollMain(player) || Utils.isScrollOff(player)) {
                            return Utils.claim(player, false, ".rewards", true);
                        }
                    }
                }
                if (Utils.isScrollMain(player) || Utils.isScrollOff(player)) {
                    return Utils.claim(player, false, ".rewards", false);
                }
                Utils.sendMessage(player, "Please hold a valid quest scroll.");
                return true;
            }
            // ------------------------------------------------------
            // --------------------- Who ----------------------------
            // ------------------------------------------------------
            if (args[0].equalsIgnoreCase("who")) {
                if (Utils.isScrollMain(player)) {
                    ItemMeta mainHandItem = player.getInventory().getItemInMainHand().getItemMeta();
                    NamespacedKey key = new NamespacedKey(Quests.getInstance(), "UUID");
                    if (mainHandItem.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
                        Utils.sendMessage(player, Bukkit.getPlayer(UUID.fromString(mainHandItem.getPersistentDataContainer().get(key, PersistentDataType.STRING))).getDisplayName());
                    } else {
                        Utils.sendMessage(player, "Anyone can use this scroll");
                    }
                }
                return true;
            }
            // ------------------------------------------------------
            // --------------------- Set Who ------------------------
            // ------------------------------------------------------
            if (args[0].equalsIgnoreCase("setwho")) {
                if (Utils.isScrollMain(player)) {
                    if (args.length < 2) {
                        Utils.sendMessage(player, "Please specify a UUID like /aquest setwho 1234-5678-etc");
                        return true;
                    }
                    ItemMeta mainHandItem = player.getInventory().getItemInMainHand().getItemMeta();
                    NamespacedKey key = new NamespacedKey(Quests.getInstance(), "UUID");
                    mainHandItem.getPersistentDataContainer().set(key, PersistentDataType.STRING, args[1]);
                    ItemStack itemStack = player.getInventory().getItemInMainHand();
                    itemStack.setItemMeta(mainHandItem);
                    if (mainHandItem.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
                        Utils.sendMessage(player, Bukkit.getOfflinePlayer(UUID.fromString(mainHandItem.getPersistentDataContainer().get(key, PersistentDataType.STRING))).getName());
                    } else {
                        Utils.sendMessage(player, "Anyone can use this scroll");
                    }
                }
                return true;
            }
            // ------------------------------------------------------
            // --------------------- Reload Lists -------------------
            // ------------------------------------------------------
            if (args[0].equalsIgnoreCase("reloadlist")) {
                Utils.loadLists();
                Utils.sendMessage(player, "List reload complete.");
                return true;
            }
            // ------------------------------------------------------
            // ----------------- Quests Version Info ----------------
            // ------------------------------------------------------
            if (args[0].equalsIgnoreCase("version") || args[0].equalsIgnoreCase("help")) {
                String bold = ChatColor.BOLD + "";
                String gold = ChatColor.GOLD + "";
                String white = ChatColor.WHITE + "";
                player.sendMessage(gold + bold + "QUESTS" + white + bold + " VERSION 1.7.2");
                player.sendMessage(white + bold + "-------------------------------------------");
                player.sendMessage(gold + bold + "Quest Documentation:" + white + " https://docs.google.com/document/d/1Zof3tSpYgvVUhJiKXcB6uv8NPTHfI35DyLOospBB7Lk/edit?pli=1#");
                return true;
            }
            // ------------------------------------------------------
            // ------------------ Set Autocomplete ------------------
            // ------------------------------------------------------
            if (args[0].equalsIgnoreCase("auto")) {
                if (args.length < 2) {
                    Utils.sendMessage(player, "Autocomplete status: " + Quests.auto);
                    return true;
                }
                Utils.getGlobal().set("auto", Boolean.valueOf(args[1]));
                Utils.saveGlobal();
                Quests.setAuto();
                Utils.sendMessage(player, "Autocomplete status: " + Quests.auto);
                return true;
            }

            // ------------------------------------------------------
            // ------------------ Check scroll/list -----------------
            // ------------------------------------------------------
            if (args[0].equalsIgnoreCase("check")) {
                if (args.length < 2) {
                    Utils.sendMessage(player, "Something something");
                    return true;
                }
                List<String> lines = Utils.getLists().getStringList(args[1]);
                for (String s : lines) {
                    List<String> line = Arrays.asList(s.split(" "));
                    switch (line.get(0)) {

                        default: break;
                    }
                }

                return true;
            }

            // ------------------------------------------------------
            // --------------------- List scrolls -------------------
            // ------------------------------------------------------
            if (args[0].equalsIgnoreCase("list")) {
                if (args.length < 2) {
                    File[] files = new File(String.valueOf(Bukkit.getServer().getPluginManager().getPlugin("Quests").getDataFolder())).listFiles();
                    for (File f : files) {
                        if (f.getName().contains(".yml") && !f.getName().contains("Global.yml") && !f.getName().contains("Lists.yml")){
                            Utils.sendMessage(player, f.getName().replace(".yml", ""));
                        }
                    }
                    return true;
                }

                File file = new File(Bukkit.getServer().getPluginManager().getPlugin("Quests").getDataFolder(), args[1] + ".yml");
                FileConfiguration fc = YamlConfiguration.loadConfiguration(file);
                Set<String> keys = fc.getKeys(false);
                for (String key : keys) {
                    Utils.sendMessage(player, "/gquest " + args[1] + " " + key + " <playerName>");
                }

                return true;
            }

            Utils.sendMessage(player, "Please use a valid quests command");
        } else {
            Utils.log("A player must run this command");
        }
        return true;
    }
}