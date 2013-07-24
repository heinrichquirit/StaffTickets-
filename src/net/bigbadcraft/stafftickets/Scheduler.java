package net.bigbadcraft.stafftickets;

import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Scheduler extends BukkitRunnable {
	public void run() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player.hasPermission(Permission.PERMISSION)) {
				if (EventListener.playerTicket.isEmpty()) {
					player.sendMessage("§9Notice§f - There are no tickets available.");
				} else {
					player.sendMessage("§9+-------- §f[ §9Notice" + " §f(" + EventListener.playerTicket.size() + ")" + " ]§9 --------+");
					for (Entry<String, String> entry : EventListener.playerTicket.entrySet()) {
						player.sendMessage("§9" + entry.getKey() + "§f: "  + entry.getValue());
					}
				}
			}
		}
	}
}

