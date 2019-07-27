package com.tek.rcore.ui;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import com.tek.rcore.ui.components.InventoryComponent;

/**
 * A class template for interface
 * components. Many implementations
 * can be found in the [com.tek.rcore.ui.components]
 * package.
 * 
 * @author RedstoneTek
 */
public class InterfaceComponent {
	
	//Whether the interface should react to clicks and changes.
	protected boolean editable;
	//The positional information of the component.
	protected int x, y, width, height;
	
	/**
	 * Creates an interface component with
	 * the specified position and size.
	 * 
	 * @param x The X position from the left (Starts at 0)
	 * @param y The Y position from the top (Starts at 0)
	 * @param width The width of the component
	 * @param height The height of the component
	 */
	public InterfaceComponent(int x, int y, int width, int height) {
		this.editable = true;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	/**
	 * Checks if a position collides
	 * with the interface component.
	 * 
	 * @param x The X position from the left (Starts at 0)
	 * @param y The Y position from the top (Starts at 0)
	 * @return Whether the position collides or not
	 */
	public boolean collides(int x, int y) {
		return x >= this.x && y >= this.y
				&& x < this.x + this.width && y < this.y + this.height;
	}
	
	/**
	 * Called when the interface is on view,
	 * renders the items onto the draw buffer.
	 * <p>NOTE: Since this is only called when the interface is the one on view,
	 * any updates to the component should be handled in the tick() method.</p>
	 * 
	 * @param interfaceState The InterfaceState which contains the component
	 * @param drawBuffer The draw buffer on which to render the component
	 */
	public void render(InterfaceState interfaceState, ItemStack[][] drawBuffer) { }
	
	/**
	 * Called every so often, even if the interface
	 * is a layer above. Component logic should be
	 * handled here.
	 * 
	 * @param interfaceState The InterfaceState which contains the component
	 */
	public void tick(InterfaceState interfaceState) { }
	
	/**
	 * Called when the user clicks on the component.
	 * <p>NOTE: The X and Y coordinates are defined relative to the component's X and Y position.
	 * For instance, if the X position of the component is X5 and the user clicks at X6, the X coordinate
	 * passed would be X1 (X6-X5=X1)</p>
	 * 
	 * @param interfaceState The InterfaceState which contains the component
	 * @param type The ClickType of the event
	 * @param item The item in the cursor of the player
	 * @param x The relative X coordinate from the left (Starts at 0, see above)
	 * @param y The relative Y coordinate from the top (Starts at 0, see above)
	 */
	public void onClick(InterfaceState interfaceState, ClickType type, ItemStack item, int x, int y) { }
	
	/**
	 * Called when the user interacts with the items of
	 * the component. This is notably used in {@link InventoryComponent}
	 * to make crafting grids, item slots and such.
	 * 
	 * @param interfaceState The InterfaceState which contains the component.
	 * @param change The new item contents of the "component area (x, y, width, height)"
	 */
	public void onItemsChange(InterfaceState interfaceState, ItemStack[][] change) { }
	
	/**
	 * Returns the actions that users can do
	 * relative to the component. For instance,
	 * if they can take items or place items in the component.
	 * 
	 * @return The ComponentCapabilities object
	 */
	public ComponentCapabilities getCapabilities() {
		return ComponentCapabilities.DEFAULT;
	}
	
	/**
	 * Checks whether the component can be edited.
	 * 
	 * @return Whether the component can be edited or not
	 */
	public boolean isEditable() {
		return editable;
	}
	
	/**
	 * Sets the component as editable or not.
	 * 
	 * @param editable Whether the component is editable or not
	 */
	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	
	/**
	 * Returns the component's X position.
	 * 
	 * @return The X position from the left (Starts at 0)
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Returns the component's Y position.
	 * 
	 * @return The Y position from the top (Starts at 0)
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Returns the component's width.
	 * 
	 * @return The width
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Returns the component's height.
	 * 
	 * @return The height
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * A class which holds the action capabilities
	 * of user's related to specific components.
	 * 
	 * @author RedstoneTek
	 */
	public static class ComponentCapabilities {
		
		//The default component capabilities, which restricts every action.
		public static final ComponentCapabilities DEFAULT = new ComponentCapabilities(false, false);
		
		//Take and Add flags.
		private boolean canTake;
		private boolean canAdd;
		
		/**
		 * Creates a ComponentCapabilities object
		 * with the specified capabilities/flags.
		 * 
		 * @param canTake Whether players can take items from the component.
		 * @param canAdd Whether players can add items to the component.
		 */
		public ComponentCapabilities(boolean canTake, boolean canAdd) {
			this.canTake = canTake;
			this.canAdd = canAdd;
		}
		
		/**
		 * Checks whether players can take items from the component.
		 * 
		 * @return Whether players can take items from the component.
		 */
		public boolean canTake() {
			return canTake;
		}
		
		/**
		 * Checks whether players can add items to the component.
		 * 
		 * @return Whether players can add items to the component
		 */
		public boolean canAdd() {
			return canAdd;
		}
		
		/**
		 * Sets the `Take` flag of the capabilities.
		 * 
		 * @param canTake The `Take` flag value
		 */
		public void setCanTake(boolean canTake) {
			this.canTake = canTake;
		}
		
		/**
		 * Sets the `Add` flag of the capabilities.
		 * 
		 * @param canAdd The `Add` flag value
		 */
		public void setCanAdd(boolean canAdd) {
			this.canAdd = canAdd;
		}
		
	}
	
}
