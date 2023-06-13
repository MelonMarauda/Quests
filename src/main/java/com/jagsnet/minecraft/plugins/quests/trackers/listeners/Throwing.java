package com.jagsnet.minecraft.plugins.quests.trackers.listeners;

import com.jagsnet.minecraft.plugins.quests.otherStuff.messages.Strings;
import com.jagsnet.minecraft.plugins.quests.otherStuff.utils.Completion;
import com.jagsnet.minecraft.plugins.quests.otherStuff.utils.Scroll;
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
            if (Scroll.isScrollOff(player)) {
                
                String entity = event.getEntity().getType().toString().replace("_", " ").toLowerCase();
                ArrayList<String> lore = (ArrayList<String>) player.getInventory().getItemInOffHand().getItemMeta().getLore();

                for (int i = 0; i < lore.size(); i++) {
                    String line = Strings.cleanLore(lore.get(i), false, true);
                    if ((line.contains(" " + entity) &&
                            line.split(" ", 0).length ==
                            entity.split(" ", 0).length + 3) && (line.contains("throw") ||
                            line.contains("launch") || line.contains("shoot"))) {
                        if (Completion.updateNumLine(lore, player, 1, i)) {
                            return;
                        }
                    }
                }
            }
        }
    }
}
