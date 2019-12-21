package me.dustin.mythicpower.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class Config {
	
	private File file;
	private FileConfiguration fileConfig;
	
	public Config(String fileName, Plugin plugin) {
		if (!fileName.contains(".yml")) {
			fileName = fileName + ".yml";
		}
		file = new File(plugin.getDataFolder(), fileName);
		if (!file.exists()) {
			fileConfig = YamlConfiguration.loadConfiguration( new InputStreamReader( plugin.getResource(fileName)) );
			fileConfig.options().copyDefaults(true);
			try {
				fileConfig.save(file);
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		} else {
			fileConfig = YamlConfiguration.loadConfiguration(file);
		}
	}
	
	public FileConfiguration get() {
		return fileConfig;
	}
	
	public void save() {
		try {
			fileConfig.save(file);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
	public void reload() {
		fileConfig =  YamlConfiguration.loadConfiguration(file);
	}
}
