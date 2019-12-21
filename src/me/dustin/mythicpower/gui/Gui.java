package me.dustin.mythicpower.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import me.dustin.mythicpower.MythicPowerAddon;



public class Gui implements InventoryHolder {
	private Inventory inv;
	private Player p;
	protected List<GuiElement> elements;
	protected Map<String, GuiElement> buttons;
	
	public Gui(Player p,int size, String title) {
		this.p = p;
		buttons = new HashMap<>();
		inv = Bukkit.createInventory(this, size, title);
		elements = new ArrayList<>();
		for ( int i = 0; i < 54;i++) {
			elements.add(null);
		}
	}
	@Override
	public Inventory getInventory() {return inv;};
	
	public ItemStack getItemBySlot( int slot) {return inv.getItem(slot);};
	
	public void setItem(int slot, GuiElement item) {
		if (slot >= inv.getSize())
			return;
		inv.setItem(slot, item.getItem());
		elements.set(slot, item);
	}
	
	public void onClick(InventoryClickEvent e) {
		GuiElement item = elements.get(e.getSlot());
		if (item != null) {
			item.onClick(e);
		} else {
			e.setCancelled(true);
		}
	}
	public void open(Player p) {p.openInventory(inv);}
	public void open() {this.p.openInventory(inv);}
	public void loadDecorationItems(String name) {
		MythicPowerAddon.gui.get(name).get().getConfigurationSection("DecorationItems").getKeys(false).forEach(key -> {
			ConfigurationSection decoSec = MythicPowerAddon.gui.get(name).get()
					.getConfigurationSection("DecorationItems." + key);
			GuiElement item = new GuiElement(decoSec, p);
			for (int slot : decoSec.getIntegerList("Slots")) {
				setItem(slot, item);
			}
		});
	}
	public void loadDecorationsButton(String name, Map<String, Consumer<InventoryClickEvent>> eventList) {
		MythicPowerAddon.gui.get(name).get().getConfigurationSection("Buttons").getKeys(false).forEach(key -> {
			ConfigurationSection decoSec = MythicPowerAddon.gui.get(name).get().getConfigurationSection("Buttons." + key);
			GuiElement item = new GuiElement(decoSec, p, eventList.get(key));
			buttons.put(key, item);
			for (int slot : decoSec.getIntegerList("Slots")) {
				setItem(slot, item);
			}
		});
	}
	public void onClose(InventoryCloseEvent e) {};
}
