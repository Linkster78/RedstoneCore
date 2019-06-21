package com.tek.rcore.ui.events;

import org.bukkit.entity.Player;

import com.tek.rcore.ui.InterfaceState;

public class InterfaceCloseEvent {
	
	private Player player;
	private InterfaceState interfaceState;
	
	public InterfaceCloseEvent(Player player, InterfaceState interfaceState) {
		this.player = player;
		this.interfaceState = interfaceState;
	}

	public Player getPlayer() {
		return player;
	}

	public InterfaceState getInterfaceState() {
		return interfaceState;
	}
	
}
