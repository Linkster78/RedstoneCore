package com.tek.rcore.ui;

import java.util.Iterator;
import java.util.UUID;

import org.bukkit.Bukkit;

/**
 * A class which takes care
 * of updating the interfaces
 * of the {@link InterfaceManager}.
 * 
 * @author RedstoneTek
 */
public class InterfaceUpdater implements Runnable {

	//The InterfaceManager instance.
	private InterfaceManager instance;
	
	/**
	 * Creates a runnable which updates
	 * the interfaces running on the interface manager.
	 * 
	 * @param instance The interface manager instance
	 */
	public InterfaceUpdater(InterfaceManager instance) {
		this.instance = instance;
	}
	
	/**
	 * Called every so often to tick and
	 * render the interfaces.
	 */
	@Override
	public void run() {
		Iterator<UUID> uuidIterator = instance.getUserInterfaces().keySet().iterator();
		while(uuidIterator.hasNext()) {
			UUID uuid = uuidIterator.next();
			if(Bukkit.getPlayer(uuid) != null) {
				int index = 0;
				for(InterfaceState interfaceState : instance.getUserInterfaces().get(uuid)) {
					interfaceState.tick();
					if(index == 0) interfaceState.render();
					index++;
				}
			} else {
				uuidIterator.remove();
			}
		}
	}

}
