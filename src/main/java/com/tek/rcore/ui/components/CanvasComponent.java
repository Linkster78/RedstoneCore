package com.tek.rcore.ui.components;

import org.bukkit.inventory.ItemStack;

import com.tek.rcore.ui.InterfaceComponent;
import com.tek.rcore.ui.InterfaceState;
import com.tek.rcore.ui.WrappedProperty;

/**
 * A component which gives an
 * itemstack array to work with.
 * 
 * @author RedstoneTek
 */
public class CanvasComponent extends InterfaceComponent {

	//The internal draw buffer
	private WrappedProperty<ItemStack[][]> canvas;
	
	/**
	 * Creates a Canvas component at the specified
	 * X position and Y position with the
	 * specified width and height.
	 * 
	 * @param x The X position from the left (Starts at 0)
	 * @param y The Y position from the top (Starts at 0)
	 * @param width The width
	 * @param height The height
	 */
	public CanvasComponent(int x, int y, int width, int height) {
		super(x, y, width, height);
		canvas = new WrappedProperty<ItemStack[][]>(new ItemStack[width][height]);
	}
	
	/**
	 * Renders the canvas's draw buffer
	 * onto the interface's draw buffer.
	 */
	@Override
	public void render(InterfaceState interfaceState, ItemStack[][] drawBuffer) {
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				drawBuffer[this.x + x][this.y + y] = canvas.getValue()[x][y];
			}
		}
	}
	
	/**
	 * Gets the canvas's internal draw buffer.
	 * 
	 * @return The internal draw buffer
	 */
	public WrappedProperty<ItemStack[][]> getCanvas() {
		return canvas;
	}

}
