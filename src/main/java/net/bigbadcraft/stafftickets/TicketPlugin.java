package main.java.net.bigbadcraft.stafftickets;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import main.java.net.bigbadcraft.stafftickets.commands.HelpopCommand;
import main.java.net.bigbadcraft.stafftickets.commands.MyTicketsCommand;
import main.java.net.bigbadcraft.stafftickets.commands.TicketCommand;
import main.java.net.bigbadcraft.stafftickets.listeners.ChatListener;
import main.java.net.bigbadcraft.stafftickets.listeners.CommandListener;
import main.java.net.bigbadcraft.stafftickets.listeners.QuitListener;
import main.java.net.bigbadcraft.stafftickets.tasks.BroadcastTask;
import main.resources.ConfigPaths;
import main.resources.Methods;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * User: Heinrich Quirit
 * Last Modified: 9/25/13
 * Time: 6:06 PM
 */
public class TicketPlugin extends JavaPlugin {

    private File config;
    private File ticketLogs;

    public Methods methods;
    
    public String messageFormat;
    public String titleHeader;

	@Override
    public void onEnable() {
        methods = new Methods(this);

        saveDefaultConfig();

        config = new File(getDataFolder(), "config.yml");
        ticketLogs = new File(getDataFolder() + "/ticketlogs");
        methods.loadFile(config);
        ticketLogs.mkdir();
        
        loadValues();
        
        getCommand("helpop").setExecutor(new HelpopCommand(this));
        getCommand("ticket").setExecutor(new TicketCommand(this));
        getCommand("mytickets").setExecutor(new MyTicketsCommand(this));

        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        getServer().getPluginManager().registerEvents(new QuitListener(this), this);
        getServer().getPluginManager().registerEvents(new CommandListener(this), this);

        if (getConfig().getBoolean(ConfigPaths.REMINDER_ENABLE)) {
            Bukkit.getScheduler().runTaskTimerAsynchronously(this, new BroadcastTask(this),
                    3600, 20 * 60 * getConfig().getInt(ConfigPaths.REMINDER_INTERVAL));
        }
        
        startMetrics();
    }
    
    @Override
    public void onDisable() {
        methods.clearTickets();
        methods.helpopClear();
    }
    
    private void startMetrics() {
    	try {
    	    Metrics metrics = new Metrics(this);
    	    metrics.start();
    	} catch (IOException e) {
    	    methods.log(Level.SEVERE, "StaffTickets failed to submit data.");
    	}
    }
    
    private void loadValues() {
    	messageFormat = getConfig().getString(ConfigPaths.MESSAGE_FORMAT);
        titleHeader = getConfig().getString(ConfigPaths.TITLE_HEADER);
    }


}
