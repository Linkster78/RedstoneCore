package com.tek.rcore.ui;

import java.util.Iterator;
import java.util.UUID;

import org.bukkit.Bukkit;

public class InterfaceUpdater implements Runnable {

	private InterfaceManager instance;
	
	public InterfaceUpdater(InterfaceManager instance) {
		this.instance = instance;
	}
	
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
