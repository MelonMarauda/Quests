package com.jagsnet.minecraft.plugins.quests.commands;

import com.jagsnet.minecraft.plugins.quests.Quests;
import com.jagsnet.minecraft.plugins.quests.otherStuff.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Generators implements CommandExecutor {

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
                Utils.sendMessage((Player) sender, "/gquest <fileName> <scrollName> <userName> [force|verbose]");
            } else { Utils.log("Invalid quests command"); }
            return true;
        }
        // ------------------------------------------------------
        // ------------------ Generate scroll -------------------
        // ------------------------------------------------------
        Utils.load(args[0]);
        Utils.loadLists();
        Boolean force = false;
        Boolean verbose = true;
        if (args.length > 3) {
            for (int i = 3; i < args.length; i++) {
                switch (args[i].toLowerCase()) {
                    case "force":
                        force = true;
                        break;
                    case "noverbose":
                        verbose = false;
                        break;
                    default:
                        break;
                }
            }
        }
        if (args.length < 3) {
            if (sender instanceof Player) {
                Utils.sendMessage((Player) sender, "Please specify a scroll name specified in the configs and a player to give it to");
            } else {
                Utils.log("Please specify a scroll name specified in the configs and a player to give it to");
            }
            return true;
        }
        Player player = Bukkit.getPlayer(args[2]);
        if (player == null) {
            if (sender instanceof Player) {
                Utils.sendMessage((Player) sender, "Please specify a player that is currently online");
            } else {
                Utils.log("Please specify a player that is currently online");
            }
            return true;
        }
        if (!player.isOnline()) {
            if (sender instanceof Player) {
                Utils.sendMessage((Player) sender, "Please specify a player that is currently online");
            } else {
                Utils.log("Please specify a player that is currently online");
            }
            return true;
        }

        if (player.hasPermission("quest." + args[0] + "_" + args[1]) && !force) {
            if (sender instanceof Player) {
                Utils.sendMessage((Player) sender, "Player has already gotten one of these quests. Use [/gquest config quest playerName force] to give them another.");
            } else {
                Utils.log("Player has already gotten one of these quests. Use [/gquest config quest playerName force] to give them another.");
            }
            if (verbose) {
                Utils.sendMessage(player, "You have already collected this quest. It has a " + Utils.get().getString(args[1] + ".permission") + " cooldown. Please wait until this is up.");
            }
            return true;
        }

        if (Utils.get().getStringList(args[1] + ".lines") == null) {
            if (sender instanceof Player) {
                Utils.sendMessage((Player) sender, "Number of lines for this scroll not specified");
            } else {
                Utils.log("Number of lines for this scroll not specified");
            }
            return true;
        }
        if (Utils.get().getStringList(args[1] + ".rewards") == null) {
            if (sender instanceof Player) {
                Utils.sendMessage((Player) sender, "Reward not set for this scroll");
            } else {
                Utils.log("Reward not set for this scroll");
            }
            return true;
        }
        if (Utils.get().getString(args[1] + ".name") == null) {
            if (sender instanceof Player) {
                Utils.sendMessage((Player) sender, "Name not set for this scroll");
            } else {
                Utils.log("Name not set for this scroll");
            }
            return true;
        }
        genScroll(args, player);
        return true;
    }

    public static boolean genScroll(String[] args, Player player) {
        Utils.load(args[0]);
        Utils.loadLists();
        List lines = Utils.get().getStringList(args[1] + ".lines");

        ItemStack scroll = new ItemStack(Material.PAPER);
        ItemMeta scrollMeta = scroll.getItemMeta();
        List<String> lore = new ArrayList<>();
        String grey = ChatColor.GRAY + "";
        String darkGrey = ChatColor.DARK_GRAY + "";
        String white = ChatColor.WHITE + "";
        String bold = ChatColor.BOLD + "";
        String random = ChatColor.MAGIC + "";
        String strike = ChatColor.STRIKETHROUGH + "";

        scrollMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + Utils.get().getString(args[1] + "." + "name"));
        for (int i = 0; i < lines.size(); i++) {
            String loreLine = lines.get(i).toString();
            if (loreLine.equals("<break>")) {
                loreLine = "- - - - - - - - - - - - - - - - - - -";
            }
            if (loreLine.equals("<empty>")) {
                loreLine = "";
            }
            if (loreLine.contains("<player>")) {
                loreLine = loreLine.replace("<player>", player.getName());
            }
            if (loreLine.contains("<date>")) {
                loreLine = loreLine.replace("<date>", new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
            }
            if (loreLine.contains("&f")) {
                loreLine = loreLine.replace("&f", white);
            }
            if (loreLine.contains("&k")) {
                loreLine = loreLine.replace("&k", random);
            }
            if (loreLine.contains("&m")) {
                loreLine = loreLine.replace("&m", strike);
            }
            while (loreLine.contains("<") && loreLine.contains(">")) {
                String listName = loreLine.substring(loreLine.indexOf("<") + 1, loreLine.indexOf(">"));
                List list = Utils.getLists().getStringList(listName);
                int randomNum = ThreadLocalRandom.current().nextInt(0, list.size());
                loreLine = loreLine.replace("<" + listName + ">", (CharSequence) list.get(randomNum));
            }
            while (loreLine.contains("[") && loreLine.contains("]")) {
                String[] range = loreLine.substring(loreLine.indexOf("[") + 1, loreLine.indexOf("]")).split("-");
                int randomNum = ThreadLocalRandom.current().nextInt(Integer.parseInt(range[0]), Integer.parseInt(range[1]));
                loreLine = loreLine.replace("[" + range[0]  + "-" + range[1] + "]", String.valueOf(randomNum));
            }
            lore.add(grey + loreLine);
        }

        NamespacedKey key;
        // Setting expiry, rewards and UUID restrictions
        Long expiry = System.currentTimeMillis();
        key = new NamespacedKey(Quests.getInstance(), "expiry");
        scrollMeta.getPersistentDataContainer().set(key, PersistentDataType.LONG, expiry);
        if (Utils.get().getString(args[1] + ".UUID") != null) {
            if (Utils.get().getString(args[1] + ".UUID").equalsIgnoreCase("true")) {
                key = new NamespacedKey(Quests.getInstance(), "UUID");
                scrollMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, player.getUniqueId().toString());
            }
        }
        if (Utils.get().getString(args[1] + ".movement") != null) {
            key = new NamespacedKey(Quests.getInstance(), "movement");
            scrollMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, Utils.get().getString(args[1] + ".movement"));
        }
        if (Utils.get().getString(args[1] + ".coords") != null) {
            key = new NamespacedKey(Quests.getInstance(), "coords");
            scrollMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, Utils.get().getString(args[1] + ".coords"));
        }
        if (Utils.get().getString(args[1] + ".locked") != null) {
            key = new NamespacedKey(Quests.getInstance(), "locked");
            scrollMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, Utils.get().getString(args[1] + ".locked"));
        }
        if (Utils.get().getString(args[1] + ".random") != null) {
            key = new NamespacedKey(Quests.getInstance(), "random");
            scrollMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, Utils.get().getString(args[1] + ".random"));
            key = new NamespacedKey(Quests.getInstance(), "randomTime");
            scrollMeta.getPersistentDataContainer().set(key, PersistentDataType.LONG, 600000L);
        }
        if (Utils.get().getString(args[1] + ".rank") != null) {
            key = new NamespacedKey(Quests.getInstance(), "rank");
            scrollMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, Utils.get().getString(args[1] + ".rank"));
        }
        if (Utils.get().getString(args[1] + ".region") != null) {
            key = new NamespacedKey(Quests.getInstance(), "region");
            scrollMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, Utils.get().getString(args[1] + ".region"));
        }
        if (Utils.get().getString(args[1] + ".autocomplete") != null) {
            key = new NamespacedKey(Quests.getInstance(), "autocomplete");
            scrollMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, Utils.get().getString(args[1] + ".autocomplete"));
        }
        if (Utils.get().getString(args[1] + ".randomTimer") != null) {
            key = new NamespacedKey(Quests.getInstance(), "randomTimer");
            scrollMeta.getPersistentDataContainer().set(key, PersistentDataType.LONG, Utils.get().getLong(args[1] + ".randomTimer"));
        }
        if (Utils.get().getString(args[1] + ".texture") != null) {
            String textureS = Utils.get().getString(args[1] + ".texture");
            int texture = 0;
            if (textureS.contains("-")) {
                texture = ThreadLocalRandom.current().nextInt(Integer.parseInt(textureS.split("-")[0]), Integer.parseInt(textureS.split("-")[1]));
            } else {
                texture = Integer.parseInt(Utils.get().getString(args[1] + ".texture"));
            }
            scrollMeta.setCustomModelData(texture);
        }
        Boolean permission = false;
        if (Utils.get().getString(args[1] + ".permission") != null) {
            permission = true;
        }

        String lorePath = args[0] + "_" + args[1];
        key = new NamespacedKey(Quests.getInstance(), "rewardLocation");
        scrollMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, lorePath);

        Utils.log(player.getName() + " collected quest scroll " + args[1] + "_" + args[2]);

        scrollMeta.setLore(lore);
        scroll.setItemMeta(scrollMeta);
        Boolean offHandFree = false;
        if (player.getInventory().getItemInOffHand().getType() == Material.AIR) {
            offHandFree = true;
        }
        for (int slot = 0; slot < 36; slot ++) {
            ItemStack is = player.getInventory().getItem(slot);
            if (is == null || offHandFree) {
                if (offHandFree) {
                    player.getInventory().setItemInOffHand(scroll);
                } else {
                    player.getInventory().setItem(slot, scroll);
                }
                slot = 50;
                if (Utils.get().getStringList(args[1] + ".message.lines").size() > 0) {
                    if (Utils.get().getString(args[1] + ".message.name") == null) {
                        Utils.sendMessage(player, Utils.get().getStringList(args[1] + ".message.lines").get(0).toString());
                    } else {
                        List messages = Utils.get().getStringList(args[1] + ".message.lines");
                        String msgName = Utils.get().getString(args[1] + ".message.name");
                        String msgColour = Utils.get().getString(args[1] + ".message.colour");
                        for (int i = 0; i < messages.size(); i++) {
                            player.sendMessage(ChatColor.valueOf(msgColour) + bold + msgName + " > " + white + messages.get(i).toString());
                        }
                    }
                } else if (Utils.get().getString(args[1] + ".message") != null) {
                    Utils.sendMessage(player, Utils.get().getString(args[1] + ".message"));
                }
                if (permission) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " permission settemp quest." + args[0] + "_" + args[1] + " true " + Utils.get().getString(args[1] + ".permission"));
                }
                return true;
            }
        }
        Utils.sendMessage(player, "Please clear room in your inventory before trying to collect a quest scroll");
        return false;
    }
}