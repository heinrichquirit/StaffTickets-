package main.java.net.bigbadcraft.stafftickets.listeners;

import main.java.net.bigbadcraft.stafftickets.TicketPlugin;
import main.resources.ConfigPaths;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * User: Heinrich Quirit
 * Last Modified: 9/25/13
 * Time: 6:12 PM
 */
public class QuitListener implements Listener {

    private TicketPlugin plugin;

    public QuitListener(TicketPlugin plugin) {
        this.plugin = plugin;
    }

    @org.bukkit.event.EventHandler
    public void onQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        if (plugin.getConfig().getBoolean(ConfigPaths.DELETE_ON_LEAVE)) {
            if (plugin.methods.hasTicket(player)) {
                plugin.methods.deleteTicket(player);
                plugin.methods.helpopDelete(player);
            }
        }
    }
}
