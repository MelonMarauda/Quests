package com.jagsnet.minecraft.plugins.quests.trackers.listeners;

import com.jagsnet.minecraft.plugins.quests.otherStuff.messages.Strings;
import com.jagsnet.minecraft.plugins.quests.otherStuff.utils.Completion;
import com.jagsnet.minecraft.plugins.quests.otherStuff.utils.Scroll;
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
        if (event.getExpToDrop() < 1) { return; }
        if (event.getCaught() != null) {
            if (event.getPlayer() instanceof Player) {
                Player player = event.getPlayer();
                if (Scroll.isScrollOff(player)) {

                    Item item = (Item) event.getCaught();
                    ItemStack is = item.getItemStack();
                    String entity = item.getItemStack().getType().toString().replace("_", " ").toLowerCase();
                    if (is.getItemMeta().hasDisplayName()) {
                        entity = ChatColor.stripColor(is.getItemMeta().getDisplayName()).replace("_", " ").toLowerCase();
                    }
                    ArrayList<String> lore = (ArrayList<String>) player.getInventory().getItemInOffHand().getItemMeta().getLore();

                    for (int i = 0; i < lore.size(); i++) {
                        String line = Strings.cleanLore(lore.get(i), true, false);
                        if ((line.contains(" " + entity) &&
                                line.split(" ", 0).length ==
                                entity.split(" ", 0).length + 3) &&
                                line.contains("catch")) {
                            if (Completion.updateNumLine(lore, player, 1, i)) {
                                return;
                            }
                        } else if (line.contains("fish") &&
                                line.contains("times")) {
                            if (Completion.updateNumLine(lore, player, 1, i)) {
                                return;
                            }
                        }
                    }
                }
            }
        }
    }
}