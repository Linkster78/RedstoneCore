package com.tek.rcore.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.tek.rcore.misc.TextFormatter;

/**
 * A class which streamlines the
 * creation of good looking itemstacks.
 * 
 * @author RedstoneTek
 */
public class ItemBuilder {
	
	//Builder variables
	private Material material;
	private int count;
	private String name;
	private List<String> lore;
	private Map<Enchantment, Integer> enchantments;
	private boolean unbreakable;
	
	/**
	 * Creates a basic ItemBuilder with the material.
	 * 
	 * @param material The itemstack material
	 */
	public ItemBuilder(Material material) {
		this.material = material;
		this.count = 1;
	}
	
	/**
	 * Sets the count of the itemstack.
	 * 
	 * @param count The count
	 * @return The ItemBuilder instance
	 */
	public ItemBuilder withCount(int count) {
		this.count = count;
		return this;
	}
	
	/**
	 * Sets the displayed name of the itemstack.
	 * 
	 * @param name The name
	 * @return The ItemBuilder instance
	 */
	public ItemBuilder withName(String name) {
		this.name = name;
		return this;
	}
	
	/**
	 * Sets the displayed name of the itemstack.
	 * The name is colored via the '&' character.
	 * 
	 * @param name The colored name
	 * @return The ItemBuilder instance
	 */
	public ItemBuilder withColoredName(String name) {
		this.name = TextFormatter.color(name);
		return this;
	}
	
	/**
	 * Sets the lore of the itemstack.
	 * 
	 * @param lore The lore
	 * @return The ItemBuilder instance
	 */
	public ItemBuilder withLore(List<String> lore) {
		this.lore = lore;
		return this;
	}
	
	/**
	 * Sets the lore of the itemstack.
	 * Every line of the lore is colored via the '&' character.
	 * 
	 * @param lore The colored lore
	 * @return The ItemBuilder instance
	 */
	public ItemBuilder withColoredLore(List<String> lore) {
		this.lore = lore.stream().map(TextFormatter::color).collect(Collectors.toList());
		return this;
	}
	
	/**
	 * Adds a line to the itemstack lore.
	 * 
	 * @param loreLine The lore line
	 * @return The ItemBuilder instance
	 */
	public ItemBuilder withLoreLine(String loreLine) {
		if(this.lore == null) this.lore = new ArrayList<String>();
		this.lore.add(loreLine);
		return this;
	}
	
	/**
	 * Adds a line to the itemstack lore.
	 * The line is colored via the '&' character.
	 * 
	 * @param loreLine The colored lore line
	 * @return The ItemBuilder instance
	 */
	public ItemBuilder withColoredLoreLine(String loreLine) {
		if(this.lore == null) this.lore = new ArrayList<String>();
		this.lore.add(TextFormatter.color(loreLine));
		return this;
	}
	
	/**
	 * Sets the enchantment map
	 * of the itemstack.
	 * 
	 * @param enchantments The enchantment map
	 * @return The ItemBuilder instance
	 */
	public ItemBuilder withEnchantments(Map<Enchantment, Integer> enchantments) {
		this.enchantments = enchantments;
		return this;
	}
	
	/**
	 * Adds an enchantment to the itemstack.
	 * 
	 * @param enchantment The enchantment
	 * @param level The enchantment level
	 * @return The ItemBuilder instance
	 */
	public ItemBuilder withEnchantment(Enchantment enchantment, int level) {
		if(this.enchantments == null) this.enchantments = new HashMap<Enchantment, Integer>();
		this.enchantments.put(enchantment, level);
		return this;
	}
	
	/**
	 * Makes the itemstack unbreakable.
	 * 
	 * @return The ItemBuilder instance
	 */
	public ItemBuilder unbreakable() {
		this.unbreakable = true;
		return this;
	}
	
	/**
	 * Takes the ItemBuilder variables
	 * and creates an ItemStack with it.
	 * 
	 * @return The created ItemStack
	 */
	public ItemStack build() {
		ItemStack built = new ItemStack(material, count);
		if(enchantments != null) built.addUnsafeEnchantments(enchantments);
		ItemMeta builtMeta = built.getItemMeta();
		if(name != null) builtMeta.setDisplayName(name);
		if(lore != null) builtMeta.setLore(lore);
		builtMeta.setUnbreakable(unbreakable);
		built.setItemMeta(builtMeta);
		return built;
	}
	
}
