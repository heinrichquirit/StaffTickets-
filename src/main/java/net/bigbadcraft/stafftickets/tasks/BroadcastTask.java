package main.java.net.bigbadcraft.stafftickets.tasks;

import main.java.net.bigbadcraft.stafftickets.TicketPlugin;
import main.resources.Permission;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * User: Heinrich Quirit
 * Last Modified: 9/26/13
 * Time: 11:16 PM
 */
public class BroadcastTask extends BukkitRunnable {

    private ChatColor BLUE = ChatColor.BLUE;
    private ChatColor WHITE = ChatColor.WHITE;

    private TicketPlugin plugin;

    public BroadcastTask(TicketPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (players.hasPermission(Permission.STAFF.getPerm())) {
                if (plugin.methods.isNotEmpty()) {
                    players.sendMessage(plugin.methods.noticeHeader());
                    plugin.methods.sendTicketList(players);
                } else {
                    players.sendMessage(BLUE + "Notice" + WHITE + " - There are no tickets available.");
                }
            }
        }
    }
}
