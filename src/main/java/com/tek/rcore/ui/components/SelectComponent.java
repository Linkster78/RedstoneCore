package com.tek.rcore.ui.components;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.tek.rcore.misc.TextFormatter;
import com.tek.rcore.ui.InterfaceComponent;
import com.tek.rcore.ui.InterfaceState;
import com.tek.rcore.ui.WrappedProperty;

public class SelectComponent extends InterfaceComponent {
	
	private LinkedHashMap<ItemStack, ItemStack> options;
	private WrappedProperty<Integer> selectedIndex;
	
	public SelectComponent(int x, int y, int width, int height, List<ItemStack> options) {
		super(x, y, width, height);
		this.options = new LinkedHashMap<ItemStack, ItemStack>();
		options.forEach(option -> this.options.put(option, hideGlow(option)));
		this.selectedIndex = new WrappedProperty<Integer>(0);
	}
	
	@Override
	public void render(InterfaceState interfaceState, ItemStack[][] drawBuffer) {
		Iterator<Entry<ItemStack, ItemStack>> optionIterator = options.entrySet().iterator();
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				if(!optionIterator.hasNext()) break;
				int index = y * height + x;
				Entry<ItemStack, ItemStack> entry = optionIterator.next();
				ItemStack item = entry.getValue();
				if(index == selectedIndex.getValue()) item = addGlow(item);
				drawBuffer[this.x + x][this.y + y] = item;
			}
		}
	}
	
	@Override
	public void onClick(InterfaceState interfaceState, ClickType type, ItemStack item, int x, int y) {
		int index = y * height + x;
		if(index < options.size()) selectedIndex.setValue(index);
	}
	
	public WrappedProperty<Integer> getSelectedIndex() {
		return selectedIndex;
	}
	
	public ItemStack getSelectedItem() {
		return (ItemStack) options.keySet().toArray()[selectedIndex.getValue()];
	}
	
	private ItemStack addGlow(ItemStack item) {
		ItemStack hidden = item.clone();
		hidden.addEnchantment(Enchantment.DURABILITY, 1);
		ItemMeta meta = hidden.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		hidden.setItemMeta(meta);
		return hidden;
	}
	
	private ItemStack hideGlow(ItemStack item) {
		ItemStack hidden = item.clone();
		ItemMeta meta = hidden.getItemMeta();
		Iterator<Enchantment> enchantmentIterator = hidden.getEnchantments().keySet().iterator();
		while(enchantmentIterator.hasNext()) {
			Enchantment enchantment = enchantmentIterator.next();
			int level = hidden.getEnchantmentLevel(enchantment);
			enchantmentIterator.remove();
			meta.getLore().add(0, TextFormatter.color("&7" + TextFormatter.capitalize(enchantment.getKey().getKey()) + " " + levelToRoman(level)));
		}
		hidden.setItemMeta(meta);
		return item;
	}
	
	private String levelToRoman(int level) {
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
		} return "enchantment.level." + level;
	}

}
