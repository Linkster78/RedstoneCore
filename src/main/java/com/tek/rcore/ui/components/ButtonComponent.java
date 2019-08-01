package com.tek.rcore.ui.components;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import com.tek.rcore.item.InventoryUtils;
import com.tek.rcore.ui.InterfaceComponent;
import com.tek.rcore.ui.InterfaceState;
import com.tek.rcore.ui.WrappedProperty;
import com.tek.rcore.ui.events.ButtonClickEvent;

/**
 * A component which displays an item
 * and calls a callback once clicked.
 * 
 * @author RedstoneTek
 */
public class ButtonComponent extends InterfaceComponent {

	//Internal Button values
	private ItemStack item;
	private WrappedProperty<ButtonClickEvent> clicked;
	
	/**
	 * Creates a button component at the
	 * specified x and y position and
	 * using the item to render.
	 * 
	 * @param x The X coordinate from the left (Starts at 0)
	 * @param y The Y coordinate from the top (Starts at 0)
	 * @param item The item to render
	 */
	public ButtonComponent(int x, int y, ItemStack item) {
		this(x, y, 1, 1, item);
	}
	
	/**
	 * Creates a button component at the
	 * specified x and y position, width and height,
	 * and using the item to render.
	 * 
	 * @param x The X coordinate from the left (Starts at 0)
	 * @param y The Y coordinate from the top (Starts at 0)
	 * @param width The width
	 * @param height The height
	 * @param item The item to render
	 */
	public ButtonComponent(int x, int y, int width, int height, ItemStack item) {
		super(x, y, width, height);
		this.item = item;
		this.clicked = new WrappedProperty<ButtonClickEvent>();
	}
	
	/**
	 * Renders the button onto the draw buffer.
	 */
	@Override
	public void render(InterfaceState interfaceState, ItemStack[][] drawBuffer) {
		InventoryUtils.drawFilledRectangle(drawBuffer, item, x, y, width, height);
	}
	
	/**
	 * Handles the click event.
	 * Sets the clicked wrapped property value.
	 */
	@Override
	public void onClick(InterfaceState interfaceState, ClickType type, ItemStack item, int x, int y) {
		this.clicked.setValue(new ButtonClickEvent(interfaceState.getOwner(), interfaceState, type, item, x, y));
	}
	
	/**
	 * Gets the button's clicked property.
	 * 
	 * @return The clicked property
	 */
	public WrappedProperty<ButtonClickEvent> getClickedProperty() {
		return clicked;
	}
	
	/**
	 * Gets the button's item.
	 * 
	 * @return The item
	 */
	public ItemStack getItem() {
		return item;
	}
	
	/**
	 * Sets the button's item.
	 * 
	 * @param item The item to set
	 */
	public void setItem(ItemStack item) {
		this.item = item;
	}

}
