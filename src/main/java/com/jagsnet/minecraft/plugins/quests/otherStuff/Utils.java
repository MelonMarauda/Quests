package com.jagsnet.minecraft.plugins.quests.otherStuff;

import com.jagsnet.minecraft.plugins.quests.Quests;
import net.jagsnet.minecraft.plugins.mlib.utils.Messaging;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Integer.parseInt;

public class Utils {

    public static void setDdeve(boolean b) {
        ddeve = b;
    }

    static boolean ddeve = false;

    // ------------- Send Formatted Message To Player ------------------
    public static void sendMessage(Player player, String msg){
        if (ddeve) {
            msg = msg.replaceAll("quest", "ddeve");
            msg = msg.replaceAll("Quest", "ddeve");
            Messaging.sendMessage(player, msg, ChatColor.GOLD, "DDEVE");
        } else {
            Messaging.sendMessage(player, msg, ChatColor.GOLD, "QUESTS");
        }
    }
    public static void log(String msg, String level){
        Messaging.log(msg, Quests.getInstance(), level, "QUESTS");
    }
    public static void log(String msg){
        Messaging.log(msg, Quests.getInstance(), "INFO", "QUESTS");
    }

    // ------------- Scroll Check For Commands -------------------------
    public static boolean isScrollMain(Player p) {
        return isScroll(p, p.getInventory().getItemInMainHand());
    }
    // ------------- Scroll Check For Listeners ------------------------
    public static boolean isScrollOff(Player p) {
        return isScroll(p, p.getInventory().getItemInOffHand());
    }

    public static boolean isScroll(Player p, ItemStack i) {
        ItemMeta iM = i.getItemMeta();
        try {
            if (!i.getType().equals(Material.PAPER)) {
                return false;
            }
        } catch (NullPointerException e) {
            return false;
        }
        if (i == null) {
            return false;
        }
        if (!iM.hasLore()) {
            return false;
        }
        if (iM.getLore().get(0).contains("Quest Path") || iM.getLore().get(0).contains("Quest Scroll") || iM.getLore().get(0).contains("Tracker")) {
            if (i.getAmount() > 1) {
                sendMessage(p, "You may only use one quest scroll at a time");
                return false;
            }
            NamespacedKey key = new NamespacedKey(Quests.getInstance(), "UUID");
            if (iM.getPersistentDataContainer().has(key, PersistentDataType.STRING)
                    && !p.hasPermission("quests.admin")) {
                String UUID = iM.getPersistentDataContainer().get(key, PersistentDataType.STRING);
                if (!UUID.equals(p.getUniqueId().toString())) {
                    sendMessage(p, "You may only use this scroll if it was assigned to you.");
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    // ------------------- Scroll Claim Stuff --------------------------
    public static boolean claim(Player player, boolean track, String confKey, boolean force) {
        ItemMeta mainHandItem = null;
        Boolean issOff = false;
        if (Utils.isScrollMain(player)) {
            mainHandItem = player.getInventory().getItemInMainHand().getItemMeta();
        }
        if (Utils.isScrollOff(player)) {
            mainHandItem = player.getInventory().getItemInOffHand().getItemMeta();
            issOff = true;
        }
        NamespacedKey key = new NamespacedKey(Quests.getInstance(), "rewardLocation");
        if (mainHandItem.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
            String location = mainHandItem.getPersistentDataContainer().get(key, PersistentDataType.STRING);
            load(location.split("_")[0]);
            String path = location.split("_")[1];
            List rewards = get().getStringList( location.split("_")[1] + confKey);
            int rewardCount = rewards.size();
            int count = 0;
            for (int slot = 0; slot < 36; slot++) {
                ItemStack is = player.getInventory().getItem(slot);
                if (is == null) {
                    count++;
                }
            }
            if (count < rewardCount) {
                Utils.sendMessage(player, "Clear some inventory space before trying to claim your reward");
                return true;
            }

            if (!player.isOp()) {
                if (player.hasPermission("quest.completed." + location) && !force) {
                    Utils.sendMessage(player, "You have already completed this quest. It has a " + get().getString(location.split("_")[1] + ".permission") + " cooldown. Please wait until this is up.");
                    return true;
                }
            }

            if (Utils.get().getString(location.split("_")[1] + ".permissionComplete") != null) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " permission settemp quest.completed." + location + " true " + Utils.get().getString(location.split("_")[1] + ".permission"));
            }

            Utils.log(player.getName() + " completed quest scroll " + location);

            if (issOff) {
                player.getInventory().setItemInOffHand(new ItemStack(Material.AIR));
            } else {
                player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
            }
            if (get().getStringList( path + ".claimMessage.lines").size() > 0) {
                String bold = ChatColor.BOLD + "";
                List messages = Utils.get().getStringList(path + ".claimMessage.lines");
                String msgName = Utils.get().getString(path + ".claimMessage.name");
                String msgColour = Utils.get().getString(path + ".claimMessage.colour");
                for (int i = 0; i < messages.size(); i++) {
                    player.sendMessage(ChatColor.valueOf(msgColour) + bold + msgName + " > " + ChatColor.WHITE + messages.get(i).toString());
                }
            }
            for (int i = 0; rewards.size() > i; i++) {
                String rewardName = (String) rewards.get(i);

                loadLists();
                int e = 0;
                while (rewardName.contains("<") && rewardName.contains(">") && e < 4) {
                    if (rewardName.contains("<player>")) {
                        rewardName = rewardName.replace("<player>", ChatColor.stripColor(player.getName()));
                    }
                    if (rewardName.contains("<date>")) {
                        rewardName = rewardName.replace("<date>", new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
                    }
                    if (rewardName.contains("<") && rewardName.contains(">")) {
                        String listName = rewardName.substring(rewardName.indexOf("<") + 1, rewardName.indexOf(">"));
                        List list = getLists().getStringList(listName);
                        int randomNum = ThreadLocalRandom.current().nextInt(0, list.size());
                        rewardName = rewardName.replace("<" + listName + ">", (CharSequence) list.get(randomNum));
                    }
                    e++;
                }
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), rewardName);
                Utils.log("Quests ran: " + rewardName);
            }
        } else {
            Utils.sendMessage(player, "Thats strange, there are no rewards for this quest scroll");
            return true;
        }
        return true;
    }
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
            log("Couldn't save file");
        }
    }
    private static File file2;
    private static FileConfiguration customFile2;
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
            Utils.log("Couldn't save file");
        }
    }

    // ----------------- Config file functions for lists --------------------
    private static File lists;
    private static FileConfiguration customLists;
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
            Utils.log("Couldn't save file");
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
            Utils.log("Couldn't save file");
        }
    }

    // ----------------- Config file functions for global --------------------
    private static File global;
    private static FileConfiguration customGlobal;
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
            Utils.log("Couldn't save file");
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
            Utils.log("Couldn't save file");
        }
    }

    // ----------------- Update lines --------------------
    public static boolean updateNumLine(ArrayList<String> lore, Player p, int newNum, int lineNum){
        try {
            String strike = ChatColor.STRIKETHROUGH + "";
            if (lore.get(lineNum).contains(strike)) {return false;}
            if (!passModifiers(p, lore)) {return false;}
            String[] loreLine = lore.get(lineNum).split(": ", 0);
            int x = parseInt(loreLine[1]) + newNum;
            lore.set(lineNum, loreLine[0] + ": " + x);
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(lore.get(lineNum)));
            if (x >= parseInt(loreLine[0].split(" ", 0)[1])) {
                NamespacedKey key = new NamespacedKey(Quests.getInstance(), "locked");
                if (p.getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
                    if (lore.get(lineNum + 1).contains(strike)) {
                        lore.set(lineNum + 1, lore.get(lineNum + 1).replace(strike, ""));
                    }
                }
            }

            ItemStack itemStack = p.getInventory().getItemInOffHand();
            ItemMeta m = itemStack.getItemMeta();
            m.setLore(lore);
            itemStack.setItemMeta(m);

            if (x >= parseInt(loreLine[0].split(" ", 0)[1])) {
                doComplete(p, itemStack.getItemMeta(), lore, itemStack);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateTxtLine(ArrayList<String> lore, Player p, int lineNum, Boolean isOff){
        try {
            String strike = ChatColor.STRIKETHROUGH + "";
            if (lore.get(lineNum).contains(strike)) {return false;}
            if (!passModifiers(p, lore)) {return false;}
            String[] loreLine = lore.get(lineNum).split(": ", 0);
            lore.set(lineNum, loreLine[0] + ": Complete");
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(lore.get(lineNum)));
            if (lore.get(lineNum).contains("Complete")) {
                NamespacedKey key = new NamespacedKey(Quests.getInstance(), "locked");
                if (p.getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
                    if (lore.get(lineNum + 1).contains(strike)) {
                        lore.set(lineNum + 1, lore.get(lineNum + 1).replace(strike, ""));
                    }
                }
            }

            ItemStack itemStack;
            if (isOff) {
                itemStack = p.getInventory().getItemInOffHand();
            } else {
                itemStack = p.getInventory().getItemInMainHand();
            }
            ItemMeta m = itemStack.getItemMeta();
            m.setLore(lore);
            itemStack.setItemMeta(m);

            if (lore.get(lineNum).contains("Complete")) {
                doComplete(p, itemStack.getItemMeta(), lore, itemStack);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean passModifiers(Player p, ArrayList<String> lore) {
        String s = ChatColor.stripColor(lore.get(1));
        if (s.contains("Modifiers: ")) {
            String[] modList = s.split(": ")[1].toUpperCase().replace(" ", "_").split(",_");
            for (String mods : modList) {
                if (!p.hasPotionEffect(PotionEffectType.getByName(mods))) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void doComplete(Player p, ItemMeta m, ArrayList<String> lore, ItemStack i) {
        NamespacedKey key = new NamespacedKey(Quests.getInstance(), "autocomplete");
        if (m.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
            if (m.getPersistentDataContainer().get(key, PersistentDataType.STRING).equals("true")) {
                if (Quests.auto) {
                    if (isComplete(m)) {
                        claim(p, false, ".rewards", false);
                    }
                }
            }
        }
    }
    public static boolean isComplete(ItemMeta mainHandItem) {
        int loreLength = mainHandItem.getLore().size();
        int totalLines = 0;
        int totalComplete = 0;
        for (int i = 0; i < loreLength; i++) {
            String loreLineC = ChatColor.stripColor(mainHandItem.getLore().get(i));
            String[] loreLine = loreLineC.split(" ", 0);
            if (loreLine.length > 2) {
                String target = loreLine[1];
                String current = loreLine[loreLine.length - 1];

                int targetCount;
                int currentCount;

                if (current.equals("Incomplete")) {
                    totalLines++;
                    return false;
                } else if (current.equals("Complete")) {
                    totalLines++;
                    totalComplete++;
                }

                try {
                    targetCount = parseInt(target);
                    try {
                        currentCount = parseInt(current);
                        totalLines++;
                        if (currentCount >= targetCount) {
                            totalComplete++;
                        } else {
                            return false;
                        }
                    } catch (Exception e) {
                    }
                } catch (Exception e) {
                }
            }
        }

        if (totalLines == totalComplete && totalComplete != 0) {
            return true;
        }
        return false;
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

    public static String cleanLore(String s, boolean fixMobs, boolean fixBlocks){
        s = ChatColor.stripColor(s);
        s = s.toLowerCase();
        if (fixMobs) {
            s = s.replace("wolves", "wolf");
            s = s.replace("endermen", "enderman");
        }
        if (fixBlocks) {

        }
        return s;
    }

    public static String cleanEntity(String s) {
        return " " + s.replace("_", " ").toLowerCase();
    }
}
