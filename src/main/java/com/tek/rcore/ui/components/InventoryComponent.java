package com.tek.rcore.ui.components;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.tek.rcore.ui.InterfaceComponent;
import com.tek.rcore.ui.InterfaceState;
import com.tek.rcore.ui.WrappedProperty;

public class InventoryComponent extends InterfaceComponent {

	private ComponentCapabilities capabilities;
	private WrappedProperty<ItemStack[][]> inventoryContents;
	
	public InventoryComponent(int x, int y) {
		this(x, y, 1, 1, true, true);
	}
	
	public InventoryComponent(int x, int y, boolean take, boolean add) {
		this(x, y, 1, 1, take, add);
	}
	
	public InventoryComponent(int x, int y, int width, int height) {
		this(x, y, width, height, true, true);
	}
	
	public InventoryComponent(int x, int y, int width, int height, boolean take, boolean add) {
		super(x, y, width, height);
		this.capabilities = new ComponentCapabilities(take, add);
		this.inventoryContents = new WrappedProperty<ItemStack[][]>(new ItemStack[width][height]);
	}
	
	@Override
	public void onItemsChange(InterfaceState interfaceState, ItemStack[][] change) {
		this.inventoryContents.setValue(change);
	}
	
	@Override
	public void render(InterfaceState interfaceState, ItemStack[][] drawBuffer) {
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				drawBuffer[this.x + x][this.y + y] = inventoryContents.getValue()[x][y];
			}
		}
	}
	
	@Override
	public ComponentCapabilities getCapabilities() {
		return capabilities;
	}
	
	public ItemStack getItemAt(int x, int y) {
		return inventoryContents.getValue()[x][y];
	}
	
	public void setItemAt(int x, int y, ItemStack item) {
		inventoryContents.getValue()[x][y] = item;
		inventoryContents.notifyChange();
	}
	
	public WrappedProperty<ItemStack[][]> getInventoryContents() {
		return inventoryContents;
	}
	
	public List<ItemStack> getContentItems() {
		List<ItemStack> contentItems = new ArrayList<ItemStack>();
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				if(inventoryContents.getValue()[x][y] != null && !inventoryContents.getValue()[x][y].getType().equals(Material.AIR))
					contentItems.add(inventoryContents.getValue()[x][y]);
			}
		}
		return contentItems;
	}

}
