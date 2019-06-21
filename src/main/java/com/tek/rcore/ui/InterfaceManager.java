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
			ArrayList<InterfaceState> stateLayers = new ArrayList<InterfaceState>();
			stateLayers.add(interfaceState);
			userInterfaces.put(player.getUniqueId(), stateLayers);
		}
		interfaceState.setUUID(player.getUniqueId());
		interfaceState.open(player);
	}
	
	public void switchInterface(Player player, InterfaceState interfaceState) {
		player.closeInventory();
		Bukkit.getScheduler().scheduleSyncDelayedTask(instance, () -> {
			openInterface(player, interfaceState);
		}, 1);
	}
	
	public void removeInterfaceLayer(Player player) {
		userInterfaces.get(player.getUniqueId()).remove(0);
		if(userInterfaces.get(player.getUniqueId()).isEmpty()) {
			userInterfaces.remove(player.getUniqueId());
		} else {
			Bukkit.getScheduler().scheduleSyncDelayedTask(instance, () -> {
				userInterfaces.get(player.getUniqueId()).get(0).show(player);
			}, 1);
		}
	}
	
	public void closeInterface(Player player) {
		List<InterfaceState> interfaces = userInterfaces.get(player.getUniqueId());
		for(InterfaceState interfaceState : interfaces) {
			interfaceState.onClose();
			interfaceState.getClosedProperty().setValue(new InterfaceCloseEvent(player, interfaceState));
		}
		userInterfaces.remove(player.getUniqueId());
		player.closeInventory();
	}
	
	public Optional<List<InterfaceState>> getInterfaceStates(UUID uuid) {
		return userInterfaces.containsKey(uuid) ? Optional.of(userInterfaces.get(uuid)) : Optional.empty();
	}
	
	public Map<UUID, List<InterfaceState>> getUserInterfaces() {
		return userInterfaces;
	}
	
}
