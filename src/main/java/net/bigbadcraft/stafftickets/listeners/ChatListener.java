package main.java.net.bigbadcraft.stafftickets.listeners;

import main.java.net.bigbadcraft.stafftickets.TicketPlugin;
import main.resources.net.bigbadcraft.stafftickets.Methods;
import main.resources.net.bigbadcraft.stafftickets.Perm;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.beans.EventHandler;

/**
 * User: Heinrich Quirit
 * Last Modified: 9/25/13
 * Time: 6:11 PM
 */
public class ChatListener implements Listener {

    private ChatColor RED = ChatColor.RED;

    private Methods methods;
    private TicketPlugin plugin;

    public ChatListener(TicketPlugin plugin) {
        this.plugin = plugin;
        this.methods = plugin.methods;
    }

    @org.bukkit.event.EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onChat(final AsyncPlayerChatEvent event) {
       final Player player = event.getPlayer();
       for (String message : plugin.getConfig().getStringList("ticket-list.trigger")) {
           if (event.getMessage().contains(message)) {
               if (!methods.hasTicket(player) && !player.hasPermission(Perm.PERM)) {
                   methods.notifyStaff(RED + player.getName() + ": " + event.getMessage());
                   methods.createTicket(player, event.getMessage());
                   if (plugin.getConfig().getBoolean("ticket-list.log-ticket-information")) {
                       methods.logTicket(plugin.ticketFile, player.getName(), event.getMessage(), player.getLocation());
                   }
               }
           }
       }
    }

}
