package com.jagsnet.minecraft.plugins.quests.commands;

import com.jagsnet.minecraft.plugins.quests.otherStuff.messages.Messaging;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Help implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {

            Player p = (Player) sender;
            if (!p.hasPermission("quests.admin")) {
                Messaging.sendMessage(p, "You are missing quests admin permissions. Speak to Melon for more info");
                return true;
            }

            Messaging.sendMessage(p, " ");
            Messaging.sendMessage(p, "Nature commands -----");
            Messaging.sendMessage(p, "/nature - Main nature plugin commands");
            Messaging.sendMessage(p, "/trees - Tree generator commands");
            Messaging.sendMessage(p, "/vines - Vine generator commands");
            Messaging.sendMessage(p, " ");
            Messaging.sendMessage(p, "Dragon Commands -----");
            Messaging.sendMessage(p, "/dragon - Player dragon commands");
            Messaging.sendMessage(p, "/adragon - Admin dragon commands");
            Messaging.sendMessage(p, " ");
            Messaging.sendMessage(p, "Quest Commands -----");
            Messaging.sendMessage(p, "/quest - Player quest commands");
            Messaging.sendMessage(p, "/aquest - Admin quest commands");
            Messaging.sendMessage(p, "/gquest - Quest generator commands");
            Messaging.sendMessage(p, "/cquest - Quest completer commands");
            Messaging.sendMessage(p, " ");

        }
        return true;
    }
}
