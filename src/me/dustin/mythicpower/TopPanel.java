package me.dustin.mythicpower;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.dustin.mythicpower.gui.Gui;
import me.dustin.mythicpower.gui.GuiElement;
import me.dustin.mythicpower.utils.Color;

public class TopPanel extends Gui{
	@SuppressWarnings("unchecked")
	public TopPanel(Player p) {
		super(p,
			MythicPowerAddon.gui.get("TopPowerGui").get().getInt("Size"),
			Color.colored(MythicPowerAddon.gui.get("TopPowerGui").get().getString("Title"))
		);
		loadDecorationItems("TopPowerGui");
		ConfigurationSection panelSec = MythicPowerAddon.gui.get("TopPowerGui").get().getConfigurationSection("");
		ConfigurationSection decoSec = panelSec.getConfigurationSection("DecorationItems");
		
		for ( String key : decoSec.getKeys(false)) {
			GuiElement item = new GuiElement(decoSec.getString(key+".Type"), decoSec.getString(key+".Name"),decoSec.getStringList(key+".Lore"),decoSec.getBoolean(key+".Glowing"));
			if ( key.equalsIgnoreCase("TopHead")) {
				TopPower top = new TopPower();
				int position =0;
				DecimalFormat df = new DecimalFormat("##.##");
				for (int slot : decoSec.getIntegerList(key+".Slots")) {
					if ( top.getTopSortedList().size() == position) {
						item.setHeadUrl(decoSec.getString(key+".EmptyTop.Head"));
						item.setName( decoSec.getString(key+".Name"), "%player_name%", decoSec.getString(key+".EmptyTop.Name"));
						item.setLore( decoSec.getStringList(key+".Lore"), new HashMap<String, String>(){
							{
								put("%ae_power%", decoSec.getString(key+".EmptyTop.Power"));
								put("%position%", decoSec.getString(key+".EmptyTop.Position"));
							}
						});
						setItem(slot, item);
					} else {
					
						String line = top.getTopSortedList().get(position);
						String name = line.substring(0, line.indexOf(":"));			
						item.setHead(name);
						item.setName( decoSec.getString(key+".Name"), "%player_name%", name);
						item.setLore( decoSec.getStringList(key+".Lore"), new HashMap<String, String>(){
							{
								put("%ae_power%", df.format(top.getPlayerPower(name)));
								put("%position%", String.valueOf(top.getPlayerPosition(name)));
							}
						});
						setItem(slot, item);
						position++;
					}
				};
			} else {
				for (int slot : decoSec.getIntegerList(key+".Slots")) {
					setItem(slot, item);
				}
			}
		};
		Map<String, Consumer<InventoryClickEvent>> eventList = new HashMap<String, Consumer<InventoryClickEvent>>();
		eventList.put("Back", event->{
			Player target = (Player) event.getWhoClicked();
			target.closeInventory();
		});
		loadDecorationsButton("TopPowerGui", eventList);
	}
}
