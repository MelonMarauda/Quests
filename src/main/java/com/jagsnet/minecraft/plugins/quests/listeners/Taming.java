package com.jagsnet.minecraft.plugins.quests.listeners;

import com.jagsnet.minecraft.plugins.quests.otherStuff.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTameEvent;

import java.util.ArrayList;

public class Taming implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onMove(EntityTameEvent event) {
        if (event.isCancelled()) { return; }
        if (event.getOwner() instanceof Player) {
            Player player = (Player) event.getOwner();
            if (Utils.isScrollOff(player)) {
                
                String entity = event.getEntity().getType().toString().replace("_", " ");
                ArrayList<String> lore = (ArrayList<String>) player.getInventory().getItemInOffHand().getItemMeta().getLore();
                boolean wolf = false;
                if (entity.equals("WOLF")) {
                    wolf = true;
                }

                for (int i = 0; i < lore.size(); i++) {
                    if (((lore.get(i).toLowerCase().contains(entity.toLowerCase()) || (wolf && lore.get(i).toLowerCase().contains("wolves")))&&
                            lore.get(i).split(" ", 0).length ==
                            entity.split(" ", 0).length + 3) && lore.get(i).contains("Tame")) {
                        if (Utils.updateNumLine(lore, player, 1, i)) {
                            return;
                        }
                    }
                }
            }
        }
    }
}