package com.abcalvin.BanItem;


import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {
	
	public static main plugin;

	public PlayerListener(main instance) {
		plugin = instance;
	}

	@EventHandler
	public void onInteractEvent(PlayerInteractEvent e){

		Player player = e.getPlayer();
		ItemStack item = player.getItemInHand();
		int id = item.getType().getId();
		byte data = item.getData().getData();
		if(plugin.worlds.contains(player.getWorld().getName())){
		if(player.hasPermission("banitem.bypass." + id + ":" + data) || player.hasPermission("banitem.int." + id + ":*")
				 || player.hasPermission("banitem.int." + id + ":" + data) || player.hasPermission("banitem.bypass." + id + ":*")
				 || player.isOp() || player.hasPermission("banitem.*")) {
		}else{
			itemcheck itemmethod = new itemcheck(plugin.all, id, data);
			itemcheck itemmethod1 = new itemcheck(plugin.interact, id, data);
			if(e instanceof Player){
			}else{
					if(itemmethod.number == 1 || itemmethod1.number == 1){
						if (plugin.getConfig().getBoolean("Confiscate")){
							e.getPlayer().setItemInHand(new ItemStack(Material.AIR, 1));
							e.setCancelled(true);
							player.sendMessage(plugin.banitem + ChatColor.RED + "This item [" +id + ":"+ data + "] is confiscated because:");
							if(itemmethod.Reason != null){
							player.sendMessage(plugin.banitem + itemmethod.Reason );
							}else{
							player.sendMessage(plugin.banitem + itemmethod1.Reason );	
							}
						}else{
							e.setCancelled(true);
							player.sendMessage(plugin.banitem + ChatColor.RED + "This item [" +id + ":"+ data + "] is banned from being Interacted because:");
							if(itemmethod.Reason != null){
							player.sendMessage(plugin.banitem + itemmethod.Reason );
							}else{
							player.sendMessage(plugin.banitem + itemmethod1.Reason );	
							}
						}	
					}
				}
			}
		}
	}
	@EventHandler
	private void onClick(InventoryClickEvent e) {
		Player player = (Player) e.getWhoClicked();
		ItemStack item = e.getCurrentItem();
		if(item == null){
			return;
		}
		int id = item.getType().getId();
		byte data = item.getData().getData();
		if(plugin.worlds.contains(player.getWorld().getName())){
		if(player.hasPermission("banitem.bypass." + id + ":" + data) || player.hasPermission("banitem.click." + id + ":*")
				 || player.hasPermission("banitem.click." + id + ":" + data) || player.hasPermission("banitem.bypass." + id + ":*")
				 || player.isOp() || player.hasPermission("banitem.*")) {
		}else{
		itemcheck itemmethod = new itemcheck(plugin.all, id, data);
		itemcheck itemmethod1 = new itemcheck(plugin.click, id, data);
		if(itemmethod.number == 1 || itemmethod1.number == 1){
			if (plugin.getConfig().getBoolean("Confiscate")){
				e.setCurrentItem(new ItemStack(Material.AIR, 1));
				e.setCancelled(true);
				player.sendMessage(plugin.banitem + ChatColor.RED + "This item [" +id + ":"+ data + "] is confiscated because:");
				if(itemmethod.Reason != null){
				player.sendMessage(plugin.banitem + itemmethod.Reason );
				}else{
				player.sendMessage(plugin.banitem + itemmethod1.Reason );	
				}
			}else{
				e.setCancelled(true);
				player.sendMessage(plugin.banitem + ChatColor.RED + "This item [" +id + ":"+ data + "] is banned from being Clicked because:");
				if(itemmethod.Reason != null){
				player.sendMessage(plugin.banitem + itemmethod.Reason );
				}else{
				player.sendMessage(plugin.banitem + itemmethod1.Reason );	
				}
			}
				}
			}
		}

	}
	@EventHandler
	private void onPickup(PlayerPickupItemEvent e) {
		Player player = e.getPlayer();
		ItemStack item = e.getItem().getItemStack();
		int id = item.getType().getId();
		byte data = item.getData().getData();
		if(plugin.worlds.contains(player.getWorld().getName())){
		if(player.hasPermission("banitem.bypass." + id + ":" + data) || player.hasPermission("banitem.pickup." + id + ":*")
				 || player.hasPermission("banitem.pickup." + id + ":" + data) || player.hasPermission("banitem.bypass." + id + ":*")
				 || player.isOp() || player.hasPermission("banitem.*")) {
		}else{
		itemcheck itemmethod = new itemcheck(plugin.all, id, data);
		itemcheck itemmethod1 = new itemcheck(plugin.pickup, id, data);
		if(itemmethod.number == 1 || itemmethod1.number == 1){
			e.setCancelled(true);
			player.sendMessage(plugin.banitem + ChatColor.RED + "This item [" +id + ":"+ data + "] is banned from being Picked up because:");
			if(itemmethod.Reason != null){
			player.sendMessage(plugin.banitem + itemmethod.Reason );
			}else{
			player.sendMessage(plugin.banitem + itemmethod1.Reason );	
			}
			}
		}
		}
	}
	@EventHandler
	private void onLogin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		if(player.isOp() && plugin.getConfig().getBoolean("UpdateChecker")){
			if(plugin.UpdateChecker.updateNeeded()){
				player.sendMessage(plugin.banitem + " Version: " +plugin.UpdateChecker.getVersion().toString()+ " is now available for update at " + plugin.UpdateChecker.getLink().toString());
			}
		}
	}
	
}
