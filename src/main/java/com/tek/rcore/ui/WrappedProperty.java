package com.tek.rcore.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class WrappedProperty<T> {
	
	private List<Consumer<T>> watchers;
	private T value;
	
	public WrappedProperty(T defaultValue) {
		this.watchers = new ArrayList<Consumer<T>>();
		this.value = defaultValue;
	}
	
	public WrappedProperty() {
		this.watchers = new ArrayList<Consumer<T>>();
	}
	
	public void setValue(T value) {
		this.value = value;
		notifyChange();
	}
	
	public void setValueSilent(T value) {
		this.value = value;
	}
	
	public T getValue() {
		return value;
	}
	
	public void addWatcher(Consumer<T> changeCallback) {
		watchers.add(changeCallback);
	}
	
	public void notifyChange() {
		for(Consumer<T> changeCallback : watchers) {
			changeCallback.accept(value);
		}
	}
	
}
