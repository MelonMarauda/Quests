package com.jagsnet.minecraft.plugins.quests.trackers.scheduledTasks;

import com.jagsnet.minecraft.plugins.quests.Quests;
import com.jagsnet.minecraft.plugins.quests.otherStuff.messages.Strings;
import com.jagsnet.minecraft.plugins.quests.otherStuff.utils.Completion;
import com.jagsnet.minecraft.plugins.quests.otherStuff.utils.Scroll;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class Rank {
    public static void track(){
        for(Player p : Bukkit.getOnlinePlayers()){
            if (Scroll.isScrollOff(p)) {
                NamespacedKey key = new NamespacedKey(Quests.getInstance(), "rank");
                if (!p.getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING)) continue;
                if (!p.getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING).toLowerCase().equals("true")) continue;
                if (p.hasPermission("group.player")) {

                    String entity = "Vote And Get Player Rank: Incomplete";
                    ArrayList<String> lore = (ArrayList<String>) p.getInventory().getItemInOffHand().getItemMeta().getLore();

                    for (int i = 0; i < lore.size(); i++) {
                        String line = Strings.cleanLore(lore.get(i), false, false);
                        if (line.contains(entity.toLowerCase())) {
                            if (Completion.updateTxtLine(lore, p, i, true)) {
                            }
                        }
                    }
                }
            }
        }
    }
}
