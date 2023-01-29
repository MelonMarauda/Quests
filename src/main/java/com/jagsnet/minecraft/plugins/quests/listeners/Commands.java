package com.jagsnet.minecraft.plugins.quests.listeners;

import com.jagsnet.minecraft.plugins.quests.otherStuff.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.ArrayList;

public class Commands implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (event.isCancelled()) { return; }
        Player player = event.getPlayer();
        if (Utils.isScrollOff(player)) {

            String entity = event.getMessage().split(" ")[0].replace("_", " ");
            ArrayList<String> lore = (ArrayList<String>) player.getInventory().getItemInOffHand().getItemMeta().getLore();

            for (int i = 0; i < lore.size(); i++) {
                if (((lore.get(i).toLowerCase().contains(entity.toLowerCase())) &&
                        lore.get(i).split(" ", 0).length ==
                        entity.split(" ", 0).length + 4) &&
                        lore.get(i).contains("Command")) {
                    if (Utils.updateTxtLine(lore, player, i, true)) {
                        return;
                    }
                }
            }
        }
    }
}
