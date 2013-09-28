package main.java.net.bigbadcraft.stafftickets.listeners;

import main.java.net.bigbadcraft.stafftickets.TicketPlugin;
import main.resources.Methods;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * User: Heinrich Quirit
 * Last Modified: 9/27/13
 * Time: 2:07 AM
 */
public class CommandListener implements Listener {

    private Methods methods;
    private TicketPlugin plugin;

    public CommandListener(TicketPlugin plugin) {
        this.plugin = plugin;
        this.methods = plugin.methods;
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        final Player player = event.getPlayer();
        String message = event.getMessage();
        if (message.length() >= 9) {
            if (methods.isHelpopCmd(message)) {
                String trimmedMsg = message.substring(8, message.length());
                // Need to create helpop ticket
                methods.helpopCreate(player, trimmedMsg);
                // Let the player know that they have created a ticket - Display /ticket view
            }
        }
    }
}
