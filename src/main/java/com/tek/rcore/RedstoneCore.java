package com.tek.rcore;

import org.bukkit.plugin.java.JavaPlugin;

import com.tek.rcore.nms.CoreProtocolImpl;
import com.tek.rcore.ui.InterfaceManager;

public class RedstoneCore extends JavaPlugin {
	
	//Static instance for internal plugin use
	private static RedstoneCore instance;
	//InterfaceManager instance for other plugins to use
	private InterfaceManager interfaceManager;
	//ProtocolImplementation instance for internal use
	private CoreProtocolImpl protocolImplementation;
	
	/**
	 * Called when the plugin enables,
	 * initializes the interface manager.
	 */
	@Override
	public void onEnable() {
		instance = this;
		
		interfaceManager = new InterfaceManager(this);
		interfaceManager.register(10);
		
		protocolImplementation = new CoreProtocolImpl(this);
	}
	
	/**
	 * Called when the plugin disables,
	 * disables the interface manager.
	 */
	@Override
	public void onDisable() {
		interfaceManager.disable();
		protocolImplementation.close();
	}
	
	/**
	 * Returns the single RedstoneCore instance.
	 * 
	 * @return The RedstoneCore instance
	 */
	public static RedstoneCore getInstance() {
		return instance;
	}
	
	/**
	 * Returns the interface manager.
	 * 
	 * @return The interface manager.
	 */
	public InterfaceManager getInterfaceManager() {
		return interfaceManager;
	}
	
	/**
	 * Returns the protocol implementation.
	 * 
	 * @return The protocol implementation
	 */
	public CoreProtocolImpl getProtocolImplementation() {
		return protocolImplementation;
	}
	
}
