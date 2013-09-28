package main.java.net.bigbadcraft.stafftickets.commands;

import main.java.net.bigbadcraft.stafftickets.TicketPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * User: Heinrich Quirit
 * Last Modified: 9/27/13
 * Time: 4:40 PM
 */
public class MyTicketsCommand implements CommandExecutor {

    private TicketPlugin plugin;

    public MyTicketsCommand(TicketPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmdObj, String lbl, String[] strings) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (cmdObj.getName().equalsIgnoreCase("mytickets")) {
                return myTickets(player, strings);
            }
        }
        return true;
    }

    private boolean myTickets(Player player, String[] strings) {
         if (strings.length == 0) {
             if (plugin.methods.hasHelpop(player)) {
                plugin.methods.displayTicket(player, player);
             } else {
                 player.sendMessage(ChatColor.BLUE + "You do not have any helpop tickets.");
             }
         } else if (strings.length == 1) {
             if (strings[0].equalsIgnoreCase("del")) {
                 player.sendMessage(ChatColor.RED + "Incorrect syntax, usage: /mytickets del <index>");
             }
         } else if (strings.length == 2) {
             try {
                 int index = Integer.valueOf(strings[1]);
                 if (strings[0].equalsIgnoreCase("del")) {
                     if (plugin.methods.hasHelpop(player)) {
                         if (index <= plugin.methods.getTicketAmount(player)) {
                             plugin.methods.helpopDelete(player, index);
                             player.sendMessage("Deleted your ticket @index " + ChatColor.BLUE + index + ".");
                         } else {
                             player.sendMessage("You only have " + ChatColor.BLUE + plugin.methods.getTicketAmount(player) + ChatColor.WHITE + " tickets.");
                         }
                     }
                 }
             } catch (NumberFormatException e) {
                 player.sendMessage(ChatColor.RED + "Error: You must use a number for your index argument.");
             }
         }
         return true;
    }
}
