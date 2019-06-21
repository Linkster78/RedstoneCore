package com.tek.rcore.ui.components;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.tek.rcore.misc.TextFormatter;
import com.tek.rcore.ui.InterfaceComponent;
import com.tek.rcore.ui.InterfaceState;
import com.tek.rcore.ui.WrappedProperty;

public class SliderComponent extends InterfaceComponent {

	private int length, minimum, maximum;
	private Direction direction;
	private Material reached, notReached;
	private WrappedProperty<Integer> value;
	private List<ItemStack> rendered;
	
	public SliderComponent(int x, int y, int length, Direction direction, int minimum, int maximum, Material reached, Material notReached) {
		this(x, y, length, direction, minimum, maximum, minimum, reached, notReached);
	}
	
	public SliderComponent(int x, int y, int length, Direction direction, int minimum, int maximum, int defaultValue, Material reached, Material notReached) {
		super(x, y, direction.equals(Direction.HORIZONTAL) ? length : 1, direction.equals(Direction.HORIZONTAL) ? 1 : length);
		this.length = length;
		this.minimum = minimum;
		this.maximum = maximum;
		this.direction = direction;
		this.reached = reached;
		this.notReached = notReached;
		this.value = new WrappedProperty<Integer>(defaultValue);
		this.rendered = new ArrayList<ItemStack>();
		for(int i = 0; i < length; i++) {
			rendered.add(new ItemStack(notReached));
		}
	}
	
	@Override
	public void render(InterfaceState interfaceState, ItemStack[][] drawBuffer) {
		double increment = ((double)maximum - (double)minimum) / (double)(length - 1);
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				int index = x * height + y;
				ItemStack item = rendered.get(index);
				int value = (int)(increment * (double)index) + minimum;
				item.setType(value <= this.value.getValue() ? reached : notReached);
				ItemMeta itemMeta = item.getItemMeta();
				itemMeta.setDisplayName(value <= this.value.getValue() ? TextFormatter.color("&a" + value) : TextFormatter.color("&c" + value));
				item.setItemMeta(itemMeta);
				drawBuffer[this.x + x][this.y + y] = item;
			}
		}
	}
	
	@Override
	public void onClick(InterfaceState interfaceState, ClickType type, ItemStack item, int x, int y) {
		double increment = ((double)maximum - (double)minimum) / (double)(length - 1);
		if(direction.equals(Direction.HORIZONTAL)) {
			int value = (int)(increment * (double)x) + minimum;
			if(this.value.getValue() != value) this.value.setValue(value);
		} else {
			int value = (int)(increment * (double)y) + minimum;
			if(this.value.getValue() != value) this.value.setValue(value);
		}
	}
	
	public WrappedProperty<Integer> getValue() {
		return value;
	}
	
	public enum Direction {
		HORIZONTAL,
		VERTICAL;
	}

}
