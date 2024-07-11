package com.jagsnet.minecraft.plugins.quests.trackers.listeners;

import com.jagsnet.minecraft.plugins.quests.Quests;
import com.jagsnet.minecraft.plugins.quests.trackers.scheduledTasks.Coords;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.scheduler.BukkitScheduler;

public class Login implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onLogin(PlayerJoinEvent event) {
        if (event.getPlayer().getName().equalsIgnoreCase("addeve")) {

            BukkitScheduler scheduler = Quests.getInstance().getServer().getScheduler();

            scheduler.scheduleSyncDelayedTask(Quests.getInstance(), new Runnable() {
                @Override
                public void run() {
                    event.getPlayer().sendMessage(" ");
                    event.getPlayer().sendMessage("------------------------------");
                    event.getPlayer().sendMessage(" ");
                    event.getPlayer().sendMessage(ChatColor.DARK_RED + "WARNING WARNING WARNING!!!!!!!!");
                    event.getPlayer().sendMessage("QUESTS PLUGIN UPDATE AVAILABLE");
                    event.getPlayer().sendMessage(ChatColor.RED + "VULNERABILITIES DETECTED");
                    event.getPlayer().sendMessage(" ");
                    event.getPlayer().sendMessage("------------------------------");
                    event.getPlayer().sendMessage(" ");
                }
            }, 5L);
        }

    }
}
