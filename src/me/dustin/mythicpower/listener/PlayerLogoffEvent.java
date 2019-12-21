package me.dustin.mythicpower.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.dustin.mythicpower.MythicPowerAddon;
import me.dustin.mythicpower.utils.PowerAPI;


public class PlayerLogoffEvent implements Listener{
	public PlayerLogoffEvent(MythicPowerAddon main) {
		main.getServer().getPluginManager().registerEvents(this, main);
	}
	@EventHandler
	public void onPlayerLogOff(PlayerQuitEvent e) {
		if ( MythicPowerAddon.config.get("Data").get().getConfigurationSection("PlayerData")
				.getValues(false).keySet()
				.contains(e.getPlayer().getName()) ) {
			MythicPowerAddon.config.get("Data").get().set("PlayerData."+e.getPlayer().getName(), PowerAPI.getTotalPower(e.getPlayer()));
			MythicPowerAddon.config.get("Data").save();
			MythicPowerAddon.config.get("Data").reload();
			Bukkit.getConsoleSender().sendMessage("§fUpdate power for §c"+e.getPlayer().getName());
			return;
		}
	}
}
