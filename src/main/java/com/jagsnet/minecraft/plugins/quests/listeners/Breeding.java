package com.jagsnet.minecraft.plugins.quests.listeners;

import com.jagsnet.minecraft.plugins.quests.otherStuff.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;

import java.util.ArrayList;

public class Breeding implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onBreed(EntityBreedEvent event) {
        if (event.isCancelled()) { return; }
        if (event.getBreeder() instanceof Player) {
            Player player = (Player) event.getBreeder();
            if (Utils.isScrollOff(player)) {

                String entity = event.getEntityType().toString().replace("_", " ");
                int entitySize = entity.split(" ", 0).length;
                ArrayList<String> lore = (ArrayList<String>) player.getInventory().getItemInOffHand().getItemMeta().getLore();
                boolean wolf = false;
                if (entity.equals("WOLF")) {
                    wolf = true;
                }

                for (int i = 0; i < lore.size(); i++) {
                    if (((lore.get(i).toLowerCase().contains(entity.toLowerCase()) ||
                            (wolf && lore.get(i).toLowerCase().contains("wolves"))) &&
                            lore.get(i).split(" ", 0).length == entitySize + 3) &&
                            lore.get(i).contains("Breed")) {
                        if (Utils.updateNumLine(lore, player, 1, i)) {
                            return;
                        }
                    }
                }
            }
        }
    }
}
