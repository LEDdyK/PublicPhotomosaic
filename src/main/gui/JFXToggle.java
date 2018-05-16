package main.gui;

import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class JFXToggle extends JFXGui {
	
	private BooleanProperty state = new SimpleBooleanProperty(false);
	
	private TranslateTransition translateAnimation = new TranslateTransition(Duration.seconds(0.25));
	private FillTransition fillAnimation = new FillTransition(Duration.seconds(0.2));
	private ParallelTransition animation = new ParallelTransition(translateAnimation, fillAnimation);
	
	public BooleanProperty getState() {
		return state;
	}
	public boolean getStateBool() {
		return state.get();
	}
	
	public Pane makeToggle(double width) {
		Pane toggleBox = new Pane();
		
		Rectangle toggleBack = new Rectangle(width, width/2);
		toggleBack.setArcWidth(width/2);
		toggleBack.setArcHeight(width/2);
		toggleBack.setFill(Color.WHITE);
		toggleBack.setStroke(Color.LIGHTGRAY);
		Circle toggleFore = new Circle(width/4);
		toggleFore.setCenterX(width/4);
		toggleFore.setCenterY(width/4);
		toggleFore.setFill(Color.WHITE);
		toggleFore.setStroke(Color.LIGHTGRAY);
		
		//animation properties
		translateAnimation.setNode(toggleFore);
		fillAnimation.setShape(toggleBack);
		
		toggleBox.getChildren().addAll(toggleBack, toggleFore);
		
		state.addListener((obs, oldState, newState) -> {
			boolean isOn = newState.booleanValue();
			translateAnimation.setToX(isOn ? (width/2):0);
			fillAnimation.setFromValue(isOn ? Color.WHITE : Color.CYAN);
			fillAnimation.setToValue(isOn ? Color.CYAN : Color.WHITE);
			animation.play();
		});
		
//		toggleFore.setOnMouseClicked(event -> {
//			state.set(!state.get());
//			System.out.println(state.get());
//		});
		
		return toggleBox;
	}
}
