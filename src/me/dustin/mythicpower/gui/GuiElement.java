package me.dustin.mythicpower.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import me.clip.placeholderapi.PlaceholderAPI;
import me.dustin.mythicpower.utils.Color;
import me.dustin.mythicpower.utils.HolderReplace;
import me.dustin.mythicpower.utils.SkullManager;

public class GuiElement {

	private ItemStack item;
	private String name;
	private String configKey;
	private List<Integer> slot;
	private List<String> lore;
	private Consumer<InventoryClickEvent> eventFunction = (event)->{event.setCancelled(true);};
	private Consumer<InventoryClickEvent> defaultEventFunction = (event)->{event.setCancelled(true);};
	public GuiElement(ConfigurationSection sec, Player player) {
		configKey = sec.getName();
		String path = sec.getString("Type");
		if ( path.contains("PlayerHead")) {
			item = SkullManager.itemFromName(player.getName());
		} else if ( path.contains("Head")) {
			item = SkullManager.createSkull(new ItemStack(Material.SKULL_ITEM), path.substring(path.indexOf(":")+1));
		} else {
			Material mat = Material.getMaterial(path.substring(0, path.indexOf(":")));
			short data =  Short.parseShort( path.substring(path.indexOf(":")+1, path.lastIndexOf(":")));
			int amount   = Integer.parseInt(path.substring(path.lastIndexOf(":")+1));
			item = new ItemStack(mat, amount, data);
		}

		this.name = sec.getString("Name");
		this.lore = sec.getStringList("Lore");
		this.slot = sec.getIntegerList("Slots");
		
		boolean isGlowing = sec.getBoolean("Glowing");
		boolean isUnbreakable = sec.getBoolean("Unbreakable") ;
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(PlaceholderAPI.setPlaceholders(player, Color.colored(name)));
		im.setLore(PlaceholderAPI.setPlaceholders(player, Color.colored(lore)));
		
		if ( isGlowing) {
			im.addEnchant(Enchantment.DURABILITY, 1, true);
			im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		} else if (isUnbreakable) {
			im.setUnbreakable(true);
		}
		
		im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		im.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		
		item.setItemMeta(im);
	};
	
	
	public GuiElement(ConfigurationSection sec, Player player, Consumer<InventoryClickEvent> event) {
		
		this.eventFunction = event;
		configKey = sec.getName();
		String path = sec.getString("Type");
		if ( path.contains("PlayerHead")) {
			item = SkullManager.itemFromName(player.getName());
		} else if ( path.contains("Head")) {
			item = SkullManager.createSkull(new ItemStack(Material.SKULL_ITEM), path.substring(path.indexOf(":")+1));
		} else {
			Material mat = Material.getMaterial(path.substring(0, path.indexOf(":")));
			short data =  Short.parseShort( path.substring(path.indexOf(":")+1, path.lastIndexOf(":")));
			int amount   = Integer.parseInt(path.substring(path.lastIndexOf(":")+1));
			item = new ItemStack(mat, amount, data);
		}

		this.name = sec.getString("Name");
		this.lore = sec.getStringList("Lore");
		this.slot = sec.getIntegerList("Slots");
		boolean isGlowing = sec.getBoolean("Glowing");
		boolean isUnbreakable = sec.getBoolean("Unbreakable") ;
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(PlaceholderAPI.setPlaceholders(player, Color.colored(name)));
		im.setLore(PlaceholderAPI.setPlaceholders(player, Color.colored(lore)));
		
		if ( isGlowing) {
			im.addEnchant(Enchantment.DURABILITY, 1, true);
			im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		} else if (isUnbreakable) {
			im.setUnbreakable(true);
		}
		
		im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		im.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		
		item.setItemMeta(im);
	};
	
	public GuiElement(String path, String name, List<String> lore, boolean isShiny) {
		
		if ( path.contains("PlayerHead")) {
			item = SkullManager.itemFromName("Corozone");
		} else if ( path.contains("Head")) {
			item = SkullManager.createSkull(new ItemStack(Material.SKULL_ITEM), path.substring(path.indexOf(":")+1));
		} 
		else {
			Material mat = Material.getMaterial(path.substring(0, path.indexOf(":")));
			short data =  Short.parseShort( path.substring(path.indexOf(":")+1, path.lastIndexOf(":")));
			int amount   = Integer.parseInt(path.substring(path.lastIndexOf(":")+1));
			item = new ItemStack(mat, amount, data);
		}

		ItemMeta im = item.getItemMeta();
		
		im.setDisplayName( Color.colored(name));
		im.setLore(Color.colored(lore));
		
		if ( isShiny) {
			im.addEnchant(Enchantment.DURABILITY, 1, true);
			im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		};
		item.setItemMeta(im);
	};
	@SuppressWarnings("deprecation")
	public GuiElement setHeadName(String name) {
		item = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        ItemMeta im = item.getItemMeta();
        
        meta.setOwner(name);
        meta.setDisplayName( im.getDisplayName());
        meta.setLore(im.getLore());
        item.setItemMeta(meta);
		return this;
	}
	public GuiElement setHeadUrl(String name) {
		item = SkullManager.createSkull(item , name);
		return this;
	}
	public GuiElement(ItemStack i) {
		item = new ItemStack(i);			
	}
	public GuiElement updateName(Player target) {
		String format = name;
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(Color.colored(PlaceholderAPI.setPlaceholders(target,format)));
		item.setItemMeta(im);
		
		return this;
	}
	public GuiElement setLore(List<String> configLore, Map<String, String> lorePlaceholder) {
		ItemMeta im = item.getItemMeta();

		for ( int i = 0 ; i < configLore.size(); i++) {
			final int j = i;
			lorePlaceholder.keySet().forEach( key->{
				configLore.set(j, configLore.get(j).replace(key,lorePlaceholder.get(key)));
			});
		};
		im.setLore(Color.colored(configLore));
		item.setItemMeta(im);
		
		return this;
	}
	public GuiElement setHead(String name) {
		item = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        ItemMeta im = item.getItemMeta();
        
        meta.setOwner(name);
        meta.setDisplayName( im.getDisplayName());
        meta.setLore(im.getLore());
        item.setItemMeta(meta);
		return this;
	}
	public GuiElement setName( String defaultName, String regex, String replace) {
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(Color.colored(defaultName.replace(regex, replace)));
		item.setItemMeta(im);
		return this;
	}
	public GuiElement updateLore(Player target, HolderReplace...holder) {
		List<String> newLore = new ArrayList<String>();
		ItemMeta im = item.getItemMeta();
		for ( String line : this.lore) {
			String temp = line;
			for ( HolderReplace hd : holder) {
				temp = Color.colored(temp.replace(hd.getHolder(), hd.getReplace()));
			}
			newLore.add(temp);
		}
		im.setLore(PlaceholderAPI.setPlaceholders(target,newLore));
		item.setItemMeta(im);
		return this;
	}
	public ItemStack getItem() {
		return item;
	}
	public String getConfigKey() {
		return configKey;
	}
	public void onClick(InventoryClickEvent e) {
		defaultEventFunction.accept(e);
		eventFunction.accept(e);
	}
	public List<Integer> getSlot() {
		return slot;
	}
	public void setSlot(List<Integer> slot) {
		this.slot = slot;
	}
	public void addSlot(int slot) {
		this.slot.add(slot);
	}
}