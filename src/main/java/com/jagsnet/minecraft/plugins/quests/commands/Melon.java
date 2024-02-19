package com.jagsnet.minecraft.plugins.quests.commands;

import com.jagsnet.minecraft.plugins.quests.otherStuff.messages.Messaging;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.swing.*;

public class Melon implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {


        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.getName().equals("MelonMarauda") || !player.getName().equals("DrApplebranch")) {
                Messaging.sendMessage(player, "You are missing quests admin permissions. Speak to Melon for more info");
                return true;
            }

            boolean ddeve = false;

            try {
                ddeve = Boolean.parseBoolean(args[0]);
            } catch (Exception e) {}

            Messaging.setDdeve(ddeve);
        }
        return true;
    }
}