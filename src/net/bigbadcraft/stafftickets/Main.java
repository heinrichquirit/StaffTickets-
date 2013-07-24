package net.bigbadcraft.stafftickets;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	private File file;
	public static File fileLog;
	public static final Logger logger = Logger.getLogger("Minecraft");
	
	private CommandHandler cmdHandler;
	
	public void onEnable() {
		createFiles();
		
		getServer().getPluginManager().registerEvents(new EventListener(this), this);
		
		registerCommands();
		
		if (getConfig().getBoolean("reminder.enable")) {
			Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(this, new Scheduler(), 
					3600, 20 * this.getConfig().getInt("reminder.interval"));
		}
		
	}
	
	public void onDisable() {
	}
	
	private void createFiles() {
		saveDefaultConfig();
		
		this.file = new File(this.getDataFolder(), "config.yml");
		Main.fileLog = new File(this.getDataFolder(), "ticketlog.txt");
		
		if (!file.exists()) {
			
			try {
				getLogger().warning("Configuration file doesn't exist, creating..");
				file.createNewFile();
				getLogger().info("Configuration file created.");
				
			} catch (IOException ex) {
				getLogger().severe("Could not create configuration file.");
				ex.printStackTrace();
			}
		}
		
		if (!fileLog.exists()) {
			
			try {
				getLogger().warning(fileLog.getName() + " doesn't exist, creating..");
				fileLog.createNewFile();
				getLogger().info(fileLog.getName() + " created.");
				
			} catch (IOException ex) {
				getLogger().severe("Could not create " + fileLog.getName());
				ex.printStackTrace();
			}
		}
	}
	
	private void registerCommands() {
		
		this.cmdHandler = new CommandHandler(this);
		
		getCommand("helpop").setExecutor(cmdHandler);
		getCommand("ticket").setExecutor(cmdHandler);
		
	}
}
