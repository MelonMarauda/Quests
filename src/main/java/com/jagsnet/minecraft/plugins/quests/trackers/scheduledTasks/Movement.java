package com.jagsnet.minecraft.plugins.quests.trackers.scheduledTasks;

import com.jagsnet.minecraft.plugins.quests.Quests;
import com.jagsnet.minecraft.plugins.quests.otherStuff.messages.Strings;
import com.jagsnet.minecraft.plugins.quests.otherStuff.utils.Completion;
import com.jagsnet.minecraft.plugins.quests.otherStuff.utils.Scroll;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class Movement {
    public static void trackBiome(){
        for(Player p : Bukkit.getOnlinePlayers()){
            if (Scroll.isScrollOff(p)) {

                String entity = p.getLocation().getBlock().getBiome().name().replace("_", " ");
                ArrayList<String> lore = (ArrayList<String>) p.getInventory().getItemInOffHand().getItemMeta().getLore();
                
                for (int i = 0; i < lore.size(); i++) {
                    if ((lore.get(i).toLowerCase().contains("visit the " + entity.toLowerCase()) &&
                            (lore.get(i).split(" ", 0).length == entity.split(" ", 0).length + 4)) &&
                            !lore.get(i).contains("Complete")) {
                        if (Completion.updateTxtLine(lore, p, i, true)) {
                        }
                    }
                }
            }
        }
    }

    public static void trackMovement(){
        for(Player p : Bukkit.getOnlinePlayers()){
            if (Scroll.isScrollOff(p)) {
                ItemMeta meta = p.getInventory().getItemInOffHand().getItemMeta();
                NamespacedKey key = new NamespacedKey(Quests.getInstance(), "movement");
                if (!meta.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
                    continue;
                }
                String[] stats = meta.getPersistentDataContainer().get(key, PersistentDataType.STRING).split(",");
                key = new NamespacedKey(Quests.getInstance(), "mUUID");
                if (!meta.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
                    meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, p.getUniqueId().toString());
                    ItemStack itemStack = p.getInventory().getItemInOffHand();
                    itemStack.setItemMeta(meta);
                    continue;
                }
                if (!meta.getPersistentDataContainer().get(key, PersistentDataType.STRING).equals(p.getUniqueId().toString())) {
                    meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, p.getUniqueId().toString());
                    for (String s : stats) {
                        switch (s) {
                            case "cake":
                                key = new NamespacedKey(Quests.getInstance(), "cake");
                                meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, p.getStatistic(Statistic.CAKE_SLICES_EATEN));
                                break;
                            case "shield":
                                key = new NamespacedKey(Quests.getInstance(), "shield");
                                meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, p.getStatistic(Statistic.DAMAGE_BLOCKED_BY_SHIELD));
                                break;
                            case "climb":
                                key = new NamespacedKey(Quests.getInstance(), "climb");
                                meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, p.getStatistic(Statistic.CLIMB_ONE_CM));
                                break;
                            case "sneak":
                                key = new NamespacedKey(Quests.getInstance(), "sneak");
                                meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, p.getStatistic(Statistic.CROUCH_ONE_CM));
                                break;
                            case "fall":
                                key = new NamespacedKey(Quests.getInstance(), "fall");
                                meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, p.getStatistic(Statistic.FALL_ONE_CM));
                                break;
                            case "sprint":
                                key = new NamespacedKey(Quests.getInstance(), "sprint");
                                meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, p.getStatistic(Statistic.SPRINT_ONE_CM));
                                break;
                            case "swim":
                                key = new NamespacedKey(Quests.getInstance(), "swim");
                                meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, p.getStatistic(Statistic.SWIM_ONE_CM));
                                break;
                            case "walk":
                                key = new NamespacedKey(Quests.getInstance(), "walk");
                                meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, p.getStatistic(Statistic.WALK_ONE_CM));
                                break;
                            case "boat":
                                key = new NamespacedKey(Quests.getInstance(), "boat");
                                meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, p.getStatistic(Statistic.BOAT_ONE_CM));
                                break;
                            case "fly":
                                key = new NamespacedKey(Quests.getInstance(), "fly");
                                meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, p.getStatistic(Statistic.FLY_ONE_CM));
                                break;
                            case "horse":
                                key = new NamespacedKey(Quests.getInstance(), "horse");
                                meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, p.getStatistic(Statistic.HORSE_ONE_CM));
                                break;
                            case "minecart":
                                key = new NamespacedKey(Quests.getInstance(), "minecart");
                                meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, p.getStatistic(Statistic.MINECART_ONE_CM));
                                break;
                            case "pig":
                                key = new NamespacedKey(Quests.getInstance(), "pig");
                                meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, p.getStatistic(Statistic.PIG_ONE_CM));
                                break;
                            case "strider":
                                key = new NamespacedKey(Quests.getInstance(), "strider");
                                meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, p.getStatistic(Statistic.STRIDER_ONE_CM));
                                break;
                            case "jump":
                                key = new NamespacedKey(Quests.getInstance(), "jump");
                                meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, p.getStatistic(Statistic.JUMP));
                                break;
                            case "ring":
                                key = new NamespacedKey(Quests.getInstance(), "ring");
                                meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, p.getStatistic(Statistic.BELL_RING));
                                break;
                            case "break":
                                key = new NamespacedKey(Quests.getInstance(), "break");
                                meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, p.getStatistic(Statistic.BREAK_ITEM));
                                break;
                            case "target":
                                key = new NamespacedKey(Quests.getInstance(), "target");
                                meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, p.getStatistic(Statistic.TARGET_HIT));
                                break;
                            case "raids":
                                key = new NamespacedKey(Quests.getInstance(), "raids");
                                meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, p.getStatistic(Statistic.RAID_TRIGGER));
                                break;
                            case "raidc":
                                key = new NamespacedKey(Quests.getInstance(), "raidc");
                                meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, p.getStatistic(Statistic.RAID_WIN));
                                break;
                            default: break;
                        }
                    }
                    ItemStack itemStack = p.getInventory().getItemInOffHand();
                    itemStack.setItemMeta(meta);
                    continue;
                }
                String entity = "Test";
                String entity2 = "Test";

                for (String s : stats) {

                    boolean setStat = false;
                    boolean notTrack = false;
                    int statS = 0;
                    int statP = 0;
                    int statD = 0;
                    switch (s) {
                        case "cake":
                            setStat(p, "cake", meta, Statistic.CAKE_SLICES_EATEN, "eat", "slices of cake", 1);
                            break;
                        case "shield":
                            setStat(p, "shield", meta, Statistic.DAMAGE_BLOCKED_BY_SHIELD, "block", "damage with a shield", 1);
                            break;
                        case "climb":
                            setStat(p, "climb", meta, Statistic.CLIMB_ONE_CM, "climb", "blocks", 100);
                            break;
                        case "sneak":
                            setStat(p, "sneak", meta, Statistic.CROUCH_ONE_CM, "sneak", "blocks", 100);
                            break;
                        case "fall":
                            setStat(p, "fall", meta, Statistic.FALL_ONE_CM, "fall", "blocks", 100);
                            break;
                        case "sprint":
                            setStat(p, "sprint", meta, Statistic.SPRINT_ONE_CM, "sprint", "blocks", 100);
                            break;
                        case "swim":
                            setStat(p, "swim", meta, Statistic.SWIM_ONE_CM, "swim", "blocks", 100);
                            break;
                        case "walk":
                            setStat(p, "walk", meta, Statistic.WALK_ONE_CM, "walk", "blocks", 100);
                            break;
                        case "boat":
                            setStat(p, "boat", meta, Statistic.BOAT_ONE_CM, "travel", "blocks in a boat", 100);
                            break;
                        case "fly":
                            setStat(p, "fly", meta, Statistic.AVIATE_ONE_CM, "fly", "blocks", 200);
                            break;
                        case "horse":
                            setStat(p, "horse", meta, Statistic.HORSE_ONE_CM, "travel", "blocks on a horse", 100);
                            break;
                        case "minecart":
                            setStat(p, "minecart", meta, Statistic.MINECART_ONE_CM, "travel", "blocks in a minecart", 100);
                            break;
                        case "pig":
                            setStat(p, "pig", meta, Statistic.PIG_ONE_CM, "travel", "blocks on a pig", 100);
                            break;
                        case "strider":
                            setStat(p, "strider", meta, Statistic.STRIDER_ONE_CM, "travel", "blocks on a strider", 1000);
                            break;
                        case "jump":
                            setStat(p, "jump", meta, Statistic.JUMP, "jump", "time", 1);
                            break;
                        case "ring":
                            setStat(p, "ring", meta, Statistic.BELL_RING, "ring", "bell", 1);
                            break;
                        case "break":
                            setStat(p, "break", meta, Statistic.BREAK_ITEM, "break", "item", 1);
                            break;
                        case "target":
                            setStat(p, "target", meta, Statistic.TARGET_HIT, "hit", "target", 1);
                            break;
                        case "raids":
                            setStat(p, "raids", meta, Statistic.RAID_TRIGGER, "trigger", "raid", 1);
                            break;
                        case "raidc":
                            setStat(p, "raidc", meta, Statistic.RAID_WIN, "beat", "raid", 1);
                            break;
                        default: break;
                    }
                }
            }
        }
    }

    public static void setStat(Player p, String k, ItemMeta meta, Statistic stat, String e1, String e2, int d) {
        boolean setStat = false;
        boolean notTrack = false;
        int statD = 0;
        NamespacedKey key = new NamespacedKey(Quests.getInstance(), k);
        if (!meta.getPersistentDataContainer().has(key, PersistentDataType.INTEGER)) {
            meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, p.getStatistic(stat));
            setStat = true; notTrack = true;
        }
        if (!setStat && !notTrack) {
            int statS = meta.getPersistentDataContainer().get(key, PersistentDataType.INTEGER);
            int statP = p.getStatistic(stat);
            if (statP <= statS) return;
            statD = (statP - statS) / d;
            meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, p.getStatistic(stat));
            setStat = true;
        }

        if (setStat) {
            ItemStack itemStack = p.getInventory().getItemInOffHand();
            itemStack.setItemMeta(meta);
            if (notTrack) {
                return;
            }
        }
        ArrayList<String> lore = (ArrayList<String>) p.getInventory().getItemInOffHand().getItemMeta().getLore();

        for (int i = 0; i < lore.size(); i++) {
            String line = Strings.cleanLore(lore.get(i), false, false);
            if (line.contains(e1) && line.contains(e2)) {
                if (Completion.updateNumLine(lore, p, statD, i)) {
                    return;
                }
            }
        }
    }
}