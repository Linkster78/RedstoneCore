package com.tek.rcore.ui.components;

import java.util.List;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import com.tek.rcore.ui.InterfaceComponent;
import com.tek.rcore.ui.InterfaceState;
import com.tek.rcore.ui.WrappedProperty;

/**
 * A component which holds other
 * components and paginates them.
 * 
 * @author RedstoneTek
 */
public class PaginatedComponent extends InterfaceComponent {

	//Paginated component internal values
	private WrappedProperty<Integer> pageIndex;
	private List<InterfaceComponent> components;
	
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
	}
	
	/**
	 * Clears the inner components.
	 */
	public void clearComponents() {
		pageIndex.setValue(0);
		components.clear();
	}
	
	/**
	 * Removes a component from the pages.
	 * 
	 * @param component The component
	 */
	public void removeComponent(InterfaceComponent component) {
		components.remove(component);
	}
	
	/**
	 * Adds a component to the pages.
	 * 
	 * @param component The component
	 */
	public void addComponent(InterfaceComponent component) {
		if(component.getWidth() != 1 || component.getHeight() != 1) throw new IllegalArgumentException("Components must have a width and height of 1.");
		components.add(component);
	}
	
	/**
	 * Updates the page index and ticks all inner components.
	 */
	@Override
	public void tick(InterfaceState interfaceState) {
		int pageSize = width * height;
		int pageCount = (int) Math.ceil((double) components.size() / (double) pageSize);
		if(pageIndex.getValue() < 0) pageIndex.setValue(0);
		if(pageIndex.getValue() >= pageCount) pageIndex.setValue(pageCount - 1);
		
		for(InterfaceComponent component : components) {
			component.tick(interfaceState);
		}
	}
	
	/**
	 * Renders the inner components and updates positions.
	 */
	@Override
	public void render(InterfaceState interfaceState, ItemStack[][] drawBuffer) {
		int pageSize = width * height;
		int startIndex = pageIndex.getValue() * pageSize;
		
		for(InterfaceComponent component : components) {
			component.setX(-1);
			component.setY(-1);
		}
		
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				int index = y * height + x;
				InterfaceComponent component = components.get(startIndex + index);
				component.setX(this.x + x);
				component.setY(this.y + y);
				component.render(interfaceState, drawBuffer);
			}
		}
	}
	
	/**
	 * Transmits the click event to the inner components.
	 */
	@Override
	public void onClick(InterfaceState interfaceState, ClickType type, ItemStack item, int x, int y) {
		for(InterfaceComponent component : components) {
			if(component.collides(x, y)) {
				if(component.isEditable()) {
					component.onClick(interfaceState, type, item, x - component.getX(), y - component.getY());
				}
			}
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
	 * Gets the component list.
	 * 
	 * @return The component list
	 */
	public List<InterfaceComponent> getItems() {
		return components;
	}

}
