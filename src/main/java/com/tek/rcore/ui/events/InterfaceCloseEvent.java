package com.tek.rcore.ui.events;

import org.bukkit.entity.Player;

import com.tek.rcore.ui.InterfaceState;

/**
 * A class which represents
 * the closing of an interface.
 * 
 * @author RedstoneTek
 */
public class InterfaceCloseEvent {
	
	//The InterfaceCloseEvent values
	private Player player;
	private InterfaceCloseType closeType;
	private InterfaceState interfaceState;
	
	/**
	 * Creates an InterfaceCloseEvent with
	 * the specified values.
	 * 
	 * @param player The player
	 * @param closeType The close type
	 * @param interfaceState The interface state closed
	 */
	public InterfaceCloseEvent(Player player, InterfaceCloseType closeType, InterfaceState interfaceState) {
		this.player = player;
		this.closeType = closeType;
		this.interfaceState = interfaceState;
	}

	/**
	 * Gets the player who closed the interface.
	 * 
	 * @return The player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Gets the close type of the event.
	 * 
	 * @return The close type
	 */
	public InterfaceCloseType getCloseType() {
		return closeType;
	}
	
	/**
	 * Gets the interface state closed.
	 * 
	 * @return The interface state
	 */
	public InterfaceState getInterfaceState() {
		return interfaceState;
	}
	
	/**
	 * An enum representing the
	 * different types of close types.
	 * 
	 * @author RedstoneTek
	 */
	public static enum InterfaceCloseType {
		PLAYER, //The interface was closed by the player
		PROGRAMMATICAL, //The interface was closed programmatically via code
		IGNORED; //The interface was closed because of some weird minecraft behaviour, ignore it
	}
	
}
