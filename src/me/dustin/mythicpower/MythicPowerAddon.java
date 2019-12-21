package me.dustin.mythicpower;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.dustin.mythicpower.commands.Commands;
import me.dustin.mythicpower.listener.GuiListener;
import me.dustin.mythicpower.listener.PlayerLogoffEvent;
import me.dustin.mythicpower.utils.Config;
import me.dustin.mythicpower.utils.PowerAPI;

public class MythicPowerAddon extends JavaPlugin {
	public static Map<String,Config> config;
	public static Map<String, Config> gui;
	public static MythicPowerAddon main;
	@Override
	public void onEnable() {
		main = this;
		config = new HashMap<String, Config>();
		gui = new HashMap<String, Config>();
		new GuiListener(this);
		new PlayerLogoffEvent(this);
		getServer().getConsoleSender().sendMessage(" ");
		getServer().getConsoleSender().sendMessage("§aName: §dMYTHIC§cPOWER");
		getServer().getConsoleSender().sendMessage("§aAuthor: §cDUSTIN");
		getServer().getConsoleSender().sendMessage("§aVersion: §fLEGACY §61.0");
		getServer().getConsoleSender().sendMessage(" ");
		registerConfigFiles();
		registerCommands();
        if(getServer().getPluginManager().getPlugin("PlaceholderAPI") != null){
            new PlaceholderExp(this).register();
        }
		new BukkitRunnable() {
			@Override
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers()) {
						config.get("Data").get().set("PlayerData."+p.getName(), PowerAPI.getTotalPower(p.getPlayer()));
				}
				config.get("Data").save();
				config.get("Data").reload();
			}
		}.runTaskTimer(MythicPowerAddon.getPlugin(MythicPowerAddon.class), 0, config.get("Config").get().getInt("UpdatePowerDelay")*20);
	};
	
	@Override
	public void onDisable() {
	};
	public void registerConfigFiles() {
		config.put("Config", new Config("Config", this));
		config.put("Message", new Config("Message", this));
		config.put("Data", new Config("Data", this));
		gui.put("TopPowerGui", new Config("TopPowerGui", this));

	};
	public void registerCommands() {
		this.getCommand("mythicpower").setExecutor(new Commands());
	};
}
