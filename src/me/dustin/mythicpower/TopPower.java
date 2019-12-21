package me.dustin.mythicpower;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import me.dustin.mythicpower.utils.Color;
import me.dustin.mythicpower.utils.PowerAPI;
public class TopPower {

	private List<String> topRankSortedPlayer;
	@SuppressWarnings("unchecked")
	public TopPower() {
		topRankSortedPlayer = new  ArrayList<String>();
		Map<String, Double> topList = new HashMap<String, Double>();
		Map<String, Double> topListSorted = new HashMap<String, Double>();
		for (Player ps : Bukkit.getOnlinePlayers()) {
			MythicPowerAddon.config.get("Data").get().set("PlayerData."+ps.getName(), PowerAPI.getTotalPower(ps.getPlayer()));
		}
		ConfigurationSection topSec = MythicPowerAddon.config.get("Data").get().getConfigurationSection("PlayerData");
		
		for (String name : topSec.getValues(false).keySet()) {
			topList.put(name, Double.valueOf(topSec.getValues(false).get(name).toString()));
		}
		MythicPowerAddon.config.get("Data").save();
		MythicPowerAddon.config.get("Data").reload();
		topListSorted = (Map)topList.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> 
                    oldValue, java.util.LinkedHashMap::new));
		int position = 1;
		for ( String name : topListSorted.keySet()) {
			topRankSortedPlayer.add(name+":"+topListSorted.get(name)+":"+position);
			position++;
		}
	}
	
	public List<String> getTopSortedList() {
		return topRankSortedPlayer;
	}
	public Double getPlayerPower(String name) {
		for ( String line : topRankSortedPlayer) {
			if ( line.contains(name)) {
				return Double.valueOf(line.substring(line.indexOf(":")+1, line.lastIndexOf(":")));
			}
		}
		return 0.0;
	}
	public Integer getPlayerPosition(String name) {
		for ( String line : topRankSortedPlayer) {
			if ( line.contains(name)) {
				return Integer.parseInt(line.substring(line.lastIndexOf(":")+1));
			}
		}
		return 0;
	}
	public List<String> getTextRank(int page) {
		String textLineFormat  = MythicPowerAddon.config.get("Config").get().getString("TopMessageFormat");
		List<String> textRankList = new ArrayList<String>();
		for ( int i =(page-1)*10; i < 10*page;i++) {
			String line  = topRankSortedPlayer.get(i);
			String name = line.substring(0, line.indexOf(":"));
			textRankList.add(Color.colored(textLineFormat
					.replace("%name%", name))
					.replace("%position%", String.valueOf(getPlayerPosition(name)))
					.replace("%power%", String.valueOf( getPlayerPower(name))));
		}
		return textRankList;
	}
}
