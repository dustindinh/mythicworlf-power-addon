package me.dustin.mythicpower.utils;

import java.lang.reflect.Field;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

public class SkullManager {
	@SuppressWarnings("unused")
	private static ItemStack getPlayerSkullItem() {
		if (newerApi()) {
			return new ItemStack(Material.valueOf("PLAYER_HEAD"));
		} else {
			return new ItemStack(Material.valueOf("SKULL_ITEM"), 1, (byte) 3);
		}
	}
	@SuppressWarnings("deprecation")
	public static ItemStack itemFromName(String name) {
		ItemStack stack = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
        SkullMeta meta = (SkullMeta) stack.getItemMeta();
        meta.setOwner(name);
        stack.setItemMeta(meta);
		return stack;
	}
	private static boolean newerApi() {
		try {

			Material.valueOf("PLAYER_HEAD");
			return true;

		} catch (IllegalArgumentException e) { // If PLAYER_HEAD doesn't exist
			return false;
		}
	}
	@SuppressWarnings("deprecation")
	public static ItemStack itemWithName(ItemStack item, String name) {
		notNull(item, "item");
		notNull(name, "name");

		return Bukkit.getUnsafe().modifyItemStack(item,
				"{SkullOwner:\"" + name + "\"}"
		);
	}
	private static void notNull(Object o, String name) {
		if (o == null) {
			throw new NullPointerException(name + " should not be null!");
		}
	}
    public static ItemStack createSkull(ItemStack i, String url) {
    	
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        if (url == null || url.isEmpty()) {
        	System.out.println("Con cac, sai roi kia");
            return head;
        }

        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", url));

        try {
            Field profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);

        } catch (IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException error) {
            error.printStackTrace();
        }
        headMeta.setDisplayName(i.getItemMeta().getDisplayName());
        headMeta.setLore(i.getItemMeta().getLore());
        head.setItemMeta(headMeta);
        return head;
    }
}
