package com.tek.rcore;

import org.bukkit.plugin.java.JavaPlugin;

import com.tek.rcore.ui.InterfaceManager;

public class RedstoneCore extends JavaPlugin {
	
	private static RedstoneCore instance;
	private InterfaceManager interfaceManager;
	
	@Override
	public void onEnable() {
		instance = this;
		
		interfaceManager = new InterfaceManager(this);
		interfaceManager.register(10);
	}
	
	@Override
	public void onDisable() {
		interfaceManager.disable();
	}
	
	public static RedstoneCore getInstance() {
		return instance;
	}
	
	public InterfaceManager getInterfaceManager() {
		return interfaceManager;
	}
	
}
