package com.tek.rcore.ui.components;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.tek.rcore.misc.NumberUtils;
import com.tek.rcore.misc.TextFormatter;
import com.tek.rcore.ui.InterfaceComponent;
import com.tek.rcore.ui.InterfaceState;
import com.tek.rcore.ui.WrappedProperty;

/**
 * A component which allows players
 * to select a numerical value with
 * an intuitive numerical slider.
 * 
 * @author RedstoneTek
 */
public class SliderComponent extends InterfaceComponent {

	//The internal slider properties
	private int length;
	private double minimum, maximum;
	private Direction direction;
	private Material reached, notReached;
	private WrappedProperty<Double> value;
	private List<ItemStack> rendered;
	private String pattern;
	
	/**
	 * Creates a slider with the specified values.
	 * 
	 * @param x The X position from the left (Starts at 0)
	 * @param y The Y position from the top (Starts at 0)
	 * @param length The length of the slider
	 * @param direction The direction
	 * @param minimum The minimum value
	 * @param maximum The maximum value
	 * @param reached The item rendered when the value is reached
	 * @param notReached The item rendered when the value is not reached
	 */
	public SliderComponent(int x, int y, int length, Direction direction, double minimum, double maximum, Material reached, Material notReached) {
		this(x, y, length, direction, minimum, maximum, minimum, reached, notReached);
	}
	
	/**
	 * Creates a slider with the specified values.
	 * 
	 * @param x The X position from the left (Starts at 0)
	 * @param y The Y position from the top (Starts at 0)
	 * @param length The length of the slider
	 * @param direction The direction
	 * @param minimum The minimum value
	 * @param maximum The maximum value
	 * @param defaultValue The default value
	 * @param reached The item rendered when the value is reached
	 * @param notReached The item rendered when the value is not reached
	 */
	public SliderComponent(int x, int y, int length, Direction direction, double minimum, double maximum, double defaultValue, Material reached, Material notReached) {
		super(x, y, direction.equals(Direction.HORIZONTAL) ? length : 1, direction.equals(Direction.HORIZONTAL) ? 1 : length);
		this.length = length;
		this.minimum = minimum;
		this.maximum = maximum;
		this.direction = direction;
		this.reached = reached;
		this.notReached = notReached;
		this.value = new WrappedProperty<Double>(defaultValue);
		this.rendered = new ArrayList<ItemStack>();
		this.pattern = "%f";
		for(int i = 0; i < length; i++) {
			rendered.add(new ItemStack(notReached));
		}
	}
	
	/**
	 * Renders the slider onto
	 * the interface draw buffer.
	 */
	@Override
	public void render(InterfaceState interfaceState, ItemStack[][] drawBuffer) {
		double increment = ((double)maximum - (double)minimum) / (double)(length - 1);
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				int index = x * height + y;
				ItemStack item = rendered.get(index);
				double value = NumberUtils.round(increment * (double)index + minimum, (short) 2);
				item.setType(value <= this.value.getValue() ? reached : notReached);
				ItemMeta itemMeta = item.getItemMeta();
				itemMeta.setDisplayName(value <= this.value.getValue() ? TextFormatter.color("&a" + String.format(pattern, value)) : TextFormatter.color("&c" + String.format(pattern, value)));
				item.setItemMeta(itemMeta);
				drawBuffer[this.x + x][this.y + y] = item;
			}
		}
	}
	
	/**
	 * Handles the click event.
	 * Sets the value of the slider.
	 */
	@Override
	public void onClick(InterfaceState interfaceState, ClickType type, ItemStack item, int x, int y) {
		double increment = ((double)maximum - (double)minimum) / (double)(length - 1);
		if(direction.equals(Direction.HORIZONTAL)) {
			double value = NumberUtils.round(increment * (double)x + minimum, (short) 2);
			if(this.value.getValue() != value) this.value.setValue(value);
		} else {
			double value = NumberUtils.round(increment * (double)y + minimum, (short) 2);
			if(this.value.getValue() != value) this.value.setValue(value);
		}
	}
	
	/**
	 * Gets the slider value.
	 * 
	 * @return The value
	 */
	public WrappedProperty<Double> getValue() {
		return value;
	}
	
	/**
	 * Gets the number pattern.
	 * 
	 * @return The pattern
	 */
	public String getPattern() {
		return pattern;
	}
	
	/**
	 * Sets the pattern to use when displaying the numbers.
	 * 
	 * @param pattern The pattern
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	
	/**
	 * An enum representing an axis.
	 * 
	 * @author RedstoneTek
	 */
	public enum Direction {
		HORIZONTAL,
		VERTICAL;
	}

}
