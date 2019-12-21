package me.dustin.mythicpower.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;

public class Color {

	public static String colored(String line) {
		return ChatColor.translateAlternateColorCodes('&', line);
	}
	public static List<String> colored(List<String> lore) {
		List<String> newLore = new ArrayList<String>();
		for ( String line : lore) {
			newLore.add( ChatColor.translateAlternateColorCodes('&', line));
		}
		return newLore;
	}
}
