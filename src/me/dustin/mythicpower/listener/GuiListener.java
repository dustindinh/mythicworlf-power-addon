package me.dustin.mythicpower.listener;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import me.dustin.mythicpower.MythicPowerAddon;
import me.dustin.mythicpower.gui.Gui;
import me.dustin.mythicpower.utils.Color;


public class GuiListener implements Listener {
	public GuiListener(MythicPowerAddon main) {
		main.getServer().getPluginManager().registerEvents(this, main);
		Bukkit.getConsoleSender().sendMessage("InventoryClickEvent registed");
	}
	@EventHandler
	public void onClickInventory(InventoryClickEvent e) {
		try {
			Inventory inv = e.getClickedInventory();
			if (inv.getHolder() != null && inv.getHolder() instanceof Gui) {
				((Gui) inv.getHolder()).onClick(e);
				return;
			}
			Player p = (Player) e.getWhoClicked();
			List<String> invName = new ArrayList<String>();
			MythicPowerAddon.gui.keySet().forEach(name->{
				invName.add(Color.colored( MythicPowerAddon.gui.get(name).get().getString("Title")));
			});
			if ( invName.contains(p.getOpenInventory().getTitle())) {e.setCancelled(true);}
		} catch(Exception ex) {};
	}
	
	@EventHandler
	public void onCloseInventory(InventoryCloseEvent e) {
		try {
			Inventory inv = e.getInventory();
			if (inv.getHolder() != null && inv.getHolder() instanceof Gui) {
				((Gui) inv.getHolder()).onClose(e);
			}
		} catch( Exception ex) {};
	}
}
