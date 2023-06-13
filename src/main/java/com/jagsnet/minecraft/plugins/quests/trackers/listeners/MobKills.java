package com.jagsnet.minecraft.plugins.quests.trackers.listeners;

import com.jagsnet.minecraft.plugins.quests.otherStuff.messages.Strings;
import com.jagsnet.minecraft.plugins.quests.otherStuff.utils.Completion;
import com.jagsnet.minecraft.plugins.quests.otherStuff.utils.Scroll;
import org.bukkit.ChatColor;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.ArrayList;

public class MobKills implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() instanceof Player) {
            Player player = event.getEntity().getKiller();
            if (Scroll.isScrollOff(player)) {
                
                String entity = event.getEntity().getType().toString().replace("_", " ").toLowerCase();
                Boolean name = false;
                if (event.getEntity().getCustomName() != null && event.getEntity().isCustomNameVisible()) {
                    entity = ChatColor.stripColor(event.getEntity().getCustomName()).toLowerCase();
                    name = true;
                }
                ArrayList<String> lore = (ArrayList<String>) player.getInventory().getItemInOffHand().getItemMeta().getLore();

                if (name) {
                    for (int i = 0; i < lore.size(); i++) {
                        String line = Strings.cleanLore(lore.get(i), true, false);
                        if (((line.contains(" " + entity)) &&
                                line.split(" ", 0).length ==
                                entity.split(" ", 0).length + 2) && (line.contains("incomplete"))) {
                            if (Completion.updateTxtLine(lore, player, i, true)) {
                                return;
                            }
                        }
                    }
                } else {
                    if (event.getEntity() instanceof Ageable) {
                        Ageable crop = (Ageable) event.getEntity();
                        if (!crop.isAdult()) {
                            entity = "baby " + entity;
                        }
                    }
                    for (int i = 0; i < lore.size(); i++) {
                        String line = Strings.cleanLore(lore.get(i), true, false);
                        if ((line.contains(" " + entity) &&
                                line.split(" ", 0).length ==
                                entity.split(" ", 0).length + 3)  && line.contains("kill")) {
                            if (Completion.updateNumLine(lore, player, 1, i)) {
                                return;
                            }
                        }
                    }
                }
            }
        }
    }
}