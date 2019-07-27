package com.tek.rcore.misc;

import java.util.function.Consumer;

import org.bukkit.scheduler.BukkitRunnable;

import com.tek.rcore.RedstoneCore;

/**
 * A class which allows for
 * simple asynchronous countdowns.
 * 
 * @author RedstoneTek
 */
public class Countdown {
	
	//Countdown variables
	private int time;
	private boolean running;
	private Consumer<Integer> tickCallback;
	private Runnable upCallback;
	
	/**
	 * Schedules a Countdown, starting
	 * at the specified time in seconds.
	 * 
	 * @param time The time in seconds
	 */
	public void schedule(int time) {
		this.time = time + 1;
		this.running = true;
		new BukkitRunnable() {
			public void run() {
				if(getTime() <= 0) {
					this.cancel();
					return;
				}
				
				countDown();
				if(getTime() == 0) {
					if(upCallback != null) upCallback.run();
					running = false;
					this.cancel();
				} else {
					if(tickCallback != null) tickCallback.accept(getTime());
				}
			}
		}.runTaskTimer(RedstoneCore.getInstance(), 0, 20);
	}
	
	/**
	 * Cancels the currently running Countdown.
	 */
	public void cancel() {
		time = 0;
		running = false;
	}
	
	/**
	 * Counts down a second, used internally.
	 */
	public void countDown() {
		time--;
	}
	
	/**
	 * Returns the current time on the Countdown.
	 * 
	 * @return The Countdown time.
	 */
	public int getTime() {
		return time;
	}
	
	/**
	 * Checks if the Countdown is running.
	 * 
	 * @return Whether the Countdown is running or not
	 */
	public boolean isRunning() {
		return running;
	}
	
	/**
	 * Sets the callback called when the time ticks down a second.
	 * 
	 * @param tickCallback The consumer to call when ticking down a second
	 */
	public void setTickCallback(Consumer<Integer> tickCallback) {
		this.tickCallback = tickCallback;
	}
	
	/**
	 * Sets the callback called once the Countdown has finished.
	 * 
	 * @param upCallback The consumer to call once the Countdown has finished
	 */
	public void setUpCallback(Runnable upCallback) {
		this.upCallback = upCallback;
	}
	
}
