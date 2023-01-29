package com.jagsnet.minecraft.plugins.quests.listeners;

import com.jagsnet.minecraft.plugins.quests.Quests;
import com.jagsnet.minecraft.plugins.quests.otherStuff.Utils;
import org.bukkit.NamespacedKey;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class Trading implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onTrade(InventoryCloseEvent event) {
        if (!event.getInventory().getType().equals(InventoryType.MERCHANT)) { return; }
        if (event.getPlayer() instanceof Player) {
            Player player = (Player) event.getPlayer();
            if (Utils.isScrollOff(player)) {
                ItemMeta offHandItem = player.getInventory().getItemInOffHand().getItemMeta();
                String entity = "times with villagers";
                ArrayList<String> lore = (ArrayList<String>) player.getInventory().getItemInOffHand().getItemMeta().getLore();

                for (int i = 0; i < lore.size(); i++) {
                    if ((lore.get(i).toLowerCase().contains(entity.toLowerCase()) &&
                            lore.get(i).split(" ", 0).length ==
                            entity.split(" ", 0).length + 3) && lore.get(i).contains("Trade")) {
                        NamespacedKey key = new NamespacedKey(Quests.getInstance(), "trades");
                        if (offHandItem.getPersistentDataContainer().has(key, PersistentDataType.INTEGER)) {
                            int itemTrades = offHandItem.getPersistentDataContainer().get(key, PersistentDataType.INTEGER);
                            int playerTrades = player.getStatistic(Statistic.TRADED_WITH_VILLAGER);
                            if (itemTrades < playerTrades) {
                                if (Utils.updateNumLine(lore, player, playerTrades - itemTrades, i)) {
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onOpen(InventoryOpenEvent event) {
        if (!event.getInventory().getType().equals(InventoryType.MERCHANT)) { return; }
        if (event.getPlayer() instanceof Player) {
            Player player = (Player) event.getPlayer();
            if (Utils.isScrollOff(player)) {
                ItemMeta offHandItem = player.getInventory().getItemInOffHand().getItemMeta();
                String entity = "times with villagers";
                ArrayList<String> lore = (ArrayList<String>) player.getInventory().getItemInOffHand().getItemMeta().getLore();

                for (int i = 0; i < lore.size(); i++) {
                    if ((lore.get(i).toLowerCase().contains(entity.toLowerCase()) &&
                            lore.get(i).split(" ", 0).length ==
                            entity.split(" ", 0).length + 3) &&
                            lore.get(i).contains("Trade")) {
                        int trades = player.getStatistic(Statistic.TRADED_WITH_VILLAGER);
                        NamespacedKey key = new NamespacedKey(Quests.getInstance(), "trades");
                        offHandItem.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, trades);
                        ItemStack itemStack = player.getInventory().getItemInOffHand();
                        itemStack.setItemMeta(offHandItem);
                    }
                }
            }
        }
    }
}