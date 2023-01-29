package com.jagsnet.minecraft.plugins.quests.scheduledTasks;

import com.google.common.collect.Lists;
import com.jagsnet.minecraft.plugins.quests.Quests;
import com.jagsnet.minecraft.plugins.quests.otherStuff.Utils;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class WorldGuardRegions {
    public static void inRegion() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (Utils.isScrollOff(p)) {

                NamespacedKey key = new NamespacedKey(Quests.getInstance(), "region");
                if (!p.getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING)) continue;
                if (!p.getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING).toLowerCase().equals("true")) continue;

                Location loc = new Location(BukkitAdapter.adapt(p.getWorld()),
                        p.getLocation().getX(),
                        p.getLocation().getY(),
                        p.getLocation().getZ());
                RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
                RegionQuery query = container.createQuery();
                ApplicableRegionSet set = query.getApplicableRegions(loc);
                List<ProtectedRegion> regions = Lists.newArrayList(set);

                for (ProtectedRegion region : regions) {
                    String entity = region.getId().replace("_", " ").toLowerCase();
                    ArrayList<String> lore = (ArrayList<String>) p.getInventory().getItemInOffHand().getItemMeta().getLore();
                    for (int i = 0; i < lore.size(); i++) {
                        if (lore.get(i).toLowerCase().contains(entity) && lore.get(i).contains("Incomplete")) {
                            if (Utils.updateTxtLine(lore, p, i, true)) {
                            }
                        }
                    }
                }
            }
        }
    }
}
