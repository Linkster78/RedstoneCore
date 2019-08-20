package com.tek.rcore.ui.components;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import com.tek.rcore.ui.InterfaceComponent;
import com.tek.rcore.ui.InterfaceState;
import com.tek.rcore.ui.WrappedProperty;

/**
 * A component which holds other
 * items and paginates them.
 * 
 * @author RedstoneTek
 */
public class PaginatedComponent extends InterfaceComponent {

	//Paginated component internal values
	private WrappedProperty<Integer> pageIndex;
	private List<IPaginatedItem> items;
	
	/**
	 * Creates a paginated component at the specified
	 * X position and Y position with width and height.
	 * 
	 * @param x The X position from the left (Starts at 0)
	 * @param y The Y position from the top (Starts at 0)
	 * @param width The width
	 * @param height The height
	 */
	public PaginatedComponent(int x, int y, int width, int height) {
		super(x, y, width, height);
		this.pageIndex = new WrappedProperty<Integer>(0);
		this.items = new ArrayList<IPaginatedItem>();
	}
	
	/**
	 * Clears the inner components.
	 */
	public void clearItems() {
		pageIndex.setValue(0);
		items.clear();
	}
	
	/**
	 * Removes an item from the pages.
	 * 
	 * @param item The item
	 */
	public void removeItem(IPaginatedItem item) {
		items.remove(item);
	}
	
	/**
	 * Adds an item to the pages.
	 * 
	 * @param item The item
	 */
	public void addItem(IPaginatedItem item) {
		items.add(item);
	}
	
	/**
	 * Updates the page index and ticks all inner components.
	 */
	@Override
	public void tick(InterfaceState interfaceState) {
		int pageSize = width * height;
		int pageCount = Math.max(1, (int) Math.ceil((double) items.size() / (double) pageSize));
		if(pageIndex.getValue() < 0) pageIndex.setValue(0);
		if(pageIndex.getValue() >= pageCount) pageIndex.setValue(pageCount - 1);
		
		for(IPaginatedItem item : items) {
			item.tick(interfaceState);
		}
	}
	
	/**
	 * Renders the items and updates positions.
	 */
	@Override
	public void render(InterfaceState interfaceState, ItemStack[][] drawBuffer) {
		int pageSize = width * height;
		int startIndex = pageIndex.getValue() * pageSize;
		
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				int index = y * width + x;
				if(index >= items.size()) break;
				IPaginatedItem item = items.get(startIndex + index);
				drawBuffer[this.x + x][this.y + y] = item.render(interfaceState);
			}
		}
	}
	
	/**
	 * Transmits the click event to the items.
	 */
	@Override
	public void onClick(InterfaceState interfaceState, ClickType type, ItemStack cursor, int x, int y) {
		int pageSize = width * height;
		int startIndex = pageIndex.getValue() * pageSize;
		int index = y * width + x;
		if(startIndex + index < items.size()) {
			IPaginatedItem item = items.get(index + startIndex);
			item.click(interfaceState, type, cursor);
		}
	}
	
	/**
	 * Sets the current page index.
	 * 
	 * @param page Page index
	 */
	public void setPage(int page) {
		pageIndex.setValue(page);
	}
	
	/**
	 * Goes to the next page.
	 */
	public void nextPage() {
		pageIndex.setValue(pageIndex.getValue() + 1);
	}
	
	/**
	 * Goes back a page.
	 */
	public void previousPage() {
		pageIndex.setValue(pageIndex.getValue() - 1);
	}
	
	/**
	 * Gets the current page index.
	 * 
	 * @return The page index
	 */
	public WrappedProperty<Integer> getPageIndex() {
		return pageIndex;
	}
	
	/**
	 * Gets the item list.
	 * 
	 * @return The item list
	 */
	public List<IPaginatedItem> getItems() {
		return items;
	}

}
