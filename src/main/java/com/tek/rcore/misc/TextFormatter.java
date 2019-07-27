package com.tek.rcore.misc;

import org.bukkit.ChatColor;

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
	
	/**
	 * Converts an enchantment level to its roman counterpart.
	 * Follows the minecraft enchantment level naming convention.
	 * 
	 * @param level The numeric level
	 * @return The roman formatted level
	 */
	public static String enchantmentLevelToRoman(int level) {
		switch(level) {
			case 1: return "I";
			case 2: return "II";
			case 3: return "III";
			case 4: return "IV";
			case 5: return "V";
			case 6: return "VI";
			case 7: return "VII";
			case 8: return "VIII";
			case 9: return "IX";
			case 10: return "X";
		} 
		
		return "enchantment.level." + level;
	}
	
}
