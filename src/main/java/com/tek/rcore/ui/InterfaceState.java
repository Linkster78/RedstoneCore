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

public abstract class InterfaceState {
	
	private UUID uuid;
	private Inventory inventory;
	protected List<InterfaceComponent> components;
	private ItemStack[][] drawBuffer;
	private WrappedProperty<InterfaceCloseEvent> closed;
	private InterfaceCloseType closeType;
	
	public InterfaceState(String title, int rows) {
		this.inventory = Bukkit.createInventory(null, rows * 9, title);
		this.components = new ArrayList<InterfaceComponent>();
		this.drawBuffer = new ItemStack[9][rows];
		this.closed = new WrappedProperty<InterfaceCloseEvent>();
		this.closeType = InterfaceCloseType.PLAYER;
	}
	
	public abstract void initialize(List<InterfaceComponent> components);
	
	public void show(Player player) {
		player.openInventory(inventory);
	}
	
	public void open(Player player) {
		initialize(components);
		tick();
		render();
		show(player);
	}
	
	public void tick() {
		for(InterfaceComponent component : components) {
			component.tick(this);
		}
	}
	
	private void clearBuffer() {
		for(int x = 0; x < 9; x++) {
			for(int y = 0; y < inventory.getSize() / 9; y++) {
				drawBuffer[x][y] = null;
			}
		}
	}
	
	private void drawBuffer() {
		for(int x = 0; x < 9; x++) {
			for(int y = 0; y < inventory.getSize() / 9; y++) {
				inventory.setItem(InventoryUtils.slotFromCoordinates(x, y), drawBuffer[x][y]);
			}
		}
	}
	
	public void render() {
		clearBuffer();
		for(InterfaceComponent component : components) {
			component.render(this, drawBuffer);
		}
		drawBuffer();
	}
	
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
	
	public void onChange(int slot, ItemStack item) {
		Map<Integer, ItemStack> changeMap = new HashMap<Integer, ItemStack>();
		changeMap.put(slot, item);
		onChange(changeMap);
	}
	
	public void onChange(Map<Integer, ItemStack> newItems) {
		Iterator<Entry<Integer, ItemStack>> itemIterator = newItems.entrySet().iterator();
		while(itemIterator.hasNext()) {
			Entry<Integer, ItemStack> entry = itemIterator.next();
			if(InventoryUtils.slotToY(entry.getKey()) >= inventory.getSize() / 9) {
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
	
	public boolean canTake(int x, int y) {
		if(y >= inventory.getSize() / 9) return true;
		
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
	
	public boolean canAdd(int x, int y) {
		if(y >= inventory.getSize() / 9) return true;
		
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
	
	public WrappedProperty<InterfaceCloseEvent> getClosedProperty() {
		return closed;
	}
	
	public void setUUID(UUID uuid) {
		this.uuid = uuid;
	}
	
	public UUID getUUID() {
		return uuid;
	}
	
	public Player getOwner() {
		return Bukkit.getPlayer(uuid);
	}
	
	public Inventory getInventory() {
		return inventory;
	}
	
	public InterfaceCloseType getCloseType() {
		return closeType;
	}
	
	public void setCloseType(InterfaceCloseType closeType) {
		this.closeType = closeType;
	}
	
}
