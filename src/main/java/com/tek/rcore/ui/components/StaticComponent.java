package com.tek.rcore.ui.components;

import org.bukkit.inventory.ItemStack;

import com.tek.rcore.item.InventoryUtils;
import com.tek.rcore.ui.InterfaceComponent;
import com.tek.rcore.ui.InterfaceState;

/**
 * A component which displays
 * a single itemstack across
 * its complete surface.
 * 
 * @author RedstoneTek
 */
public class StaticComponent extends InterfaceComponent {

	//The item to display
	private ItemStack item;
	
	/**
	 * Creates a static component.
	 * 
	 * @param x The X position from the left (Starts at 0)
	 * @param y The Y position from the top (Starts at 0)
	 * @param item The item to display
	 */
	public StaticComponent(int x, int y, ItemStack item) {
		this(x, y, 1, 1, item);
	}
	
	/**
	 * Creates a static component.
	 * 
	 * @param x The X position from the left (Starts at 0)
	 * @param y The Y position from the top (Starts at 0)
	 * @param width The width
	 * @param height The height
	 * @param item The item to display
	 */
	public StaticComponent(int x, int y, int width, int height, ItemStack item) {
		super(x, y, width, height);
		this.item = item;
	}
	
	/**
	 * Renders the item onto the interface draw buffer.
	 */
	@Override
	public void render(InterfaceState interfaceState, ItemStack[][] drawBuffer) {
		InventoryUtils.drawFilledRectangle(drawBuffer, item, x, y, width, height);
	}
	
	/**
	 * Gets the item to render.
	 * 
	 * @return The item
	 */
	public ItemStack getItem() {
		return item;
	}
	
	/**
	 * Sets the item to render.
	 * 
	 * @param item The item
	 */
	public void setItem(ItemStack item) {
		this.item = item;
	}

}
