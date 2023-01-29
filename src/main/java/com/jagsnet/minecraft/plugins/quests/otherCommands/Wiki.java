package com.jagsnet.minecraft.plugins.quests.otherCommands;

import com.jagsnet.minecraft.plugins.quests.otherStuff.Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Wiki implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;
            p.sendMessage(ChatColor.DARK_GRAY + "");
            p.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "> > " + ChatColor.YELLOW + "Our wiki has FAQs, info about our server and features, and a guide for new players! " +
                    ChatColor.YELLOW + ChatColor.UNDERLINE + "www.applecraft.org/wiki");
            p.sendMessage(ChatColor.DARK_GRAY + "");
        }
        return true;
    }
}
