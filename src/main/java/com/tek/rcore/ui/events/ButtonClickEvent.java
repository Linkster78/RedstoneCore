package com.tek.rcore.ui.events;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import com.tek.rcore.ui.InterfaceState;

public class ButtonClickEvent {
	
	private Player player;
	private InterfaceState interfaceState;
	private ClickType clickType;
	private ItemStack item;
	private int x, y;
	
	public ButtonClickEvent(Player player, InterfaceState interfaceState, ClickType clickType, ItemStack item, int x, int y) {
		this.player = player;
		this.interfaceState = interfaceState;
		this.clickType = clickType;
		this.item = item;
		this.x = x;
		this.y = y;
	}

	public Player getPlayer() {
		return player;
	}
	
	public InterfaceState getInterfaceState() {
		return interfaceState;
	}
	
	public ClickType getClickType() {
		return clickType;
	}
	
	public ItemStack getItem() {
		return item;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
}
