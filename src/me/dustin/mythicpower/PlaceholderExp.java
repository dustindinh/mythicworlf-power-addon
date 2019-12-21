package me.dustin.mythicpower;

import java.text.DecimalFormat;

import org.bukkit.entity.Player;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.dustin.mythicpower.utils.PowerAPI;

public class PlaceholderExp extends PlaceholderExpansion  {

	private MythicPowerAddon main;
	public PlaceholderExp(MythicPowerAddon plugin) {
		this.main = plugin;
	}
    @Override
    public boolean persist(){
        return true;
    }
    @Override
    public boolean canRegister(){
        return true;
    }
	@Override
	 public String getAuthor(){
        return main.getDescription().getAuthors().toString();
    }

	@Override
	public String getIdentifier() {

		return "mythic";
	}
	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return "1.0.1";
	}
	
	@Override
	public String onPlaceholderRequest(Player player, String identifier){
	  
	        // %example_placeholder1%
	        if(identifier.equals("power")){
	    		DecimalFormat df = new DecimalFormat("##.##");
	            return df.format(PowerAPI.getTotalPower(player));
	        } else if ( identifier.equals("top")) {
	        	TopPower top = new TopPower();
	        	return String.valueOf( top.getPlayerPosition(player.getName()).toString());
	        }
	        return "none";
	    }

}
