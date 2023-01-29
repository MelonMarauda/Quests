package com.jagsnet.minecraft.plugins.quests.listeners;

import com.jagsnet.minecraft.plugins.quests.otherStuff.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

import java.util.ArrayList;

public class Throwing implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onXPGain(ProjectileLaunchEvent event) {
        if (event.isCancelled()) { return; }
        if (event.getEntity().getShooter() instanceof Player) {
            Player player = (Player) event.getEntity().getShooter();
            if (Utils.isScrollOff(player)) {
                
                String entity = event.getEntity().getType().toString().replace("_", " ");
                ArrayList<String> lore = (ArrayList<String>) player.getInventory().getItemInOffHand().getItemMeta().getLore();

                for (int i = 0; i < lore.size(); i++) {
                    if ((lore.get(i).toLowerCase().contains(entity.toLowerCase()) &&
                            lore.get(i).split(" ", 0).length ==
                            entity.split(" ", 0).length + 3) && (lore.get(i).contains("Throw") ||
                            lore.get(i).contains("Launch") || lore.get(i).contains("Shoot"))) {
                        if (Utils.updateNumLine(lore, player, 1, i)) {
                            return;
                        }
                    }
                }
            }
        }
    }
}
