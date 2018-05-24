package main.gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;

import apt.annotations.Future;
import apt.annotations.Gui;
import apt.annotations.TaskInfoType;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
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
import main.MosaicBuilder;
import main.images.AvgRGB;
import main.images.ImageGrid;
import main.images.RGBLibrary;
import main.images.downloader.ImageDownloader;
import main.images.reader.ImageLibrary;
import pt.runtime.ParaTask.TaskType;
import javafx.stage.Stage;

public class JFXGui extends Application {
	
	private File selectedFile;
	private File saveToFile;
	
	private Boolean downState;
	private Boolean paraGCState;
	
	private TextField refPath;
	private TextField libScale;
	private TextField threadCount;
	private TextField gridWidth;
	private TextField gridHeight;
	
	private int numberOfCells;

	private HBox hboxOut;
	private ImageView dispOut;
	private double frameCounting;
	
	private ProgressBar downProgress;
	private ProgressBar imgLibProgress;
	private ProgressBar rgbLibProgress;
	private ProgressBar imgGridProgress;
	private ProgressBar mosaicBuildProgress;
	
	private Label downloadLabel;
	private Label imgLibLabel;
	private Label rgbLibLabel;
	private Label imgGridLabel;
	private Label mosaicBuildLabel;

	
	private Button saveImageButton;
	private Button runCompButton;
	
	private BufferedImage finishedImage;
	
	private ImageDownloader imageDownloader;
	private ImageLibrary imageLibrary;
	private ImageGrid imageGrid;
	private RGBLibrary rgbLibrary;
	private MosaicBuilder mosaicBuilder;
	
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
		runCompButton = new Button("Run Computation");
			//set position
		runCompButton.setLayoutX(545);
		runCompButton.setLayoutY(400);
		
//		browse for reference image button
		saveImageButton = new Button("Save Image To...");
			//set position
		saveImageButton.setLayoutX(545);
		saveImageButton.setLayoutY(440);
		saveImageButton.setDisable(true);
		
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
		display.setFitWidth(250);
		display.setFitHeight(250);
		display.setPreserveRatio(true);//TODO revert
			//set position and border
		HBox hbox = new HBox();
		hbox.setBorder(new Border(new BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		hbox.setLayoutX(15);
		hbox.setLayoutY(135);
		hbox.setPrefWidth(250);
		hbox.setPrefHeight(250);
		
//		output display box
		dispOut = new ImageView();
			//set details
		dispOut.setFitWidth(810);
		dispOut.setFitHeight(720);
		dispOut.setPreserveRatio(true);
			//set position and border
		hboxOut = new HBox();
		hboxOut.setBorder(new Border(new BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		hboxOut.setLayoutX(675);
		hboxOut.setLayoutY(15);
		hboxOut.setPrefWidth(810);
		hboxOut.setPrefHeight(720);

//		downloader toggle switch
		Rectangle downBack = new Rectangle(15, 397, 502, 23);
		downBack.setFill(Color.STEELBLUE);
		Label downLabel = new Label ("Enable Download");
		downLabel.setTextFill(Color.WHITE);
		JFXToggle downToggle = new JFXToggle();
		Pane downToggleBox = downToggle.makeToggle(34);
			//set position
		downLabel.setLayoutX(20);
		downLabel.setLayoutY(400);
		downToggleBox.setLayoutX(480);
		downToggleBox.setLayoutY(400);
		
//		GUI-computation parallelisation toggle switch
		Rectangle paraGCBack = new Rectangle(15, 420, 502, 23);
		paraGCBack.setFill(Color.WHITE);
		Label paraGCLabel = new Label("Run tasks in Parallel");
		JFXToggle paraGC = new JFXToggle();
		Pane paraGCBox = paraGC.makeToggle(34);
		paraGC.getState().set(true);
		paraGCState = true;
			//set position
		paraGCLabel.setLayoutX(20);
		paraGCLabel.setLayoutY(423);
		paraGCBox.setLayoutX(480);
		paraGCBox.setLayoutY(423);
		
//		download progress bar and label
		downProgress = new ProgressBar();
		downProgress.setLayoutX(280);
		downProgress.setLayoutY(135);
		downProgress.setProgress(0F);
		
		downloadLabel = new Label("Waiting");
		downloadLabel.setLayoutX(280);
		downloadLabel.setLayoutY(155);
		
//		image library progress bar and label
		imgLibProgress = new ProgressBar();
		imgLibProgress.setLayoutX(280);
		imgLibProgress.setLayoutY(185);
		imgLibProgress.setProgress(0F);
		
		imgLibLabel = new Label("Waiting");
		imgLibLabel.setLayoutX(280);
		imgLibLabel.setLayoutY(205);
		
		// rgb library progress bar and label
		rgbLibProgress = new ProgressBar();
		rgbLibProgress.setLayoutX(280);
		rgbLibProgress.setLayoutY(235);
		rgbLibProgress.setProgress(0F);
		
		rgbLibLabel = new Label("Waiting");
		rgbLibLabel.setLayoutX(280);
		rgbLibLabel.setLayoutY(255);
		
		// img grid progress bar and label
		imgGridProgress = new ProgressBar();
		imgGridProgress.setLayoutX(280);
		imgGridProgress.setLayoutY(285);
		imgGridProgress.setProgress(0F);
		
		imgGridLabel = new Label("Waiting");
		imgGridLabel.setLayoutX(280);
		imgGridLabel.setLayoutY(305);
		
		
//		mosaic build progress bar and label
		mosaicBuildProgress = new ProgressBar();
		mosaicBuildProgress.setLayoutX(280);
		mosaicBuildProgress.setLayoutY(335);
		mosaicBuildProgress.setProgress(0F);
		
		mosaicBuildLabel = new Label("Waiting");
		mosaicBuildLabel.setLayoutX(280);
		mosaicBuildLabel.setLayoutY(355);
		
//		set actions on browse button click
		browse.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Select Reference Image");
				fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image Files", "*.png", "*.jpg"));
				selectedFile = fileChooser.showOpenDialog(stage);
				if (selectedFile != null) {
					refPath.setText(selectedFile.getAbsolutePath());
					try {
						FileInputStream filePath = new FileInputStream(refPath.getText());
						Image refImage = new Image(filePath);
						display.setImage(refImage);

						//center image position within box
						if (refImage.getHeight() > refImage.getWidth()) {
							double ratio = refImage.getHeight()/250;
							display.setTranslateX(125 - refImage.getWidth()/(ratio*2));
							display.setTranslateY(0);
						}
						else if (refImage.getHeight() < refImage.getWidth()) {
							double ratio = refImage.getWidth()/250;
							display.setTranslateX(0);
							display.setTranslateY(125 - refImage.getHeight()/(ratio*2));
						}
						else {
							display.setTranslateX(0);
							display.setTranslateY(0);
						}
						hbox.getChildren().add(display);
					} catch (FileNotFoundException e) {
						//TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		
//		run computations
		runCompButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				((Button)arg0.getSource()).setDisable(true);
				imgLibProgress.setProgress(0);
				downProgress.setProgress(0);
				rgbLibProgress.setProgress(0);
				imgGridProgress.setProgress(0);
				mosaicBuildProgress.setProgress(0);
				saveImageButton.setDisable(true);
				hboxOut.getChildren().remove(dispOut);
				initialiseProcessingObjects(false);
				
				if (paraGCState) {
					runComputations();				
				} else {
					runComputationsSequentially();
				}				
			}
		});
		
//		saving image
		saveImageButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				FileChooser saveChooser = new FileChooser();
				saveChooser.setTitle("Save Image To...");
				saveChooser.getExtensionFilters().addAll(new ExtensionFilter("Image Files", "*.png"));
				saveToFile = saveChooser.showOpenDialog(stage);
				if (saveToFile != null) {
					((Button)arg0.getSource()).setDisable(true);
					
					@Future(taskType = TaskInfoType.INTERACTIVE)
					int ioTask = mosaicBuilder.saveImageOnDisk(saveToFile);
					
					@Gui(notifiedBy = "ioTask")
					Void enableSave = mosaicBuilder.enableSave((Button)arg0.getSource());
				}
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
		//add run computations button
		root.getChildren().add(runCompButton);
		//add save image to button
		root.getChildren().add(saveImageButton);
		//add download progress bar
		root.getChildren().add(downProgress);
		//add image library progress bar
		root.getChildren().add(imgLibProgress);
		//add rgb library progress bar
		root.getChildren().add(rgbLibProgress);
		//add img grid progress bar
		root.getChildren().add(imgGridProgress);
		//add tinder/substitution porgress bar
		root.getChildren().add(mosaicBuildProgress);

		//add image output display to GUI
		root.getChildren().add(hboxOut);
		
		// add progress labels
		root.getChildren().add(downloadLabel);
		root.getChildren().add(imgLibLabel);
		root.getChildren().add(rgbLibLabel);
		root.getChildren().add(imgGridLabel);
		root.getChildren().add(mosaicBuildLabel);
		
		initialiseProcessingObjects(true);
		
		Scene scene = new Scene(root, width, height);
		
		new AnimationTimer() {
			@Override
			public void handle(long arg0) {
				if (!mosaicBuilder.isFinished()) {
					++frameCounting;
					if (frameCounting % 60 == 0) {
						dispOut.setImage(mosaicBuilder.getOutputImage());
						hboxOut.getChildren().remove(dispOut);
						hboxOut.getChildren().add(dispOut);
						frameCounting = 0;
					}	
				}		
			}
		}.start();
	
		//display UI
		stage.setScene(scene);
        stage.show();
	}
	
	private void initialiseProcessingObjects(boolean firstStartup) {
		try {
			imageDownloader = new ImageDownloader(downProgress, downloadLabel);
			imageLibrary = new ImageLibrary(imgLibProgress, imgLibLabel);
			
			if (firstStartup) {
				imageGrid = new ImageGrid(null, imgGridProgress, imgGridLabel);
			} else {
				imageGrid = new ImageGrid(ImageIO.read(new File(refPath.getText())), imgGridProgress, imgGridLabel);
			}
			
			rgbLibrary = new RGBLibrary(rgbLibProgress, rgbLibLabel);
			mosaicBuilder = new MosaicBuilder(mosaicBuildProgress, mosaicBuildLabel);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void runComputations() {		
		// Download images
		@Future
		int imageDownloadTask = imageDownloader.downloadRecentImages(Integer.parseInt(threadCount.getText()));

		@Gui(notifiedBy="imageDownloadTask")
		Void imageDownloadGuiUpdate = imageDownloader.postExecutionUpdate();
		
		
		// Process sub-images in directory
		@Future(depends="imageDownloadTask")
		Map<String, BufferedImage> imageLibraryResult = imageLibrary.readDirectory("photos", Double.parseDouble(libScale.getText()), Integer.parseInt(threadCount.getText()));	
					
		@Gui(notifiedBy="imageLibraryResult")
		Void imgLibraryGuiUpdate = imageLibrary.postExecutionUpdate();
		
		
		// Calculate RGB values for sub-images in library
		@Future()
		Map<String, AvgRGB> rgbList = rgbLibrary.calculateRGB(imageLibraryResult);
		
		@Gui(notifiedBy="rgbList")
		Void rgbListGuiUpdate = rgbLibrary.postExecutionUpdate();
		
		
		// Calculate RGB values of cells for reference image
		@Future()
		int imageGridTask = imageGrid.createGrid(false, Integer.parseInt(gridWidth.getText()), Integer.parseInt(gridHeight.getText()));	
		
		@Gui(notifiedBy="imageGridTask")
		Void imageGridGuiUpdate = imageGrid.postExecutionUpdate();
		
				
		// Create Photomosaic using the processed sub-images
		@Future()
		int mosaicBuild = mosaicBuilder.createMosaic(imageLibrary, rgbList, imageGrid, Integer.parseInt(threadCount.getText()), 'R');
		
		@Gui(notifiedBy="mosaicBuild")
		Void mosaicBuildGuiUpdate = mosaicBuilder.postExecutionUpdate(dispOut, saveImageButton, runCompButton);
		
	}
	
	private void runComputationsSequentially() {

		int imageDownloadTask = imageDownloader.downloadRecentImages(1);

		Void imageDownloadGuiUpdate = imageDownloader.postExecutionUpdate();

		Map<String, BufferedImage> imageLibraryResult = imageLibrary.readDirectory("photos", Double.parseDouble(libScale.getText()), 1);	

		Void imgLibraryGuiUpdate = imageLibrary.postExecutionUpdate();		

		Map<String, AvgRGB> rgbList = rgbLibrary.calculateRGB(imageLibraryResult);

		Void rgbListGuiUpdate = rgbLibrary.postExecutionUpdate();

		int imageGridTask = imageGrid.createGrid(false, Integer.parseInt(gridWidth.getText()), Integer.parseInt(gridHeight.getText()));	

		Void imageGridGuiUpdate = imageGrid.postExecutionUpdate();

		int mosaicBuild = mosaicBuilder.createMosaic(imageLibrary, rgbList, imageGrid, 1, 'R');

		Void mosaicBuildGuiUpdate = mosaicBuilder.postExecutionUpdate(dispOut, saveImageButton, runCompButton);
	}
}
