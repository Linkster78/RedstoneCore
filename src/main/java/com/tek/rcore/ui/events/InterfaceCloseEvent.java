package com.tek.rcore.ui.events;

import org.bukkit.entity.Player;

import com.tek.rcore.ui.InterfaceState;

public class InterfaceCloseEvent {
	
	private Player player;
	private InterfaceCloseType closeType;
	private InterfaceState interfaceState;
	
	public InterfaceCloseEvent(Player player, InterfaceCloseType closeType, InterfaceState interfaceState) {
		this.player = player;
		this.closeType = closeType;
		this.interfaceState = interfaceState;
	}

	public Player getPlayer() {
		return player;
	}

	public InterfaceCloseType getCloseType() {
		return closeType;
	}
	
	public InterfaceState getInterfaceState() {
		return interfaceState;
	}
	
	public static enum InterfaceCloseType {
		PLAYER,
		PROGRAMMATICAL,
		IGNORED;
	}
	
}
