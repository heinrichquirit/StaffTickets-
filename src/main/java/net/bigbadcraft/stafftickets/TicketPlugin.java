package main.java.net.bigbadcraft.stafftickets;

import main.java.net.bigbadcraft.stafftickets.commands.HelpopCommand;
import main.java.net.bigbadcraft.stafftickets.commands.MyTicketsCommand;
import main.java.net.bigbadcraft.stafftickets.commands.TicketCommand;
import main.java.net.bigbadcraft.stafftickets.listeners.ChatListener;
import main.java.net.bigbadcraft.stafftickets.listeners.CommandListener;
import main.java.net.bigbadcraft.stafftickets.listeners.QuitListener;
import main.java.net.bigbadcraft.stafftickets.tasks.BroadcastTask;
import main.resources.Methods;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * User: Heinrich Quirit
 * Last Modified: 9/25/13
 * Time: 6:06 PM
 */
public class TicketPlugin extends JavaPlugin {

    private File config;
    public File ticketFile;

    public Methods methods;

    @Override
    public void onEnable() {
        methods = new Methods(this);

        saveDefaultConfig();

        config = new File(getDataFolder(), "config.yml");
        ticketFile = new File(getDataFolder(), "ticketlog.txt");
        methods.loadFile(config);
        methods.loadFile(ticketFile);

        getCommand("helpop").setExecutor(new HelpopCommand(this));
        getCommand("ticket").setExecutor(new TicketCommand(this));
        getCommand("mytickets").setExecutor(new MyTicketsCommand(this));

        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        getServer().getPluginManager().registerEvents(new QuitListener(this), this);
        getServer().getPluginManager().registerEvents(new CommandListener(this), this);

        if (getConfig().getBoolean("reminder.enable")) {
            Bukkit.getScheduler().runTaskTimerAsynchronously(this, new BroadcastTask(this),
                    3600, 20 * 60 * getConfig().getInt("reminder.interval"));
        }
    }

    @Override
    public void onDisable() {
        methods.clearTickets();
        methods.helpopClear();
    }

}
