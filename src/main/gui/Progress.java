package main.gui;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class Progress {
	private DoubleProperty count = new SimpleDoubleProperty();
	
	public final double getCount() {
		return count.get();
	}
	public final void setCount(double value) {
		count.set(value);
	}
	public DoubleProperty countProperty() {
		return count;
	}
}
