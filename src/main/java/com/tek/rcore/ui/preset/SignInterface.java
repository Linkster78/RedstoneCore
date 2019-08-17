package com.tek.rcore.ui.preset;

import java.util.List;

import org.bukkit.Bukkit;

import com.tek.rcore.RedstoneCore;
import com.tek.rcore.misc.SignMenu;
import com.tek.rcore.ui.InterfaceComponent;
import com.tek.rcore.ui.InterfaceManager;
import com.tek.rcore.ui.InterfaceState;
import com.tek.rcore.ui.events.InterfaceCloseEvent;
import com.tek.rcore.ui.events.InterfaceCloseEvent.InterfaceCloseType;

/**
 * An interface preset that represents
 * a sign, to get textual input from
 * a user.
 * 
 * @author RedstoneTek
 */
public class SignInterface extends InterfaceState {

	//The initial textual contents.
	private String[] initialContents;
	private SignMenu menu;
	private String[] lines;
	
	/**
	 * Creates a SignInterface with
	 * the specified initial contents.
	 * 
	 * @param initialContents The initial contents
	 */
	public SignInterface(String[] initialContents) {
		this.initialContents = initialContents;
	}

	/**
	 * Initializes the interface state.
	 */
	@Override
	public void initialize(List<InterfaceComponent> components) { 
		menu = new SignMenu(getOwner())
				.setLines(initialContents)
				.setResponseCallback((player, lines) -> {
					this.lines = lines;
					
					getClosedProperty().setValue(new InterfaceCloseEvent(getOwner(), InterfaceCloseType.PLAYER, this));		
					InterfaceManager instance = RedstoneCore.getInstance().getInterfaceManager();
					
					if(!instance.getUserInterfaces().containsKey(getUUID())) return;
					instance.getUserInterfaces().get(getUUID()).remove(0);
					if(instance.getUserInterfaces().get(getUUID()).isEmpty()) {
						instance.getUserInterfaces().remove(getUUID());
					} else {
						InterfaceState newState = instance.getUserInterfaces().get(getUUID()).get(0);
						newState.setCloseType(InterfaceCloseType.IGNORED);
						Bukkit.getScheduler().scheduleSyncDelayedTask(RedstoneCore.getInstance(), () -> {
							newState.show();
						}, 0l);
					}
				});
	}
	
	/**
	 * Show the sign editor instead of the regular inventory interface.
	 */
	@Override
	public void show() {
		menu.open();
	}
	
	/**
	 * Ticks the interface, overriden in this case to do nothing.
	 */
	@Override
	public void tick() { }
	
	/**
	 * Renders the interface, overriden in this case to do nothing.
	 */
	@Override
	public void render() { }
	
	/**
	 * Closes the interface, overriden in this case to do nothing.
	 */
	@Override
	public void close() { }
	
	/**
	 * Gets the lines inputted by the user.
	 * 
	 * @return The lines
	 */
	public String[] getLines() {
		return lines;
	}

}
