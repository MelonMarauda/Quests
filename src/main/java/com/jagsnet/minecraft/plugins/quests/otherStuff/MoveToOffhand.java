package com.jagsnet.minecraft.plugins.quests.otherStuff;

import com.sk89q.worldedit.event.platform.InputType;
import com.sk89q.worldedit.event.platform.PlayerInputEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class MoveToOffhand implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onRightClick(PlayerInteractEvent e) {
        if (!e.getAction().toString().contains("RIGHT")) { return; }
        Player p = e.getPlayer();
        if (Utils.isScrollMain(p) && p.getInventory().getItemInOffHand().getType() == Material.AIR) {
            p.getInventory().setItemInOffHand(p.getInventory().getItemInMainHand());
            p.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
        }
    }
}
