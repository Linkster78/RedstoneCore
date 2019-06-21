package com.tek.rcore.ui.components;

import java.util.Optional;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.tek.rcore.item.SkullFactory;
import com.tek.rcore.item.SkullFactory.NumberSet;
import com.tek.rcore.item.SkullFactory.NumberSkulls;
import com.tek.rcore.ui.InterfaceComponent;
import com.tek.rcore.ui.InterfaceState;
import com.tek.rcore.ui.WrappedProperty;
import com.tek.rcore.ui.components.SliderComponent.Direction;

public class NumberDisplayComponent extends InterfaceComponent {

	private Direction direction;
	private ItemStack[] digits;
	private int rendered;
	private int renderedTicker;
	private DrawOrder order;
	private WrappedProperty<Integer> value;
	
	public NumberDisplayComponent(int x, int y, ItemStack[] digits, int length, Direction direction, DrawOrder order) {
		this(x, y, digits, 0, length, direction, order);
	}
	
	public NumberDisplayComponent(int x, int y, ItemStack[] digits, int defaultValue, int length, Direction direction, DrawOrder order) {
		super(x, y, direction.equals(Direction.HORIZONTAL) ? length : 1, direction.equals(Direction.HORIZONTAL) ? 1 : length);
		this.direction = direction;
		this.value = new WrappedProperty<Integer>(defaultValue);
		this.value.addWatcher(i -> {
			if(i < 0) this.value.setValue(0);
		});
		this.rendered = 0;
		this.digits = digits;
		this.order = order;
		
		for(int i = 0; i < digits.length; i++) {
			ItemMeta meta = digits[i].getItemMeta();
			meta.setDisplayName(i + "");
			digits[i].setItemMeta(meta);
		}
	}
	
	public NumberDisplayComponent(int x, int y, NumberSet set, int length, Direction direction, DrawOrder order) {
		this(x, y, set, 0, length, direction, order);
	}
	
	public NumberDisplayComponent(int x, int y, NumberSet set, int defaultValue, int length, Direction direction, DrawOrder order) {
		super(x, y, direction.equals(Direction.HORIZONTAL) ? length : 1, direction.equals(Direction.HORIZONTAL) ? 1 : length);
		this.direction = direction;
		this.value = new WrappedProperty<Integer>(defaultValue);
		this.value.addWatcher(i -> {
			if(i < 0) this.value.setValue(0);
		});
		this.order = order;
		this.rendered = 0;
		digits = new ItemStack[10];
		for(int i = 0; i < digits.length; i++) {
			Optional<NumberSkulls> skullOpt = NumberSkulls.getNumberSkulls(set, i);
			if(skullOpt.isPresent()) digits[i] = SkullFactory.createSkull(skullOpt.get().getGameProfile());
			ItemMeta meta = digits[i].getItemMeta();
			meta.setDisplayName(i + "");
			digits[i].setItemMeta(meta);
		}
	}
	
	@Override
	public void render(InterfaceState interfaceState, ItemStack[][] drawBuffer) {
		if(rendered < digits.length) {
			renderedTicker++;
			if(renderedTicker % 2 == 0) {
				int drawIndex = 0;
				for(int i = rendered; i < digits.length; i++) {
					if(drawIndex >= (direction.equals(Direction.HORIZONTAL) ? width : height)) break;
					drawBuffer[direction.equals(Direction.HORIZONTAL) ? this.x + drawIndex : this.x][direction.equals(Direction.VERTICAL) ? this.y + drawIndex : this.y] = digits[i];
					rendered = i + 1;
					drawIndex++;
				}
			}
		} else {
			String number = "" + value.getValue();
			number.replace("-", "");
			if(order.equals(DrawOrder.START_FIRST)) {
				int index = 0;
				for(int x = 0; x < width; x++) {
					for(int y = 0; y < height; y++) {
						if(index >= number.length()) {
							drawBuffer[this.x + x][this.y + y] = digits[0];
						} else {
							char digit = number.charAt(index);
							int n = Integer.parseInt(digit + "");
							drawBuffer[this.x + x][this.y + y] = digits[n];
						}
						index++;
					}
				}
			} else {
				int index = number.length() - 1;
				for(int x = width - 1; x >= 0; x--) {
					for(int y = height - 1; y >= 0; y--) {
						if(index < 0) {
							drawBuffer[this.x + x][this.y + y] = digits[0];
						} else {
							char digit = number.charAt(index);
							int n = Integer.parseInt(digit + "");
							drawBuffer[this.x + x][this.y + y] = digits[n];
						}
						index--;
					}
				}
			}
		}
	}
	
	public WrappedProperty<Integer> getValue() {
		return value;
	}
	
	public enum DrawOrder {
		START_FIRST,
		START_LAST;
	}
	
}
