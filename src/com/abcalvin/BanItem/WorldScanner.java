package com.abcalvin.BanItem;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

public class WorldScanner implements Listener {
	
	public static main plugin;

	public WorldScanner(main instance) {
		plugin = instance;
	}
    @EventHandler (priority = EventPriority.HIGHEST)
    public void onWorldLoad(ChunkLoadEvent e) {
    	if(plugin.getConfig().getStringList("Blacklist World").size() >0){
    		if(!e.isNewChunk()){
    			Chunk c = e.getChunk();
    			World world = e.getWorld();
    			int bx = c.getX() << 4;
    			int bz = c.getZ() << 4;   
    			for (int xx = bx; xx < bx + 16; xx++) {
    				for (int zz = bz; zz < bz + 16; zz++) {
    					for (int yy = 0; yy < 128; yy++) {
    						int id = world.getBlockTypeIdAt(xx, yy, zz);
    						byte data = world.getBlockAt(xx, yy, zz).getData();
    						if ( plugin.world.contains(id +":" + data) || plugin.world.contains(id+":-1")) {
    							plugin.log.info("A Banned Item [" + id +":"+ data + "] is Detected at x:" +xx +  ", y:"+yy +", z:"+ zz+", World:"+ world.getName()+"Removed");
    							Player[] players = Bukkit.getOnlinePlayers();
    	                        for(Player op: players){
                                    if(op.isOp() || op.hasPermission("banitem.*")) {
                                            op.sendMessage(plugin.banitem + "A Banned Item [" + id +":"+ data + "] is Detected at x: " +xx +  ", y: "+yy +", z: "+ zz+", World: "+ world.getName());
                                    }
    	                        }

    							if(plugin.getConfig().getBoolean("WorldBlockRemove")){
    								world.getBlockAt(xx, yy, zz).setType(Material.AIR);
    							}
    						}
    					}
    				}
    			}
    		}
    	}
    }
}