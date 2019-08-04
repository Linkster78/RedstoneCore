package com.tek.rcore.misc;

/**
 * A class which provides functions
 * with which to work with whilst
 * using integers and floating point values.
 * 
 * @author RedstoneTek
 */
public class NumberUtils {
	
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
	
	/**
	 * Rounds a number to a specific
	 * amount of decimals.
	 * 
	 * @param value The value to round
	 * @param decimals The amount of decimals to leave
	 * @return The rounded value
	 */
	public static double round(double value, short decimals) {
		return Math.round(value * Math.pow(10, decimals)) / Math.pow(10, decimals);
	}
	
}
