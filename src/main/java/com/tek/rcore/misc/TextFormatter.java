package com.tek.rcore.misc;

import org.bukkit.ChatColor;

public class TextFormatter {
	
	public static String color(String str) {
		return ChatColor.translateAlternateColorCodes('&', str);
	}
	
	public static String capitalize(String text) {
		StringBuffer buffer = new StringBuffer();
		for(String part : text.split(" ")) {
			if(part.length() < 2) {
				buffer.append(part.toUpperCase() + " ");
			} else {
				buffer.append(part.substring(0, 1).toUpperCase() + part.substring(1) + " ");
			}
		}
		if(buffer.length() > 0) buffer.setLength(buffer.length() - 1);
		return buffer.toString();
	}
	
}
