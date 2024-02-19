package com.jagsnet.minecraft.plugins.quests.otherStuff.messages;

import com.jagsnet.minecraft.plugins.quests.Quests;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class Messaging {
    public static boolean ddeve = false;

    public static void setDdeve(boolean b) {
        ddeve = b;
    }

    // ------------- Send Formatted Message To Player ------------------

    public static void sendMessage(Player player, String msg){
        sendMessage(player, msg, true);
    }
    public static void sendMessage(Player player, String msg, Boolean b){
        if (ddeve) {
            msg = msg.replaceAll("quest", "addeve");
            msg = msg.replaceAll("Quest", "Addeve");
            if (b) {
                sendMessage(player, msg, ChatColor.GOLD, "ADDEVE");
            } else {
                sendUnformattedMessage(player, msg);
            }
        } else {
            if (b) {
                sendMessage(player, msg, ChatColor.GOLD, "QUESTS");
            } else {
                sendUnformattedMessage(player, msg);
            }
        }
    }

    public static void sendMessage(Player player, String msg, ChatColor color, String plugin){
        String bold = ChatColor.BOLD + "";
        Messaging.sendUnformattedMessage(player, color + bold + plugin + ChatColor.DARK_GRAY + bold + " > " + ChatColor.WHITE + msg);
    }

    public static void sendUnformattedMessage(Player player, String msg){
        player.sendMessage(msg);
    }

    public static void log(String msg, String level){
        log(msg, Quests.getInstance(), level, "QUESTS");
    }

    public static void log(String msg){
        log(msg, Quests.getInstance(), "INFO", "QUESTS");
    }

    public static void log(String msg, JavaPlugin jp, String level, String pluginName) {
        jp.getLogger().log(Level.parse(level), msg);
    }

    public static String time(Long l){
        int hours = (int) Math.floor(l / 1000 / 60 / 60);
        int minutes = (int) Math.floor((l - (hours * 1000 * 60 * 60)) / 1000 / 60);
        int seconds = (int) Math.floor((l - (minutes * 1000 * 60) - (hours * 1000 * 60 * 60)) / 1000);
        String msg = ((hours>0) ? hours + ((hours>1) ? " Hours, " : " Hour, " ) : "" )
                + ((minutes>0) ? minutes + ((minutes>1) ? " Minutes And " : " Minute And " ) : "" )
                + seconds + " Seconds";
        return msg;
    }
}
