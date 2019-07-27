package com.tek.rcore.ui.events;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import com.tek.rcore.ui.InterfaceState;

/**
 * A class which represents a
 * click on a {@link ButtonComponent}.
 * 
 * @author RedstoneTek
 */
public class ButtonClickEvent {
	
	//The ButtonClickEvent values
	private Player player;
	private InterfaceState interfaceState;
	private ClickType clickType;
	private ItemStack item;
	private int x, y;
	
	/**
	 * Creates a ButtonClickEvent with
	 * the specified values.
	 * <p>NOTE: The X and Y coordinates are defined relative to the component's X and Y position.
	 * For instance, if the X position of the component is X5 and the user clicks at X6, the X coordinate
	 * passed would be X1 (X6-X5=X1)</p>
	 * 
	 * @param player The player who clicked
	 * @param interfaceState The interface state clicked
	 * @param clickType The click type
	 * @param item The cursor item
	 * @param x The relative X position from the left (Starts at 0, see above)
	 * @param y The relative Y position from the top (Starts at 0, see above)
	 */
	public ButtonClickEvent(Player player, InterfaceState interfaceState, ClickType clickType, ItemStack item, int x, int y) {
		this.player = player;
		this.interfaceState = interfaceState;
		this.clickType = clickType;
		this.item = item;
		this.x = x;
		this.y = y;
	}

	/**
	 * Gets the player who clicked.
	 * 
	 * @return The player who clicked
	 */
	public Player getPlayer() {
		return player;
	}
	
	/**
	 * Gets the interface state clicked.
	 * 
	 * @return The interface state
	 */
	public InterfaceState getInterfaceState() {
		return interfaceState;
	}
	
	/**
	 * Gets the click type.
	 * 
	 * @return The click type
	 */
	public ClickType getClickType() {
		return clickType;
	}
	
	/**
	 * Gets the item in the cursor.
	 * 
	 * @return The cursor item
	 */
	public ItemStack getItem() {
		return item;
	}
	
	/**
	 * Gets the relative X position from the left.
	 * 
	 * @return The relative X position
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Gets the relative Y position from the top.
	 * 
	 * @return The relative Y position
	 */
	public int getY() {
		return y;
	}
	
}
