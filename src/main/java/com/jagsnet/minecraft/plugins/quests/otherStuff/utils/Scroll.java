package com.jagsnet.minecraft.plugins.quests.otherStuff.utils;

import com.jagsnet.minecraft.plugins.quests.Quests;
import com.jagsnet.minecraft.plugins.quests.otherStuff.messages.Messaging;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class Scroll {
    // ------------- Scroll Check For Commands -------------------------
    public static boolean isScrollMain(Player p) {
        return isScroll(p, p.getInventory().getItemInMainHand());
    }

    // ------------- Scroll Check For Listeners ------------------------
    public static boolean isScrollOff(Player p) {
        return isScroll(p, p.getInventory().getItemInOffHand());
    }

    public static boolean isScroll(Player p, ItemStack i) {
        ItemMeta iM = i.getItemMeta();
        try {
            if (!i.getType().equals(Material.PAPER)) {
                return false;
            }
        } catch (NullPointerException e) {
            return false;
        }
        if (i == null) {
            return false;
        }
        if (!iM.hasLore()) {
            return false;
        }
        if (iM.getLore().get(0).contains("Quest Path") || iM.getLore().get(0).contains("Quest Scroll") || iM.getLore().get(0).contains("Tracker")) {
            if (i.getAmount() > 1) {
                Messaging.sendMessage(p, "You may only use one quest scroll at a time");
                return false;
            }
            NamespacedKey key = new NamespacedKey(Quests.getInstance(), "UUID");
            if (iM.getPersistentDataContainer().has(key, PersistentDataType.STRING)
                    && !p.hasPermission("quests.admin")) {
                String UUID = iM.getPersistentDataContainer().get(key, PersistentDataType.STRING);
                if (!UUID.equals(p.getUniqueId().toString())) {
                    Messaging.sendMessage(p, "You may only use this scroll if it was assigned to you.");
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
