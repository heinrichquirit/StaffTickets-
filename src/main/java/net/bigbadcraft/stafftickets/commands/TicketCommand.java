package main.java.net.bigbadcraft.stafftickets.commands;

import main.java.net.bigbadcraft.stafftickets.TicketPlugin;
import main.resources.net.bigbadcraft.stafftickets.Methods;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * User: Heinrich Quirit
 * Last Modified: 9/25/13
 * Time: 6:14 PM
 */
public class TicketCommand implements CommandExecutor {

    private ChatColor WHITE = ChatColor.WHITE;
    private ChatColor BLUE = ChatColor.BLUE;
    private ChatColor RED = ChatColor.RED;

    private TicketPlugin plugin;
    private Methods methods;

    public TicketCommand(TicketPlugin plugin) {
        this.plugin = plugin;
        this.methods = plugin.methods;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmdObj, String lbl, String[] strings) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (cmdObj.getName().equalsIgnoreCase("ticket")) {
                return ticket(player, strings);
            }
        }
        return true;
    }

    private boolean ticket(Player player, String[] strings) {
        if (strings.length == 0) {
            player.sendMessage(methods.helpHeader());
            player.sendMessage(methods.helpMenu());
            return true;
        } else if (strings.length == 1) {
            if (strings[0].equalsIgnoreCase("readfile")) {
                methods.readLoggedTickets(player);
            } else if (strings[0].equalsIgnoreCase("list")) {
                if (methods.isNotEmpty()) {
                    player.sendMessage(methods.ticketListHeader());
                    methods.sendTicketList(player);
                    player.sendMessage(methods.helpFooter());
                } else {
                    player.sendMessage(BLUE + "There are no tickets available.");
                }
            } else if (strings[0].equalsIgnoreCase("view")) {
                player.sendMessage(RED + "Incorrect syntax, usage: /ticket view <player>");
            } else if (strings[0].equalsIgnoreCase("tp")) {
                player.sendMessage(RED + "Incorrect syntax, usage: /ticket tp <player>");
            } else if (strings[0].equalsIgnoreCase("cl")) {
                player.sendMessage(RED + "Incorrect syntax, usage: /ticket cl <player>");
            } else if (strings[0].equalsIgnoreCase("del")) {
                player.sendMessage(RED + "Incorrect syntax, usage: /ticket del <player> or");
                player.sendMessage(RED + "Incorrecy syntax, usage: /ticket del <player> <index>");
            } else if (strings[0].equalsIgnoreCase("clear")) {
                if (methods.isNotEmpty()) {
                    methods.clearTickets();
                    methods.helpopClear();
                    player.sendMessage(BLUE + "Ticket list has been cleared.");
                } else {
                    player.sendMessage(BLUE + "Ticket list is already empty.");
                }
            }
        } else if (strings.length == 2) {
            Player target = Bukkit.getPlayer(strings[1]);
            if (target != null) {
                if (strings[0].equalsIgnoreCase("view")) {
                    if (methods.hasHelpop(target)) {
                        methods.displayTicket(player, target);
                    } else {
                        player.sendMessage(BLUE + target.getName() + WHITE + " has no helpop ticket(s).");
                    }
                } else if (strings[0].equalsIgnoreCase("tp")) {
                    if (methods.hasTicket(target)) {
                        methods.deleteTicket(target);
                        methods.helpopDelete(target);
                        player.sendMessage("Teleporting to " + BLUE + target.getName() + WHITE + "..");
                        player.teleport(target.getLocation());
                    } else {
                        player.sendMessage(BLUE + target.getName() + WHITE + " has no ticket(s).");
                    }
                } else if (strings[0].equalsIgnoreCase("cl")) {
                    if (methods.hasTicket(target)) {
                        methods.deleteTicket(target);
                        methods.helpopDelete(target);
                        player.sendMessage("Claimed " + BLUE + target.getName() + WHITE + "'s ticket.");
                    } else {
                        player.sendMessage(BLUE + target.getName() + WHITE + " has no ticket(s).");
                    }
                } else if (strings[0].equalsIgnoreCase("del")) {
                    if (methods.hasTicket(target)) {
                        methods.deleteTicket(target);
                        methods.helpopDelete(target);
                        player.sendMessage("Deleted " + BLUE + target.getName() + WHITE + "'s ticket.");
                    } else {
                        player.sendMessage(BLUE + target.getName() + WHITE + " has no ticket(s).");
                    }
                }
            } else {
                player.sendMessage(RED + "Error: " + strings[1] + " is offline!");
            }
        } else if (strings.length == 3) {
            try {
                Player target = Bukkit.getPlayer(strings[1]);
                int index = Integer.valueOf(strings[2]);
                if (strings[0].equalsIgnoreCase("del")) {
                    if (index <= methods.getTicketAmount(target)) {
                        methods.helpopDelete(target, index);
                        player.sendMessage("Deleted " + BLUE + target.getName() + WHITE + "'s ticket @index " + BLUE + index + ".");
                    } else {
                        player.sendMessage(BLUE + target.getName() + WHITE
                                + " only has " + BLUE + methods.getTicketAmount(target) + WHITE + " tickets.");
                    }
                }
            } catch (NumberFormatException e) {
                player.sendMessage(RED + "Error: You must use a number for your index argument.");
            }
        }
        return true;
    }
}