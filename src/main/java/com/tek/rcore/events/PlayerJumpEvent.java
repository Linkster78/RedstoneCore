package com.tek.rcore.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * A class which facilitates the
 * detection of player jump events.
 * 
 * @author RedstoneTek
 */
public class PlayerJumpEvent extends Event {

	private static final HandlerList HANDLERS = new HandlerList();
	
	//The player who jumped
	private Player player;
	
	/**
	 * Creates a PlayerJumpEvent
	 * object with the player.
	 * 
	 * @param player The player who jumped
	 */
	public PlayerJumpEvent(Player player) {
		this.player = player;
	}
	
	/**
	 * Gets the player who jumped.
	 * 
	 * @return The player
	 */
	public Player getPlayer() {
		return player;
	}
	
	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}

}
