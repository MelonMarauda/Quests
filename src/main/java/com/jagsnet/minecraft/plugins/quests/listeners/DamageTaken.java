package com.jagsnet.minecraft.plugins.quests.listeners;

import com.jagsnet.minecraft.plugins.quests.otherStuff.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.ArrayList;

public class DamageTaken implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onDamage(EntityDamageEvent event) {
        if (event.isCancelled()) { return; }
        if (event.getFinalDamage() < 1) { return; }
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (Utils.isScrollOff(player)) {

                String entity = "take";
                ArrayList<String> lore = (ArrayList<String>) player.getInventory().getItemInOffHand().getItemMeta().getLore();
                boolean isEntityEvent = false;

                if (event instanceof EntityDamageByEntityEvent) {
                    EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
                    entity = e.getDamager().getType().toString().replace("_", " ").toLowerCase();
                    isEntityEvent = true;
                }

                for (int i = 0; i < lore.size(); i++) {
                    String line = Utils.cleanLore(lore.get(i), true, false);
                    if ((line.split(" ", 0).length ==
                            4) && (line.contains("damage") &&
                            line.contains("take"))) {
                        if (Utils.updateNumLine(lore, player, (int) event.getFinalDamage(), i)) {
                            return;
                        }
                    }

                    if (isEntityEvent && ((line.contains(" " + entity) &&
                            line.split(" ", 0).length ==
                            entity.split(" ").length + 5) &&
                            (line.contains("damage") && line.contains("take")))) {
                        if (Utils.updateNumLine(lore, player, (int) event.getFinalDamage(), i)) {
                            return;
                        }
                    }
                }
            }
        }
    }
}
