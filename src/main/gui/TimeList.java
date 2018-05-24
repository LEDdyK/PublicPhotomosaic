package main.gui;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class TimeList {
	public Pane makeTimeList(String A, String B, String C, String D, String E, String F) {
		Pane timeList = new Pane();
		timeList.setPrefWidth(138);
		Rectangle listA = new Rectangle(0, 0, 138, 23);
		listA.setFill(Color.STEELBLUE);
		Rectangle listB = new Rectangle(0, 23, 138, 23);
		listB.setFill(Color.WHITE);
		Rectangle listC = new Rectangle(0, 46, 138, 23);
		listC.setFill(Color.STEELBLUE);
		Rectangle listD = new Rectangle(0, 69, 138, 23);
		listD.setFill(Color.WHITE);
		Rectangle listE = new Rectangle(0, 92, 138, 23);
		listE.setFill(Color.STEELBLUE);
		Rectangle listF = new Rectangle(0, 115, 138, 23);
		listF.setFill(Color.WHITE);
		timeList.getChildren().addAll(listA, listB, listC, listD, listE, listF);
		return timeList;
	}
	
	public Pane makeTimeList() {
		Pane timeList = new Pane();
		timeList.setPrefWidth(138);
		Rectangle listA = new Rectangle(0, 0, 138, 23);
		listA.setFill(Color.STEELBLUE);
		Rectangle listB = new Rectangle(0, 23, 138, 23);
		listB.setFill(Color.WHITE);
		Rectangle listC = new Rectangle(0, 46, 138, 23);
		listC.setFill(Color.STEELBLUE);
		Rectangle listD = new Rectangle(0, 69, 138, 23);
		listD.setFill(Color.WHITE);
		Rectangle listE = new Rectangle(0, 92, 138, 23);
		listE.setFill(Color.STEELBLUE);
		Rectangle listF = new Rectangle(0, 115, 138, 23);
		listF.setFill(Color.WHITE);
		timeList.getChildren().addAll(listA, listB, listC, listD, listE, listF);
		return timeList;
	}
}
