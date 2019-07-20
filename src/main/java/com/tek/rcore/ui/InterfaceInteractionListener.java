package com.tek.rcore.ui;

import static org.bukkit.event.inventory.InventoryAction.CLONE_STACK;
import static org.bukkit.event.inventory.InventoryAction.COLLECT_TO_CURSOR;
import static org.bukkit.event.inventory.InventoryAction.DROP_ALL_CURSOR;
import static org.bukkit.event.inventory.InventoryAction.DROP_ALL_SLOT;
import static org.bukkit.event.inventory.InventoryAction.DROP_ONE_CURSOR;
import static org.bukkit.event.inventory.InventoryAction.DROP_ONE_SLOT;
import static org.bukkit.event.inventory.InventoryAction.HOTBAR_MOVE_AND_READD;
import static org.bukkit.event.inventory.InventoryAction.HOTBAR_SWAP;
import static org.bukkit.event.inventory.InventoryAction.MOVE_TO_OTHER_INVENTORY;
import static org.bukkit.event.inventory.InventoryAction.NOTHING;
import static org.bukkit.event.inventory.InventoryAction.PICKUP_ALL;
import static org.bukkit.event.inventory.InventoryAction.PICKUP_HALF;
import static org.bukkit.event.inventory.InventoryAction.PICKUP_ONE;
import static org.bukkit.event.inventory.InventoryAction.PICKUP_SOME;
import static org.bukkit.event.inventory.InventoryAction.PLACE_ALL;
import static org.bukkit.event.inventory.InventoryAction.PLACE_ONE;
import static org.bukkit.event.inventory.InventoryAction.PLACE_SOME;
import static org.bukkit.event.inventory.InventoryAction.SWAP_WITH_CURSOR;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.DragType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

import com.tek.rcore.RedstoneCore;
import com.tek.rcore.item.InventoryUtils;
import com.tek.rcore.ui.events.InterfaceCloseEvent;

public class InterfaceInteractionListener implements Listener {
	
	private InterfaceManager instance;
	
	public InterfaceInteractionListener(InterfaceManager instance) {
		this.instance = instance;
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		InventoryAction a = event.getAction();
		int x = InventoryUtils.slotToX(event.getRawSlot());
		int y = InventoryUtils.slotToY(event.getRawSlot());
		
		if(!a.equals(NOTHING) && !a.equals(NOTHING)) {
			Optional<List<InterfaceState>> interfaceStates = instance.getInterfaceStates(event.getWhoClicked().getUniqueId());
			if(interfaceStates.isPresent()) {
				InterfaceState current = interfaceStates.get().get(0);
				
				if(a.equals(COLLECT_TO_CURSOR) || a.equals(HOTBAR_MOVE_AND_READD)) {
					event.setCancelled(true);
				}
				
				else if(a.equals(CLONE_STACK) || a.equals(DROP_ALL_CURSOR) || a.equals(DROP_ALL_SLOT) || a.equals(DROP_ONE_CURSOR) || a.equals(DROP_ONE_SLOT) ||a.equals(HOTBAR_SWAP)) {
					if(event.getClickedInventory().equals(current.getInventory())) {
						event.setCancelled(true);
					}
				} 
				
				else if(a.equals(MOVE_TO_OTHER_INVENTORY)) {
					if(event.getClickedInventory().equals(current.getInventory())) {
						if(current.canTake(x, y)) {
							ItemStack slot = event.getCurrentItem().clone();
							if(getFitAmount((Player)event.getWhoClicked(), slot) < slot.getAmount()) {
								slot.setAmount(slot.getAmount() - getFitAmount((Player)event.getWhoClicked(), slot));
							} else {
								slot.setAmount(0);
							}
							current.onChange(event.getRawSlot(), slot);
						} else {
							event.setCancelled(true);
						}
					} else {
						event.setCancelled(true);
					}
				}
				
				else {
					if(a.equals(PICKUP_ALL) || a.equals(PICKUP_HALF) || a.equals(PICKUP_ONE) || a.equals(PICKUP_SOME)) {
						if(!current.canTake(x, y)) {
							event.setCancelled(true);
						} else {
							ItemStack cursor = event.getCursor() == null ? null : event.getCursor().clone();
							ItemStack slot = event.getCurrentItem() == null ? null : event.getCurrentItem().clone();
							ItemStack pickedUp = slot.clone();
							if(a.equals(PICKUP_ALL)) pickedUp.setAmount(0);
							if(a.equals(PICKUP_HALF)) pickedUp.setAmount(cursor == null ? (int)Math.ceil(slot.getAmount() / 2) : (int)Math.ceil(slot.getAmount() / 2) + cursor.getAmount());
							if(a.equals(PICKUP_ONE)) pickedUp.setAmount(cursor == null ? 1 : cursor.getAmount() + 1);
							if(a.equals(PICKUP_SOME)) pickedUp.setAmount(Math.min(cursor.getMaxStackSize(), cursor.getAmount() + slot.getAmount()));
							current.onChange(event.getRawSlot(), pickedUp);
						}
					} else if(a.equals(PLACE_ALL) || a.equals(PLACE_ONE) || a.equals(PLACE_SOME)) {
						if(!current.canAdd(x, y)) {
							event.setCancelled(true);
						} else {
							ItemStack cursor = event.getCursor() == null ? null : event.getCursor().clone();
							ItemStack slot = event.getCurrentItem() == null ? null : event.getCurrentItem().clone();
							ItemStack placed = cursor.clone();
							if(a.equals(PLACE_ALL)) placed.setAmount(slot == null ? cursor.getAmount() : slot.getAmount() + cursor.getAmount());
							if(a.equals(PLACE_ONE)) placed.setAmount(slot == null ? 1 : slot.getAmount() + 1);
							if(a.equals(PLACE_SOME)) placed.setAmount(Math.min(slot.getMaxStackSize(), slot.getAmount() + cursor.getAmount()));
							current.onChange(event.getRawSlot(), placed);
						}
					} else if(a.equals(SWAP_WITH_CURSOR)) {
						if(!current.canAdd(x, y) || !current.canTake(x, y)) {
							event.setCancelled(true);
						} else {
							current.onChange(event.getRawSlot(), event.getCursor());
						}
					} else {
						event.setCancelled(true);
					}
				}
				
				current.onClick(event.getClick(), event.getCursor(), x, y);
			}
		}
	}
	
	@EventHandler
	public void onInventoryDrag(InventoryDragEvent event) {
		Optional<List<InterfaceState>> interfaceStates = instance.getInterfaceStates(event.getWhoClicked().getUniqueId());
		if(interfaceStates.isPresent()) {
			int slotCount = event.getRawSlots().size();
			Iterator<Integer> slotIterator = event.getRawSlots().iterator();
			while(slotIterator.hasNext()) {
				int slot = slotIterator.next();
				if(!interfaceStates.get().get(0).canAdd(InventoryUtils.slotToX(slot), InventoryUtils.slotToY(slot))) {
					slotIterator.remove();
				}
			}
			
			if(event.getRawSlots().isEmpty()) {
				event.setCancelled(true);
			} else {
				ItemStack toDivide = event.getOldCursor();
				int perSlot = event.getType().equals(DragType.SINGLE) ? 1 : (int) Math.floor((double)toDivide.getAmount() / (double)slotCount);
				int remaining = toDivide.getAmount();
				for(Integer slot : event.getRawSlots()) {
					ItemStack atSlot = event.getView().getItem(slot);
					if(atSlot == null || atSlot.getType().equals(Material.AIR)) {
						remaining -= perSlot;
						continue;
					}
					remaining -= Math.min(perSlot, atSlot.getMaxStackSize() - atSlot.getAmount());
				}
				ItemStack cursorItem = event.getOldCursor();
				cursorItem.setAmount(remaining);
				event.setCursor(cursorItem);
				
				Map<Integer, ItemStack> changeMap = new HashMap<Integer, ItemStack>();
				event.getNewItems().forEach(changeMap::put);
				interfaceStates.get().get(0).onChange(changeMap);
				
				RedstoneCore.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(RedstoneCore.getInstance(), () -> {
					interfaceStates.get().get(0).render();
				}, 0);
			}
		}
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		Optional<List<InterfaceState>> interfaceStates = instance.getInterfaceStates(event.getPlayer().getUniqueId());
		if(interfaceStates.isPresent()) {
			if(interfaceStates.get().get(0).getInventory().equals(event.getInventory())) {
				interfaceStates.get().get(0).onClose();
				interfaceStates.get().get(0).getClosedProperty().setValue(new InterfaceCloseEvent((Player)event.getPlayer(), interfaceStates.get().get(0)));
				instance.removeInterfaceLayer((Player) event.getPlayer());
			}
		}
	}
	
	private int getFitAmount(Player p, ItemStack item) {
		int sum = 0;
		for(ItemStack slot : p.getInventory().getStorageContents()) {
			if(slot == null || slot.getType().equals(Material.AIR)) {
				sum += item.getMaxStackSize();
				continue;
			}
			
			if(slot.isSimilar(item)) sum += item.getMaxStackSize() - slot.getAmount();
		}
		
		return sum;
	}
	
}
