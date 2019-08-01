package com.tek.rcore.ui.components;

import java.util.List;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import com.tek.rcore.item.InventoryUtils;
import com.tek.rcore.ui.InterfaceComponent;
import com.tek.rcore.ui.InterfaceState;
import com.tek.rcore.ui.WrappedProperty;

/**
 * A component which rotates through 
 * a selection of items when clicked.
 * 
 * @author RedstoneTek
 */
public class RotateComponent extends InterfaceComponent {

	//The internal rotation values
	private List<ItemStack> rotation;
	private WrappedProperty<Integer> index;
	
	/**
	 * Creates a rotate component at the specified
	 * X and Y position with the rotation list.
	 * 
	 * @param x The X position from the left (Starts at 0)
	 * @param y The Y position from the top (Starts at 0)
	 * @param rotation The item rotation
	 */
	public RotateComponent(int x, int y, List<ItemStack> rotation) {
		this(x, y, 1, 1, rotation);
	}
	
	/**
	 * Creates a rotate component at the specified
	 * X and Y position with the width and height
	 * and the rotation list.
	 * 
	 * @param x The X position from the left (Starts at 0)
	 * @param y The Y position from the top (Starts at 0)
	 * @param width The width
	 * @param height The height
	 * @param rotation The item rotation
	 */
	public RotateComponent(int x, int y, int width, int height, List<ItemStack> rotation) {
		super(x, y, width, height);
		this.rotation = rotation;
		this.index = new WrappedProperty<Integer>(0);
	}
	
	/**
	 * Renders the rotation component onto the interface draw buffer.
	 */
	@Override
	public void render(InterfaceState interfaceState, ItemStack[][] drawBuffer) {
		InventoryUtils.drawFilledRectangle(drawBuffer, rotation.get(index.getValue()), x, y, width, height);
	}
	
	/**
	 * Handles the click event.
	 * Rotates the current index.
	 */
	@Override
	public void onClick(InterfaceState interfaceState, ClickType type, ItemStack item, int x, int y) {
		int newIndex = index.getValue() + (type.equals(ClickType.LEFT) ? 1 : type.equals(ClickType.RIGHT) ? -1 : 0);
		if(newIndex >= rotation.size()) newIndex = 0;
		if(newIndex < 0) newIndex = rotation.size() - 1;
		index.setValue(newIndex);
	}
	
	/**
	 * Gets the current item index.
	 * 
	 * @return The index
	 */
	public WrappedProperty<Integer> getIndex() {
		return index;
	}
	
	/**
	 * Gets the currently displayed item.
	 * 
	 * @return The displayed item
	 */
	public ItemStack getCurrentItem() {
		return rotation.get(index.getValue());
	}

}
