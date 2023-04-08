package com.jagsnet.minecraft.plugins.quests.scheduledTasks;

import com.jagsnet.minecraft.plugins.quests.Quests;
import com.jagsnet.minecraft.plugins.quests.otherStuff.Utils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class Coords {
    public static void track(){
        for(Player p : Bukkit.getOnlinePlayers()){
            if (Utils.isScrollOff(p)) {
                NamespacedKey key = new NamespacedKey(Quests.getInstance(), "coords");
                if (!p.getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING)) continue;

                String entity = "Go To X" + (int) Math.floor(p.getLocation().getBlockX()) + " Y" + (int) Math.floor(p.getLocation().getBlockY()) + " Z" + (int) Math.floor(p.getLocation().getBlockZ()) + ": Incomplete";
                ArrayList<String> lore = (ArrayList<String>) p.getInventory().getItemInOffHand().getItemMeta().getLore();

                for (int i = 0; i < lore.size(); i++) {
                    String line = Utils.cleanLore(lore.get(i), false, false);
                    if (line.contains(entity.toLowerCase())) {
                        if (Utils.updateTxtLine(lore, p, i, true)) {
                        }
                    }
                }
            }
        }
    }
}
