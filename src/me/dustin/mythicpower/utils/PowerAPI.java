package me.dustin.mythicpower.utils;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.Main;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import api.praya.myitems.builder.lorestats.LoreStatsEnum;
import api.praya.myitems.builder.lorestats.LoreStatsOption;
import api.praya.myitems.builder.socket.SocketEnum;
import api.praya.myitems.main.MyItemsAPI;
import api.praya.myitems.manager.game.LoreStatsManagerAPI;
import api.praya.myitems.manager.game.SocketManagerAPI;
import api.praya.myitems.manager.player.PlayerItemStatsManagerAPI;
import me.dustin.mythicpower.MythicPowerAddon;
import n3kas.ae.api.AEAPI;

public class PowerAPI {
	public static double getTotalPower(Player p) {
		double armorPower = 0;
		double handPower = 0;
		ItemStack mainHandItem = p.getInventory().getItemInMainHand();
		ItemStack offHandItem = p.getInventory().getItemInOffHand();

		handPower = getAEEnchantPower(mainHandItem)+
						getVanillaEnchantPower(mainHandItem) +
						getAEEnchantPower(offHandItem) +
						getVanillaEnchantPower(offHandItem)+
						getMyItemPower(offHandItem)+
						getMyItemPower(mainHandItem);
		if ( p.getInventory().getArmorContents().length != 0) {
			for ( ItemStack item : p.getInventory().getArmorContents()) {
				if ( item == null)
					continue;
				armorPower += getAEEnchantPower(item) + getVanillaEnchantPower(item) +getMyItemPower(item);
			}
		}
		return armorPower + handPower;
	}
	public static double getAEEnchantPower(ItemStack i) {
		if ( (i == null) || i.getType().equals(Material.AIR)) {
			return 0;
		}
		HashMap<String, Integer> enchantMap =  AEAPI.getEnchantmentsOnItem(i);
		if (enchantMap.keySet().size()==0) {
			return 0;
		}
		double power = 0;
		ConfigurationSection enchantSec = MythicPowerAddon.config.get("Config").get().getConfigurationSection("AEEnchantmentPower");
		for ( String enchantName : enchantMap.keySet()) {
			if ( enchantSec.getKeys(false).contains(enchantName))
			{
				power += enchantSec.getInt(enchantName) *enchantMap.get(enchantName) ;
			}
		}
		return power;
	}
	public static double getVanillaEnchantPower(ItemStack i) {
		if ( (i== null) || i.getType().equals(Material.AIR)) {
			return 0;
		}
		Map<Enchantment, Integer> enchantMap =  i.getEnchantments();
		if (enchantMap.keySet().size()==0) {
			return 0;
		}
		double power = 0;
		ConfigurationSection enchantSec = MythicPowerAddon.config.get("Config").get().getConfigurationSection("VanillaEnchantmentPower");
		for ( Enchantment en : enchantMap.keySet()) {
			if ( enchantSec.getKeys(false).contains(en.getName())) {
				power += enchantSec.getInt(en.getName()) * enchantMap.get(en);
			}
		}
		return power;
	}
	public static double getMyItemPower( ItemStack i) {
		double power = 0;
		ItemStack item = i;
		LoreStatsManagerAPI loreManage = MyItemsAPI
			.getInstance()
			.getGameManagerAPI()
			.getLoreStatsManagerAPI();
		ConfigurationSection sec = MythicPowerAddon.config.get("Config").get().getConfigurationSection("MyItemAttributePower");
		Map<String, Object> validStat = sec.getValues(false);
		for ( String statName : validStat.keySet()) {
			LoreStatsEnum stat = LoreStatsEnum.get(statName);
			if ( loreManage.hasLoreStats(item, stat)) {
				Double statValue = loreManage.getLoreValue(item, stat, LoreStatsOption.CURRENT);
				Double powerValue = Double.valueOf( sec.getInt(statName));
				power += statValue*powerValue;
			}
		}
		return power;
	}
	public static String getPowerItem(ItemStack i) {
		DecimalFormat df = new DecimalFormat("##.##");
		return df.format(getMyItemPower(i)+ getAEEnchantPower(i) +getVanillaEnchantPower(i));
	}
}
