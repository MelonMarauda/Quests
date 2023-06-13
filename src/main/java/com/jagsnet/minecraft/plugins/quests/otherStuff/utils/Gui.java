package com.jagsnet.minecraft.plugins.quests.otherStuff.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;

public class Gui implements Listener {
    private final Inventory inv;

    JavaPlugin plugin;

    public Gui(ArrayList<ItemStack> items, String title, JavaPlugin pl) {
        // Create a new inventory, with no owner (as this isn't a real inventory), a size of nine, called example
        int size = 9;
        plugin = pl;

        if (items.size() > 9) size = 18;
        if (items.size() > 18) size = 27;
        if (items.size() > 27) size = 36;

        inv = Bukkit.createInventory(null, size, title);

        // Put the items into the inventory
        initializeItems(items);
    }

    // You can call this whenever you want to put the items in
    public void initializeItems(ArrayList<ItemStack> items) {
        for (ItemStack is : items) {
            inv.addItem(is);
        }
    }

    // Nice little method to create a gui item with a custom name, and description
    public static ItemStack createGuiItem(JavaPlugin pl, final Material material, final String name, final String command, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        // Set the name of the item
        meta.setDisplayName(name);

        // Set the lore of the item
        meta.setLore(Arrays.asList(lore));

        NamespacedKey key = new NamespacedKey(pl, "command");
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, command);

        item.setItemMeta(meta);

        return item;
    }

    // You can open the inventory with this
    public void openInventory(final HumanEntity ent) {
        ent.openInventory(inv);
    }

    // Check for clicks on items
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (!e.getInventory().equals(inv)) return;

        e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();

        // verify current item is not null
        if (clickedItem == null || clickedItem.getType().isAir()) return;

        final Player p = (Player) e.getWhoClicked();

        NamespacedKey key = new NamespacedKey(plugin, "command");
        String command = clickedItem.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING);
        if (command.equals("")) return;

        // Using slots click is a best option for your inventory click's
        for (String s : command.split("&")) {
            p.performCommand(s);
        }
    }

    // Cancel dragging in our inventory
    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (e.getInventory().equals(inv)) {
            e.setCancelled(true);
        }
    }
}
