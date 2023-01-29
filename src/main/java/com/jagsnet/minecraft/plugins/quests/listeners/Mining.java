package com.jagsnet.minecraft.plugins.quests.listeners;

import com.jagsnet.minecraft.plugins.quests.otherStuff.Utils;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.block.data.Ageable;

import java.util.ArrayList;

public class Mining implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDeath(BlockBreakEvent event) {
        if (event.isCancelled()) { return; }
        if (event.getPlayer() instanceof Player) {
            Player player = event.getPlayer();
            if (Utils.isScrollOff(player)) {

                if(event.getBlock().getBlockData() instanceof Ageable) {
                    Ageable crop = (Ageable) event.getBlock().getBlockData();
                    if (crop.getMaximumAge() != crop.getAge()) {
                        return;
                    }
                }

                String entity = event.getBlock().getType().toString().replace("_", " ");
                ArrayList<String> lore = (ArrayList<String>) player.getInventory().getItemInOffHand().getItemMeta().getLore();

                if (entity.contains("DEEPSLATE ") && entity.contains("ORE")) {
                    entity = entity.replace("DEEPSLATE ", "");
                }

                for (int i = 0; i < lore.size(); i++) {
                    if ((lore.get(i).toLowerCase().contains(entity.toLowerCase()) &&
                            lore.get(i).split(" ", 0).length ==
                            entity.split(" ", 0).length + 3) &&
                            (lore.get(i).contains("Mine") || lore.get(i).contains("Dig") ||
                            lore.get(i).contains("Harvest") || lore.get(i).contains("Chop") ||
                            lore.get(i).contains("Break") || lore.get(i).contains("Pick"))) {
                        if (player.getInventory().getItemInMainHand().getItemMeta() != null) {
                            if (player.getInventory().getItemInMainHand().getItemMeta().hasEnchant(Enchantment.SILK_TOUCH)) {
                                Utils.sendMessage(player, "You cannot use silk touch items while completing quests");
                                return;
                            }
                        }
                        if (Utils.updateNumLine(lore, player, 1, i)) {
                            return;
                        }
                    }
                }
            }
        }
    }
}