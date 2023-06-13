package com.jagsnet.minecraft.plugins.quests.otherStuff.utils;

import com.jagsnet.minecraft.plugins.quests.otherStuff.messages.Messaging;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Configs {
    private static File file2;
    private static FileConfiguration customFile2;
    // ----------------- Config file functions for lists --------------------
    private static File lists;
    private static FileConfiguration customLists;
    // ----------------- Config file functions for global --------------------
    private static File global;
    private static FileConfiguration customGlobal;

    // ------------- Config file functions -----------------------------
    public static void setup(String configName){
        File file;
        FileConfiguration customFile;
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("Quests").getDataFolder(), configName + ".yml");
        if (!file.exists()){
            try{
                file.createNewFile();
            }catch (IOException e){
            }
        }
        customFile = YamlConfiguration.loadConfiguration(file);
        try{
            customFile.save(file);
        }catch (IOException e){
            Messaging.log("Couldn't save file");
        }
    }

    public static FileConfiguration get(){
        return customFile2;
    }

    public static void load(String name){
        file2 = new File(Bukkit.getServer().getPluginManager().getPlugin("Quests").getDataFolder(), name + ".yml");
        customFile2 = YamlConfiguration.loadConfiguration(file2);
    }

    public static void save(){
        try{
            customFile2.save(file2);
        }catch (IOException e){
            Messaging.log("Couldn't save file");
        }
    }

    public static void setupLists(){
        lists = new File(Bukkit.getServer().getPluginManager().getPlugin("Quests").getDataFolder(),"Lists.yml");
        if (!lists.exists()){
            try{
                lists.createNewFile();
            }catch (IOException e){
            }
        }
        customLists = YamlConfiguration.loadConfiguration(lists);
        try{
            customLists.save(lists);
        }catch (IOException e){
            Messaging.log("Couldn't save file");
        }
    }

    public static FileConfiguration getLists(){
        return customLists;
    }

    public static void loadLists(){
        lists = new File(Bukkit.getServer().getPluginManager().getPlugin("Quests").getDataFolder(), "Lists.yml");
        customLists = YamlConfiguration.loadConfiguration(lists);
    }

    public static void saveLists(){
        try{
            customLists.save(lists);
        }catch (IOException e){
            Messaging.log("Couldn't save file");
        }
    }

    public static void setupGlobal(){
        global = new File(Bukkit.getServer().getPluginManager().getPlugin("Quests").getDataFolder(),"Global.yml");
        if (!global.exists()){
            try{
                global.createNewFile();
            }catch (IOException e){
            }
        }
        customGlobal = YamlConfiguration.loadConfiguration(global);
        try{
            customGlobal.save(global);
        }catch (IOException e){
            Messaging.log("Couldn't save file");
        }
    }

    public static FileConfiguration getGlobal(){
        return customGlobal;
    }

    public static void loadGlobal(){
        global = new File(Bukkit.getServer().getPluginManager().getPlugin("Quests").getDataFolder(), "Global.yml");
        customGlobal = YamlConfiguration.loadConfiguration(global);
    }

    public static void saveGlobal(){
        try{
            customGlobal.save(global);
        }catch (IOException e){
            Messaging.log("Couldn't save file");
        }
    }
}
