package com.jagsnet.minecraft.plugins.quests.otherStuff.utils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Inventory {
    public static Boolean space(Player p, int size) {
        int rewardCount = size;
        int count = 0;
        for (int slot = 0; slot < 36; slot++) {
            ItemStack is = p.getInventory().getItem(slot);
            if (is == null) {
                count++;
            }
        }
        if (count < rewardCount) {
            return false;
        }
        return true;
    }
}
