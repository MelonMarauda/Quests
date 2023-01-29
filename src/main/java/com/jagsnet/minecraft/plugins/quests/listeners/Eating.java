package com.jagsnet.minecraft.plugins.quests.listeners;

import com.jagsnet.minecraft.plugins.quests.otherStuff.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import java.util.ArrayList;

public class Eating implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onEat(PlayerItemConsumeEvent event) {
        if (event.isCancelled()) { return; }
        if (event.getPlayer() instanceof Player) {
            Player player = event.getPlayer();
            if (Utils.isScrollOff(player)) {

                String entity = event.getItem().getType().toString().replace("_", " ");
                ArrayList<String> lore = (ArrayList<String>) player.getInventory().getItemInOffHand().getItemMeta().getLore();

                for (int i = 0; i < lore.size(); i++) {
                    if ((lore.get(i).toLowerCase().contains(entity.toLowerCase()) &&
                            lore.get(i).split(" ", 0).length ==
                            entity.split(" ", 0).length + 3) &&
                            (lore.get(i).contains("Eat") || lore.get(i).contains("Drink"))) {
                        if (Utils.updateNumLine(lore, player, 1, i)) {
                            return;
                        }
                    }
                }
            }
        }
    }
}