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

            String entity = event.getBlock().getType().toString().replace("_", " ");
            if (event.getItemStack().getType() == Material.MILK_BUCKET) {
                entity = "Milk";
            }
            ArrayList<String> lore = (ArrayList<String>) player.getInventory().getItemInOffHand().getItemMeta().getLore();

            for (int i = 0; i < lore.size(); i++) {
                if ((lore.get(i).toLowerCase().contains(entity.toLowerCase()) &&
                        lore.get(i).split(" ", 0).length ==
                                entity.split(" ", 0).length + 5) && lore.get(i).contains("Fill") && lore.get(i).contains("Buckets With")) {
                    if (Utils.updateNumLine(lore, player, 1, i)) {
                        return;
                    }
                }
            }
        }
    }
}
