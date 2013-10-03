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

public class main extends JavaPlugin{
	public final Logger log = Logger.getLogger("Minecraft");
	public final PlayerListener pl = new PlayerListener(this);
	public final BlockListener bl = new BlockListener(this);
	public ArrayList<String> all = new ArrayList<String>();
	public ArrayList<String> place = new ArrayList<String>();
	public ArrayList<String> pickup = new ArrayList<String>();
	public ArrayList<String> interact = new ArrayList<String>();
	public PluginDescriptionFile pdfFile = this.getDescription();
	public ArrayList<String> click = new ArrayList<String>();
	public ArrayList<String> br = new ArrayList<String>();
	public ArrayList<String> worlds = new ArrayList<String>();
	public final String banitem = ChatColor.RED + "[" + ChatColor.GRAY
			+ "BanItem" + ChatColor.RED + "] " +ChatColor.AQUA;

	protected UpdateChecker UpdateChecker;
	

	@Override
	public void onEnable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		PluginManager pm = getServer().getPluginManager();
		log.info(pdfFile.getName() + " is now Enabled!");
		pm.registerEvents(this.pl, this);
		pm.registerEvents(this.bl, this);
        if (!new File(getDataFolder(), "config.yml").exists()) {
            saveDefaultConfig();
        }
        if(this.getConfig().getStringList("Worlds").size() == 0){
        	worlds.add("world");
        	worlds.add("world_nether");
        	worlds.add("world_the_end");
        	this.reloadConfig();
        	this.getConfig().set("Worlds", worlds);
        	this.saveConfig();
        }
		all = (ArrayList<String>) this.getConfig().getStringList("Blacklist");
		place = (ArrayList<String>) this.getConfig().getStringList("Blacklist Placement");
		pickup = (ArrayList<String>) this.getConfig().getStringList("Blacklist Pickup");
		interact = (ArrayList<String>) this.getConfig().getStringList("Blacklist Interaction");
		click = (ArrayList<String>) this.getConfig().getStringList("Blacklist Click");
		br = (ArrayList<String>) this.getConfig().getStringList("Blacklist Break");
		if(all.isEmpty()){
			this.reloadConfig();
			this.getConfig().set("Blacklist", all);
        	this.saveConfig();
		}
		if(place.isEmpty()){ 
			this.reloadConfig();
			this.getConfig().set("Blacklist Placement", place);
        	this.saveConfig();
		}
		if(pickup.isEmpty()){
			this.reloadConfig();
			this.getConfig().set("Blacklist Pickup", pickup);
        	this.saveConfig();
		}
		if(interact.isEmpty()){
			this.reloadConfig();
			this.getConfig().set("Blacklist Interaction", interact);
        	this.saveConfig();
		}
		if(click.isEmpty()){
			this.reloadConfig();
			this.getConfig().set("Blacklist Click", click);
        	this.saveConfig();
			
		}
		if(br.isEmpty()){ 
			this.reloadConfig();
			this.getConfig().set("Blacklist Break", br);
			this.saveConfig();
			
		}
		worlds = (ArrayList<String>) this.getConfig().getStringList("Worlds");
		this.UpdateChecker = new UpdateChecker(this, "http://dev.bukkit.org/bukkit-plugins/banitem/files.rss");

	}
	@Override
	public void onDisable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		log.info(pdfFile.getName() + " is now Disabled!");
		if(all.isEmpty()){
			this.getConfig().set("Blacklist", all);
		}
		if(place.isEmpty()){ 
			this.getConfig().set("Blacklist Placement", place);
		}
		if(pickup.isEmpty()){
			this.getConfig().set("Blacklist Pickup", pickup);
		}
		if(interact.isEmpty()){
			this.getConfig().set("Blacklist Interaction", interact);
		}
		if(click.isEmpty()){
			this.getConfig().set("Blacklist Click", click);
		}
		if(br.isEmpty()){ 
			this.getConfig().set("Blacklist Break", br);
			
		}
		this.reloadConfig();
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
				player.sendMessage(banitem + " Version 2.0");
			}
			else if(commandLabel.equalsIgnoreCase("BanItem") && args[0].equalsIgnoreCase("add") && args.length == 1){
				if(player.hasPermission("banitem.add") || player.hasPermission("banitem.*") || player.isOp()){
					player.sendMessage(banitem + ChatColor.DARK_RED + " Wrong use of Command! /banitem add <reason>...");
				}else{
					player.sendMessage(banitem
							+ ChatColor.DARK_RED
							+ "You do not have permission to use this command");
				}
			}
			else if(commandLabel.equalsIgnoreCase("BanItem") && args[0].equalsIgnoreCase("add") && args.length > 1){
				if(player.hasPermission("banitem.add") || player.hasPermission("banitem.*") || player.isOp()){
					if(itemmethod.number == 0){
						
						all.add(id + ":" + data + ":" + allArgs);
						
						player.sendMessage(banitem + " Item ["+ id + ":" + data + "] is added to the ban list with the reason:");
						
						player.sendMessage(banitem + " "+allArgs );
						
						this.reloadConfig();
						this.getConfig().set("Blacklist", all);
						this.saveConfig();
						
						}else{
							
							player.sendMessage(banitem + " This item [" + id + ":" + data +"] is already banned because:");
							
							player.sendMessage(banitem + " "+ itemmethod.Reason );
							
						}
				}else{
					player.sendMessage(banitem
							+ ChatColor.DARK_RED
							+ "You do not have permission to use this command");
				}
			}else if(commandLabel.equalsIgnoreCase("BanItem") && args[0].equalsIgnoreCase("check") && args.length ==1){
				if(player.hasPermission("banitem.*") || player.isOp() || player.hasPermission("banitem.check")){
					if(itemmethod.number == 1) {
						
						player.sendMessage(banitem + " [" +itemmethod.Id +":"+itemmethod.Data+ "] is banned because:");
						
						player.sendMessage(banitem +" "+ itemmethod.Reason);
						
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
					if(itemmethod.number==1){
						
						all.remove(itemmethod.Id + ":" + itemmethod.Data + ":" + itemmethod.Reason);
						
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
				
			}else if(commandLabel.equalsIgnoreCase("BanItem") && args[0].equalsIgnoreCase("toggle") && args[1].equalsIgnoreCase("conf") && args.length ==2){
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
				
			}else if(commandLabel.equalsIgnoreCase("BanItem") && args[0].equalsIgnoreCase("toggle")&& args.length ==1){
				if(player.hasPermission("banitem.toggle") || player.hasPermission("banitem.*")){
					player.sendMessage(banitem + ChatColor.DARK_RED + "Params needed. Param list: conf");
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
				sender.sendMessage(banitem + " Reload Complete.");
		}
		}
		return false;
	}
	
}
