package com.tek.rcore.ui.components;

import org.bukkit.inventory.ItemStack;

import com.tek.rcore.item.InventoryUtils;
import com.tek.rcore.ui.InterfaceComponent;
import com.tek.rcore.ui.InterfaceState;

public class StaticComponent extends InterfaceComponent {

	private ItemStack item;
	
	public StaticComponent(int x, int y, ItemStack item) {
		this(x, y, 1, 1, item);
	}
	
	public StaticComponent(int x, int y, int width, int height, ItemStack item) {
		super(x, y, width, height);
		this.item = item;
	}
	
	@Override
	public void render(InterfaceState interfaceState, ItemStack[][] drawBuffer) {
		InventoryUtils.drawFilledRectangle(drawBuffer, item, x, y, width, height);
	}
	
	public ItemStack getItem() {
		return item;
	}
	
	public void setItem(ItemStack item) {
		this.item = item;
	}

}
