package com.jagsnet.minecraft.plugins.quests.listeners;

import com.jagsnet.minecraft.plugins.quests.otherStuff.Utils;
import org.bukkit.ChatColor;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class Fishing implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onFish(PlayerFishEvent event) {
        if (event.isCancelled()) { return; }
        if (event.getCaught() != null) {
            if (event.getPlayer() instanceof Player) {
                Player player = event.getPlayer();
                if (Utils.isScrollOff(player)) {

                    Item item = (Item) event.getCaught();
                    ItemStack is = item.getItemStack();
                    String entity = item.getItemStack().getType().toString().replace("_", " ");
                    if (is.getItemMeta().hasDisplayName()) {
                        entity = ChatColor.stripColor(is.getItemMeta().getDisplayName()).replace("_", " ");
                    }
                    ArrayList<String> lore = (ArrayList<String>) player.getInventory().getItemInOffHand().getItemMeta().getLore();

                    for (int i = 0; i < lore.size(); i++) {
                        if ((lore.get(i).toLowerCase().contains(entity.toLowerCase()) &&
                                lore.get(i).split(" ", 0).length ==
                                entity.split(" ", 0).length + 3) &&
                                lore.get(i).contains("Catch")) {
                            if (Utils.updateNumLine(lore, player, 1, i)) {
                                return;
                            }
                        } else if (lore.get(i).contains("Fish") &&
                                lore.get(i).contains("Times")) {
                            if (Utils.updateNumLine(lore, player, 1, i)) {
                                return;
                            }
                        }
                    }
                }
            }
        }
    }
}