package net.bigbadcraft.stafftickets;

import java.io.FileNotFoundException;
import java.util.Map.Entry;
import java.util.Scanner;

import net.bigbadcraft.stafftickets.resources.DataStorage;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor {
	
	private String pluginName;
	private String pluginVersion;
	
	protected Main plugin;
	public CommandHandler(Main plugin) {
		this.plugin = plugin;
		
		this.pluginName = plugin.getDescription().getName();
		this.pluginVersion = plugin.getDescription().getVersion();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		
		if (sender instanceof Player == false) {
			sender.sendMessage("Please use this command in game.");
			return true;
		}
		
		if (cmd.getName().equalsIgnoreCase("helpop")) {
			return helpop(sender, args);
		}
		
		if (cmd.getName().equalsIgnoreCase("ticket")) {
			return ticket(sender, args);
		}
		
		return true;
	}
	
	private boolean helpop(CommandSender sender, String[] args) {
		
		if (args.length == 0) {
			sender.sendMessage("§cIncorrect syntax, usage: /helpop <msg>");
			return true;
		}
		
		// Store player's helpop ticket
		if (args.length > 0) {
			
			String message = StringUtils.join(args, ' ', 0, args.length);
			String helpopSender = sender.getName();
			
			if (!EventListener.playerTicket.containsKey(helpopSender)) { 
				
				Player player = (Player) sender;
				
				Bukkit.broadcast("§c" + helpopSender + ": " + message, Permission.PERMISSION);
				EventListener.playerTicket.put(helpopSender, message);
				
				int x = (int) Math.round(player.getLocation().getX());
				int y = (int) Math.round(player.getLocation().getY());
				int z = (int) Math.round(player.getLocation().getZ());
				
				DataStorage.logData(helpopSender, message, x, y, z);
				sender.sendMessage("§9Successfully submitted ticket, current position§f: " + EventListener.playerTicket.size()); 
			} else {
				sender.sendMessage("§9You already have an existing ticket, current position§f: " + EventListener.playerTicket.size());
			}
			
		}
		
		return true;
	}
	
	private boolean ticket(CommandSender sender, String[] args) {
		
		if (args.length == 0) {
			initCommandList(sender);
		}
		
		else if (args.length == 1) {
			
			switch(args[0]) {
			case "readfile":
				try {
					@SuppressWarnings("resource")
					Scanner in = new Scanner(Main.fileLog);
					
					if (!in.hasNextLine()) {
						sender.sendMessage("§cNo data was found in the file."); 
					} else {
						sender.sendMessage("§9Fetching file for you...");

						while (in.hasNextLine()) {
							sender.sendMessage(in.nextLine());
						}	
					}
					
				} catch (FileNotFoundException ex) {
					Main.logger.severe(Main.fileLog.getName() + " could not be found!");
					ex.printStackTrace();
				}
				break;
			case "list":
				if (!EventListener.playerTicket.isEmpty()) {
					sender.sendMessage("§9+-------- [ §f" + pluginName + " (" + EventListener.playerTicket.size() + ")" + "§9 ] --------+");
				
					for (Entry<String, String> entry : EventListener.playerTicket.entrySet()) {
						sender.sendMessage("§9" + entry.getKey() + "§f: " + entry.getValue());
					}
				
					sender.sendMessage("§9+---------------------------------+");
					
				} else {
					sender.sendMessage("§9There are no tickets available.");
				}
				break;
			case "tp":
				sender.sendMessage("§cIncorrect syntax, usage: /ticket tp <name>");
				break;
			case "cl":
				sender.sendMessage("§cIncorrect syntax, usage: /ticket cl <name>");
				break;
			case "del":
				sender.sendMessage("§cIncorrect syntax, usage: /ticket del <name>");
				break;
			case "clear":
				if (!EventListener.playerTicket.isEmpty()) {
					EventListener.playerTicket.clear();
					sender.sendMessage("§9Ticket list has been cleared!");
				} else {
					sender.sendMessage("§9Ticket list is already empty!");
				}
				break;
			default:
				sender.sendMessage("Please use §9/ticket§f to view a list of commands.");
				break;
			}
		}
		else if (args.length == 2) {
			
			Player player = Bukkit.getPlayer(args[1]);
			
			if (player != null) {
				
				switch(args[0]) {
				case "tp":
					if (EventListener.playerTicket.containsKey(player.getName())) {
					
						EventListener.playerTicket.remove(player.getName());
						sender.sendMessage("Teleporting to " + "§9" + player.getName() + "§f...");
						((Player) sender).teleport(player.getLocation());
						
					} else {
						
						sender.sendMessage("§9" + player.getName() + " §fhasn't triggered a ticket.");
					}
					break;
				case "cl":
					if (EventListener.playerTicket.containsKey(player.getName())) {
					
						EventListener.playerTicket.remove(player.getName());
						sender.sendMessage("Claimed§9 " + player.getName() + "'s §fticket.");
						
					} else {
						
						sender.sendMessage("§9" + player.getName() + " §fhasn't triggered a ticket.");
					}
					break;
				case "del":
					if (EventListener.playerTicket.containsKey(player.getName())) {
					
						EventListener.playerTicket.remove(player.getName());
						sender.sendMessage("Removed§9 " + player.getName() + "'s §fticket.");
						
					} else {
						
						sender.sendMessage("§9" + player.getName() + " §fhasn't triggered a ticket.");
					}
					break;
				default:
					sender.sendMessage("Please use §9/ticket§f to view a list of commands.");
					break;
				}
				
			} else {
				sender.sendMessage("§cError: " + args[1] + " is offline!");
			}
		}
		
		return true;
	}
	
	private void initCommandList(CommandSender sender) {
		
		StringBuilder sb = new StringBuilder();
		sb.append("§9+-------------- [ §f" + "v" + pluginVersion + " " + pluginName + "§9 ] -------------------+\n")
		.append("§9-/ticket§f - Displays a list of commands.\n")
		.append("§9-/ticket readfile§f - Displays logged info from file.\n")
		.append("§9-/ticket list§f - Displays a list of open tickets.\n")
		.append("§9-/ticket tp <name>§f - Teleports to specified player.\n")
		.append("§9-/ticket cl <name>§f - Claims specified ticket.\n")
		.append("§9-/ticket del <name>§f - Deletes specified ticket.\n")
		.append("§9-/ticket clear§f - Clears all open tickets.\n")
		.append("§9+---------------------------------------------------+");
		
		sender.sendMessage(sb.toString());
	}
}
