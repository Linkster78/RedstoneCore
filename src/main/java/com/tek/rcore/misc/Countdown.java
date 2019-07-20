package com.tek.rcore.misc;

import java.util.function.Consumer;

import org.bukkit.scheduler.BukkitRunnable;

import com.tek.rcore.RedstoneCore;

public class Countdown {
	
	private int time;
	private boolean running;
	private Consumer<Integer> tickCallback;
	private Runnable upCallback;
	
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
	
	public void cancel() {
		time = 0;
		running = false;
	}
	
	public void countDown() {
		time--;
	}
	
	public int getTime() {
		return time;
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public void setTickCallback(Consumer<Integer> tickCallback) {
		this.tickCallback = tickCallback;
	}
	
	public void setUpCallback(Runnable upCallback) {
		this.upCallback = upCallback;
	}
	
}
