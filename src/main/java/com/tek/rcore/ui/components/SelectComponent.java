package com.tek.rcore.ui.components;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import com.tek.rcore.item.InventoryUtils;
import com.tek.rcore.ui.InterfaceComponent;
import com.tek.rcore.ui.InterfaceState;
import com.tek.rcore.ui.WrappedProperty;

public class SelectComponent extends InterfaceComponent {
	
	private LinkedHashMap<ItemStack, ItemStack> options;
	private WrappedProperty<Integer> selectedIndex;
	
	public SelectComponent(int x, int y, int width, int height, List<ItemStack> options) {
		this(x, y, width, height, options, 0);
	}
	
	public SelectComponent(int x, int y, int width, int height, List<ItemStack> options, int defaultSelected) {
		super(x, y, width, height);
		this.options = new LinkedHashMap<ItemStack, ItemStack>();
		options.forEach(option -> this.options.put(option, InventoryUtils.hideGlow(option)));
		this.selectedIndex = new WrappedProperty<Integer>(defaultSelected);
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
				if(index == selectedIndex.getValue()) item = InventoryUtils.addGlow(item);
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
	
	public void setItem(int index, ItemStack item) {
		ItemStack key = (ItemStack) options.keySet().toArray()[index];
		options.put(key, item);
	}

}
