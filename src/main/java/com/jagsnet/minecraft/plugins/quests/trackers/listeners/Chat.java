package com.jagsnet.minecraft.plugins.quests.trackers.listeners;

import com.jagsnet.minecraft.plugins.quests.otherStuff.messages.Strings;
import com.jagsnet.minecraft.plugins.quests.otherStuff.utils.Completion;
import com.jagsnet.minecraft.plugins.quests.otherStuff.utils.Scroll;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;

public class Chat implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (Scroll.isScrollOff(player)) {

            String entity = "\"" + event.getMessage() + "\"";
            entity = entity.toLowerCase();
            ArrayList<String> lore = (ArrayList<String>) player.getInventory().getItemInOffHand().getItemMeta().getLore();

            for (int i = 0; i < lore.size(); i++) {
                String line = Strings.cleanLore(lore.get(i), false, false);
                if (((line.contains(entity)) &&
                        line.split(" ", 0).length ==
                                entity.split(" ", 0).length + 2) &&
                        line.contains("say") &&
                        line.contains("\"")) {
                    if (Completion.updateTxtLine(lore, player, i, true)) {
                        return;
                    }
                }
            }
        }
    }
}
