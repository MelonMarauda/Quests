package com.jagsnet.minecraft.plugins.quests;

import com.jagsnet.minecraft.plugins.quests.commands.*;
import com.jagsnet.minecraft.plugins.quests.otherStuff.bedrock.MoveToOffhand;
import com.jagsnet.minecraft.plugins.quests.otherStuff.utils.Configs;
import com.jagsnet.minecraft.plugins.quests.trackers.listeners.*;
import com.jagsnet.minecraft.plugins.quests.trackers.scheduledTasks.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public final class Quests extends JavaPlugin {
    private static Quests instance;
    public static Quests getInstance() {
        return instance;
    }
    public static Boolean auto = false;

    @Override
    public void onEnable() {
        // Register event listeners for tracking quests
        getServer().getPluginManager().registerEvents(new MobKills(), this);
        getServer().getPluginManager().registerEvents(new Crafting(), this);
        getServer().getPluginManager().registerEvents(new Mining(), this);
        getServer().getPluginManager().registerEvents(new Fishing(), this);
        getServer().getPluginManager().registerEvents(new Eating(), this);
        getServer().getPluginManager().registerEvents(new Breeding(), this);
        getServer().getPluginManager().registerEvents(new Taming(), this);
        getServer().getPluginManager().registerEvents(new Enchanting(), this);
        getServer().getPluginManager().registerEvents(new Trading(), this);
        getServer().getPluginManager().registerEvents(new XP(), this);
        getServer().getPluginManager().registerEvents(new DamageTaken(), this);
        getServer().getPluginManager().registerEvents(new DamageDone(), this);
        getServer().getPluginManager().registerEvents(new Throwing(), this);
        getServer().getPluginManager().registerEvents(new Commands(), this);
        getServer().getPluginManager().registerEvents(new Smelting(), this);
        getServer().getPluginManager().registerEvents(new Shearing(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeath(), this);
        getServer().getPluginManager().registerEvents(new FillBucketFish(), this);
        getServer().getPluginManager().registerEvents(new FillBucketOther(), this);
        getServer().getPluginManager().registerEvents(new Strange(), this);
        getServer().getPluginManager().registerEvents(new Chat(), this);
        getServer().getPluginManager().registerEvents(new MoveToOffhand(), this);

        // Schedule check for player movement
        BukkitScheduler scheduler = getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                Movement.trackBiome();
            }
        }, 0L, 100L);
        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                Movement.trackMovement();
            }
        }, 1L, 100L);
        // Schedule check for player rank for newbie scroll
        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                Rank.track();
            }
        }, 2L, 100L);
        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                Random.randomise();
            }
        }, 3L, 100L);
        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() { WorldGuardRegions.inRegion(); }
        }, 4L, 100L);

        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() { Coords.track(); }
        }, 5L, 100L);

        // Register commands for quests
        this.getCommand("quest").setExecutor(new Main());
        this.getCommand("aquest").setExecutor(new Admin());
        this.getCommand("gquest").setExecutor(new Generators());
        this.getCommand("cquest").setExecutor(new Completer());
        this.getCommand("addeve").setExecutor(new Main());
        this.getCommand("melon").setExecutor(new Melon());

        // For using persistent data container, don't touch k thx
        instance = this;

        Configs.setupLists();
        Configs.setupGlobal();
        setAuto();
    }

    @Override
    public void onDisable() {

    }

       public static void setAuto() {
        auto = Configs.getGlobal().getBoolean("auto");
    }
}
