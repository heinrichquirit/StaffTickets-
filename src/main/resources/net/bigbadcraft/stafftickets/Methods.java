package main.resources.net.bigbadcraft.stafftickets;

import main.java.net.bigbadcraft.stafftickets.TicketPlugin;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.*;
import java.util.logging.Level;

/**
 * User: Heinrich Quirit
 * Last Modified: 9/25/13
 * Time: 6:09 PM
 */
public class Methods {

    private ChatColor BLUE = ChatColor.BLUE;
    private ChatColor WHITE = ChatColor.WHITE;
    private ChatColor RED = ChatColor.RED;

    private Map<String, String> tickets;
    private Map<String, ArrayList<String>> helpopTickets;

    private TicketPlugin plugin;
    private String plugName;
    private String plugVer;

    public Methods(TicketPlugin plugin) {
        tickets = new HashMap<String, String>();
        helpopTickets = new HashMap<String, ArrayList<String>>();
        this.plugin = plugin;
        this.plugName = plugin.getDescription().getName();
        this.plugVer = plugin.getDescription().getVersion();
    }

    public void log(Level lvl, String message) {
        Bukkit.getLogger().log(lvl, message);
    }

    // Managing general tickets.

    public void createTicket(Player player, String message) {
        tickets.put(player.getName(), message);
    }

    public void deleteTicket(Player player) {
       tickets.remove(player.getName());
    }

    public void clearTickets() {
        tickets.clear();
    }

    public void notifyStaff(String message) {
        Bukkit.broadcast(message, Perm.PERM);
    }

    public void sendTicketList(Player player) {
        for (Map.Entry<String, String> entry : tickets.entrySet()) {
            player.sendMessage(BLUE + entry.getKey() + WHITE + ": " + entry.getValue());
        }
    }

    public void logTicket(File file, String name, String message, Location loc) {
        try(BufferedWriter out = new BufferedWriter(new FileWriter(file, true))) {
            out.append(name + ": " + message + " | World:"
                    + loc.getWorld().getName()
                    + " XYZ:"
                    + Math.round(loc.getX()) + ", "
                    + Math.round(loc.getY()) + ", "
                    + Math.round(loc.getZ()));
            out.newLine();
            log(Level.INFO, plugName + " - " + name + "'s ticket has been logged.");
            out.close();
        } catch (IOException ioe) {
            log(Level.SEVERE, "Could not write to " + file.getName());
            ioe.printStackTrace();
        }
    }

    public int getTickets() {
        return tickets.size();
    }

    public String noticeHeader() {
        return BLUE
                + "+-------- " + WHITE + "[ " + BLUE
                + "Notice" + " " + WHITE
                + "(" + getTickets() + ")"
                + " ]" + BLUE + " --------+";
    }

    public String ticketListHeader() {
        return BLUE + "+-------- [ " + WHITE + plugName
                + " (" + getTickets() + ")"
                + BLUE + " ] --------+";
    }

    public String helpHeader() {
        return BLUE + "+-------------- [ " + WHITE + "v" + plugVer + " " + plugName + BLUE + " ] -------------------+";
    }

    public String helpFooter() {
        return BLUE + "+---------------------------------+";
    }

    public String helpMenu() {
        return  BLUE + " -/ticket" + WHITE + " - Displays a list of commands.\n" +
                BLUE + "-/ticket readfile" + WHITE + " - Displays logged info from file.\n" +
                BLUE + "-/ticket list" + WHITE + " - Displays a list of open tickets.\n" +
                BLUE + "-/ticket view <player>" + WHITE + " - View player's helpop tickets.\n" +
                BLUE + "-/ticket del <player> <index>" + WHITE + " - Deletes ticket at index.\n" +
                BLUE + "-/ticket tp <player>" + WHITE + " - Teleports to specified player.\n" +
                BLUE + "-/ticket cl <player>" + WHITE + " - Claims specified ticket.\n" +
                BLUE + "-/ticket del <player>" + WHITE + " - Deletes specified ticket.\n" +
                BLUE + "-/ticket clear" + WHITE + " - Clears all open tickets.\n" +
                BLUE + "+---------------------------------------------------+";
    }

    public boolean hasTicket(Player player) {
        return tickets.containsKey(player.getName());
    }

    public boolean isNotEmpty() {
        return !tickets.isEmpty();
    }

    public void readLoggedTickets(Player player) {
        try {
            Scanner in = new Scanner(plugin.ticketFile);
            if (in.hasNextLine()) {
                player.sendMessage(BLUE + "Fetching tickets for you..");
                while (in.hasNextLine()) {
                    player.sendMessage(in.nextLine());
                }
            } else {
                player.sendMessage(RED + "No data was found in the file.");
            }
        } catch (FileNotFoundException e) {
            log(Level.SEVERE, plugin.ticketFile.getName() + " could not be found, let's create it!");
            plugin.methods.loadFile(plugin.ticketFile);
            log(Level.INFO, plugin.ticketFile.getName() + " has been successfully created.");
        }
    }

    public void loadFile(File file) {
        if (!file.exists()) {
            try {
                log(Level.WARNING, file.getName() + " could not be found, creating..");
                file.createNewFile();
                log(Level.INFO, file.getName() + " successfully created.");
            } catch (IOException ioe) {
                log(Level.SEVERE, file.getName() + " could not be created!");
                ioe.printStackTrace();
            }
        }
    }

    // Managing helpop tickets

    public synchronized void helpopCreate(Player player, String messages) {
        ArrayList<String> playerTickets = helpopTickets.get(player.getName());
        if(playerTickets == null) {
            playerTickets = new ArrayList<String>();
            playerTickets.add(messages);
            helpopTickets.put(player.getName(), playerTickets);
        } else {
            if(!playerTickets.contains(messages)) playerTickets.add(messages);
        }
    }

    public void helpopDelete(Player player) {
        helpopTickets.remove(player.getName());
    }

    public void helpopClear() {
        helpopTickets.clear();
    }

    public void displayTicket(Player sender, Player target) {
        int counter = 1;
        sender.sendMessage(BLUE + "+-------- [ " + WHITE + "Viewing "
                + BLUE + target.getName() + WHITE
                + "'s ticket" + BLUE + " ] --------+");
        for (String message : helpopTickets.get(target.getName())) {
            sender.sendMessage(counter++ + ". " + BLUE + message);
        }
    }

    public void helpopDelete(Player target, int index) {
        helpopTickets.get(target.getName()).remove(index - 1);
    }

    public boolean hasHelpop(Player player) {
        return helpopTickets.containsKey(player.getName());
    }

    public String getOutstandingTicket(Player player) {
        return helpopTickets.get(player.getName()).get(0);
    }

    public int getTicketAmount(Player player) {
        return helpopTickets.get(player.getName()).size();
    }

    public boolean isHelpopCmd(String str) {
        return str.substring(0, 7).equalsIgnoreCase("/helpop");
    }
}
