package com.jagsnet.minecraft.plugins.quests.otherStuff.utils;

import com.jagsnet.minecraft.plugins.quests.Quests;
import com.jagsnet.minecraft.plugins.quests.otherStuff.messages.Messaging;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Claim {
    // ------------------- Scroll Claim Stuff --------------------------
    public static boolean claim(Player player, boolean track, String confKey, boolean force) {
        ItemMeta mainHandItem = null;
        Boolean issOff = false;
        if (Scroll.isScrollMain(player)) {
            mainHandItem = player.getInventory().getItemInMainHand().getItemMeta();
        }
        if (Scroll.isScrollOff(player)) {
            mainHandItem = player.getInventory().getItemInOffHand().getItemMeta();
            issOff = true;
        }
        NamespacedKey key = new NamespacedKey(Quests.getInstance(), "rewardLocation");
        if (mainHandItem.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
            String location = mainHandItem.getPersistentDataContainer().get(key, PersistentDataType.STRING);
            Configs.load(location.split("_")[0]);
            String path = location.split("_")[1];
            List rewards = Configs.get().getStringList( location.split("_")[1] + confKey);
            int rewardCount = rewards.size();
            int count = 0;
            for (int slot = 0; slot < 36; slot++) {
                ItemStack is = player.getInventory().getItem(slot);
                if (is == null) {
                    count++;
                }
            }
            if (count < rewardCount) {
                Messaging.sendMessage(player, "Clear some inventory space before trying to claim your reward");
                return true;
            }

            if (!player.isOp()) {
                if (player.hasPermission("quest.completed." + location) && !force) {
                    Messaging.sendMessage(player, "You have already completed this quest. It has a " + Configs.get().getString(location.split("_")[1] + ".permission") + " cooldown. Please wait until this is up.");
                    return true;
                }
            }

            if (Configs.get().getString(location.split("_")[1] + ".permissionComplete") != null) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " permission settemp quest.completed." + location + " true " + Configs.get().getString(location.split("_")[1] + ".permission"));
            }

            Messaging.log(player.getName() + " completed quest scroll " + location);

            if (issOff) {
                player.getInventory().setItemInOffHand(new ItemStack(Material.AIR));
            } else {
                player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
            }
            if (Configs.get().getStringList( path + ".claimMessage.lines").size() > 0) {
                String bold = ChatColor.BOLD + "";
                List messages = Configs.get().getStringList(path + ".claimMessage.lines");
                String msgName = Configs.get().getString(path + ".claimMessage.name");
                String msgColour = Configs.get().getString(path + ".claimMessage.colour");
                for (int i = 0; i < messages.size(); i++) {
                    player.sendMessage(ChatColor.valueOf(msgColour) + bold + msgName + " > " + ChatColor.WHITE + messages.get(i).toString());
                }
            }
            for (int i = 0; rewards.size() > i; i++) {
                String rewardName = (String) rewards.get(i);

                Configs.loadLists();
                int e = 0;
                while (rewardName.contains("<") && rewardName.contains(">") && e < 4) {
                    if (rewardName.contains("<player>")) {
                        rewardName = rewardName.replace("<player>", ChatColor.stripColor(player.getName()));
                    }
                    if (rewardName.contains("<date>")) {
                        rewardName = rewardName.replace("<date>", new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
                    }
                    if (rewardName.contains("<") && rewardName.contains(">")) {
                        String listName = rewardName.substring(rewardName.indexOf("<") + 1, rewardName.indexOf(">"));
                        List list = Configs.getLists().getStringList(listName);
                        int randomNum = ThreadLocalRandom.current().nextInt(0, list.size());
                        rewardName = rewardName.replace("<" + listName + ">", (CharSequence) list.get(randomNum));
                    }
                    e++;
                }
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), rewardName);
                Messaging.log("Quests ran: " + rewardName);
            }
        } else {
            Messaging.sendMessage(player, "Thats strange, there are no rewards for this quest scroll");
            return true;
        }
        return true;
    }
}
