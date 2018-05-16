package main.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class JFXGui extends Application {
	
	public static File selectedFile;
	public static Boolean downState;
	public static TextField refPath;
	
	@Override
	public void start(Stage stage) {
		double height = 750;
		double width = 1500;
		
		//set window properties
		stage.setTitle("SOFTENG 751: Photomosaic - Development mode");
		stage.setMinWidth(width);
		stage.setMinHeight(height);
		
		Group root = new Group();

		//reference image path text field
		refPath = new TextField();
			//set position
		refPath.setLayoutX(15);
		refPath.setLayoutY(15);
			//set details
		refPath.setPrefWidth(300);
		
		//browse for reference image button
		Button browse = new Button("Choose Reference");
			//set position
		browse.setLayoutX(330);
		browse.setLayoutY(15);
		
		//image display box
		ImageView display = new ImageView();
			//set details
		display.setFitWidth(500);
		display.setFitHeight(500);
		display.setPreserveRatio(true);
			//set position and border
		HBox hbox = new HBox();
		hbox.setBorder(new Border(new BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		hbox.setLayoutX(15);
		hbox.setLayoutY(100);
		hbox.setPrefHeight(500);
		hbox.setPrefWidth(500);
		hbox.getChildren().add(display);
		
		//downloader toggle switch
		JFXToggle downToggle = new JFXToggle();
		Pane toggleBox = downToggle.makeToggle(50);
			//set position
		toggleBox.setLayoutX(465);
		toggleBox.setLayoutY(615);
		
		System.out.println(browse.getHeight());
		System.out.println(refPath.getHeight());
		
		
		//set actions on browse button click
		browse.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Browse");
				fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image Files", "*.png", "*.jpg"));
				selectedFile = fileChooser.showOpenDialog(stage);
				refPath.setText(selectedFile.getAbsolutePath());
				try {
					FileInputStream filePath = new FileInputStream(refPath.getText());
					Image refImage = new Image(filePath);
					display.setImage(refImage);
					//center image position within box
					if (refImage.getHeight() > refImage.getWidth()) {
						double ratio = refImage.getHeight()/500;
						display.setTranslateX(250 - refImage.getWidth()/(ratio*2));
						display.setTranslateY(0);
					}
					else if (refImage.getHeight() < refImage.getWidth()) {
						double ratio = refImage.getWidth()/500;
						display.setTranslateX(0);
						display.setTranslateY(250 - refImage.getHeight()/(ratio*2));
					}
					else {
						display.setTranslateX(0);
						display.setTranslateY(0);
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		toggleBox.setOnMouseClicked(event -> {
			downToggle.getState().set(!downToggle.getStateBool());
			downState = downToggle.getStateBool();
		});
		
		//add browse button to UI object group
		root.getChildren().add(browse);
		//add reference image path to GUI
		root.getChildren().add(refPath);
		//add image display to GUI
		root.getChildren().add(hbox);
		//add download toggle
		root.getChildren().add(toggleBox);
		
		Scene scene = new Scene(root, width, height);
		
		//display UI
		stage.setScene(scene);
        stage.show();
	}
}
