package main.gui;

import java.io.File;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class JFXGui extends Application {
	
	public static File selectedFile;
	public static Boolean enableDown;
	
	@Override
	public void start(Stage stage) {
		double height = 750;
		double width = 1500;
		
		//set window properties
		stage.setTitle("SOFTENG 751: Photomosaic - Development mode");
		stage.setMinWidth(width);
		stage.setMinHeight(height);
		
		Group root = new Group();
		
		//browse for reference image button
		Button browse = new Button("Choose Reference");
		browse.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Browse Reference");
				fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image Files", "*.png", "*.jpg"));
				selectedFile = fileChooser.showOpenDialog(stage);
			}
		});
		//set browse button positioning
		browse.setLayoutX(15);
		browse.setLayoutY(15);
		//add browse button to UI object group
		root.getChildren().add(browse);
		
		Scene scene = new Scene(root, width, height);
		
		//display UI
		stage.setScene(scene);
        stage.show();
	}

}
