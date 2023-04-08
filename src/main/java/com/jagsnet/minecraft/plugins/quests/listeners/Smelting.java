package com.jagsnet.minecraft.plugins.quests.listeners;

import com.jagsnet.minecraft.plugins.quests.otherStuff.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceExtractEvent;

import java.util.ArrayList;

public class Smelting implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onSmelt(FurnaceExtractEvent event) {
        Player player = event.getPlayer();
        if (Utils.isScrollOff(player)) {

            String entity = event.getItemType().toString().split(" ")[0].replace("_", " ").toLowerCase();
            ArrayList<String> lore = (ArrayList<String>) player.getInventory().getItemInOffHand().getItemMeta().getLore();

            for (int i = 0; i < lore.size(); i++) {
                String line = Utils.cleanLore(lore.get(i), false, true);
                if ((line.contains(" " + entity) &&
                        line.split(" ", 0).length ==
                        entity.split(" ", 0).length + 3) &&
                        line.contains("smelt")) {
                    if (Utils.updateNumLine(lore, player, event.getItemAmount(), i)) {
                        return;
                    }
                }
            }
        }
    }
}