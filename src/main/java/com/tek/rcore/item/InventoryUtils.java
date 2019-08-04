package com.tek.rcore.item;

import java.util.Iterator;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.tek.rcore.misc.NumberUtils;
import com.tek.rcore.misc.TextFormatter;

/**
 * A class which contains many different
 * utilities related to items and inventories.
 * 
 * @author RedstoneTek
 */
public class InventoryUtils {
	
	//The size of an inventory row
	private static final int ROW_SIZE = 9;
	
	/**
	 * Fills a rectangular area of an inventory
	 * with the specified itemstack.
	 * 
	 * @param inv The inventory to fill
	 * @param item The itemstack to fill with
	 * @param x The X coordinate from the left (Starts at 0)
	 * @param y The Y coordinate from the top (Starts at 0)
	 * @param width The width of the rectangle
	 * @param height The height of the rectangle
	 */
	public static void drawFilledRectangle(Inventory inv, ItemStack item, int x, int y, int width, int height) {
		for(int x1 = 0; x1 < width; x1++) {
			for(int y1 = 0; y1 < height; y1++) {
				int slot = slotFromCoordinates(x + x1, y + y1);
				if(slot >= inv.getSize()) continue;
				inv.setItem(slot, item);
			}
		}
	}
	
	/**
	 * Fills a rectangular area of a two-dimensional
	 * itemstack array with the specified itemstack.
	 * 
	 * @param drawBuffer A two-dimensional array which houses itemstacks
	 * @param item The itemstack to fill with
	 * @param x The X coordinate from the left (Starts at 0)
	 * @param y The Y coordinate from the top (Starts at 0)
	 * @param width The width of the rectangle
	 * @param height The height of the rectangle
	 */
	public static void drawFilledRectangle(ItemStack[][] drawBuffer, ItemStack item, int x, int y, int width, int height) {
		for(int x1 = 0; x1 < width; x1++) {
			for(int y1 = 0; y1 < height; y1++) {
				if(x + x1 >= drawBuffer.length || y + y1 >= drawBuffer[0].length) continue;
				drawBuffer[x + x1][y + y1] = item;
			}
		}
	}
	
	/**
	 * Draws a hollow rectangle of the specified
	 * width and height in the inventory with
	 * the specified itemstack.
	 * 
	 * @param inv The inventory to draw on
	 * @param item The itemstack to draw with
	 * @param x The X coordinate from the left (Starts at 0)
	 * @param y The Y coordinate from the top(Starts at 0)
	 * @param width The width of the rectangle
	 * @param height The height of the rectangle
	 */
	public static void drawHollowRectangle(Inventory inv, ItemStack item, int x, int y, int width, int height) {
		for(int x1 = 0; x1 < width; x1++) {
			for(int y1 = 0; y1 < height; y1++) {
				int slot = slotFromCoordinates(x + x1, y + y1);
				if(slot >= inv.getSize()) continue;
				if(x1 == 0 || y1 == 0 || x1 == width - 1 || y1 == height - 1) inv.setItem(slot, item);
			}
		}
	}
	
	/**
	 * Draws a hollow rectangle of the specified
	 * width and height in the two-dimensional
	 * array with the specified itemstack.
	 * 
	 * @param drawBuffer A two-dimensional array which houses itemstacks
	 * @param item The itemstack to draw with
	 * @param x The X coordinate from the left (Starts at 0)
	 * @param y The Y coordinate from the top (Starts at 0)
	 * @param width The width of the rectangle
	 * @param height The height of the rectangle
	 */
	public static void drawHollowRectangle(ItemStack[][] drawBuffer, ItemStack item, int x, int y, int width, int height) {
		for(int x1 = 0; x1 < width; x1++) {
			for(int y1 = 0; y1 < height; y1++) {
				if(x + x1 >= drawBuffer.length || y + y1 >= drawBuffer[0].length) continue;
				if(x1 == 0 || y1 == 0 || x1 == width - 1 || y1 == height - 1) drawBuffer[x + x1][y + y1] = item;
			}
		}
	}
	
	/**
	 * Fills a vertical line of an inventory
	 * with the specified itemstack.
	 * 
	 * @param inv The inventory to draw on
	 * @param item The itemstack to draw with
	 * @param column The column from the left (Starts at 0)
	 */
	public static void drawLineVertical(Inventory inv, ItemStack item, int column) {
		int invheight = getHeight(inv.getSize());
		for(int i = 0; i < invheight; i++) {
			int slot = column + i * ROW_SIZE;
			if(slot >= inv.getSize()) continue;
			inv.setItem(slot, item);
		}
	}
	
	/**
	 * Fills a vertical line of a two-dimensional
	 * array with the specified itemstack.
	 * 
	 * @param drawBuffer A two-dimensional array which houses itemstacks
	 * @param item The itemstack to draw with
	 * @param column The column from the left (Starts at 0)
	 */
	public static void drawLineVertical(ItemStack[][] drawBuffer, ItemStack item, int column) {
		int invheight = drawBuffer[0].length;
		for(int i = 0; i < invheight; i++) {
			if(column >= drawBuffer.length || i >= drawBuffer[0].length) continue;
			drawBuffer[column][i] = item;
		}
	}
	
	/**
	 * Fills a horizontal line of an inventory
	 * with the specified itemstack.
	 * 
	 * @param inv The inventory to draw on
	 * @param item The itemstack to draw with
	 * @param row The row from the top (Starts at 0)
	 */
	public static void drawLineHorizontal(Inventory inv, ItemStack item, int row) {
		for(int i = 0; i <= 8; i++) {
			int base = row * ROW_SIZE;
			int slot = base + i;
			if(slot >= inv.getSize()) continue;
			inv.setItem(slot, item);
		}
	}
	
	/**
	 * Fills a horizontal line of a two-dimensional
	 * array with the specified itemstack.
	 * 
	 * @param drawBuffer A two-dimensional array which houses itemstacks
	 * @param item The itemstack to draw with
	 * @param row The row from the top (Starts at 0)
	 */
	public static void drawLineHorizontal(ItemStack[][] drawBuffer, ItemStack item, int row) {
		for(int i = 0; i < ROW_SIZE; i++) {
			if(i >= drawBuffer.length || row >= drawBuffer[0].length) continue;
			drawBuffer[i][row] = item;
		}
	}
	
	/**
	 * Converts a X and Y position into a slot ID.
	 * 
	 * @param x The X position of the slot from the left (Starts at 0)
	 * @param y The Y position of the slot from the top (Starts at 0)
	 * @return The slot ID
	 */
	public static int slotFromCoordinates(int x, int y) {
		return y * ROW_SIZE + x;
	}
	
	/**
	 * Converts a slot ID to an X coordinate.
	 * 
	 * @param slot The slot ID
	 * @return The X coordinate from the left (Starts at 0)
	 */
	public static int slotToX(int slot) {
		return slot % ROW_SIZE;
	}
	
	/**
	 * Converts a slot ID to an Y coordinate.
	 * 
	 * @param slot The slot ID
	 * @return The Y coordinate from the top (Starts at 0)
	 */
	public static int slotToY(int slot) {
		return (slot - slot % ROW_SIZE) / ROW_SIZE;
	}
	
	/**
	 * Scans the inventory of a player and
	 * returns the amount of times that 
	 * the specified itemstack is present.
	 * 
	 * @param p The player who's inventory to scan
	 * @param item The itemstack to check for
	 * @return The amount of the specified item found
	 */
	public static int getItemCount(Player p, ItemStack item) {
		int sum = 0;
		for(ItemStack slot : p.getInventory().getStorageContents()) {
			if(isItemEmpty(slot)) continue;
			if(slot.isSimilar(item)) {
				sum += slot.getAmount();
			}
		}
		return sum;
	}
	
	/**
	 * Scans the inventory of a player and
	 * returns the amount of times that
	 * the specified itemstack can fit.
	 * 
	 * @param p The player who's inventory to scan
	 * @param item The itemstack to try to fit in
	 * @return The amount of times that the specified item can be fit in
	 */
	public static int getItemFitCount(Player p, ItemStack item) {
		int sum = 0;
		for(ItemStack slot : p.getInventory().getStorageContents()) {
			if(isItemEmpty(slot)) {
				sum += item.getMaxStackSize();
				continue;
			}
			
			if(slot.isSimilar(item)) sum += item.getMaxStackSize() - slot.getAmount();
		}
		
		return sum;
	}
	
	/**
	 * Removes the specified item 
	 * the specified amount of times
	 * from the player's inventory.
	 * 
	 * @param p The player who's inventory to deduct from
	 * @param item The itemstack to remove
	 * @param amount The amount of times to remove the item
	 */
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
	
	/**
	 * Adds the specified item 
	 * the specified amount of times
	 * in the player's inventory.
	 * 
	 * @param p The player who's inventory to add into
	 * @param item The itemstack to add
	 * @param amount The amount of times to add the item
	 */
	public static void addItemCount(Player p, ItemStack item, int amount) {
		int count = amount;
		int slotId = 0;
		for(ItemStack slot : p.getInventory().getStorageContents()) {
			if(count <= 0) break;
			
			if(isItemEmpty(slot)) {
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
	
	/**
	 * Returns the displayed name of an itemstack.
	 * 
	 * @param item The item
	 * @return The displayed name of the item
	 */
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
	
	/**
	 * Returns the displayed name of a material.
	 * 
	 * @param material The material
	 * @return The displayed name of the material
	 */
	public static String getName(Material material) {
		String mat = material.name().toLowerCase().replace('_', ' ');
		return TextFormatter.capitalize(mat);
	}
	
	/**
	 * Adds a glowing effect onto an itemstack.
	 * 
	 * @param item The item
	 * @return A version of the item with a glowing effect applied
	 */
	public static ItemStack addGlow(ItemStack item) {
		ItemStack hidden = item.clone();
		ItemMeta meta = hidden.getItemMeta();
		meta.addEnchant(Enchantment.DURABILITY, 1, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		hidden.setItemMeta(meta);
		return hidden;
	}
	
	/**
	 * Hides the glowing effect of an itemstack.
	 * If the item is enchanted, it will replace
	 * the enchants with lore so to remove the effect.
	 * 
	 * @param item The item
	 * @return A version of the item without the glowing effect applied
	 */
	public static ItemStack hideGlow(ItemStack item) {
		ItemStack hidden = item.clone();
		ItemMeta meta = hidden.getItemMeta();
		Iterator<Enchantment> enchantmentIterator = hidden.getEnchantments().keySet().iterator();
		while(enchantmentIterator.hasNext()) {
			Enchantment enchantment = enchantmentIterator.next();
			int level = hidden.getEnchantmentLevel(enchantment);
			enchantmentIterator.remove();
			meta.getLore().add(0, TextFormatter.color("&7" + TextFormatter.capitalize(enchantment.getKey().getKey()) + " " + NumberUtils.enchantmentLevelToRoman(level)));
		}
		hidden.setItemMeta(meta);
		return item;
	}
	
	/**
	 * Checks whether an itemstack is empty.
	 * 
	 * @param item The item to check
	 * @return Whether the itemstack is empty or not
	 */
	public static boolean isItemEmpty(ItemStack item) {
		return item == null || item.getType().equals(Material.AIR) || item.getAmount() <= 0;
	}
	
	/**
	 * Returns the height of an inventory from its size.
	 * 
	 * @param size The size of the inventory
	 * @return The height of the inventory
	 */
	public static int getHeight(int size) {
		return size / ROW_SIZE;
	}
	
	/**
	 * Renames an itemstack.
	 * 
	 * @param item The item
	 * @param name The displayed name
	 * @return The renamed item
	 */
	public static ItemStack renameItem(ItemStack item, String name) {
		ItemStack stack = item.clone();
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(name);
		stack.setItemMeta(meta);
		return stack;
	}
	
	/**
	 * Changes the lore of an itemstack.
	 * 
	 * @param item The item
	 * @param lore The lore
	 * @return The re-lored item
	 */
	public static ItemStack changeLore(ItemStack item, List<String> lore) {
		ItemStack stack = item.clone();
		ItemMeta meta = stack.getItemMeta();
		meta.setLore(lore);
		stack.setItemMeta(meta);
		return stack;
	}
	
}