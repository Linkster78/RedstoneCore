package com.tek.rcore.ui.components;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import com.tek.rcore.ui.InterfaceState;

public interface IPaginatedItem {
	
	public ItemStack render(InterfaceState interfaceState);
	public void tick(InterfaceState interfaceState);
	public void click(InterfaceState interfaceState, ClickType type, ItemStack item);
	
}
