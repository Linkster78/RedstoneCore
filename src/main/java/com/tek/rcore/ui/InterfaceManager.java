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

public class InterfaceManager {
	
	private Plugin instance;
	private Map<UUID, List<InterfaceState>> userInterfaces;
	
	public InterfaceManager(Plugin instance) {
		this.instance = instance;
		userInterfaces = new HashMap<UUID, List<InterfaceState>>();
	}
	
	public void register(double hz) {
		instance.getServer().getPluginManager().registerEvents(new InterfaceInteractionListener(this), instance);
		int tickInterval = (int)Math.ceil(20d / hz);
		instance.getServer().getScheduler().scheduleSyncRepeatingTask(instance, new InterfaceUpdater(this), 0, tickInterval);
	}
	
	public void openInterface(Player player, InterfaceState interfaceState) {
		if(userInterfaces.containsKey(player.getUniqueId())) {
			userInterfaces.get(player.getUniqueId()).add(0, interfaceState);
		} else {
			List<InterfaceState> stateLayers = new ArrayList<InterfaceState>();
			stateLayers.add(interfaceState);
			userInterfaces.put(player.getUniqueId(), stateLayers);
		}
		
		interfaceState.setUUID(player.getUniqueId());
		interfaceState.open(player);
	}
	
	public void switchInterface(Player player, InterfaceState interfaceState) {
		if(!userInterfaces.containsKey(player.getUniqueId())) return;
		userInterfaces.get(player.getUniqueId()).get(0).setCloseType(InterfaceCloseType.PROGRAMMATICAL);
		player.closeInventory();
		openInterface(player, interfaceState);
	}
	
	public void closeLayer(Player player) {
		if(!userInterfaces.containsKey(player.getUniqueId())) return;
		userInterfaces.get(player.getUniqueId()).get(0).setCloseType(InterfaceCloseType.PROGRAMMATICAL);
		player.closeInventory();
	}
	
	public void close(Player player) {
		if(!userInterfaces.containsKey(player.getUniqueId())) return;
		List<InterfaceState> states = userInterfaces.get(player.getUniqueId());
		userInterfaces.remove(player.getUniqueId());
		player.closeInventory();
		for(int i = states.size() - 1; i >= 0; i--) {
			InterfaceState state = states.get(i);
			state.getClosedProperty().setValue(new InterfaceCloseEvent(player, InterfaceCloseType.PROGRAMMATICAL, state));
		}
	}
	
	public void disable() {
		for(Player player : Bukkit.getOnlinePlayers()) {
			close(player);
		}
	}
	
	public Optional<List<InterfaceState>> getInterfaceStates(UUID uuid) {
		return userInterfaces.containsKey(uuid) ? Optional.of(userInterfaces.get(uuid)) : Optional.empty();
	}
	
	public Map<UUID, List<InterfaceState>> getUserInterfaces() {
		return userInterfaces;
	}
	
}
