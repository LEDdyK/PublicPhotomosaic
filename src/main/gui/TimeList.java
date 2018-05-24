package main.gui;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class TimeList {
	Label labelA;
	Label labelB;
	Label labelC;
	Label labelD;
	Label labelE;
	Label labelF;
	
	public Pane makeTimeList(String A, String B, String C, String D, String E, String F) {
		Pane timeList = new Pane();
		timeList.setPrefWidth(138);
		Rectangle listA = new Rectangle(0, 0, 138, 23);
		listA.setFill(Color.STEELBLUE);
		labelA = new Label(A);
		labelA.setTextFill(Color.WHITE);
		labelA.setLayoutX(5);
		labelA.setLayoutY(3);
		Rectangle listB = new Rectangle(0, 23, 138, 23);
		listB.setFill(Color.WHITE);
		labelB = new Label(B);
		labelB.setLayoutX(5);
		labelB.setLayoutY(26);
		Rectangle listC = new Rectangle(0, 46, 138, 23);
		listC.setFill(Color.STEELBLUE);
		labelC = new Label(C);
		labelC.setTextFill(Color.WHITE);
		labelC.setLayoutX(5);
		labelC.setLayoutY(49);
		Rectangle listD = new Rectangle(0, 69, 138, 23);
		listD.setFill(Color.WHITE);
		labelD = new Label(D);
		labelD.setLayoutX(5);
		labelD.setLayoutY(72);
		Rectangle listE = new Rectangle(0, 92, 138, 23);
		listE.setFill(Color.STEELBLUE);
		labelE = new Label(E);
		labelE.setTextFill(Color.WHITE);
		labelE.setLayoutX(5);
		labelE.setLayoutY(95);
		Rectangle listF = new Rectangle(0, 115, 138, 23);
		listF.setFill(Color.WHITE);
		labelF = new Label(F);
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
		labelA = new Label("0");
		listA.setFill(Color.STEELBLUE);
		labelA.setTextFill(Color.WHITE);
		labelA.setLayoutX(5);
		labelA.setLayoutY(3);
		Rectangle listB = new Rectangle(0, 23, 138, 23);
		listB.setFill(Color.WHITE);
		labelB = new Label("0");
		labelB.setLayoutX(5);
		labelB.setLayoutY(26);
		Rectangle listC = new Rectangle(0, 46, 138, 23);
		listC.setFill(Color.STEELBLUE);
		labelC = new Label("0");
		labelC.setTextFill(Color.WHITE);
		labelC.setLayoutX(5);
		labelC.setLayoutY(49);
		Rectangle listD = new Rectangle(0, 69, 138, 23);
		listD.setFill(Color.WHITE);
		labelD = new Label("0");
		labelD.setLayoutX(5);
		labelD.setLayoutY(72);
		Rectangle listE = new Rectangle(0, 92, 138, 23);
		listE.setFill(Color.STEELBLUE);
		labelE = new Label("0");
		labelE.setTextFill(Color.WHITE);
		labelE.setLayoutX(5);
		labelE.setLayoutY(95);
		Rectangle listF = new Rectangle(0, 115, 138, 23);
		listF.setFill(Color.WHITE);
		labelF = new Label("0");
		labelF.setLayoutX(5);
		labelF.setLayoutY(118);
		timeList.getChildren().addAll(listA, listB, listC, listD, listE, listF);
		timeList.getChildren().addAll(labelA, labelB, labelC, labelD, labelE, labelF);
		return timeList;
	}
	
	public void setLabel (String label, String millis) {
		switch (label) {
			case "overall":
				this.labelA.setText(millis);
				break;
			case "download":
				System.out.println(millis);
				this.labelB.setText(millis);
				break;
			case "reference":
				this.labelC.setText(millis);
				break;
			case "library":
				this.labelD.setText(millis);
				break;
			case "rgb":
				this.labelE.setText(millis);
				break;
			case "mosaic":
				this.labelF.setText(millis);
				break;
		}
	}
	
	public Label getLabelA() {
		return labelA;
	}

	public Label getLabelB() {
		return labelB;
	}

	public Label getLabelC() {
		return labelC;
	}

	public Label getLabelD() {
		return labelD;
	}

	public Label getLabelE() {
		return labelE;
	}

	public Label getLabelF() {
		return labelF;
	}

	public void setLabel (TimeList list) {
		setText(this.labelA, list.getLabelA());
		setText(this.labelB, list.getLabelB());
		setText(this.labelC, list.getLabelC());
		setText(this.labelD, list.getLabelD());
		setText(this.labelE, list.getLabelE());
		setText(this.labelF, list.getLabelF());
	}
	
	private static void setText(Label to, Label from) {
		to.setText(from.getText());
	}
}
