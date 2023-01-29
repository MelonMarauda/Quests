package com.jagsnet.minecraft.plugins.quests.listeners;

import com.jagsnet.minecraft.plugins.quests.otherStuff.Utils;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerShearEntityEvent;

import java.util.ArrayList;

public class Shearing implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onEnchant(PlayerShearEntityEvent event) {
        if (event.isCancelled()) { return; }
        Player player = event.getPlayer();
        if (Utils.isScrollOff(player)) {

            String entity = event.getEntity().getType().name().replace("_", " ");
            ArrayList<String> lore = (ArrayList<String>) player.getInventory().getItemInOffHand().getItemMeta().getLore();

            if (event.getEntity() instanceof Sheep) {
                Sheep eventEnt = (Sheep) event.getEntity();
                entity = eventEnt.getColor().name().toLowerCase().replace("_", " ") + " " + entity;
            }

            if (event.getEntity() instanceof MushroomCow) {
                MushroomCow eventEnt = (MushroomCow) event.getEntity();
                entity = eventEnt.getVariant().name() + " Mooshroom";
            }

            for (int i = 0; i < lore.size(); i++) {
                if ((lore.get(i).toLowerCase().contains(entity.toLowerCase()) &&
                        lore.get(i).split(" ", 0).length ==
                        entity.split(" ", 0).length + 3) && lore.get(i).contains("Shear")) {
                    if (Utils.updateNumLine(lore, player, 1, i)) {
                        return;
                    }
                }
            }
        }
    }
}
