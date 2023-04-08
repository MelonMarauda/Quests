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

            String entity = player.getLastDamageCause().getCause().name().replace("_", " ").toLowerCase();
            if (entity.equalsIgnoreCase("fire tick")) {
                entity = "fire";
            }
            if (player.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
                EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) player.getLastDamageCause();
                entity = e.getDamager().getType().toString().replace("_", " ").toLowerCase();
            }
            ArrayList<String> lore = (ArrayList<String>) player.getInventory().getItemInOffHand().getItemMeta().getLore();

            for (int i = 0; i < lore.size(); i++) {
                String line = Utils.cleanLore(lore.get(i), true, false);
                if (line.contains(" " + entity) &&
                        (line.contains("die by") || line.contains("die to") || line.contains("to death") || line.contains("fall victim to") || line.contains("and die") || line.contains("die from"))
                        && line.contains("incomplete")) {
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
