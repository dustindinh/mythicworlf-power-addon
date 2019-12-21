package me.dustin.mythicpower.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import me.dustin.mythicpower.MythicPowerAddon;

public class MessageManager {

	public static void send(Player player, String path, String regex, Object value) {
		String message = MythicPowerAddon.config.get("Message").get().getString(path).replaceAll("&", "§");
		if ( message == null) {
			message = "";
		}
		player.sendMessage(message.replace(regex,value.toString()));
	};
	
	public static void send(Player player, String path,  HolderReplace...holder) {
		String message = MythicPowerAddon.config.get("Message").get().getString(path).replaceAll("&", "§");
		for ( HolderReplace hr : holder) {
			message = message.replace(hr.getHolder(), hr.getReplace());
		}
		player.sendMessage(message);
	};
	
	public static void send(CommandSender sender, String path,  HolderReplace...holder) {
		String message = MythicPowerAddon.config.get("Message").get().getString(path).replaceAll("&", "§");
		for ( HolderReplace hr : holder) {
			message = message.replace(hr.getHolder(), hr.getReplace());
		}
		sender.sendMessage(message);
	};
	
	public static void send(CommandSender sender, String path) {
		String message = MythicPowerAddon.config.get("Message").get().getString(path).replaceAll("&", "§");
		if ( message == null) {
			message = "";
		}
		sender.sendMessage(message);
	};
	
	public static void sends(Player p, String path) {
		List<String> message = MythicPowerAddon.config.get("Message").get().getStringList(path);
		
		if ( message == null) {
			message = new ArrayList<String>();
			message.add("Empty String");
		}
		message.forEach( line->{
			p.sendMessage(line.replaceAll("&", "§"));
		});
		
	};
	
	public static void sends(CommandSender sender, String path) {
		List<String> message = MythicPowerAddon.config.get("Message").get().getStringList(path);
		
		if ( message == null) {
			message = new ArrayList<String>();
			message.add("Empty String");
		}
		message.forEach( line->{
			sender.sendMessage(line.replaceAll("&", "§"));
		});
	};
	
	public static void send(Player player, String path, Map<String, String> placeholder) {
		String message = MythicPowerAddon.config.get("Message").get().getString(path).replaceAll("&", "§");
		if ( message == null) {
			message = "";
		}
		for ( String key : placeholder.keySet()) {
			message = message.replace(key, placeholder.get(key));
		}		
		player.sendMessage(message);
	};
	
	public static void sendDf(Player player, String message) {
		player.sendMessage( message.replaceAll("&", "§"));
	};
	
	public static void broadcasts(String path, HolderReplace...holder) {
		String message = MythicPowerAddon.config.get("Message").get().getString(path).replaceAll("&", "§");
		if ( message == null) {
			message= "Empty String";
		}
		for ( HolderReplace hr : holder) {
			message = message.replace(hr.getHolder(), hr.getReplace());
		}	
		Bukkit.broadcastMessage(message);
	}
}

