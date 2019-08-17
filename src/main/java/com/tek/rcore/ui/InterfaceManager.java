package com.tek.rcore.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.tek.rcore.ui.events.InterfaceCloseEvent;
import com.tek.rcore.ui.events.InterfaceCloseEvent.InterfaceCloseType;

/**
 * A class which manages all the
 * open player interfaces and
 * provides functions to control these.
 * 
 * @author RedstoneTek
 */
public class InterfaceManager {
	
	//The plugin instance, used to register events and tasks.
	private Plugin instance;
	//The Map containing all the open interfaces.
	private Map<UUID, List<InterfaceState>> userInterfaces;
	//The Refresh Rate (Hz) of the interfaces/
	private double refreshRate;
	
	/**
	 * Creates an InterfaceManager instance
	 * with the provided plugin instance.
	 * 
	 * @param instance The plugin instance
	 */
	public InterfaceManager(Plugin instance) {
		this.instance = instance;
		userInterfaces = new HashMap<UUID, List<InterfaceState>>();
	}
	
	/**
	 * Registers the GUI events as well
	 * as the updating/rendering task.
	 * 
	 * @param hz The tick/render frequency per seconds
	 */
	public void register(double refreshRate) {
		instance.getServer().getPluginManager().registerEvents(new InterfaceInteractionListener(this), instance);
		int tickInterval = (int)Math.ceil(20d / refreshRate);
		instance.getServer().getScheduler().scheduleSyncRepeatingTask(instance, new InterfaceUpdater(this), 0, tickInterval);
	}
	
	/**
	 * Opens an interface over the player's
	 * current one (if any).
	 * 
	 * @param player The player
	 * @param interfaceState The interface
	 */
	public void openInterface(Player player, InterfaceState interfaceState) {
		if(userInterfaces.containsKey(player.getUniqueId())) {
			userInterfaces.get(player.getUniqueId()).add(0, interfaceState);
		} else {
			List<InterfaceState> stateLayers = new ArrayList<InterfaceState>();
			stateLayers.add(interfaceState);
			userInterfaces.put(player.getUniqueId(), stateLayers);
		}
		
		interfaceState.setUUID(player.getUniqueId());
		interfaceState.open();
	}
	
	/**
	 * Switches the current interface for
	 * the provided one to the player.
	 * 
	 * @param player The player
	 * @param interfaceState The interface
	 */
	public void switchInterface(Player player, InterfaceState interfaceState) {
		if(!userInterfaces.containsKey(player.getUniqueId())) return;
		userInterfaces.get(player.getUniqueId()).get(0).setCloseType(InterfaceCloseType.PROGRAMMATICAL);
		userInterfaces.get(player.getUniqueId()).get(0).close();
		openInterface(player, interfaceState);
	}
	
	/**
	 * Closes the current interface
	 * layer of the player.
	 * 
	 * @param player The player
	 */
	public void closeLayer(Player player) {
		if(!userInterfaces.containsKey(player.getUniqueId())) return;
		userInterfaces.get(player.getUniqueId()).get(0).setCloseType(InterfaceCloseType.PROGRAMMATICAL);
		userInterfaces.get(player.getUniqueId()).get(0).close();
	}
	
	/**
	 * Closes every layer of the GUI
	 * of the specified player.
	 * 
	 * @param player The player
	 */
	public void close(Player player) {
		if(!userInterfaces.containsKey(player.getUniqueId())) return;
		List<InterfaceState> states = userInterfaces.get(player.getUniqueId());
		userInterfaces.remove(player.getUniqueId());
		userInterfaces.get(player.getUniqueId()).get(0).close();
		for(int i = states.size() - 1; i >= 0; i--) {
			InterfaceState state = states.get(i);
			state.getClosedProperty().setValue(new InterfaceCloseEvent(player, InterfaceCloseType.PROGRAMMATICAL, state));
		}
	}
	
	/**
	 * Disables the interface manager,
	 * closes the interfaces of the players.
	 */
	public void disable() {
		for(Player player : Bukkit.getOnlinePlayers()) {
			close(player);
		}
	}
	
	/**
	 * Returns the current interface state
	 * stack of the player if present.
	 * 
	 * @param uuid The player UUID
	 * @return The interface state stack
	 */
	public Optional<List<InterfaceState>> getInterfaceStates(UUID uuid) {
		return userInterfaces.containsKey(uuid) ? Optional.of(userInterfaces.get(uuid)) : Optional.empty();
	}
	
	/**
	 * Returns the map containing all
	 * of the player interfaces.
	 * 
	 * @return The player interfaces map
	 */
	public Map<UUID, List<InterfaceState>> getUserInterfaces() {
		return userInterfaces;
	}
	
	/**
	 * Returns the interval at which interfaces are ticked and rendered.
	 * 
	 * @return The tick interval
	 */
	public int getTickInterval() {
		return (int) Math.ceil(20d / refreshRate);
	}
	
	/**
	 * Returns the refresh rate of the interfaces.
	 * 
	 * @return The refresh rate
	 */
	public double getRefreshRate() {
		return refreshRate;
	}
	
}
