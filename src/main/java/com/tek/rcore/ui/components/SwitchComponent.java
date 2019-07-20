package com.tek.rcore.ui.components;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import com.tek.rcore.item.InventoryUtils;
import com.tek.rcore.ui.InterfaceComponent;
import com.tek.rcore.ui.InterfaceState;
import com.tek.rcore.ui.WrappedProperty;

public class SwitchComponent extends InterfaceComponent {

	private ItemStack off, on;
	private WrappedProperty<Boolean> state;
	
	public SwitchComponent(int x, int y, ItemStack icon) {
		this(x, y, 1, 1, icon, false);
	}
	
	public SwitchComponent(int x, int y, ItemStack icon, boolean defaultState) {
		this(x, y, 1, 1, icon, defaultState);
	}
	
	public SwitchComponent(int x, int y, int width, int height, ItemStack icon) {
		this(x, y, width, height, icon, false);
	}
	
	public SwitchComponent(int x, int y, int width, int height, ItemStack icon, boolean defaultState) {
		super(x, y, width, height);
		this.off = InventoryUtils.hideGlow(icon);
		this.on = InventoryUtils.addGlow(icon);
		this.state = new WrappedProperty<Boolean>(defaultState);
	}
	
	public SwitchComponent(int x, int y, ItemStack off, ItemStack on) {
		this(x, y, 1, 1, off, on, false);
	}
	
	public SwitchComponent(int x, int y, ItemStack off, ItemStack on, boolean defaultState) {
		this(x, y, 1, 1, off, on, defaultState);
	}
	
	public SwitchComponent(int x, int y, int width, int height, ItemStack off, ItemStack on) {
		this(x, y, width, height, off, on, false);
	}
	
	public SwitchComponent(int x, int y, int width, int height, ItemStack off, ItemStack on, boolean defaultState) {
		super(x, y, width, height);
		this.off = off;
		this.on = on;
		this.state = new WrappedProperty<Boolean>(defaultState);
	}
	
	@Override
	public void render(InterfaceState interfaceState, ItemStack[][] drawBuffer) {
		InventoryUtils.drawFilledRectangle(drawBuffer, state.getValue() ? on : off, x, y, width, height);
	}
	
	@Override
	public void onClick(InterfaceState interfaceState, ClickType type, ItemStack item, int x, int y) {
		state.setValue(!state.getValue());
	}
	
	public WrappedProperty<Boolean> getState() {
		return state;
	}
	
	public ItemStack getStateItem() {
		return state.getValue() ? on : off;
	}

}
