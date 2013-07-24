package net.bigbadcraft.stafftickets;

import java.util.HashMap;
import java.util.Map;

import net.bigbadcraft.stafftickets.resources.DataStorage;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventListener implements Listener {
	
	public static Map<String, String> playerTicket;
	
	private Main plugin;
	public EventListener(Main plugin) {
		this.plugin = plugin;
		
		EventListener.playerTicket = new HashMap<String, String>();
	}
	
	// Storing the triggered ticket
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		final Player player = event.getPlayer();
		
		for (String message : plugin.getConfig().getStringList("trigger")) {
			
			if (event.getMessage().contains(message)) {
				
				if (!playerTicket.containsKey(event.getPlayer().getName()) && !event.getPlayer().hasPermission(Permission.PERMISSION)) {
					Bukkit.broadcast("§c" + event.getPlayer().getName() + ": " + event.getMessage(), Permission.PERMISSION);
					playerTicket.put(event.getPlayer().getName(), event.getMessage());
					
					if (plugin.getConfig().getBoolean("ticket-list.log-ticket-information")) {
						
						int x = (int) Math.round(player.getLocation().getX());
						int y = (int) Math.round(player.getLocation().getY());
						int z = (int) Math.round(player.getLocation().getZ());
						
						DataStorage.logData(player.getName(), event.getMessage(), x, y, z);
					}
					
				}
			}
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		if (plugin.getConfig().getBoolean("deleteOnPlayerLeave")) {
			if (playerTicket.containsKey(event.getPlayer().getName())) {
				playerTicket.remove(event.getPlayer().getName());
			}
		}
	}
}
