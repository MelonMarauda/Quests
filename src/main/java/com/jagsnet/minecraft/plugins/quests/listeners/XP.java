package com.jagsnet.minecraft.plugins.quests.listeners;

import com.jagsnet.minecraft.plugins.quests.otherStuff.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;

import java.util.ArrayList;

public class XP implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onXPGain(PlayerExpChangeEvent event) {
        if (event.getAmount() < 1) { return; }
        Player player = event.getPlayer();
        if (Utils.isScrollOff(player)) {

            ArrayList<String> lore = (ArrayList<String>) player.getInventory().getItemInOffHand().getItemMeta().getLore();

            for (int i = 0; i < lore.size(); i++) {
                String line = Utils.cleanLore(lore.get(i), false, false);
                if ((line.contains("gain") &&
                        line.split(" ", 0).length == 4) &&
                        line.contains("exp")) {
                    if (Utils.updateNumLine(lore, player, event.getAmount(), i)) {
                        continue;
                    }
                }
            }
        }
    }
}
