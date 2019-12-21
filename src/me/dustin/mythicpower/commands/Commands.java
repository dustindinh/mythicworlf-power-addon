package me.dustin.mythicpower.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.dustin.mythicpower.MythicPowerAddon;
import me.dustin.mythicpower.TopPanel;
import me.dustin.mythicpower.TopPower;
import me.dustin.mythicpower.utils.Color;
import me.dustin.mythicpower.utils.MessageManager;
import me.dustin.mythicpower.utils.PowerAPI;
public class Commands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if ( args.length == 1) {
			if ( args[0].equalsIgnoreCase("me") && sender.hasPermission("mythicpower.command.power")) {
				if ( !(sender instanceof Player)) {
					MessageManager.send(sender, "console");
					return true;
				}
				Player p =  (Player) sender;
				TopPower top = new TopPower();
				MessageManager.send(p, "PlayerPower", new HashMap<String, String>() {
					{
						put("%power%", top.getPlayerPower(p.getName()).toString());
						put("%position%", top.getPlayerPosition(p.getName()).toString());
					}
				});
				return true;
			} else if ( args[0].equalsIgnoreCase("top") && sender.hasPermission("mythicpower.command.top")) {
				if ( !(sender instanceof Player)) {
					MessageManager.send(sender, "console");
					return true;
				}
				Player p =  (Player) sender;
				TopPanel panel = new TopPanel(p);
				panel.open(p);
				return true;
			} else if  (args[0].equalsIgnoreCase("update") && sender.hasPermission("mythicpower.command.update")) {
				if ( !(sender instanceof Player)) {
					MessageManager.send(sender, "console");
					return true;
				}
				Player p =  (Player) sender;
				String rawFormat =Color.colored( MythicPowerAddon.config.get("Config").get().getString("PowerLore"));
				ItemStack item = p.getInventory().getItemInMainHand();
				ItemMeta im = item.getItemMeta();
				List<String> lore = im.getLore();
				if (lore!= null) {
					String nonePowerFormat = rawFormat.replace("%power%", "");
					for ( int i = 0; i < lore.size();i++) {
						if ( lore.get(i).trim().contains(nonePowerFormat)) {
							lore.set(i, rawFormat.replace("%power%", PowerAPI.getPowerItem(item)));
							im.setLore(lore);
							item.setItemMeta(im);
							p.getInventory().setItemInMainHand(item);
							MessageManager.send(sender, "update_success");
							return true;
						}
					}
					lore.add(rawFormat.replace("%power%", PowerAPI.getPowerItem(item)));
					im.setLore(lore);
					item.setItemMeta(im);
					p.getInventory().setItemInMainHand(item);
					MessageManager.send(sender, "update_success");
					return true;
				}
				lore = new ArrayList<>();
				lore.add(rawFormat.replace("%power%",PowerAPI.getPowerItem(item)));
				im.setLore(lore);
				item.setItemMeta(im);
				p.getInventory().setItemInMainHand(item);
				MessageManager.send(sender, "update_success");
				return true;
				
			} else if ( args[0].equalsIgnoreCase("reload") && sender.hasPermission("mythicpower.command.reload")) {
				
				MessageManager.send(sender, "reload_success");
				MythicPowerAddon.config.get("Data").reload();
				MythicPowerAddon.config.get("Data").save();
				
				MythicPowerAddon.config.get("Config").reload();
				MythicPowerAddon.config.get("Config").save();
				
				MythicPowerAddon.config.get("Message").reload();
				MythicPowerAddon.config.get("Message").save();
				
				MythicPowerAddon.gui.get("TopPowerGui").reload();
				MythicPowerAddon.gui.get("TopPowerGui").save();
				
				
				return true;
			} else if ( args[0].equalsIgnoreCase("help")) {
				MessageManager.sends(sender, "help");
				return true;
			}
			MessageManager.send(sender, "wrong_command");
			return true;
		}
		MessageManager.send(sender, "wrong_command");
		return true;
	}
}
