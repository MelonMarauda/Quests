package com.jagsnet.minecraft.plugins.quests.trackers.listeners;

import com.jagsnet.minecraft.plugins.quests.otherStuff.messages.Strings;
import com.jagsnet.minecraft.plugins.quests.otherStuff.utils.Completion;
import com.jagsnet.minecraft.plugins.quests.otherStuff.utils.Scroll;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import java.util.ArrayList;

public class Eating implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onEat(PlayerItemConsumeEvent event) {
        if (event.isCancelled()) { return; }
        if (event.getPlayer() instanceof Player) {
            Player player = event.getPlayer();
            if (Scroll.isScrollOff(player)) {

                String entity = event.getItem().getType().toString().replace("_", " ").toLowerCase();
                ArrayList<String> lore = (ArrayList<String>) player.getInventory().getItemInOffHand().getItemMeta().getLore();

                for (int i = 0; i < lore.size(); i++) {
                    String line = Strings.cleanLore(lore.get(i), false, true);
                    if ((line.contains(" " + entity) &&
                            line.split(" ", 0).length ==
                            entity.split(" ", 0).length + 3) &&
                            (line.contains("eat") || line.contains("drink"))) {
                        if (Completion.updateNumLine(lore, player, 1, i)) {
                            return;
                        }
                    }
                }
            }
        }
    }
}