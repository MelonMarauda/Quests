package com.jagsnet.minecraft.plugins.quests.otherStuff.utils;

import org.bukkit.Bukkit;

public class Perms {
    public static void setPerm(String player, String perm, String time) {
        if (Configs.getGlobal().get("permissions") == null) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player + " permission settemp " + perm + " true " + time);
            return;
        }
        String command = Configs.getGlobal().getString("permissions.set");
        while (command.contains("<player>")) command.replace("<player>", player);
        while (command.contains("<permission>")) command.replace("<permission>", perm);
        while (command.contains("<time>")) command.replace("<time>", time);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        return;
    }
}
