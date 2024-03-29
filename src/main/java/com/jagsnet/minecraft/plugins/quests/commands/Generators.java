package com.jagsnet.minecraft.plugins.quests.commands;

import com.jagsnet.minecraft.plugins.quests.Quests;
import com.jagsnet.minecraft.plugins.quests.otherStuff.messages.Messaging;
import com.jagsnet.minecraft.plugins.quests.otherStuff.utils.Configs;
import com.jagsnet.minecraft.plugins.quests.otherStuff.utils.Perms;
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
                Messaging.sendMessage(player, "You are missing quests admin permissions. Speak to Melon for more info");
                return true;
            }
        }
        if (args.length < 1) {
            if (sender instanceof Player) {
                Messaging.sendMessage((Player) sender, "/gquest <fileName> <scrollName> <userName> [force|verbose]");
            } else { Messaging.log("Invalid quests command"); }
            return true;
        }
        // ------------------------------------------------------
        // ------------------ Generate scroll -------------------
        // ------------------------------------------------------
        Configs.load(args[0]);
        Configs.loadLists();
        Boolean force = false;
        Boolean verbose = true;
        Boolean track = false;
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
                Messaging.sendMessage((Player) sender, "Please specify a scroll name specified in the configs and a player to give it to");
            } else {
                Messaging.log("Please specify a scroll name specified in the configs and a player to give it to");
            }
            return true;
        }
        Player player = Bukkit.getPlayer(args[2]);
        if (player == null) {
            if (sender instanceof Player) {
                Messaging.sendMessage((Player) sender, "Please specify a player that is currently online");
            } else {
                Messaging.log("Please specify a player that is currently online");
            }
            return true;
        }
        if (!player.isOnline()) {
            if (sender instanceof Player) {
                Messaging.sendMessage((Player) sender, "Please specify a player that is currently online");
            } else {
                Messaging.log("Please specify a player that is currently online");
            }
            return true;
        }

        if (player.hasPermission("quest." + args[0] + "_" + args[1]) && !force) {
            if (sender instanceof Player) {
                Messaging.sendMessage((Player) sender, "Player has already gotten one of these quests. Use [/gquest config quest playerName force] to give them another.");
            } else {
                Messaging.log("Player has already gotten one of these quests. Use [/gquest config quest playerName force] to give them another.");
            }
            if (verbose) {
                Messaging.sendMessage(player, "You have already collected this quest. It has a " + Configs.get().getString(args[1] + ".permission") + " cooldown. Please wait until this is up.");
            }
            return true;
        }

        if (Configs.get().getStringList(args[1] + ".lines") == null) {
            if (sender instanceof Player) {
                Messaging.sendMessage((Player) sender, "Number of lines for this scroll not specified");
            } else {
                Messaging.log("Number of lines for this scroll not specified");
            }
            return true;
        }
        if (Configs.get().getStringList(args[1] + ".rewards") == null) {
            if (sender instanceof Player) {
                Messaging.sendMessage((Player) sender, "Reward not set for this scroll");
            } else {
                Messaging.log("Reward not set for this scroll");
            }
            return true;
        }
        if (Configs.get().getString(args[1] + ".name") == null) {
            if (sender instanceof Player) {
                Messaging.sendMessage((Player) sender, "Name not set for this scroll");
            } else {
                Messaging.log("Name not set for this scroll");
            }
            return true;
        }
        genScroll(args, player);
        return true;
    }

    public static boolean genScroll(String[] args, Player player) {
        Configs.load(args[0]);
        Configs.loadLists();
        List lines = Configs.get().getStringList(args[1] + ".lines");

        ItemStack scroll = new ItemStack(Material.PAPER);
        ItemMeta scrollMeta = scroll.getItemMeta();
        List<String> lore = new ArrayList<>();
        String grey = ChatColor.GRAY + "";
        String darkGrey = ChatColor.DARK_GRAY + "";
        String white = ChatColor.WHITE + "";
        String bold = ChatColor.BOLD + "";
        String random = ChatColor.MAGIC + "";
        String strike = ChatColor.STRIKETHROUGH + "";
        String stats = "";
        scrollMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + Configs.get().getString(args[1] + "." + "name"));
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
            while (loreLine.contains("<") && loreLine.contains(">")) {
                String listName = loreLine.substring(loreLine.indexOf("<") + 1, loreLine.indexOf(">"));
                List list = Configs.getLists().getStringList(listName);
                int randomNum = ThreadLocalRandom.current().nextInt(0, list.size());
                String s = String.valueOf(list.get(randomNum));
                if (s.contains("|")) {
                    String[] ss = s.split("\\|");
                    if (stats.equalsIgnoreCase("")) {
                        stats = stats + ss[1];
                    } else {
                        stats = stats + "," + ss[1];
                    }
                    s = ss[0];
                }
                loreLine = loreLine.replace("<" + listName + ">", s);
            }
            while (loreLine.contains("[") && loreLine.contains("]")) {
                String[] range = loreLine.substring(loreLine.indexOf("[") + 1, loreLine.indexOf("]")).split("-");
                int randomNum = ThreadLocalRandom.current().nextInt(Integer.parseInt(range[0]), Integer.parseInt(range[1]));
                loreLine = loreLine.replace("[" + range[0]  + "-" + range[1] + "]", String.valueOf(randomNum));
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
            lore.add(grey + loreLine);
        }

        NamespacedKey key;
        // Setting expiry, rewards and UUID restrictions
        Long expiry = System.currentTimeMillis();
        key = new NamespacedKey(Quests.getInstance(), "expiry");
        scrollMeta.getPersistentDataContainer().set(key, PersistentDataType.LONG, expiry);
        if (Configs.get().getString(args[1] + ".UUID") != null) {
            if (Configs.get().getString(args[1] + ".UUID").equalsIgnoreCase("true")) {
                key = new NamespacedKey(Quests.getInstance(), "UUID");
                scrollMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, player.getUniqueId().toString());
            }
        }

        String[] configs = {"movement", "coords", "locked", "rank", "region", "autocomplete", "randomTimer", "random"};

        for (String s : configs) {
            if (s.equalsIgnoreCase("movement")) {
                String m = "";
                if (Configs.get().getString(args[1] + "." + s) != null) {
                    m = Configs.get().getString(args[1] + "." + s);
                }
                if (stats.equalsIgnoreCase("") && m.equalsIgnoreCase("")) {
                } else if (stats.equalsIgnoreCase("") || m.equalsIgnoreCase("")) {
                    key = new NamespacedKey(Quests.getInstance(), s);
                    scrollMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, m + stats);
                } else {
                    key = new NamespacedKey(Quests.getInstance(), s);
                    scrollMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, m + "," + stats);
                }
            } else if (Configs.get().getString(args[1] + "." + s) != null) {
                key = new NamespacedKey(Quests.getInstance(), s);
                scrollMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, Configs.get().getString(args[1] + "." + s));

                if (s.equalsIgnoreCase("random")) {
                    key = new NamespacedKey(Quests.getInstance(), "randomTime");
                    if (Configs.get().getString(args[1] + ".randomTimer") != null) {
                        key = new NamespacedKey(Quests.getInstance(), "randomTimer");
                        scrollMeta.getPersistentDataContainer().set(key, PersistentDataType.LONG, Configs.get().getLong(args[1] + ".randomTimer"));
                    } else {
                        scrollMeta.getPersistentDataContainer().set(key, PersistentDataType.LONG, 600000L);
                    }
                }
            }
        }

        if (Configs.get().getString(args[1] + ".texture") != null) {
            String textureS = Configs.get().getString(args[1] + ".texture");
            int texture = 0;
            if (textureS.contains("-")) {
                texture = ThreadLocalRandom.current().nextInt(Integer.parseInt(textureS.split("-")[0]), Integer.parseInt(textureS.split("-")[1]));
            } else {
                texture = Integer.parseInt(Configs.get().getString(args[1] + ".texture"));
            }
            scrollMeta.setCustomModelData(texture);
        }
        Boolean permission = false;
        if (Configs.get().getString(args[1] + ".permission") != null) {
            permission = true;
        }

        String lorePath = args[0] + "_" + args[1];
        key = new NamespacedKey(Quests.getInstance(), "rewardLocation");
        scrollMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, lorePath);

        Messaging.log(player.getName() + " collected quest scroll " + args[1] + "_" + args[2]);

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
                if (Configs.get().getStringList(args[1] + ".message.lines").size() > 0) {
                    if (Configs.get().getString(args[1] + ".message.name") == null) {
                        Messaging.sendMessage(player, Configs.get().getStringList(args[1] + ".message.lines").get(0).toString());
                    } else {
                        List messages = Configs.get().getStringList(args[1] + ".message.lines");
                        String msgName = Configs.get().getString(args[1] + ".message.name");
                        String msgColour = Configs.get().getString(args[1] + ".message.colour");
                        for (int i = 0; i < messages.size(); i++) {
                            Messaging.sendUnformattedMessage(player, ChatColor.valueOf(msgColour) + bold + msgName + " > " + white + messages.get(i).toString());
                        }
                    }
                } else if (Configs.get().getString(args[1] + ".message") != null) {
                    Messaging.sendMessage(player, Configs.get().getString(args[1] + ".message"));
                }
                if (permission) {
                    Perms.setPerm(player.getName(), "quest." + args[0] + "_" + args[1], Configs.get().getString(args[1] + ".permission"));
                    //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " permission settemp quest." + args[0] + "_" + args[1] + " true " + Configs.get().getString(args[1] + ".permission"));
                }
                return true;
            }
        }
        Messaging.sendMessage(player, "Please clear room in your inventory before trying to collect a quest scroll");
        return false;
    }
}