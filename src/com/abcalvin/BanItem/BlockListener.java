package com.abcalvin.BanItem;


import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class BlockListener implements Listener {
	public static main plugin;

	public BlockListener(main instance) {
		plugin = instance;
	}
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e){
		Player player = e.getPlayer();
		Block item = e.getBlock();
		int id = item.getType().getId();
		byte data = item.getData();
		if(plugin.worlds.contains(player.getWorld().getName())){
			if(player.hasPermission("banitem.bypass." + id + ":" + data) || player.hasPermission("banitem.break." + id + ":*")
					|| player.hasPermission("banitem.break." + id + ":" + data) || player.hasPermission("banitem.bypass." + id + ":*") 
					|| player.isOp() || player.hasPermission("banitem.*")) {
			}else{
				itemcheck itemmethod = new itemcheck(plugin.all, id,data);
				itemcheck itemmethod1 = new itemcheck(plugin.br, id,data);
				if(itemmethod.getnumber() == 1 || itemmethod1.getnumber() == 1){
					e.setCancelled(true);
					player.sendMessage(plugin.banitem + ChatColor.RED + "This item [" +id + ":"+ data + "] is banned from being Broken because:");
					if(itemmethod.getReason() != null){
						player.sendMessage(plugin.banitem + itemmethod.getReason() );
					}else{
						player.sendMessage(plugin.banitem + itemmethod.getReason() );	
					}
				}
			}
		}
	}
	@EventHandler
	private void onPlayerPlacement(BlockPlaceEvent e) {
		Player player = e.getPlayer();
		ItemStack item = player.getItemInHand();
		int id = item.getType().getId();
		byte data = item.getData().getData();
		if(plugin.worlds.contains(player.getWorld().getName())){
			if(player.hasPermission("banitem.bypass." + id + ":" + data) || player.hasPermission("banitem.place." + id + ":*")
					|| player.hasPermission("banitem.place." + id + ":" + data) || player.hasPermission("banitem.bypass." + id + ":*")
					|| player.isOp() || player.hasPermission("banitem.*")) {
			}else{
				itemcheck itemmethod = new itemcheck(plugin.all, id, data);
				itemcheck itemmethod1 = new itemcheck(plugin.place, id, data);
				if(itemmethod.getnumber() == 1 || itemmethod1.getnumber() == 1){
					e.setCancelled(true);
					player.sendMessage(plugin.banitem + ChatColor.RED + "This item [" +id + ":"+ data + "] is banned from being Placed because:");
					if(itemmethod.getReason() != null){
						player.sendMessage(plugin.banitem + itemmethod.getReason() );
					}else{
						player.sendMessage(plugin.banitem + itemmethod.getReason() );	
					}
				}
			}
		}
	}
}
