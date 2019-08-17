package com.tek.rcore.misc;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.tek.rcore.nms.ReflectionUtils;

/**
 * A class which provides a simple
 * way to take text input from players.
 * 
 * @author RedstoneTek
 */
public class SignMenu {
	
	//The list of active menus, used in the packet listener.
	public static final List<SignMenu> activeMenus;
	
	//Internal sign menu values
	private Player player;
	private Location signLocation;
	private String[] lines;
	private BiConsumer<Player, String[]> linesCallback;
	
	/**
	 * Creates a sign menu for the specified player
	 * with the default lines specified.
	 * 
	 * @param player The player
	 * @param defaultLines The lines to show on the sign by default
	 */
	public SignMenu(Player player) {
		this.player = player;
		this.lines = new String[] {"", "", "", ""};
	}
	
	/**
	 * Opens/Displays the sign menu to the user.
	 */
	public void open() {
		signLocation = ReflectionUtils.openSignEditor(player, lines);
		activeMenus.add(this);
	}
	
	/**
	 * Sets the default lines.
	 * <p>NOTE: The array SHOULD have a length of 4 and empty strings should be depicted as non null.</p>
	 * 
	 * @param lines The text lines
	 * @return The SignMenu instance
	 */
	public SignMenu setLines(String[] lines) {
		this.lines = lines;
		return this;
	}
	
	/**
	 * Sets the response callback.
	 * 
	 * @param linesCallback The callback
	 * @return The SignMenu instance
	 */
	public SignMenu setResponseCallback(BiConsumer<Player, String[]> linesCallback) {
		this.linesCallback = linesCallback;
		return this;
	}
	
	/**
	 * Finishes up, sends a block change and calls the callback.
	 */
	public void finish(String[] lines) {
		player.sendBlockChange(signLocation, signLocation.getBlock().getBlockData());
		if(linesCallback != null) linesCallback.accept(player, lines);
	}
	
	/**
	 * Gets the player.
	 * 
	 * @return The player
	 */
	public Player getPlayer() {
		return player;
	}
	
	/**
	 * Gets the text lines.
	 * 
	 * @return The text lines
	 */
	public String[] getLines() {
		return lines;
	}
	
	//Initializes the static activeMenus list.
	static {
		activeMenus = new ArrayList<SignMenu>();
	}
	
}
