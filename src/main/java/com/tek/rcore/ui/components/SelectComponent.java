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

/**
 * A component which allows a player
 * to select an item out of many items.
 * 
 * @author RedstoneTek
 */
public class SelectComponent extends InterfaceComponent {
	
	//The map representing the original item selection and their neutral (unenchanted) counterparts used for rendering
	private LinkedHashMap<ItemStack, ItemStack> options;
	//The current selection index
	private WrappedProperty<Integer> selectedIndex;
	
	/**
	 * Creates a selection component at the
	 * specified X and Y position, with the specified
	 * width and height and option list.
	 * 
	 * @param x The X position from the left (Starts at 0)
	 * @param y The Y position from the top (Starts at 0)
	 * @param width The width
	 * @param height The height
	 * @param options The option list
	 */
	public SelectComponent(int x, int y, int width, int height, List<ItemStack> options) {
		this(x, y, width, height, options, 0);
	}
	
	/**
	 * Creates a selection component at the
	 * specified X and Y position, with the specified
	 * width and height, option list and default value.
	 * 
	 * @param x The X position from the left (Starts at 0)
	 * @param y The Y position from the top (Starts at 0)
	 * @param width The width
	 * @param height The height
	 * @param options The option list
	 * @param defaultSelected The default selected index
	 */
	public SelectComponent(int x, int y, int width, int height, List<ItemStack> options, int defaultSelected) {
		super(x, y, width, height);
		this.options = new LinkedHashMap<ItemStack, ItemStack>();
		options.forEach(option -> this.options.put(option, InventoryUtils.hideGlow(option)));
		this.selectedIndex = new WrappedProperty<Integer>(defaultSelected);
	}
	
	/**
	 * Renders the component onto
	 * the interface draw buffer.
	 */
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
	
	/**
	 * Handles the click event.
	 * Sets the current selected index to the clicked item.
	 */
	@Override
	public void onClick(InterfaceState interfaceState, ClickType type, ItemStack item, int x, int y) {
		int index = y * height + x;
		if(index < options.size()) selectedIndex.setValue(index);
	}
	
	/**
	 * Gets the selected index.
	 * 
	 * @return The index
	 */
	public WrappedProperty<Integer> getSelectedIndex() {
		return selectedIndex;
	}
	
	/**
	 * Gets the item at the selected index.
	 * 
	 * @return The selected item
	 */
	public ItemStack getSelectedItem() {
		return (ItemStack) options.keySet().toArray()[selectedIndex.getValue()];
	}

}
