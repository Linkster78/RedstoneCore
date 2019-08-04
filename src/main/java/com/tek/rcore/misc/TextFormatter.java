package com.tek.rcore.misc;

import org.bukkit.ChatColor;

/**
 * A class which provides a few
 * methods to work with Strings.
 * 
 * @author RedstoneTek
 */
public class TextFormatter {
	
	/**
	 * Formats a string and adds color with '&' as a color character.
	 * 
	 * @param str The string to format
	 * @return The formatted string
	 */
	public static String color(String str) {
		return ChatColor.translateAlternateColorCodes('&', str);
	}
	
	/**
	 * Capitalizes every word in the string.
	 * 
	 * @param text The text to capitalize
	 * @return The capitalized text
	 */
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
