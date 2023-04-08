package com.jagsnet.minecraft.plugins.quests.listeners;

import com.jagsnet.minecraft.plugins.quests.Quests;
import com.jagsnet.minecraft.plugins.quests.otherStuff.Utils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class Strange implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onRight(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        if (event.getHand() == EquipmentSlot.OFF_HAND) return;
        if (Utils.isScrollOff(player)) {
            NamespacedKey key = new NamespacedKey(Quests.getInstance(), "strangeCooldown");
            if (event.getRightClicked().getPersistentDataContainer().has(key, PersistentDataType.LONG)) {
                if (event.getRightClicked().getPersistentDataContainer().get(key, PersistentDataType.LONG) > System.currentTimeMillis()) {
                    return;
                }
            }

            String mainHandItem = event.getPlayer().getInventory().getItemInMainHand().getType().toString().toLowerCase();
            String entity = event.getRightClicked().getType().toString().toLowerCase();

            if (mainHandItem.equalsIgnoreCase("bucket")) mainHandItem = "milk";
            if (mainHandItem.equals("milk") && player.getInventory().getItemInMainHand().getAmount() > 1) {
                Utils.sendMessage(player, "You can't use more than one bucket at a time. I know, a pain, sorry.");
                return;
            }
            if (mainHandItem.equalsIgnoreCase("shears")) mainHandItem = "shear";

            ArrayList<String> lore = (ArrayList<String>) player.getInventory().getItemInOffHand().getItemMeta().getLore();
            int length = 2 + mainHandItem.split(" ", 0).length + entity.split(" ", 0).length;

            entity = entity.toLowerCase();

            for (int i = 0; i < lore.size(); i++) {
                String line = Utils.cleanLore(lore.get(i), true, false);
                if ((line.contains(mainHandItem) &&
                        line.split(" ", 0).length == (length)) &&
                        line.contains(" " + entity)) {
                    if (Utils.updateNumLine(lore, player, 1, i)) {
                        if (mainHandItem.equalsIgnoreCase("milk") && entity.equalsIgnoreCase("cow")) return;
                        event.getRightClicked().getPersistentDataContainer().set(key, PersistentDataType.LONG, System.currentTimeMillis() + 30000);

                        String b = ChatColor.BOLD + "";

                        ItemStack is;
                        ItemMeta im;
                        switch (mainHandItem) {
                            case "shear":
                                is  = new ItemStack(Material.WHITE_WOOL);
                                im = is.getItemMeta();
                                im.setDisplayName(b + StringUtils.capitalize(entity) + " Wool");
                                is.setItemMeta(im);
                                event.getRightClicked().getWorld().dropItem(event.getRightClicked().getLocation(), is);
                                break;
                            case "milk":
                                is  = new ItemStack(Material.MILK_BUCKET);
                                im = is.getItemMeta();
                                im.setDisplayName(b + StringUtils.capitalize(entity) + " Milk");
                                is.setItemMeta(im);
                                player.getInventory().setItemInMainHand(is);
                                break;
                            default: break;
                        }
                        return;
                    }
                }
            }
        }
    }
}
