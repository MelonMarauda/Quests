package com.jagsnet.minecraft.plugins.quests.scheduledTasks;

import com.jagsnet.minecraft.plugins.quests.Quests;
import com.jagsnet.minecraft.plugins.quests.otherStuff.Utils;
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
            if (Utils.isScrollOff(p)) {

                String entity = p.getLocation().getBlock().getBiome().name().replace("_", " ");
                ArrayList<String> lore = (ArrayList<String>) p.getInventory().getItemInOffHand().getItemMeta().getLore();
                
                for (int i = 0; i < lore.size(); i++) {
                    if ((lore.get(i).toLowerCase().contains("visit the " + entity.toLowerCase()) &&
                            (lore.get(i).split(" ", 0).length == entity.split(" ", 0).length + 4)) &&
                            !lore.get(i).contains("Complete")) {
                        if (Utils.updateTxtLine(lore, p, i, true)) {
                        }
                    }
                }
            }
        }
    }

    public static void trackMovement(){
        for(Player p : Bukkit.getOnlinePlayers()){
            if (Utils.isScrollOff(p)) {
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
                            default: break;
                        }
                    }
                    ItemStack itemStack = p.getInventory().getItemInOffHand();
                    itemStack.setItemMeta(meta);
                    continue;
                }
                String entity = "Test";
                String entity2 = "Test";
                ArrayList<String> lore = (ArrayList<String>) p.getInventory().getItemInOffHand().getItemMeta().getLore();

                for (String s : stats) {

                    boolean setStat = false;
                    boolean notTrack = false;
                    int statS = 0;
                    int statP = 0;
                    int statD = 0;
                    switch (s) {
                        case "cake":
                            key = new NamespacedKey(Quests.getInstance(), "cake");
                            if (!meta.getPersistentDataContainer().has(key, PersistentDataType.INTEGER)) {
                                meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, p.getStatistic(Statistic.CAKE_SLICES_EATEN));
                                setStat = true; notTrack = true;
                                break;
                            }
                            statS = meta.getPersistentDataContainer().get(key, PersistentDataType.INTEGER);
                            statP = p.getStatistic(Statistic.CAKE_SLICES_EATEN);
                            if (statP <= statS) break;
                            statD = statP - statS;
                            meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, p.getStatistic(Statistic.CAKE_SLICES_EATEN));
                            setStat = true;
                            entity = "eat";
                            entity2 = "slices of cake";
                            break;
                        case "shield":
                            key = new NamespacedKey(Quests.getInstance(), "shield");
                            if (!meta.getPersistentDataContainer().has(key, PersistentDataType.INTEGER)) {
                                meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, p.getStatistic(Statistic.DAMAGE_BLOCKED_BY_SHIELD));
                                setStat = true; notTrack = true;
                                break;
                            }
                            statS = meta.getPersistentDataContainer().get(key, PersistentDataType.INTEGER);
                            statP = p.getStatistic(Statistic.DAMAGE_BLOCKED_BY_SHIELD);
                            if (statP <= statS) break;
                            statD = statP - statS;
                            meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, p.getStatistic(Statistic.DAMAGE_BLOCKED_BY_SHIELD));
                            setStat = true;
                            entity = "block";
                            entity2 = "damage with a shield";
                            break;
                        case "climb":
                            key = new NamespacedKey(Quests.getInstance(), "climb");
                            if (!meta.getPersistentDataContainer().has(key, PersistentDataType.INTEGER)) {
                                meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, p.getStatistic(Statistic.CLIMB_ONE_CM));
                                setStat = true; notTrack = true;
                                break;
                            }
                            statS = meta.getPersistentDataContainer().get(key, PersistentDataType.INTEGER);
                            statP = p.getStatistic(Statistic.CLIMB_ONE_CM);
                            if (statP <= statS) break;
                            statD = (statP - statS) / 100;
                            meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, p.getStatistic(Statistic.CLIMB_ONE_CM));
                            setStat = true;
                            entity = "climb";
                            entity2 = "blocks";
                            break;
                        case "sneak":
                            key = new NamespacedKey(Quests.getInstance(), "sneak");
                            if (!meta.getPersistentDataContainer().has(key, PersistentDataType.INTEGER)) {
                                meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, p.getStatistic(Statistic.CROUCH_ONE_CM));
                                setStat = true; notTrack = true;
                                break;
                            }
                            statS = meta.getPersistentDataContainer().get(key, PersistentDataType.INTEGER);
                            statP = p.getStatistic(Statistic.CROUCH_ONE_CM);
                            if (statP <= statS) break;
                            statD = (statP - statS) / 100;
                            meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, p.getStatistic(Statistic.CROUCH_ONE_CM));
                            setStat = true;
                            entity = "sneak";
                            entity2 = "blocks";
                            break;
                        case "fall":
                            key = new NamespacedKey(Quests.getInstance(), "fall");
                            if (!meta.getPersistentDataContainer().has(key, PersistentDataType.INTEGER)) {
                                meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, p.getStatistic(Statistic.FALL_ONE_CM));
                                setStat = true; notTrack = true;
                                break;
                            }
                            statS = meta.getPersistentDataContainer().get(key, PersistentDataType.INTEGER);
                            statP = p.getStatistic(Statistic.FALL_ONE_CM);
                            if (statP <= statS) break;
                            statD = (statP - statS) / 100;
                            meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, p.getStatistic(Statistic.FALL_ONE_CM));
                            setStat = true;
                            entity = "fall";
                            entity2 = "blocks";
                            break;
                        case "sprint":
                            key = new NamespacedKey(Quests.getInstance(), "sprint");
                            if (!meta.getPersistentDataContainer().has(key, PersistentDataType.INTEGER)) {
                                meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, p.getStatistic(Statistic.SPRINT_ONE_CM));
                                setStat = true; notTrack = true;
                                break;
                            }
                            statS = meta.getPersistentDataContainer().get(key, PersistentDataType.INTEGER);
                            statP = p.getStatistic(Statistic.SPRINT_ONE_CM);
                            if (statP <= statS) break;
                            statD = (statP - statS) / 100;
                            meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, p.getStatistic(Statistic.SPRINT_ONE_CM));
                            setStat = true;
                            entity = "sprint";
                            entity2 = "blocks";
                            break;
                        case "swim":
                            key = new NamespacedKey(Quests.getInstance(), "swim");
                            if (!meta.getPersistentDataContainer().has(key, PersistentDataType.INTEGER)) {
                                meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, p.getStatistic(Statistic.SWIM_ONE_CM));
                                setStat = true; notTrack = true;
                                break;
                            }
                            statS = meta.getPersistentDataContainer().get(key, PersistentDataType.INTEGER);
                            statP = p.getStatistic(Statistic.SWIM_ONE_CM);
                            if (statP <= statS) break;
                            statD = (statP - statS) / 100;
                            meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, p.getStatistic(Statistic.SWIM_ONE_CM));
                            setStat = true;
                            entity = "swim";
                            entity2 = "blocks";
                            break;
                        case "walk":
                            key = new NamespacedKey(Quests.getInstance(), "walk");
                            if (!meta.getPersistentDataContainer().has(key, PersistentDataType.INTEGER)) {
                                meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, p.getStatistic(Statistic.WALK_ONE_CM));
                                setStat = true; notTrack = true;
                                break;
                            }
                            statS = meta.getPersistentDataContainer().get(key, PersistentDataType.INTEGER);
                            statP = p.getStatistic(Statistic.WALK_ONE_CM);
                            if (statP <= statS) break;
                            statD = (statP - statS) / 100;
                            meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, p.getStatistic(Statistic.WALK_ONE_CM));
                            setStat = true;
                            entity = "walk";
                            entity2 = "blocks";
                            break;
                        case "boat":
                            key = new NamespacedKey(Quests.getInstance(), "boat");
                            if (!meta.getPersistentDataContainer().has(key, PersistentDataType.INTEGER)) {
                                meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, p.getStatistic(Statistic.BOAT_ONE_CM));
                                setStat = true; notTrack = true;
                                break;
                            }
                            statS = meta.getPersistentDataContainer().get(key, PersistentDataType.INTEGER);
                            statP = p.getStatistic(Statistic.BOAT_ONE_CM);
                            if (statP <= statS) break;
                            statD = (statP - statS) / 100;
                            meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, p.getStatistic(Statistic.BOAT_ONE_CM));
                            setStat = true;
                            entity = "travel";
                            entity2 = "blocks in a boat";
                            break;
                        case "fly":
                            key = new NamespacedKey(Quests.getInstance(), "fly");
                            if (!meta.getPersistentDataContainer().has(key, PersistentDataType.INTEGER)) {
                                meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, p.getStatistic(Statistic.AVIATE_ONE_CM));
                                setStat = true; notTrack = true;
                                break;
                            }
                            statS = meta.getPersistentDataContainer().get(key, PersistentDataType.INTEGER);
                            statP = p.getStatistic(Statistic.AVIATE_ONE_CM);
                            if (statP <= statS) break;
                            statD = (statP - statS) / 200;
                            meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, p.getStatistic(Statistic.AVIATE_ONE_CM));
                            setStat = true;
                            entity = "fly";
                            entity2 = "blocks";
                            break;
                        case "horse":
                            key = new NamespacedKey(Quests.getInstance(), "horse");
                            if (!meta.getPersistentDataContainer().has(key, PersistentDataType.INTEGER)) {
                                meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, p.getStatistic(Statistic.HORSE_ONE_CM));
                                setStat = true; notTrack = true;
                                break;
                            }
                            statS = meta.getPersistentDataContainer().get(key, PersistentDataType.INTEGER);
                            statP = p.getStatistic(Statistic.HORSE_ONE_CM);
                            if (statP <= statS) break;
                            statD = (statP - statS) / 100;
                            meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, p.getStatistic(Statistic.HORSE_ONE_CM));
                            setStat = true;
                            entity = "travel";
                            entity2 = "blocks on a horse";
                            break;
                        case "minecart":
                            key = new NamespacedKey(Quests.getInstance(), "minecart");
                            if (!meta.getPersistentDataContainer().has(key, PersistentDataType.INTEGER)) {
                                meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, p.getStatistic(Statistic.MINECART_ONE_CM));
                                setStat = true; notTrack = true;
                                break;
                            }
                            statS = meta.getPersistentDataContainer().get(key, PersistentDataType.INTEGER);
                            statP = p.getStatistic(Statistic.MINECART_ONE_CM);
                            if (statP <= statS) break;
                            statD = (statP - statS) / 100;
                            meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, p.getStatistic(Statistic.MINECART_ONE_CM));
                            setStat = true;
                            entity = "travel";
                            entity2 = "blocks in a minecart";
                            break;
                        case "pig":
                            key = new NamespacedKey(Quests.getInstance(), "pig");
                            if (!meta.getPersistentDataContainer().has(key, PersistentDataType.INTEGER)) {
                                meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, p.getStatistic(Statistic.PIG_ONE_CM));
                                setStat = true; notTrack = true;
                                break;
                            }
                            statS = meta.getPersistentDataContainer().get(key, PersistentDataType.INTEGER);
                            statP = p.getStatistic(Statistic.PIG_ONE_CM);
                            if (statP <= statS) break;
                            statD = (statP - statS) / 100;
                            meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, p.getStatistic(Statistic.PIG_ONE_CM));
                            setStat = true;
                            entity = "travel";
                            entity2 = "blocks on a pig";
                            break;
                        case "strider":
                            key = new NamespacedKey(Quests.getInstance(), "strider");
                            if (!meta.getPersistentDataContainer().has(key, PersistentDataType.INTEGER)) {
                                meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, p.getStatistic(Statistic.STRIDER_ONE_CM));
                                setStat = true; notTrack = true;
                                break;
                            }
                            statS = meta.getPersistentDataContainer().get(key, PersistentDataType.INTEGER);
                            statP = p.getStatistic(Statistic.STRIDER_ONE_CM);
                            if (statP <= statS) break;
                            statD = (statP - statS) / 1000;
                            meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, p.getStatistic(Statistic.STRIDER_ONE_CM));
                            setStat = true;
                            entity = "travel";
                            entity2 = "blocks on a strider";
                            break;
                        case "jump":
                            key = new NamespacedKey(Quests.getInstance(), "jump");
                            if (!meta.getPersistentDataContainer().has(key, PersistentDataType.INTEGER)) {
                                meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, p.getStatistic(Statistic.JUMP));
                                setStat = true; notTrack = true;
                                break;
                            }
                            statS = meta.getPersistentDataContainer().get(key, PersistentDataType.INTEGER);
                            statP = p.getStatistic(Statistic.JUMP);
                            if (statP <= statS) break;
                            statD = statP - statS;
                            meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, p.getStatistic(Statistic.JUMP));
                            setStat = true;
                            entity = "jump";
                            entity2 = "times";
                            break;
                        case "ring":
                            key = new NamespacedKey(Quests.getInstance(), "ring");
                            if (!meta.getPersistentDataContainer().has(key, PersistentDataType.INTEGER)) {
                                meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, p.getStatistic(Statistic.BELL_RING));
                                setStat = true; notTrack = true;
                                break;
                            }
                            statS = meta.getPersistentDataContainer().get(key, PersistentDataType.INTEGER);
                            statP = p.getStatistic(Statistic.BELL_RING);
                            if (statP <= statS) break;
                            statD = statP - statS;
                            meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, p.getStatistic(Statistic.BELL_RING));
                            setStat = true;
                            entity = "ring";
                            entity2 = "bells";
                            break;
                        default: break;
                    }
                    if (setStat) {
                        ItemStack itemStack = p.getInventory().getItemInOffHand();
                        itemStack.setItemMeta(meta);
                        if (notTrack) {
                            continue;
                        }
                    }

                    for (int i = 0; i < lore.size(); i++) {
                        if (lore.get(i).toLowerCase().contains(entity) && lore.get(i).toLowerCase().contains(entity2)) {
                            if (Utils.updateNumLine(lore, p, statD, i)) {
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
}