package com.tek.rcore.ui;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class InterfaceComponent {
	
	protected boolean editable;
	protected int x, y, width, height;
	
	public InterfaceComponent(int x, int y, int width, int height) {
		this.editable = true;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public boolean collides(int x, int y) {
		return x >= this.x && y >= this.y
				&& x < this.x + this.width && y < this.y + this.height;
	}
	
	public void render(InterfaceState interfaceState, ItemStack[][] drawBuffer) { }
	public void tick(InterfaceState interfaceState) { }
	public void onClick(InterfaceState interfaceState, ClickType type, ItemStack item, int x, int y) { }
	public void onItemsChange(InterfaceState interfaceState, ItemStack[][] change) { }
	
	public ComponentCapabilities getCapabilities() {
		return ComponentCapabilities.DEFAULT;
	}
	
	public boolean isEditable() {
		return editable;
	}
	
	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public static class ComponentCapabilities {
		
		public static final ComponentCapabilities DEFAULT = new ComponentCapabilities(false, false);
		
		private boolean canTake;
		private boolean canAdd;
		
		public ComponentCapabilities(boolean canTake, boolean canAdd) {
			this.canTake = canTake;
			this.canAdd = canAdd;
		}
		
		public boolean canTake() {
			return canTake;
		}
		
		public boolean canAdd() {
			return canAdd;
		}
		
		public void setCanTake(boolean canTake) {
			this.canTake = canTake;
		}
		
		public void setCanAdd(boolean canAdd) {
			this.canAdd = canAdd;
		}
		
	}
	
}
