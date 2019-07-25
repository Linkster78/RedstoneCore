package com.tek.rcore.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;

import com.tek.rcore.events.PlayerJumpEvent;

public class PlayerStatisticIncrementListener implements Listener {
	
	/**
	 * Listens for the {@link PlayerStatisticIncrementEvent}
	 * and if the incremented statistic is jump, calls the
	 * custom {@link PlayerJumpEvent} event.
	 * @see PlayerStatisticIncrementEvent
	 * @see PlayerJumpEvent
	 * 
	 * @param event The event to listen to
	 */
	@EventHandler
	public void onPlayerStatisticIncrement(PlayerStatisticIncrementEvent event) {
		if(event.getStatistic().equals(Statistic.JUMP)) {
			PlayerJumpEvent jumpEvent = new PlayerJumpEvent(event.getPlayer());
			Bukkit.getPluginManager().callEvent(jumpEvent);
		}
	}
	
}
