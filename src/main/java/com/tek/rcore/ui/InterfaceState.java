package com.tek.rcore.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.tek.rcore.item.InventoryUtils;
import com.tek.rcore.ui.events.InterfaceCloseEvent;
import com.tek.rcore.ui.events.InterfaceCloseEvent.InterfaceCloseType;

/**
 * A class template for interface
 * states. Interface States are
 * basically one "window" or one
 * "State" of your application.
 * 
 * @author RedstoneTek
 */
public abstract class InterfaceState {
	
	//The internal interface state values.
	private UUID uuid;
	private Inventory inventory;
	protected List<InterfaceComponent> components;
	private ItemStack[][] drawBuffer;
	private WrappedProperty<InterfaceCloseEvent> closed;
	private InterfaceCloseType closeType;
	
	/**
	 * Creates an unconventional interface state which doesn't utilize an inventory.
	 */
	public InterfaceState() {
		this.closed = new WrappedProperty<InterfaceCloseEvent>();
		this.closeType = InterfaceCloseType.PLAYER;
	}
	
	/**
	 * Creates an InterfaceState with
	 * the specified title and row count.
	 * 
	 * @param title The inventory title
	 * @param rows The inventory row count/height
	 */
	public InterfaceState(String title, int rows) {
		this.inventory = Bukkit.createInventory(null, rows * 9, title);
		this.components = new ArrayList<InterfaceComponent>();
		this.drawBuffer = new ItemStack[9][rows];
		this.closed = new WrappedProperty<InterfaceCloseEvent>();
		this.closeType = InterfaceCloseType.PLAYER;
	}
	
	/**
	 * Initializes the Interface State.
	 * All components must be registered here.
	 * <p>NOTE: Components added later will be rendered above the earlier components.
	 * As such, you could have a large pane in the back, with components in the front.</p>
	 * 
	 * @param components The component list in which to add components.
	 */
	public abstract void initialize(List<InterfaceComponent> components);
	
	/**
	 * Displays the interface state to the player.
	 */
	public void show() {
		getOwner().openInventory(inventory);
	}
	
	/**
	 * Initializes and opens the interface
	 * state to the player.
	 */
	public void open() {
		initialize(components);
		show();
		tick();
		render();
	}
	
	/**
	 * Closes the interface on the player's side.
	 */
	public void close() {
		getOwner().closeInventory();
	}
	
	/**
	 * Called every so often, updates the values
	 * of the interface components.
	 */
	public void tick() {
		for(InterfaceComponent component : components) {
			component.tick(this);
		}
	}
	
	/**
	 * Clears the rendering draw buffer.
	 */
	private void clearBuffer() {
		for(int x = 0; x < 9; x++) {
			for(int y = 0; y < inventory.getSize() / 9; y++) {
				drawBuffer[x][y] = null;
			}
		}
	}
	
	/**
	 * Transfers the draw buffer to the inventory.
	 */
	private void drawBuffer() {
		for(int x = 0; x < 9; x++) {
			for(int y = 0; y < inventory.getSize() / 9; y++) {
				inventory.setItem(InventoryUtils.slotFromCoordinates(x, y), drawBuffer[x][y]);
			}
		}
	}
	
	/**
	 * Clears the buffer, renders every component
	 * and then transfers the draw buffer onto the inventory.
	 */
	public void render() {
		clearBuffer();
		for(InterfaceComponent component : components) {
			component.render(this, drawBuffer);
		}
		drawBuffer();
	}
	
	/**
	 * Called when the player clicks on the interface.
	 * The click is then transferred to components that apply.
	 * 
	 * @param type The ClickType
	 * @param item The item on the cursor
	 * @param x The X position from the left (Starts at 0)
	 * @param y The Y position from the top (Starts at 0)
	 */
	public void onClick(ClickType type, ItemStack item, int x, int y) {
		if(y >= inventory.getSize() / 9) return;
		for(InterfaceComponent component : components) {
			if(component.collides(x, y)) {
				if(component.isEditable()) {
					component.onClick(this, type, item, x - component.getX(), y - component.getY());
				}
			}
		}
	}
	
	/**
	 * Called when the player adds/takes an item from the interface.
	 * The change is then transferred to components that apply.
	 * 
	 * @param slot The slot which had a change
	 * @param item The item in the slot
	 */
	public void onChange(int slot, ItemStack item) {
		Map<Integer, ItemStack> changeMap = new HashMap<Integer, ItemStack>();
		changeMap.put(slot, item);
		onChange(changeMap);
	}
	
	/**
	 * Called when the player adds/takes multiple items from the interface.
	 * The changes are then transferred to components that apply.
	 * 
	 * @param newItems The item change map
	 */
	public void onChange(Map<Integer, ItemStack> newItems) {
		Iterator<Entry<Integer, ItemStack>> itemIterator = newItems.entrySet().iterator();
		while(itemIterator.hasNext()) {
			Entry<Integer, ItemStack> entry = itemIterator.next();
			if(InventoryUtils.slotToY(entry.getKey()) >= InventoryUtils.getHeight(inventory.getSize())) {
				itemIterator.remove();
			}
		}
		
		components.stream()
				.filter(component -> newItems.keySet().stream()
						.anyMatch(slot -> component.collides(InventoryUtils.slotToX(slot), InventoryUtils.slotToY(slot))))
				.filter(component -> component.getCapabilities().canAdd() || component.getCapabilities().canTake())
				.filter(InterfaceComponent::isEditable)
				.distinct()
				.forEach(component -> {
					component.onItemsChange(this, spliceInventorySection(newItems, component.getX(), component.getY(), component.getWidth(), component.getHeight()));
				});
	}
	
	/**
	 * Checks whether the player can take the item
	 * at the specified X and Y coordinate.
	 * 
	 * @param x The X coordinate from the left (Starts at 0)
	 * @param y The Y coordinate from the top (Start at 0)
	 * @return Whether the player can take the item
	 */
	public boolean canTake(int x, int y) {
		if(y >= InventoryUtils.getHeight(inventory.getSize())) return true;
		
		//Reverse to get components in render order (first is the last to render, so the top one)
		Collections.reverse(components);
		//Get top rendering component at position.
		Optional<InterfaceComponent> componentOpt = components.stream().filter(component -> component.collides(x, y)).findFirst();
		//Return components to their original order
		Collections.reverse(components);
		
		if(componentOpt.isPresent()) {
			return componentOpt.get().getCapabilities().canTake();
		} else {
			return false;
		}
	}
	
	/**
	 * Checks whether the player can add the item
	 * at the specified X and Y coordinate.
	 * 
	 * @param x The X coordinate from the left (Starts at 0)
	 * @param y The Y coordinate from the top (Start at 0)
	 * @return Whether the player can add the item
	 */
	public boolean canAdd(int x, int y) {
		if(y >= InventoryUtils.getHeight(inventory.getSize())) return true;
		
		//Reverse to get components in render order (first is the last to render, so the top one)
		Collections.reverse(components);
		//Get top rendering component at position.
		Optional<InterfaceComponent> componentOpt = components.stream().filter(component -> component.collides(x, y)).findFirst();
		//Return components to their original order
		Collections.reverse(components);
		
		if(componentOpt.isPresent()) {
			return componentOpt.get().getCapabilities().canAdd();
		} else {
			return false;
		}
	}
	
	/**
	 * Splices a part of the inventory and
	 * applies the changes of the change map.
	 * 
	 * @param changeMap The change map
	 * @param x The X coordinate from the left (Starts at 0)
	 * @param y The Y coordinate from the top (Starts at 0)
	 * @param width The width of the area
	 * @param height The height of the area
	 * @return The spliced inventory section
	 */
	private ItemStack[][] spliceInventorySection(Map<Integer, ItemStack> changeMap, int x, int y, int width, int height) {
		ItemStack[][] spliced = new ItemStack[width][height];
		for(int x1 = 0; x1 < width; x1++) {
			for(int y1 = 0; y1 < height; y1++) {
				if(changeMap != null && changeMap.containsKey(InventoryUtils.slotFromCoordinates(x + x1, y + y1))) {
					spliced[x1][y1] = changeMap.get(InventoryUtils.slotFromCoordinates(x + x1, y + y1));
				} else {
					ItemStack item = inventory.getItem(InventoryUtils.slotFromCoordinates(x + x1, y + y1));
					spliced[x1][y1] = item == null || item.getType().equals(Material.AIR) ? null : item;
				}
			}
		}
		return spliced;
	}
	
	/**
	 * Returns the closed property.
	 * <p>NOTE: To add a close watcher, use this property.</p>
	 * 
	 * @return The closed property
	 */
	public WrappedProperty<InterfaceCloseEvent> getClosedProperty() {
		return closed;
	}
	
	/**
	 * Sets the viewer's UUID.
	 * 
	 * @param uuid The viewer's UUID
	 */
	public void setUUID(UUID uuid) {
		this.uuid = uuid;
	}
	
	/**
	 * Returns the UUID of the viewer.
	 * 
	 * @return The viewer UUID
	 */
	public UUID getUUID() {
		return uuid;
	}
	
	/**
	 * Returns the player associated with the UUID.
	 * 
	 * @return The player/owner
	 */
	public Player getOwner() {
		return Bukkit.getPlayer(uuid);
	}
	
	/**
	 * Returns the inventory behind
	 * the interface state.
	 * 
	 * @return The inventory
	 */
	public Inventory getInventory() {
		return inventory;
	}
	
	/**
	 * Returns the next close event's close type.
	 * 
	 * @return The next close event's close type
	 */
	public InterfaceCloseType getCloseType() {
		return closeType;
	}
	
	/**
	 * Sets the next close event's close type.
	 * 
	 * @param closeType The next close event's close type
	 */
	public void setCloseType(InterfaceCloseType closeType) {
		this.closeType = closeType;
	}
	
}
