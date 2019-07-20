package com.tek.rcore.item;

import java.util.Iterator;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.tek.rcore.misc.TextFormatter;

public class InventoryUtils {
	
	private static final int ROW_SIZE = 9;
	
	public static void drawFilledRectangle(Inventory inv, ItemStack item, int x, int y, int width, int height) {
		for(int x1 = 0; x1 < width; x1++) {
			for(int y1 = 0; y1 < height; y1++) {
				int slot = slotFromCoordinates(x + x1, y + y1);
				if(slot >= inv.getSize()) continue;
				inv.setItem(slot, item);
			}
		}
	}
	
	public static void drawFilledRectangle(ItemStack[][] drawBuffer, ItemStack item, int x, int y, int width, int height) {
		for(int x1 = 0; x1 < width; x1++) {
			for(int y1 = 0; y1 < height; y1++) {
				if(x + x1 >= drawBuffer.length || y + y1 >= drawBuffer[0].length) continue;
				drawBuffer[x + x1][y + y1] = item;
			}
		}
	}
	
	public static void drawHollowRectangle(Inventory inv, ItemStack item, int x, int y, int width, int height) {
		for(int x1 = 0; x1 < width; x1++) {
			for(int y1 = 0; y1 < height; y1++) {
				int slot = slotFromCoordinates(x + x1, y + y1);
				if(slot >= inv.getSize()) continue;
				if(x1 == 0 || y1 == 0 || x1 == width - 1 || y1 == height - 1) inv.setItem(slot, item);
			}
		}
	}
	
	public static void drawHollowRectangle(ItemStack[][] drawBuffer, ItemStack item, int x, int y, int width, int height) {
		for(int x1 = 0; x1 < width; x1++) {
			for(int y1 = 0; y1 < height; y1++) {
				if(x + x1 >= drawBuffer.length || y + y1 >= drawBuffer[0].length) continue;
				if(x1 == 0 || y1 == 0 || x1 == width - 1 || y1 == height - 1) drawBuffer[x + x1][y + y1] = item;
			}
		}
	}
	
	public static void drawLineVertical(Inventory inv, ItemStack item, int column) {
		int invheight = getHeight(inv.getSize());
		for(int i = 0; i < invheight; i++) {
			int slot = column + i * ROW_SIZE;
			if(slot >= inv.getSize()) continue;
			inv.setItem(slot, item);
		}
	}
	
	public static void drawLineVertical(ItemStack[][] drawBuffer, ItemStack item, int column) {
		int invheight = drawBuffer[0].length;
		for(int i = 0; i < invheight; i++) {
			if(column >= drawBuffer.length || i >= drawBuffer[0].length) continue;
			drawBuffer[column][i] = item;
		}
	}
	
	public static void drawLineHorizontal(Inventory inv, ItemStack item, int row) {
		for(int i = 0; i <= 8; i++) {
			int base = row * ROW_SIZE;
			int slot = base + i;
			if(slot >= inv.getSize()) continue;
			inv.setItem(slot, item);
		}
	}
	
	public static void drawLineHorizontal(ItemStack[][] drawBuffer, ItemStack item, int row) {
		for(int i = 0; i < ROW_SIZE; i++) {
			if(i >= drawBuffer.length || row >= drawBuffer[0].length) continue;
			drawBuffer[i][row] = item;
		}
	}
	
	public static int slotFromCoordinates(int x, int y) {
		return y * ROW_SIZE + x;
	}
	
	public static int slotToX(int slot) {
		return slot % ROW_SIZE;
	}
	
	public static int slotToY(int slot) {
		return (slot - slot % ROW_SIZE) / ROW_SIZE;
	}
	
	public static int getItemCount(Player p, ItemStack item) {
		int sum = 0;
		for(ItemStack slot : p.getInventory().getStorageContents()) {
			if(slot == null) continue;
			if(slot.isSimilar(item)) {
				sum += slot.getAmount();
			}
		}
		return sum;
	}
	
	public static int getItemFitCount(Player p, ItemStack item) {
		int sum = 0;
		for(ItemStack slot : p.getInventory().getStorageContents()) {
			if(slot == null || slot.getType().equals(Material.AIR)) {
				sum += item.getMaxStackSize();
				continue;
			}
			
			if(slot.isSimilar(item)) sum += item.getMaxStackSize() - slot.getAmount();
		}
		
		return sum;
	}
	
	public static void removeItemCount(Player p, ItemStack item, int amount) {
		int count = amount;
		for(ItemStack slot : p.getInventory().getStorageContents()) {
			if(count <= 0) break;
			if(slot == null) continue;
			
			if(slot.isSimilar(item)) {
				int toRemove = Math.min(slot.getAmount(), Math.min(item.getMaxStackSize(), count));
				slot.setAmount(slot.getAmount() - toRemove);
				count -= toRemove;
			}
		}
	}
	
	public static void addItemCount(Player p, ItemStack item, int amount) {
		int count = amount;
		int slotId = 0;
		for(ItemStack slot : p.getInventory().getStorageContents()) {
			if(count <= 0) break;
			
			if(slot == null || slot.getType().equals(Material.AIR)) {
				int toAdd = Math.min(count, item.getMaxStackSize());
				ItemStack stack = item.clone();
				stack.setAmount(toAdd);
				p.getInventory().setItem(slotId, stack);
				count -= toAdd;
			} else if(slot.isSimilar(item)) {
				int toAdd = Math.min(count, Math.min(item.getMaxStackSize(), item.getMaxStackSize() - slot.getAmount()));
				slot.setAmount(slot.getAmount() + toAdd);
				count -= toAdd;
			}
			slotId++;
		}
	}
	
	public static String getName(ItemStack item) {
		if(item.hasItemMeta()) {
			if(item.getItemMeta().getDisplayName() != null) {
				return item.getItemMeta().getDisplayName();
			} else {
				return getName(item.getType());
			}
		} else {
			return getName(item.getType());
		}
	}
	
	public static String getName(Material material) {
		String mat = material.name().toLowerCase().replace('_', ' ');
		return TextFormatter.capitalize(mat);
	}
	
	public static ItemStack addGlow(ItemStack item) {
		ItemStack hidden = item.clone();
		ItemMeta meta = hidden.getItemMeta();
		meta.addEnchant(Enchantment.DURABILITY, 1, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		hidden.setItemMeta(meta);
		return hidden;
	}
	
	public static ItemStack hideGlow(ItemStack item) {
		ItemStack hidden = item.clone();
		ItemMeta meta = hidden.getItemMeta();
		Iterator<Enchantment> enchantmentIterator = hidden.getEnchantments().keySet().iterator();
		while(enchantmentIterator.hasNext()) {
			Enchantment enchantment = enchantmentIterator.next();
			int level = hidden.getEnchantmentLevel(enchantment);
			enchantmentIterator.remove();
			meta.getLore().add(0, TextFormatter.color("&7" + TextFormatter.capitalize(enchantment.getKey().getKey()) + " " + TextFormatter.enchantmentLevelToRoman(level)));
		}
		hidden.setItemMeta(meta);
		return item;
	}
	
	public static int getHeight(int size) {
		return size / ROW_SIZE;
	}
	
}
