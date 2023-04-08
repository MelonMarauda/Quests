package com.jagsnet.minecraft.plugins.quests.listeners;

import com.jagsnet.minecraft.plugins.quests.otherStuff.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;

public class Crafting implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onCraft(CraftItemEvent event){
        if (event.isCancelled()) { return; }

        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            if (Utils.isScrollOff(player)) {
                if (event.isShiftClick()){
                    Utils.sendMessage(player, "You cannot shift click craft while using a quest. Sorry.");
                    return;
                }

                if (event.getClick() == ClickType.NUMBER_KEY){
                    Utils.sendMessage(player, "You cannot use number keys to craft while using a quest. Sorry.");
                    return;
                }


                if ((event.getCursor().getType() != Material.AIR) && (event.getCursor().getMaxStackSize() == event.getCursor().getAmount())) return;
                if ((event.getCursor().getType() != event.getRecipe().getResult().getType()) && (event.getCursor().getType() != Material.AIR)) return;
                if (event.getClick() == ClickType.DROP && event.getCursor().getType() != Material.AIR) return;

                ItemStack stack = event.getRecipe().getResult().clone();
                String itemName = stack.getType().toString();
                int recipeAmount = stack.getAmount();
                if (recipeAmount == 0) return;
                itemName = itemName.replace("_", " ").toLowerCase();
                event.getResult();
                ArrayList<String> lore = (ArrayList<String>) player.getInventory().getItemInOffHand().getItemMeta().getLore();

                if ((stack.getAmount() + event.getCursor().getAmount()) > event.getCursor().getMaxStackSize()&& event.getCursor().getType() != Material.AIR) return;

                /*
                int itemsChecked = 0;
                int possibleCreations = 1;
                if (event.isShiftClick()) {
                    for (ItemStack item : event.getInventory().getMatrix()) {
                        if (item != null && !item.getType().equals(Material.AIR)) {
                            System.out.println(item.toString());
                            if (itemsChecked == 0)
                                possibleCreations = item.getAmount();
                            else
                                possibleCreations = Math.min(possibleCreations, item.getAmount());
                            itemsChecked++;
                        }
                    }
                    int freeSpace = invSpace(player.getInventory(), event.getRecipe().getResult().getType());
                    recipeAmount = event.getRecipe().getResult().getAmount() * possibleCreations;
                    if (freeSpace < recipeAmount) {
                        recipeAmount = freeSpace;
                    }
                }
                Utils.sendMessage(player, String.valueOf(recipeAmount));
                Utils.sendMessage(player, event.getCursor().getType().name());
                 */

                for (int i = 0; i < lore.size(); i++) {
                    String line = Utils.cleanLore(lore.get(i), false, true);
                    if ((line.contains(" " + itemName) &&
                            line.split(" ", 0).length ==
                            itemName.split(" ", 0).length + 3) &&
                            (line.contains("craft") || line.contains("make"))) {
                        if (Utils.updateNumLine(lore, player, recipeAmount, i)) {
                            return;
                        }
                    }
                }
            }
        }
    }

    public int invSpace (PlayerInventory inv, Material m) {
        int count = 0;
        for (int slot = 0; slot < 36; slot ++) {
            ItemStack is = inv.getItem(slot);
            if (is == null) {
                count += m.getMaxStackSize();
            }
            if (is != null) {
                if (is.getType() == m){
                    count += (m.getMaxStackSize() - is.getAmount());
                }
            }
        }
        return count;
    }
}
