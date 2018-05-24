package main.gui;

import javafx.scene.control.Label;
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
		Label labelA = new Label(A);
		labelA.setTextFill(Color.WHITE);
		labelA.setLayoutX(5);
		labelA.setLayoutY(3);
		Rectangle listB = new Rectangle(0, 23, 138, 23);
		listB.setFill(Color.WHITE);
		Label labelB = new Label(B);
		labelB.setLayoutX(5);
		labelB.setLayoutY(26);
		Rectangle listC = new Rectangle(0, 46, 138, 23);
		listC.setFill(Color.STEELBLUE);
		Label labelC = new Label(C);
		labelC.setTextFill(Color.WHITE);
		labelC.setLayoutX(5);
		labelC.setLayoutY(49);
		Rectangle listD = new Rectangle(0, 69, 138, 23);
		listD.setFill(Color.WHITE);
		Label labelD = new Label(D);
		labelD.setLayoutX(5);
		labelD.setLayoutY(72);
		Rectangle listE = new Rectangle(0, 92, 138, 23);
		listE.setFill(Color.STEELBLUE);
		Label labelE = new Label(E);
		labelE.setTextFill(Color.WHITE);
		labelE.setLayoutX(5);
		labelE.setLayoutY(95);
		Rectangle listF = new Rectangle(0, 115, 138, 23);
		listF.setFill(Color.WHITE);
		Label labelF = new Label(F);
		labelF.setLayoutX(5);
		labelF.setLayoutY(118);
		timeList.getChildren().addAll(listA, listB, listC, listD, listE, listF);
		timeList.getChildren().addAll(labelA, labelB, labelC, labelD, labelE, labelF);
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
