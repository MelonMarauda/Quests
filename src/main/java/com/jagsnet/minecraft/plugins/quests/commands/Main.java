package com.jagsnet.minecraft.plugins.quests.commands;

import com.jagsnet.minecraft.plugins.quests.Quests;
import com.jagsnet.minecraft.plugins.quests.otherStuff.utils.*;
import com.jagsnet.minecraft.plugins.quests.otherStuff.messages.Messaging;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Integer.parseInt;
import static org.bukkit.Bukkit.getServer;

public class Main implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            String white = ChatColor.WHITE + "";

            if (!Messaging.ddeve) {
                if (cmd.getName().equals("addeve")){
                    Messaging.sendMessage(player, white + "Unknown command. Type \"/help\" for help.", false);
                    return true;
                }
            }

            if (Messaging.ddeve) {
                if (cmd.getName().equals("quest")){
                    Messaging.sendMessage(player, white + "Unknown command. Type \"/addeve\" for help.", false);
                    return true;
                }
            }
            // ------------------------------------------------------
            // ------------------ See help info ---------------------
            // ------------------------------------------------------
            if (args.length < 1 || args[0].equalsIgnoreCase("help")) {
                String boldGold = ChatColor.GOLD + "" + ChatColor.BOLD + "";

                Messaging.sendMessage(player, boldGold + "---General Quest Help---", false);
                Messaging.sendMessage(player, boldGold + "/quest help " + white + "lists quest specific help if available.", false);
                Messaging.sendMessage(player, boldGold + "/quest claim " + white + "claims the rewards on a quest.", false);
                Messaging.sendMessage(player, boldGold + "/quest expiry " + white + "checks when a quest expires.", false);
                Messaging.sendMessage(player, boldGold + "/quest list " + white + "lists quests available to collect.", false);
                Messaging.sendMessage(player, boldGold + "/quest secret <code> " + white + "tests the supplied code with the one defined secretly on the scroll IF the scroll has one.", false);
                Messaging.sendMessage(player, boldGold + "Remember " + white + "always hold your scroll in your offhand when completing objectives and always hold it in your main hand when running quest commands.", false);


                ItemMeta mainHandItem = null;
                if (Scroll.isScrollMain(player)) {
                    mainHandItem = player.getInventory().getItemInMainHand().getItemMeta();
                }
                if (Scroll.isScrollOff(player)) {
                    mainHandItem = player.getInventory().getItemInOffHand().getItemMeta();
                }
                if (mainHandItem != null) {
                    NamespacedKey key = new NamespacedKey(Quests.getInstance(), "rewardLocation");
                    if (mainHandItem.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
                        String[] location = mainHandItem.getPersistentDataContainer().get(key, PersistentDataType.STRING).split("_");
                        Configs.load(location[0]);
                        List helps = Configs.get().getStringList( location[1] + ".help");
                        if (helps != null && helps.size() != 0) {
                            Messaging.sendMessage(player, boldGold + "---Quest Specific Help---", false);
                            for (int i = 0; i < helps.size(); i++) {
                                Messaging.sendMessage(player, boldGold + (i+1) + ". " + ChatColor.WHITE + helps.get(i), false);
                            }
                        }
                    }
                }
                return true;
            }
            // ------------------------------------------------------
            // ------------------ Claim completed quest -------------
            // ------------------------------------------------------
            if (args[0].equalsIgnoreCase("claim")) {
                if (Scroll.isScrollMain(player) || Scroll.isScrollOff(player)) {
                    ItemMeta mainHandItem = null;
                    if (Scroll.isScrollMain(player)) {
                        mainHandItem = player.getInventory().getItemInMainHand().getItemMeta();
                    }
                    if (Scroll.isScrollOff(player)) {
                        mainHandItem = player.getInventory().getItemInOffHand().getItemMeta();
                    }
                    NamespacedKey key = new NamespacedKey(Quests.getInstance(), "rewardLocation");
                    String location = mainHandItem.getPersistentDataContainer().get(key, PersistentDataType.STRING);
                    key = new NamespacedKey(Quests.getInstance(), "expiry");
                    if (mainHandItem.getPersistentDataContainer().has(key, PersistentDataType.LONG)) {
                        key = new NamespacedKey(Quests.getInstance(), "rewardLocation");
                        if (mainHandItem.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
                            Configs.load(location.split("_")[0]);
                            if (Configs.get().getString(location.split("_")[1] + ".expiry") != null) {
                                long expiry = Long.valueOf(Configs.get().getString(location.split("_")[1] + ".expiry"));
                                key = new NamespacedKey(Quests.getInstance(), "expiry");
                                expiry += mainHandItem.getPersistentDataContainer().get(key, PersistentDataType.LONG);
                                if (expiry <= System.currentTimeMillis()) {
                                    Messaging.sendMessage(player, "This scroll has expired. It can no longer be claimed");
                                    return true;
                                }
                            }
                        }
                    }

                    if (Completion.isComplete(mainHandItem)) {
                        if (player.getInventory().getItemInMainHand().getAmount() > 1) {
                            Messaging.sendMessage(player, "You may only claim one quest scroll at a time");
                            return true;
                        }
                        return Claim.claim(player, true, ".rewards", false);
                    } else {
                        Messaging.sendMessage(player, "You have not completed this quest yet");
                        return true;
                    }
                }
                Messaging.sendMessage(player, "Please hold a valid quest scroll in your main hand to run this command");
                return true;
            }
            // ------------------------------------------------------
            // ------------------ See expiry ------------------------
            // ------------------------------------------------------
            if (args[0].equalsIgnoreCase("expiry")) {
                if (Scroll.isScrollMain(player) || Scroll.isScrollOff(player)) {
                    ItemMeta mainHandItem = null;
                    if (Scroll.isScrollMain(player)) {
                        mainHandItem = player.getInventory().getItemInMainHand().getItemMeta();
                    }
                    if (Scroll.isScrollOff(player)) {
                        mainHandItem = player.getInventory().getItemInOffHand().getItemMeta();
                    }
                    NamespacedKey key = new NamespacedKey(Quests.getInstance(), "rewardLocation");
                    if (mainHandItem.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
                        String location = mainHandItem.getPersistentDataContainer().get(key, PersistentDataType.STRING);
                        Configs.load(location.split("_")[0]);
                        if (Configs.get().getString(location.split("_")[1] + ".expiry") != null) {
                            Long expiry = Long.valueOf(Configs.get().getString(location.split("_")[1] + ".expiry"));
                            key = new NamespacedKey(Quests.getInstance(), "expiry");
                            if (!mainHandItem.getPersistentDataContainer().has(key, PersistentDataType.LONG)) {
                                Messaging.sendMessage(player, "This quest scroll does not expire");
                                return true;
                            }
                            expiry += mainHandItem.getPersistentDataContainer().get(key, PersistentDataType.LONG);
                            Long current = System.currentTimeMillis();
                            if (current > expiry) {
                                Messaging.sendMessage(player, "This scroll has expired. It can no longer be claimed");
                                return true;
                            }
                            Long hours = (expiry / 60 / 60 / 1000) - (current / 60 / 60 / 1000);
                            if (hours < 6) {
                                Long minutes = (expiry / 60 / 1000) - (current / 60 / 1000);
                                Messaging.sendMessage(player, "Expiry on this quest is set for " + minutes + " minutes from now");
                            } else {
                                Messaging.sendMessage(player, "Expiry on this quest is set for " + hours + " hours from now");
                            }
                            return true;
                        }
                        Messaging.sendMessage(player, "This quest does not have an expiry set");
                        return true;
                    }

                    key = new NamespacedKey(Quests.getInstance(), "expiry");
                    if (!mainHandItem.getPersistentDataContainer().has(key, PersistentDataType.LONG)) {
                        Messaging.sendMessage(player, "This quest scroll does not expire");
                        return true;
                    }
                    Long expiry = mainHandItem.getPersistentDataContainer().get(key, PersistentDataType.LONG);
                    Long current = System.currentTimeMillis();
                    if (current > expiry) {
                        Messaging.sendMessage(player, "This scroll has expired. It can no longer be claimed");
                        return true;
                    }
                    Long hours = (expiry / 60 / 60 / 1000) - (current / 60 / 60 / 1000);
                    if (hours < 6) {
                        Long minutes = (expiry / 60 / 1000) - (current / 60 / 1000);
                        Messaging.sendMessage(player, "Expiry on this quest is set for " + minutes + " minutes from now");
                    } else {
                        Messaging.sendMessage(player, "Expiry on this quest is set for " + hours + " hours from now");
                    }
                    return true;
                }
                Messaging.sendMessage(player, "Please hold a valid quest scroll in your main hand to run this command");
                return true;
            }
            // ------------------------------------------------------
            // ----------------- Complete a secret ------------------
            // ------------------------------------------------------
            if (args[0].equalsIgnoreCase("secret")) {
                if (Scroll.isScrollMain(player) || Scroll.isScrollOff(player)) {
                    ItemMeta mainHandItem = null;
                    Boolean isOff = false;
                    if (Scroll.isScrollMain(player)) {
                        mainHandItem = player.getInventory().getItemInMainHand().getItemMeta();
                    } else if (Scroll.isScrollOff(player)) {
                        mainHandItem = player.getInventory().getItemInOffHand().getItemMeta();
                        isOff = true;
                    }
                    if (args.length < 2) {
                        Messaging.sendMessage(player, "You must provide a code to test.");
                        return true;
                    }
                    NamespacedKey key = new NamespacedKey(Quests.getInstance(), "secretCooldown");
                    if (mainHandItem.getPersistentDataContainer().has(key, PersistentDataType.LONG)) {
                        if (mainHandItem.getPersistentDataContainer().get(key, PersistentDataType.LONG) > System.currentTimeMillis()) {
                            long t = mainHandItem.getPersistentDataContainer().get(key, PersistentDataType.LONG) - System.currentTimeMillis();

                            String msg = "You cannot guess this secret code again for another " + Messaging.time(t) + "!";

                            Messaging.sendMessage(player, msg);
                            return true;
                        }
                    }

                    key = new NamespacedKey(Quests.getInstance(), "ultraSecret");
                    if (mainHandItem.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
                        if (mainHandItem.getPersistentDataContainer().get(key, PersistentDataType.STRING).equals("Claimed")) {
                            Messaging.sendMessage(player, "You have already correctly guessed the secret on this scroll.");
                            return true;
                        }
                    }

                    key = new NamespacedKey(Quests.getInstance(), "rewardLocation");
                    String secret;
                    List ultraSecret = null;
                    String[] location = mainHandItem.getPersistentDataContainer().get(key, PersistentDataType.STRING).split("_");
                    if (mainHandItem.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
                        Configs.load(location[0]);
                        if (Configs.get().getStringList(location[1] + ".secret") == null) {
                            Messaging.sendMessage(player, "This quest scroll does not have a secret set");
                            return true;
                        }
                        secret = Configs.get().getString(location[1] + ".secret");
                        if (Configs.get().getStringList(location[1] + ".ultraSecret") != null) {
                            ultraSecret = Configs.get().getStringList(location[1] + ".ultraSecret");
                        }
                    } else {
                        Messaging.sendMessage(player, "This quest scroll does not have a secret set");
                        return true;
                    }

                    key = new NamespacedKey(Quests.getInstance(), "secretCooldown");
                    ItemStack is;
                    if (isOff) {
                        is = player.getInventory().getItemInOffHand();
                    } else {
                        is = player.getInventory().getItemInMainHand();
                    }
                    ItemMeta m = is.getItemMeta();

                    if (Configs.get().getString(location[1] + ".secretCooldown") != null) {
                        m.getPersistentDataContainer().set(key, PersistentDataType.LONG, System.currentTimeMillis() + Configs.get().getLong(location[1] + ".secretCooldown"));
                    } else {
                        m.getPersistentDataContainer().set(key, PersistentDataType.LONG, System.currentTimeMillis() + 60000);
                    }

                    is.setItemMeta(m);

                    String suppliedCode = "";
                    for (int i = 1; i < args.length; i++) {
                        if (i == args.length - 1) {
                            suppliedCode = suppliedCode + args[i];
                        } else {
                            suppliedCode = suppliedCode + args[i] + " ";
                        }
                    }

                    if (!secret.equals(suppliedCode)) {
                        if (Configs.get().getString(location[1] + ".secretCooldown") != null) {
                            Messaging.sendMessage(player, "The provided secret code was wrong, try again in " + Messaging.time(Configs.get().getLong(location[1] + ".secretCooldown")) + ".");
                        } else {
                            Messaging.sendMessage(player, "The provided secret code was wrong, try again in 60 seconds.");
                        }
                        return true;
                    }

                    ArrayList<String> lore;
                    if (isOff) {
                        lore = (ArrayList<String>) player.getInventory().getItemInOffHand().getItemMeta().getLore();
                    } else {
                        lore = (ArrayList<String>) player.getInventory().getItemInMainHand().getItemMeta().getLore();
                    }
                    for (int i = 0; i < lore.size(); i++) {
                        if (lore.get(i).contains("Guess The Secret Code") && lore.get(i).contains("Incomplete")) {
                            Completion.updateTxtLine(lore, player, i, isOff);
                        }
                    }

                    ItemStack itemStack;
                    if (isOff) {
                        itemStack = player.getInventory().getItemInOffHand();
                    } else {
                        itemStack = player.getInventory().getItemInMainHand();
                    }
                    ItemMeta meta = itemStack.getItemMeta();

                    if (ultraSecret != null) {
                        int rewardCount = ultraSecret.size();
                        int count = 0;
                        for (int slot = 0; slot < 36; slot++) {
                            ItemStack is2 = player.getInventory().getItem(slot);
                            if (is2 == null) {
                                count++;
                            }
                        }
                        if (count < rewardCount) {
                            Messaging.sendMessage(player, "Clear some inventory space before guessing correctly again.");
                            return true;
                        }
                        key = new NamespacedKey(Quests.getInstance(), "ultraSecret");
                        if (!meta.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
                            for (int i = 0; ultraSecret.size() > i; i++) {
                                String rewardName = (String) ultraSecret.get(i);
                                if (rewardName.contains("<player>")) {
                                    rewardName = rewardName.replace("<player>", ChatColor.stripColor(player.getName()));
                                }
                                Configs.loadLists();
                                while (rewardName.contains("<") && rewardName.contains(">")) {
                                    String listName = rewardName.substring(rewardName.indexOf("<") + 1, rewardName.indexOf(">"));
                                    List list = Configs.getLists().getStringList(listName);
                                    int randomNum = ThreadLocalRandom.current().nextInt(0, list.size());
                                    rewardName = rewardName.replace("<" + listName + ">", (CharSequence) list.get(randomNum));
                                }
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), rewardName);
                                Messaging.log("Quests ran: " + rewardName);
                            }
                        }
                    }

                    meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "Claimed");
                    meta.setLore(lore);
                    itemStack.setItemMeta(meta);
                    Messaging.sendMessage(player, "You guessed the code correctly!");
                    return true;
                }
                Messaging.sendMessage(player, "Please hold a valid quest scroll in your main hand to run this command");
                return true;
            }

            // ------------------------------------------------------
            // ------------------ Claim Quest Path ------------------
            // ------------------------------------------------------
            if (args[0].equalsIgnoreCase("path")) {
                if (args.length > 1) {
                    if (Scroll.isScrollMain(player) || Scroll.isScrollOff(player)) {
                        ItemMeta mainHandItem = null;
                        if (Scroll.isScrollMain(player)) {
                            mainHandItem = player.getInventory().getItemInMainHand().getItemMeta();
                        }
                        if (Scroll.isScrollOff(player)) {
                            mainHandItem = player.getInventory().getItemInOffHand().getItemMeta();
                        }
                        String location;
                        NamespacedKey key = new NamespacedKey(Quests.getInstance(), "rewardLocation");
                        if (mainHandItem.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
                            location = mainHandItem.getPersistentDataContainer().get(key, PersistentDataType.STRING);
                            Configs.load(location.split("_")[0]);
                        } else {
                            Messaging.sendMessage(player, "Er, this quest isn't real, strange.");
                            return true;
                        }
                        key = new NamespacedKey(Quests.getInstance(), "expiry");
                        if (mainHandItem.getPersistentDataContainer().has(key, PersistentDataType.LONG)) {
                            if (Configs.get().getString(location.split("_")[1] + ".expiry") != null) {
                                long expiry = Long.valueOf(Configs.get().getString(location.split("_")[1] + ".expiry"));
                                key = new NamespacedKey(Quests.getInstance(), "expiry");
                                expiry += mainHandItem.getPersistentDataContainer().get(key, PersistentDataType.LONG);
                                if (expiry <= System.currentTimeMillis()) {
                                    Messaging.sendMessage(player, "This scroll has expired. It can no longer be claimed");
                                    return true;
                                }
                            }
                        }
                        if (Configs.get().getStringList(location.split("_")[1] + ".path." + args[1]).size() < 1) {
                            Messaging.sendMessage(player, "Specify a valid path like /quest path 1");
                            return true;
                        }
                        return Claim.claim(player, true, ".path." +args[1], false);
                    }
                } else {
                    Messaging.sendMessage(player, "Specify a path like /quest path 2");
                    return true;
                }
            }

            // ------------------------------------------------------
            // -------------------- List Quests ---------------------
            // ------------------------------------------------------
            Configs.loadGlobal();
            if (args[0].equalsIgnoreCase("list")) {
                String boldGold = ChatColor.GOLD + "" + ChatColor.BOLD + "";
                String boldRed = ChatColor.RED + "" + ChatColor.BOLD + "";
                String boldGreen = ChatColor.GREEN + "" + ChatColor.BOLD + "";
                //Messaging.sendUnformattedMessage(player, boldGold + "---Quests available to collect---");
                //Messaging.sendUnformattedMessage(player, boldGreen + "Green" + white + " - Available | " + boldRed + "Red" + white + " - Unavailable");

                ArrayList<ItemStack> items = new ArrayList<>();

                for (String s : Configs.getGlobal().getKeys(true)) {
                    if (s.split("\\.").length == 2 && s.split("\\.")[0].equals("commands")) {
                        String command = s.split("\\.")[1];
                        String dateFormat = Configs.getGlobal().getString("commands." + command + ".dateFormat");
                        String permissionName = Configs.getGlobal().getString("commands." + command + ".permissionName");
                        String description = Configs.getGlobal().getString("commands." + command + ".description");

                        SimpleDateFormat df = new SimpleDateFormat(dateFormat);
                        String date = df.format(new Date());

                        if (!player.hasPermission("quest." + permissionName + "." + date)) {
                            //Messaging.sendUnformattedMessage(player, boldGreen + "/quest " + command + " - " + white + description);
                            items.add(Gui.createGuiItem(Quests.getInstance(), Material.PAPER, boldGreen + "/quest " + command, "quest " + command + "&quest list", white + description, white + "Click to collect the scroll!"));
                        } else {
                            //Messaging.sendUnformattedMessage(player, boldRed + "/quest " + command + " - " + white + description);
                            items.add(Gui.createGuiItem(Quests.getInstance(), Material.RED_STAINED_GLASS_PANE, boldRed + "/quest " + command, "", white + description, white + "Scroll is unavailable, check back later!"));
                        }
                    }
                }
                Gui inv = new Gui(items, boldGold + "Quests!", Quests.getInstance());
                getServer().getPluginManager().registerEvents(inv, Quests.getInstance());
                inv.openInventory(player);
                return true;
            }

            // ------------------------------------------------------
            // ------------------ Custom Commands -------------------
            // ------------------------------------------------------
            String command = args[0].toLowerCase();
            if (Configs.getGlobal().getString("commands." + command + ".dateFormat") != null) {
                String dateFormat = Configs.getGlobal().getString("commands." + command + ".dateFormat");
                String permissionName = Configs.getGlobal().getString("commands." + command + ".permissionName");
                String permissionCooldown = Configs.getGlobal().getString("commands." + command + ".permissionCooldown");
                String quest = Configs.getGlobal().getString("commands." + command + ".quest");
                String denyMessage = Configs.getGlobal().getString("commands." + command + ".denyMessage");

                SimpleDateFormat df = new SimpleDateFormat(dateFormat);
                String date = df.format(new Date());

                if (player.hasPermission("quest." + permissionName + "." + date)) {
                    Messaging.sendMessage(player, denyMessage);
                    return true;
                }

                String[] genStuff = {quest.split("_")[0], quest.split("_")[1], player.getName()};

                if (Generators.genScroll(genStuff, player)) {
                    Perms.setPerm(player.getName(), "quest." + permissionName + "." + date, permissionCooldown);
                    //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " permission settemp quest." + permissionName + "." + date + " true " + permissionCooldown);
                    return true;
                }

                return true;
            }

            Messaging.sendMessage(player, "Please use a valid quests command");
        } else {
            Messaging.log("A player must run this command");
        }
        return true;
    }
}
