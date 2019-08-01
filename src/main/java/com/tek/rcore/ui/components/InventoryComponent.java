package com.tek.rcore.ui.components;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import com.tek.rcore.item.InventoryUtils;
import com.tek.rcore.ui.InterfaceComponent;
import com.tek.rcore.ui.InterfaceState;
import com.tek.rcore.ui.WrappedProperty;

/**
 * A component which allows players
 * to interact with items.
 * 
 * @author RedstoneTek
 */
public class InventoryComponent extends InterfaceComponent {

	//The inventory capabilities and internal item inventory
	private ComponentCapabilities capabilities;
	private WrappedProperty<ItemStack[][]> inventoryContents;
	
	/**
	 * Creates an inventory slot at the X and Y position.
	 * 
	 * @param x The X position from the left (Starts at 0)
	 * @param y The Y position from the top (Starts at 0)
	 */
	public InventoryComponent(int x, int y) {
		this(x, y, 1, 1, true, true);
	}
	
	/**
	 * Creates an inventory slot at the X and Y position
	 * with the specified take and add permissions.
	 * 
	 * @param x The X position from the left (Starts at 0)
	 * @param y The Y position from the top (Starts at 0)
	 * @param take Whether the user can take items from the slot
	 * @param add Whether the user can add items to the slot
	 */
	public InventoryComponent(int x, int y, boolean take, boolean add) {
		this(x, y, 1, 1, take, add);
	}
	
	/**
	 * Creates an inventory area at the
	 * X and Y position with the width and height.
	 * 
	 * @param x The X position from the left (Starts at 0)
	 * @param y The Y position from the top (Starts at 0)
	 * @param width The width
	 * @param height The height
	 */
	public InventoryComponent(int x, int y, int width, int height) {
		this(x, y, width, height, true, true);
	}
	
	/**
	 * Creates an inventory area at the
	 * X and Y position with the width and height
	 * with the specified take and add permissions.
	 * 
	 * @param x The X position from the left (Starts at 0)
	 * @param y The Y position from the top (Starts at 0)
	 * @param width The width
	 * @param height The height
	 * @param take Whether the user can take items from the slot
	 * @param add Whether the user can add items to the slot
	 */
	public InventoryComponent(int x, int y, int width, int height, boolean take, boolean add) {
		super(x, y, width, height);
		this.capabilities = new ComponentCapabilities(take, add);
		this.inventoryContents = new WrappedProperty<ItemStack[][]>(new ItemStack[width][height]);
	}
	
	/**
	 * Updates the item contents of the inventory component.
	 */
	@Override
	public void onItemsChange(InterfaceState interfaceState, ItemStack[][] change) {
		this.inventoryContents.setValue(change);
	}
	
	/**
	 * Removes empty items and replaces
	 * them with null items.
	 */
	@Override
	public void tick(InterfaceState interfaceState) {
		boolean changed = false;
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				ItemStack item = inventoryContents.getValue()[x][y];
				if(item != null && InventoryUtils.isItemEmpty(item)) {
					inventoryContents.getValue()[x][y] = null;
					changed = true;
				}
			}
		}
		if(changed) inventoryContents.notifyChange();
	}
	
	/**
	 * Renders the inventory contents onto
	 * the interface draw buffer.
	 */
	@Override
	public void render(InterfaceState interfaceState, ItemStack[][] drawBuffer) {
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				drawBuffer[this.x + x][this.y + y] = inventoryContents.getValue()[x][y];
			}
		}
	}
	
	/**
	 * Returns the inventory capabilities (permissions).
	 */
	@Override
	public ComponentCapabilities getCapabilities() {
		return capabilities;
	}
	
	/**
	 * Returns the item at the specified
	 * X and Y position in the inventory.
	 * 
	 * @param x The X position from the left (Starts at 0)
	 * @param y The Y position from the top (Starts at 0)
	 * @return The ItemStack at the position
	 */
	public ItemStack getItemAt(int x, int y) {
		return inventoryContents.getValue()[x][y];
	}
	
	/**
	 * Sets the item at the specified
	 * X and Y position in the inventory.
	 * 
	 * @param x The X position from the left (Starts at 0)
	 * @param y The Y position from the top (Starts at 0)
	 * @param item The ItemStack at the position
	 */
	public void setItemAt(int x, int y, ItemStack item) {
		inventoryContents.getValue()[x][y] = item;
		inventoryContents.notifyChange();
	}
	
	/**
	 * Returns the inventory content wrapped property.
	 * 
	 * @return The wrapped property
	 */
	public WrappedProperty<ItemStack[][]> getInventoryContents() {
		return inventoryContents;
	}
	
	/**
	 * Returns the inventory contents as an array list.
	 * 
	 * @return The inventory contents
	 */
	public List<ItemStack> getContentItems() {
		List<ItemStack> contentItems = new ArrayList<ItemStack>();
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				if(inventoryContents.getValue()[x][y] != null) contentItems.add(inventoryContents.getValue()[x][y]);
			}
		}
		return contentItems;
	}

}
