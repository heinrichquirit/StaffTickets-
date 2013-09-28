package main.java.net.bigbadcraft.stafftickets.commands;

import main.java.net.bigbadcraft.stafftickets.TicketPlugin;
import main.resources.net.bigbadcraft.stafftickets.Methods;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;

/**
 * User: Heinrich Quirit
 * Last Modified: 9/25/13
 * Time: 6:14 PM
 */
public class HelpopCommand implements CommandExecutor {

    private ChatColor RED = ChatColor.RED;
    private ChatColor BLUE = ChatColor.BLUE;
    private ChatColor WHITE = ChatColor.WHITE;

    private TicketPlugin plugin;
    private Methods methods;

    private File ticketFile;

    public HelpopCommand(TicketPlugin plugin) {
        this.plugin = plugin;
        this.methods = plugin.methods;
        this.ticketFile = plugin.ticketFile;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmdObj, String lbl, String[] strings) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (cmdObj.getName().equalsIgnoreCase("helpop")) {
                return helpop(player, strings);
            }
        }
        return true;
    }

    private boolean helpop(Player player, String[] strings) {
        if (strings.length == 0) {
            player.sendMessage(RED + "Incorrect syntax, usage: /helpop <message>");
            return true;
        }
        if (strings.length > 0) {
            String name = player.getName();
            String message = StringUtils.join(strings, ' ', 0, strings.length);
            if (!methods.hasTicket(player)) {
                methods.createTicket(player, message);
                methods.logTicket(ticketFile, name, message, player.getLocation());
                methods.notifyStaff(RED + name + ": " + message);
                player.sendMessage(BLUE + "Successfully submitted your ticket, position" + WHITE + ": " + methods.getTickets());
            } else {
                player.sendMessage(BLUE + "You have one outstanding ticket in queue, position" + WHITE + ": " + methods.getTickets());
                player.sendMessage("Outstanding Ticket: " + BLUE + methods.getOutstandingTicket(player));
            }
        }
        return true;
    }
}
