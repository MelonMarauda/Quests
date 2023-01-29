package com.jagsnet.minecraft.plugins.quests.commands;

import com.jagsnet.minecraft.plugins.quests.otherStuff.Utils;
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
                Utils.sendMessage(p, "You are missing quests admin permissions. Speak to Melon for more info");
                return true;
            }

            Utils.sendMessage(p, " ");
            Utils.sendMessage(p, "Nature commands -----");
            Utils.sendMessage(p, "/nature - Main nature plugin commands");
            Utils.sendMessage(p, "/trees - Tree generator commands");
            Utils.sendMessage(p, "/vines - Vine generator commands");
            Utils.sendMessage(p, " ");
            Utils.sendMessage(p, "Dragon Commands -----");
            Utils.sendMessage(p, "/dragon - Player dragon commands");
            Utils.sendMessage(p, "/adragon - Admin dragon commands");
            Utils.sendMessage(p, " ");
            Utils.sendMessage(p, "Quest Commands -----");
            Utils.sendMessage(p, "/quest - Player quest commands");
            Utils.sendMessage(p, "/aquest - Admin quest commands");
            Utils.sendMessage(p, "/gquest - Quest generator commands");
            Utils.sendMessage(p, "/cquest - Quest completer commands");
            Utils.sendMessage(p, " ");

        }
        return true;
    }
}
