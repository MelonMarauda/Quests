package com.jagsnet.minecraft.plugins.quests.trackers.listeners;

import com.jagsnet.minecraft.plugins.quests.otherStuff.messages.Strings;
import com.jagsnet.minecraft.plugins.quests.otherStuff.utils.Completion;
import com.jagsnet.minecraft.plugins.quests.otherStuff.utils.Scroll;
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
            if (Scroll.isScrollOff(player)) {

                String entity = event.getEntityType().toString().replace("_", " ").toLowerCase();
                int entitySize = entity.split(" ", 0).length;
                ArrayList<String> lore = (ArrayList<String>) player.getInventory().getItemInOffHand().getItemMeta().getLore();

                for (int i = 0; i < lore.size(); i++) {
                    String line = Strings.cleanLore(lore.get(i), true, false);
                    if (line.contains(" " + entity) &&
                            line.split(" ", 0).length == entitySize + 3 &&
                            line.contains("breed")) {
                        if (Completion.updateNumLine(lore, player, 1, i)) {
                            return;
                        }
                    }
                }
            }
        }
    }
}
