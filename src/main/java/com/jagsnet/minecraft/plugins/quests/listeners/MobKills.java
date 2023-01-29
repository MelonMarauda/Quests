package com.jagsnet.minecraft.plugins.quests.listeners;

import com.jagsnet.minecraft.plugins.quests.otherStuff.Utils;
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
            if (Utils.isScrollOff(player)) {
                
                String entity = event.getEntity().getType().toString().replace("_", " ");
                Boolean name = false;
                if (event.getEntity().getCustomName() != null && event.getEntity().isCustomNameVisible()) {
                    entity = ChatColor.stripColor(event.getEntity().getCustomName());
                    name = true;
                }
                ArrayList<String> lore = (ArrayList<String>) player.getInventory().getItemInOffHand().getItemMeta().getLore();

                if (name) {
                    for (int i = 0; i < lore.size(); i++) {
                        if (((lore.get(i).toLowerCase().contains(entity.toLowerCase())) &&
                                lore.get(i).split(" ", 0).length ==
                                entity.split(" ", 0).length + 2) && (lore.get(i).toLowerCase().contains("incomplete"))) {
                            if (Utils.updateTxtLine(lore, player, i, true)) {
                                return;
                            }
                        }
                    }
                } else {
                    boolean wolf = false;
                    if (entity.equals("WOLF")) {
                        wolf = true;
                    }
                    if (event.getEntity() instanceof Ageable) {
                        Ageable crop = (Ageable) event.getEntity();
                        if (!crop.isAdult()) {
                            entity = "baby " + entity;
                        }
                    }
                    for (int i = 0; i < lore.size(); i++) {
                        if (((lore.get(i).toLowerCase().contains(entity.toLowerCase()) || (wolf && lore.get(i).toLowerCase().contains("wolves")))&&
                                lore.get(i).split(" ", 0).length ==
                                entity.split(" ", 0).length + 3)  && lore.get(i).contains("Kill")) {
                            if (Utils.updateNumLine(lore, player, 1, i)) {
                                return;
                            }
                        }
                    }
                }
            }
        }
    }
}