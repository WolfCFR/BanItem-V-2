package com.abcalvin.BanItem;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.abcalvin.BanItem.UpdateChecker.UpdateResult;
import com.abcalvin.BanItem.UpdateChecker.UpdateType;

public class main extends JavaPlugin{
	public final Logger log = Logger.getLogger("Minecraft");
	public final PlayerListener pl = new PlayerListener(this);
	public final BlockListener bl = new BlockListener(this);
	public final WorldScanner ws = new WorldScanner(this);
	public ArrayList<String> all = new ArrayList<String>();
	public ArrayList<String> place = new ArrayList<String>();
	public ArrayList<String> pickup = new ArrayList<String>();
	public ArrayList<String> world = new ArrayList<String>();
	public ArrayList<String> interact = new ArrayList<String>();
	public PluginDescriptionFile pdfFile = this.getDescription();
	public ArrayList<String> click = new ArrayList<String>();
	public ArrayList<String> br = new ArrayList<String>();
	public ArrayList<String> worlds = new ArrayList<String>();
	public final String banitem = ChatColor.RED + "[" + ChatColor.GRAY
			+ "BanItem" + ChatColor.RED + "] " +ChatColor.AQUA;

	@Override
	public void onEnable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		PluginManager pm = getServer().getPluginManager();
		log.info("[BanItem] "+pdfFile.getName() + " is now Enabled!");
		pm.registerEvents(this.pl, this);
		pm.registerEvents(this.bl, this);
		pm.registerEvents(this.ws, this);
        if(this.getConfig().getStringList("Worlds").size() == 0){
        	worlds.add("world");
        	worlds.add("world_nether");
        	worlds.add("world_the_end");
        	this.reloadConfig();
        	this.getConfig().set("Worlds", worlds);
        	this.saveConfig();
        }
        if (!new File(getDataFolder(), "config.yml").exists()) {
            saveDefaultConfig();
            log.info("A new config.yml is generated for BanItem.");
        }else{
        	log.info("BanItem's config.yml detected");
        }
		all = (ArrayList<String>) this.getConfig().getStringList("Blacklist");
		place = (ArrayList<String>) this.getConfig().getStringList("Blacklist Placement");
		pickup = (ArrayList<String>) this.getConfig().getStringList("Blacklist Pickup");
		interact = (ArrayList<String>) this.getConfig().getStringList("Blacklist Interaction");
		click = (ArrayList<String>) this.getConfig().getStringList("Blacklist Click");
		world = (ArrayList<String>) this.getConfig().getStringList("Blacklist World");
		br = (ArrayList<String>) this.getConfig().getStringList("Blacklist Break");
		worlds = (ArrayList<String>) this.getConfig().getStringList("Worlds");
		if(this.getConfig().getBoolean("UpdateChecker")){
			UpdateChecker updater = new UpdateChecker(this, 59068, this.getFile(), UpdateType.NO_DOWNLOAD, true);
			if (updater.getResult() == UpdateResult.UPDATE_AVAILABLE) {
				this.getLogger().info("New version available! " + updater.getLatestName()+" at " + updater.getLatestFileLink());
			}
		}

	}
	@Override
	public void onDisable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		
		log.info(pdfFile.getName() + " is now Disabled!");
    	this.reloadConfig();
    		this.getConfig().set("Worlds", worlds);
			this.getConfig().set("Blacklist", all);
			this.getConfig().set("Blacklist Placement", place);
			this.getConfig().set("Blacklist Pickup", pickup);
			this.getConfig().set("Blacklist Interaction", interact);
			this.getConfig().set("Blacklist Click", click);
			this.getConfig().set("Blacklist Break", br);
			this.getConfig().set("Blacklist World", world);
		this.saveConfig();
	}
	// THIS IS FOR /banitem ADD command!
	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {
		
		if(sender instanceof Player){
			
			Player player = (Player)sender;
			
			ItemStack itemHand = player.getItemInHand();
			
			int id = itemHand.getType().getId();
			
			byte data = itemHand.getData().getData();
			
			itemcheck itemmethod = new itemcheck(all, id, data);
			
			//The Reason for item banned
			StringBuilder sb = new StringBuilder();
			for (int i = 1; i < args.length; i++){
			sb.append(args[i]).append(" ");
			}
			String allArgs = sb.toString();
			
			if(commandLabel.equalsIgnoreCase("BanItem") && args.length==0){
				player.sendMessage(banitem + " Version 2.3");
			}
			else if(commandLabel.equalsIgnoreCase("BanItem") && args[0].equalsIgnoreCase("add") && args.length == 1){
				if(player.hasPermission("banitem.add") || player.hasPermission("banitem.*") || player.isOp()){
					player.sendMessage(banitem + ChatColor.DARK_RED + " Wrong use of Command! /banitem add <reason>...");
				}else{
					player.sendMessage(banitem
							+ ChatColor.DARK_RED
							+ "You do not have permission to use this command");
				}
			}else if(commandLabel.equalsIgnoreCase("BanItem") && args[0].equalsIgnoreCase("add") && args.length > 1){
				if(player.hasPermission("banitem.add") || player.hasPermission("banitem.*") || player.isOp()){
					if(itemmethod.getnumber() == 0){
						
						all.add(id + ":" + data + ":" + allArgs);
						
						player.sendMessage(banitem + " Item ["+ id + ":" + data + "] is added to the ban list with the reason:");
						
						player.sendMessage(banitem + " "+allArgs );
						
						this.reloadConfig();
						this.getConfig().set("Blacklist", all);
						this.saveConfig();
						
						}else{
							
							player.sendMessage(banitem + " This item [" + id + ":" + data +"] is already banned because:");
							
							player.sendMessage(banitem + " "+ itemmethod.getReason() );
							
						}
				}else{
					player.sendMessage(banitem
							+ ChatColor.DARK_RED
							+ "You do not have permission to use this command");
				}
			}else if(commandLabel.equalsIgnoreCase("BanItem") && args[0].equalsIgnoreCase("check") && args.length ==1){
				if(player.hasPermission("banitem.*") || player.isOp() || player.hasPermission("banitem.check")){
					if(itemmethod.getnumber() == 1) {
						
						player.sendMessage(banitem + " [" +itemmethod.getId() +":"+itemmethod.getData()+ "] is banned because:");
						
						player.sendMessage(banitem +" "+ itemmethod.getReason());
						
					}else{
						
						player.sendMessage(banitem + " Not Banned");
						
					}
				}else{
					player.sendMessage(banitem + ChatColor.DARK_RED + "You do not have permission to use this command");
				}
			}else if(commandLabel.equalsIgnoreCase("BanItem") && (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("del"))
					&& args.length == 1){
				if(player.hasPermission("banitem.remove") || player.hasPermission("banitem.del") || player.hasPermission("banitem.*") 
						|| player.isOp()){
					if(itemmethod.getnumber()==1){
						
						all.remove(itemmethod.getId() + ":" + itemmethod.getData() + ":" + itemmethod.getReason());
						
						player.sendMessage(banitem + " Removed item from ban list");
						this.reloadConfig();
						this.getConfig().set("Blacklist", all);
						this.saveConfig();

						
					}else{
						
						player.sendMessage(banitem + " Does not exist in the ban list.");
						
					}
					
				}else{
					player.sendMessage(banitem
							+ ChatColor.DARK_RED
							+ "You do not have permission to use this command");
				}
				
			}else if(commandLabel.equalsIgnoreCase("BanItem") && args[0].equalsIgnoreCase("toggle") && args.length == 1){
				if(player.hasPermission("banitem.toggle") || player.hasPermission("banitem.*")){
					player.sendMessage(banitem + ChatColor.DARK_RED + "Params needed. Param list: conf");
				}else{
					player.sendMessage(banitem
							+ ChatColor.DARK_RED
							+ "You do not have permission to use this command");
				}
				
			}else if(commandLabel.equalsIgnoreCase("BanItem") && args[0].equalsIgnoreCase("toggle") && args[1].equalsIgnoreCase("conf")){
				if(player.hasPermission("banitem.toggle.conf") || player.hasPermission("banitem.toggle") || player.hasPermission("banitem.*")
						|| player.isOp()){
					if(this.getConfig().getBoolean("Confiscate")== true) {
						this.getConfig().set("Confiscate", false);
						player.sendMessage(banitem + "Toggled False");
					}else{
						this.getConfig().set("Confiscate", true);
						player.sendMessage(banitem + "Toggled True");
					}
				}else{
					player.sendMessage(banitem
							+ ChatColor.DARK_RED
							+ "You do not have permission to use this command");
				}
				
			}else if(commandLabel.equalsIgnoreCase("BanItem") && args[0].equalsIgnoreCase("reload") && args.length ==1){
				if(player.hasPermission("banitem.reload") || player.hasPermission("banitem.reload") || player.hasPermission("banitem.*")){
					this.reloadConfig();
					this.saveConfig();
					all = (ArrayList<String>) this.getConfig().getStringList("Blacklist");
					place = (ArrayList<String>) this.getConfig().getStringList("Blacklist Placement");
					pickup = (ArrayList<String>) this.getConfig().getStringList("Blacklist Pickup");
					interact = (ArrayList<String>) this.getConfig().getStringList("Blacklist Interaction");
					click = (ArrayList<String>) this.getConfig().getStringList("Blacklist Click");
					br = (ArrayList<String>) this.getConfig().getStringList("Blacklist Break");
					worlds = (ArrayList<String>) this.getConfig().getStringList("Worlds");
					world = (ArrayList<String>) this.getConfig().getStringList("Blacklist World");
					player.sendMessage(banitem + " Reload Complete.");
				}else{
					player.sendMessage(banitem
							+ ChatColor.DARK_RED
							+ "You do not have permission to use this command");
				}
			}else{
			player.sendMessage(banitem+ ChatColor.DARK_RED+ "Use /help banitem.");
			}
		}else{
			
		if(commandLabel.equalsIgnoreCase("BanItem") && args[0].equalsIgnoreCase("reload") && args.length ==1){
				this.reloadConfig();
				this.saveConfig();
				all = (ArrayList<String>) this.getConfig().getStringList("Blacklist");
				place = (ArrayList<String>) this.getConfig().getStringList("Blacklist Placement");
				pickup = (ArrayList<String>) this.getConfig().getStringList("Blacklist Pickup");
				interact = (ArrayList<String>) this.getConfig().getStringList("Blacklist Interaction");
				click = (ArrayList<String>) this.getConfig().getStringList("Blacklist Click");
				br = (ArrayList<String>) this.getConfig().getStringList("Blacklist Break");
				worlds = (ArrayList<String>) this.getConfig().getStringList("Worlds");
				world = (ArrayList<String>) this.getConfig().getStringList("Blacklist World");
				sender.sendMessage(banitem + " Reload Complete.");
		}
		}
		return false;
	}
}
