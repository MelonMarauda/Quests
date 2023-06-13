package com.jagsnet.minecraft.plugins.quests.trackers.scheduledTasks;

import com.jagsnet.minecraft.plugins.quests.Quests;
import com.jagsnet.minecraft.plugins.quests.otherStuff.messages.Strings;
import com.jagsnet.minecraft.plugins.quests.otherStuff.utils.Configs;
import com.jagsnet.minecraft.plugins.quests.otherStuff.utils.Scroll;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Random {
    public static void randomise(){
        for(Player p : Bukkit.getOnlinePlayers()){
            if (Scroll.isScrollOff(p)) {
                NamespacedKey key = new NamespacedKey(Quests.getInstance(), "random");
                if (!p.getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING))
                    continue;
                key = new NamespacedKey(Quests.getInstance(), "randomTime");
                if (p.getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.LONG) >= System.currentTimeMillis())
                    continue;

                key = new NamespacedKey(Quests.getInstance(), "random");
                String listName = p.getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING);
                List list = Configs.getLists().getStringList(listName);
                int randomNum = ThreadLocalRandom.current().nextInt(0, list.size() - 1);
                String newLine = (String) list.get(randomNum);

                while (newLine.contains("<") && newLine.contains(">")) {
                    String listNames = newLine.substring(newLine.indexOf("<") + 1, newLine.indexOf(">"));
                    List lists = Configs.getLists().getStringList(listNames);
                    int randomNums = ThreadLocalRandom.current().nextInt(0, lists.size());
                    newLine = newLine.replace("<" + listNames + ">", (CharSequence) lists.get(randomNums));
                }

                String white = ChatColor.WHITE + "";

                ArrayList<String> lore = (ArrayList<String>) p.getInventory().getItemInOffHand().getItemMeta().getLore();
                lore.set(3, white + Strings.colourIt(newLine) + lore.get(3).split(": ")[1]);

                ItemStack itemStack = p.getInventory().getItemInOffHand();
                ItemMeta meta = itemStack.getItemMeta();

                long time = 600000;
                key = new NamespacedKey(Quests.getInstance(), "randomTimer");
                if (meta.getPersistentDataContainer().has(key, PersistentDataType.LONG)) {
                    time = meta.getPersistentDataContainer().get(key, PersistentDataType.LONG);
                }

                key = new NamespacedKey(Quests.getInstance(), "randomTime");
                meta.getPersistentDataContainer().set(key, PersistentDataType.LONG, System.currentTimeMillis() + time);
                meta.setLore(lore);
                itemStack.setItemMeta(meta);
                if (!p.getGameMode().equals(GameMode.SPECTATOR)) {
                    p.getWorld().playSound(p.getLocation(), Sound.BLOCK_BELL_USE, 2.0f, 1.0f);
                }
            }
        }
    }
}
