package com.tek.rcore.ui.components;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import com.tek.rcore.item.InventoryUtils;
import com.tek.rcore.ui.InterfaceComponent;
import com.tek.rcore.ui.InterfaceState;
import com.tek.rcore.ui.WrappedProperty;

/**
 * A component which switches
 * between two items as an
 * on and off state.
 * 
 * @author RedstoneTek
 */
public class SwitchComponent extends InterfaceComponent {

	//The internal switch properties
	private ItemStack off, on;
	private WrappedProperty<Boolean> state;
	
	/**
	 * Creates a switch component with the specified values.
	 * 
	 * @param x The X position from the left (Starts at 0)
	 * @param y The Y position from the top (Starts at 0)
	 * @param icon The item to show unenchanted/enchanted as the switch
	 */
	public SwitchComponent(int x, int y, ItemStack icon) {
		this(x, y, 1, 1, icon, false);
	}
	
	/**
	 * Creates a switch component with the specified values.
	 * 
	 * @param x The X position from the left (Starts at 0)
	 * @param y The Y position from the top (Starts at 0)
	 * @param icon The item to show unenchanted/enchanted as the switch
	 * @param defaultState The default switch state
	 */
	public SwitchComponent(int x, int y, ItemStack icon, boolean defaultState) {
		this(x, y, 1, 1, icon, defaultState);
	}
	
	/**
	 * Creates a switch component with the specified values.
	 * 
	 * @param x The X position from the left (Starts at 0)
	 * @param y The Y position from the top (Starts at 0)
	 * @param width The width
	 * @param height The height
	 * @param icon The item to show unenchanted/enchanted as the switch
	 */
	public SwitchComponent(int x, int y, int width, int height, ItemStack icon) {
		this(x, y, width, height, icon, false);
	}
	
	/**
	 * Creates a switch component with the specified values.
	 * 
	 * @param x The X position from the left (Starts at 0)
	 * @param y The Y position from the top (Starts at 0)
	 * @param width The width
	 * @param height The height
	 * @param icon The item to show unenchanted/enchanted as the switch
	 * @param defaultState The default switch state
	 */
	public SwitchComponent(int x, int y, int width, int height, ItemStack icon, boolean defaultState) {
		super(x, y, width, height);
		this.off = InventoryUtils.hideGlow(icon);
		this.on = InventoryUtils.addGlow(icon);
		this.state = new WrappedProperty<Boolean>(defaultState);
	}
	
	/**
	 * Creates a switch component with the specified values.
	 * 
	 * @param x The X position from the left (Starts at 0)
	 * @param y The Y position from the top (Starts at 0)
	 * @param off The item to display when the switch is off
	 * @param on The item to display when the switch is on
	 */
	public SwitchComponent(int x, int y, ItemStack off, ItemStack on) {
		this(x, y, 1, 1, off, on, false);
	}
	
	/**
	 * Creates a switch component with the specified values.
	 * 
	 * @param x The X position from the left (Starts at 0)
	 * @param y The Y position from the top (Starts at 0)
	 * @param off The item to display when the switch is off
	 * @param on The item to display when the switch is on
	 * @param defaultState The default switch state
	 */
	public SwitchComponent(int x, int y, ItemStack off, ItemStack on, boolean defaultState) {
		this(x, y, 1, 1, off, on, defaultState);
	}
	
	/**
	 * Creates a switch component with the specified values.
	 * 
	 * @param x The X position from the left (Starts at 0)
	 * @param y The Y position from the top (Starts at 0)
	 * @param width The width
	 * @param height The height
	 * @param off The item to display when the switch is off
	 * @param on The item to display when the switch is on
	 */
	public SwitchComponent(int x, int y, int width, int height, ItemStack off, ItemStack on) {
		this(x, y, width, height, off, on, false);
	}
	
	/**
	 * Creates a switch component with the specified values.
	 * 
	 * @param x The X position from the left (Starts at 0)
	 * @param y The Y position from the top (Starts at 0)
	 * @param width The width
	 * @param height The height
	 * @param off The item to display when the switch is off
	 * @param on The item to display when the switch is on
	 * @param defaultState The default switch state
	 */
	public SwitchComponent(int x, int y, int width, int height, ItemStack off, ItemStack on, boolean defaultState) {
		super(x, y, width, height);
		this.off = off;
		this.on = on;
		this.state = new WrappedProperty<Boolean>(defaultState);
	}
	
	/**
	 * Renders the switch onto the interface draw buffer.
	 */
	@Override
	public void render(InterfaceState interfaceState, ItemStack[][] drawBuffer) {
		InventoryUtils.drawFilledRectangle(drawBuffer, state.getValue() ? on : off, x, y, width, height);
	}
	
	/**
	 * Handles the click event.
	 * Switches the switch state from on to off and vice verca.
	 */
	@Override
	public void onClick(InterfaceState interfaceState, ClickType type, ItemStack item, int x, int y) {
		state.setValue(!state.getValue());
	}
	
	/**
	 * Gets the switch state.
	 * 
	 * @return The state
	 */
	public WrappedProperty<Boolean> getState() {
		return state;
	}
	
	/**
	 * Gets the switch's displayed item.
	 * 
	 * @return The displayed item
	 */
	public ItemStack getStateItem() {
		return state.getValue() ? on : off;
	}

}
