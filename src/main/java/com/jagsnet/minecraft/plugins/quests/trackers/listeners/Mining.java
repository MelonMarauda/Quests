package com.jagsnet.minecraft.plugins.quests.trackers.listeners;

import com.jagsnet.minecraft.plugins.quests.otherStuff.messages.Messaging;
import com.jagsnet.minecraft.plugins.quests.otherStuff.messages.Strings;
import com.jagsnet.minecraft.plugins.quests.otherStuff.utils.Completion;
import com.jagsnet.minecraft.plugins.quests.otherStuff.utils.Scroll;
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
            if (Scroll.isScrollOff(player)) {

                if(event.getBlock().getBlockData() instanceof Ageable) {
                    Ageable crop = (Ageable) event.getBlock().getBlockData();
                    if (crop.getMaximumAge() != crop.getAge()) {
                        return;
                    }
                }

                String entity = event.getBlock().getType().toString().replace("_", " ").toLowerCase();
                ArrayList<String> lore = (ArrayList<String>) player.getInventory().getItemInOffHand().getItemMeta().getLore();

                if (entity.contains("deepslate ") && entity.contains("ore")) {
                    entity = entity.replace("deepslate ", "");
                }

                for (int i = 0; i < lore.size(); i++) {
                    String line = Strings.cleanLore(lore.get(i), false, true);
                    if ((line.contains(" " + entity) &&
                            line.split(" ", 0).length ==
                            entity.split(" ", 0).length + 3) &&
                            (line.contains("mine") || line.contains("dig") ||
                            line.contains("harvest") || line.contains("chop") ||
                            line.contains("break") || line.contains("pick"))) {
                        if (player.getInventory().getItemInMainHand().getItemMeta() != null) {
                            if (player.getInventory().getItemInMainHand().getItemMeta().hasEnchant(Enchantment.SILK_TOUCH)) {
                                Messaging.sendMessage(player, "You cannot use silk touch items while completing quests");
                                return;
                            }
                        }
                        if (Completion.updateNumLine(lore, player, 1, i)) {
                            return;
                        }
                    }
                }
            }
        }
    }
}