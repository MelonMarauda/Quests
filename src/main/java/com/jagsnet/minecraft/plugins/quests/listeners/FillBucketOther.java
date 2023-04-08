package com.jagsnet.minecraft.plugins.quests.listeners;

import com.jagsnet.minecraft.plugins.quests.otherStuff.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketFillEvent;

import java.util.ArrayList;

public class FillBucketOther implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onBucketFill(PlayerBucketFillEvent event) {
        if (event.isCancelled()) { return; }
        Player player = event.getPlayer();
        if (Utils.isScrollOff(player)) {

            String entity = event.getBlock().getType().toString().replace("_", " ").toLowerCase();
            if (event.getItemStack().getType() == Material.MILK_BUCKET) {
                entity = "milk";
            }
            ArrayList<String> lore = (ArrayList<String>) player.getInventory().getItemInOffHand().getItemMeta().getLore();

            for (int i = 0; i < lore.size(); i++) {
                String line = Utils.cleanLore(lore.get(i), false, false);
                if ((line.contains(" " + entity) &&
                        line.split(" ", 0).length ==
                                entity.split(" ", 0).length + 5) && line.contains("fill") && line.contains("buckets with")) {
                    if (Utils.updateNumLine(lore, player, 1, i)) {
                        return;
                    }
                }
            }
        }
    }
}
