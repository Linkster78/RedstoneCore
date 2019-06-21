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

public class ItemBuilder {
	
	private Material material;
	private int count;
	private String name;
	private List<String> lore;
	private Map<Enchantment, Integer> enchantments;
	private boolean unbreakable;
	
	public ItemBuilder(Material material) {
		this.material = material;
		this.count = 1;
	}
	
	public ItemBuilder withCount(int count) {
		this.count = count;
		return this;
	}
	
	public ItemBuilder withName(String name) {
		this.name = name;
		return this;
	}
	
	public ItemBuilder withColoredName(String name) {
		this.name = TextFormatter.color(name);
		return this;
	}
	
	public ItemBuilder withLore(List<String> lore) {
		this.lore = lore;
		return this;
	}
	
	public ItemBuilder withColoredLore(List<String> lore) {
		this.lore = lore.stream().map(TextFormatter::color).collect(Collectors.toList());
		return this;
	}
	
	public ItemBuilder withLoreLine(String loreLine) {
		if(this.lore == null) this.lore = new ArrayList<String>();
		this.lore.add(loreLine);
		return this;
	}
	
	public ItemBuilder withColoredLoreLine(String loreLine) {
		if(this.lore == null) this.lore = new ArrayList<String>();
		this.lore.add(TextFormatter.color(loreLine));
		return this;
	}
	
	public ItemBuilder withEnchantments(Map<Enchantment, Integer> enchantments) {
		this.enchantments = enchantments;
		return this;
	}
	
	public ItemBuilder withEnchantment(Enchantment enchantment, int level) {
		if(this.enchantments == null) this.enchantments = new HashMap<Enchantment, Integer>();
		this.enchantments.put(enchantment, level);
		return this;
	}
	
	public ItemBuilder unbreakable() {
		this.unbreakable = true;
		return this;
	}
	
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
