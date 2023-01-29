package com.jagsnet.minecraft.plugins.quests.listeners;

import com.jagsnet.minecraft.plugins.quests.otherStuff.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.ArrayList;

public class PlayerDeath implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (Utils.isScrollOff(player)) {

            String entity = player.getLastDamageCause().getCause().name().replace("_", " ");
            if (entity.equalsIgnoreCase("fire tick")) {
                entity = "fire";
            }
            if (player.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
                EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) player.getLastDamageCause();
                entity = e.getDamager().getType().toString().replace("_", " ");
            }
            ArrayList<String> lore = (ArrayList<String>) player.getInventory().getItemInOffHand().getItemMeta().getLore();

            for (int i = 0; i < lore.size(); i++) {
                if ((lore.get(i).toLowerCase().contains(entity.toLowerCase())) &&
                        (lore.get(i).contains("Die By") || lore.get(i).contains("Die To") || lore.get(i).contains("To Death") || lore.get(i).contains("Fall Victim To") || lore.get(i).contains("And Die") || lore.get(i).contains("Die From"))
                        && lore.get(i).contains("Incomplete")) {
                    if (Utils.updateTxtLine(lore, player, i, true)) {
                        event.setKeepLevel(true);
                        event.setDroppedExp(0);
                        return;
                    }
                }
            }
        }
    }
}
