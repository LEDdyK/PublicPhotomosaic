package main.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
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
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import main.Main;
import javafx.stage.Stage;

public class JFXGui extends Application {
	
	public static File selectedFile;
	public static Boolean downState;
	public static Boolean paraGCState;
	public static TextField refPath;
	public static TextField libScale;
	public static TextField threadCount;
	public static TextField gridWidth;
	public static TextField gridHeight;
	public static Progress downProp;
	
	@Override
	public void start(Stage stage) {
		double height = 750;
		double width = 1500;
		
//		set window properties
		stage.setTitle("SOFTENG 751: Photomosaic - Development mode");
		stage.setMinWidth(width);
		stage.setMinHeight(height);
		
		Group root = new Group();

//		reference image path text field
		refPath = new TextField();
			//set position
		refPath.setLayoutX(15);
		refPath.setLayoutY(15);
			//set details
		refPath.setPrefWidth(375);
		
//		browse for reference image button
		Button browse = new Button("Choose Reference");
			//set position
		browse.setLayoutX(405);
		browse.setLayoutY(15);
		
//		run computations button
		Button runComp = new Button("Run Computation");
			//set position
		runComp.setLayoutX(800);
		runComp.setLayoutY(400);
		
//		library scale input
		Label libScaleLabel = new Label ("Library Scale");
		libScale = new TextField();
			//set position
		libScaleLabel.setLayoutX(15);
		libScaleLabel.setLayoutY(59);
		libScale.setLayoutX(200);
		libScale.setLayoutY(55);
			//set details
		libScale.setPrefWidth(50);
		
//		thread count input
		Label threadCountLabel = new Label ("Number of Threads");
		threadCount = new TextField();
			//set position
		threadCountLabel.setLayoutX(280);
		threadCountLabel.setLayoutY(59);
		threadCount.setLayoutX(465);
		threadCount.setLayoutY(55);
			//set details
		threadCount.setPrefWidth(50);
		
//		grid width input
		Label gridWidthLabel = new Label ("Grid Width");
		gridWidth = new TextField();
			//set position
		gridWidthLabel.setLayoutX(15);
		gridWidthLabel.setLayoutY(99);
		gridWidth.setLayoutX(200);
		gridWidth.setLayoutY(95);
			//set details
		gridWidth.setPrefWidth(50);
		
//		grid height input
		Label gridHeightLabel = new Label ("Grid Height");
		gridHeight = new TextField();
			//set position
		gridHeightLabel.setLayoutX(280);
		gridHeightLabel.setLayoutY(99);
		gridHeight.setLayoutX(465);
		gridHeight.setLayoutY(95);
			//set details
		gridHeight.setPrefWidth(50);
		
//		separator
		Line line = new Line(265, 55, 265, 120);
		line.setStroke(Color.LIGHTGRAY);
		
//		image display box
		ImageView display = new ImageView();
			//set details
//		display.setFitWidth(500);
//		display.setFitHeight(500);
		display.setPreserveRatio(false);//TODO revert
			//set position and border
		HBox hbox = new HBox();
		hbox.setBorder(new Border(new BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		hbox.setLayoutX(15);
		hbox.setLayoutY(135);
		hbox.setPrefHeight(500);
		hbox.setPrefWidth(500);
		//TODO adjust to fit
		ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(500, 500);
        scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
        scrollPane.setContent(display);
		hbox.getChildren().add(scrollPane);
		
//		downloader toggle switch
		Rectangle downBack = new Rectangle(15, 647, 502, 23);
		downBack.setFill(Color.STEELBLUE);
		Label downLabel = new Label ("Enable Download");
		downLabel.setTextFill(Color.WHITE);
		JFXToggle downToggle = new JFXToggle();
		Pane downToggleBox = downToggle.makeToggle(34);
			//set position
		downLabel.setLayoutX(20);
		downLabel.setLayoutY(650);
		downToggleBox.setLayoutX(480);
		downToggleBox.setLayoutY(650);
		
//		GUI-computation parallelisation toggle switch
		Rectangle paraGCBack = new Rectangle(15, 670, 502, 23);
		paraGCBack.setFill(Color.WHITE);
		Label paraGCLabel = new Label("Parallelise GUI with Computations");
		JFXToggle paraGC = new JFXToggle();
		Pane paraGCBox = paraGC.makeToggle(34);
			//set position
		paraGCLabel.setLayoutX(20);
		paraGCLabel.setLayoutY(673);
		paraGCBox.setLayoutX(480);
		paraGCBox.setLayoutY(673);
		
//		download progress
		downProp = new Progress();
		ProgressBar downProgress = new ProgressBar();
		downProgress.setLayoutX(800);
		downProgress.setProgress(0F);
		
//		set actions on browse button click
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
	                scrollPane.setContent(null);
	                scrollPane.setContent(display);
					//center image position within box
//					if (refImage.getHeight() > refImage.getWidth()) {
//						double ratio = refImage.getHeight()/500;
//						display.setTranslateX(250 - refImage.getWidth()/(ratio*2));
//						display.setTranslateY(0);
//					}
//					else if (refImage.getHeight() < refImage.getWidth()) {
//						double ratio = refImage.getWidth()/500;
//						display.setTranslateX(0);
//						display.setTranslateY(250 - refImage.getHeight()/(ratio*2));
//					}
//					else {
//						display.setTranslateX(0);
//						display.setTranslateY(0);
//					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		//run computations
		runComp.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				Main.runComputations();
			}
		});
		
//		set actions on download toggle click
		downToggleBox.setOnMouseClicked(event -> {
			downToggle.getState().set(!downToggle.getStateBool());
			downState = downToggle.getStateBool();
		});
		
//		set actions on GUI-computation parallelisation toggle click
		paraGCBox.setOnMouseClicked(event -> {
			paraGC.getState().set(!paraGC.getStateBool());
			paraGCState = paraGC.getStateBool();
		});
		
//		set actions when variable changes
		downProp.countProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != oldVal) {
        		downProgress.setProgress((float)downProp.getCount()/100);
            }
		});
		
//		add GUI elements
		//add browse button to UI object group
		root.getChildren().add(browse);
		//add reference image path to GUI
		root.getChildren().add(refPath);
		//add library scale input to GUI
		root.getChildren().addAll(libScaleLabel, libScale);
		//add thread count input to GUI
		root.getChildren().addAll(threadCountLabel, threadCount);
		//add grid width input to GUI
		root.getChildren().addAll(gridWidthLabel, gridWidth);
		//add grid height input to GUI
		root.getChildren().addAll(gridHeightLabel, gridHeight);
		//add separator
		root.getChildren().add(line);
		//add image display to GUI
		root.getChildren().add(hbox);
		//add download toggle
		root.getChildren().addAll(downBack, downLabel, downToggleBox);
		//add GUI-computation parallelisation toggle
		root.getChildren().addAll(paraGCBack, paraGCLabel, paraGCBox);
		//run computations button
		root.getChildren().add(runComp);
		//download progress bar
		root.getChildren().add(downProgress);
		
		Scene scene = new Scene(root, width, height);
		
		//display UI
		stage.setScene(scene);
        stage.show();
	}
}