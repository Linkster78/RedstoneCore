package com.tek.rcore.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * A class which allows for event-based
 * properties and callbacks.
 * 
 * @author RedstoneTek
 */
public class WrappedProperty<T> {
	
	//The list of watchers to call once the value changes.
	private List<Consumer<T>> watchers;
	//The internal property value.
	private T value;
	
	/**
	 * Creates a WrappedProperty with
	 * the specified default value.
	 * 
	 * @param defaultValue The default value
	 */
	public WrappedProperty(T defaultValue) {
		this.watchers = new ArrayList<Consumer<T>>(4);
		this.value = defaultValue;
	}
	
	/**
	 * Creates a WrappedProperty object.
	 */
	public WrappedProperty() {
		this.watchers = new ArrayList<Consumer<T>>(4);
	}
	
	/**
	 * Sets the value and notifies
	 * all watchers with the new value.
	 * 
	 * @param value The new value
	 */
	public void setValue(T value) {
		this.value = value;
		notifyChange();
	}
	
	/**
	 * Sets the value and doesn't
	 * notify the watchers about it.
	 * 
	 * @param value The new value
	 */
	public void setValueSilent(T value) {
		this.value = value;
	}
	
	/**
	 * Returns the value.
	 * 
	 * @return The value
	 */
	public T getValue() {
		return value;
	}
	
	/**
	 * Adds a change watcher to
	 * the wrapped property.
	 * 
	 * @param changeCallback A callback to call once a change has happened
	 */
	public void addWatcher(Consumer<T> changeCallback) {
		watchers.add(changeCallback);
	}
	
	/**
	 * Forcefully sends the new value to the watchers.
	 */
	public void notifyChange() {
		for(Consumer<T> changeCallback : watchers) {
			changeCallback.accept(value);
		}
	}
	
}
