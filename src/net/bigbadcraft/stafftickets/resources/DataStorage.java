package net.bigbadcraft.stafftickets.resources;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import net.bigbadcraft.stafftickets.Main;

public class DataStorage {
	
	public static void logData(String name, String message, int x, int y, int z) {
		try(BufferedWriter out = new BufferedWriter(new FileWriter(Main.fileLog, true))) {
			
			out.append("Info - " + name + ": " + message + ". Location: x: " + x + " y: " + y + " z: " + z);
			out.newLine();
			out.close();
			
		} catch (IOException e) {
			Main.logger.severe("Could not write to " + Main.fileLog.getName()); 
			e.printStackTrace();
		}
	}
}
