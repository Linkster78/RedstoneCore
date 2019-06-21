package com.tek.rcore.item;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryUtils {
	
	public static void drawFilledRectangle(Inventory inv, ItemStack item, int x, int y, int width, int height) {
		for(int x1 = 0; x1 < width; x1++) {
			for(int y1 = 0; y1 < height; y1++) {
				inv.setItem(slotFromCoordinates(x + x1, y + y1), item);
			}
		}
	}
	
	public static void drawFilledRectangle(ItemStack[][] drawBuffer, ItemStack item, int x, int y, int width, int height) {
		for(int x1 = 0; x1 < width; x1++) {
			for(int y1 = 0; y1 < height; y1++) {
				drawBuffer[x + x1][y + y1] = item;
			}
		}
	}
	
	public static void drawHollowRectangle(Inventory inv, ItemStack item, int x, int y, int width, int height) {
		for(int x1 = 0; x1 < width; x1++) {
			for(int y1 = 0; y1 < height; y1++) {
				if(x1 == 0 || y1 == 0 || x1 == width - 1 || y1 == height - 1) inv.setItem(slotFromCoordinates(x + x1, y + y1), item);
			}
		}
	}
	
	public static void drawHollowRectangle(ItemStack[][] drawBuffer, ItemStack item, int x, int y, int width, int height) {
		for(int x1 = 0; x1 < width; x1++) {
			for(int y1 = 0; y1 < height; y1++) {
				if(x1 == 0 || y1 == 0 || x1 == width - 1 || y1 == height - 1) drawBuffer[x + x1][y + y1] = item;
			}
		}
	}
	
	public static void drawLineVertical(Inventory inv, ItemStack item, int column) {
		int invheight = inv.getSize() / 9 - 1;
		for(int i = 0; i <= invheight; i++) {
			inv.setItem(column + i * 9, item);
		}
	}
	
	public static void drawLineVertical(ItemStack[][] drawBuffer, ItemStack item, int column) {
		int invheight = drawBuffer[0].length;
		for(int i = 0; i <= invheight; i++) {
			drawBuffer[column][i] = item;
		}
	}
	
	public static void drawLineHorizontal(Inventory inv, ItemStack item, int row) {
		for(int i = 0; i <= 8; i++) {
			int base = row * 9;
			inv.setItem(base + i, item);
		}
	}
	
	public static void drawLineHorizontal(ItemStack[][] drawBuffer, ItemStack item, int row) {
		for(int i = 0; i < 9; i++) {
			drawBuffer[i][row] = item;
		}
	}
	
	public static int slotFromCoordinates(int x, int y) {
		return y * 9 + x;
	}
	
	public static int slotToX(int slot) {
		return slot % 9;
	}
	
	public static int slotToY(int slot) {
		return (slot - slot % 9) / 9;
	}
	
}
