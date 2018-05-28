package main.gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import apt.annotations.AsyncCatch;
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
import main.exceptions.ImageTooBigException;
import main.images.AvgRGB;
import main.images.ImageGrid;
import main.images.RGBLibrary;
import main.images.downloader.ImageDownloader;
import main.images.reader.ImageLibrary;
import pt.runtime.ParaTask.TaskType;
import javafx.stage.Stage;

public class JFXGui extends Application implements GUICallback {
	
	private HashMap<String, String> latestTimes = new HashMap<>();
	
	private File selectedFile;
	private File saveToFile;
	
	private Boolean downState;
	private Boolean paraTTState;
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
	
	private TimeList timeItem1;
	private TimeList timeItem2;
	private TimeList timeItem3;
	private TimeList timeItem4;
	private TimeList timeItem5;
	private TimeList timeItem6;
	
	@Override
	public void start(Stage stage) {
		double height = 708;
		double width = 1049;
		
//		set window properties
		stage.setTitle("SOFTENG 751: Photomosaic - Development mode");
		stage.setWidth(width);
		stage.setHeight(height);
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
		runCompButton.setPrefWidth(235);
			//set position
		runCompButton.setLayoutX(280);
		runCompButton.setLayoutY(397);
		
//		browse for reference image button
		saveImageButton = new Button("Save Image To...");
		saveImageButton.setPrefWidth(235);
			//set position
		saveImageButton.setLayoutX(280);
		saveImageButton.setLayoutY(441);
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
		dispOut.setFitWidth(449);
		dispOut.setFitHeight(449);
		dispOut.setPreserveRatio(true);
			//set position and border
		hboxOut = new HBox();
		hboxOut.setBorder(new Border(new BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		hboxOut.setLayoutX(530);
		hboxOut.setLayoutY(15);
		hboxOut.setPrefWidth(449);
		hboxOut.setPrefHeight(449);

//		downloader toggle switch
		Rectangle downBack = new Rectangle(15, 397, 250, 23);
		downBack.setFill(Color.STEELBLUE);
		Label downLabel = new Label ("Enable Download");
		downLabel.setTextFill(Color.WHITE);
		JFXToggle downToggle = new JFXToggle();
		Pane downToggleBox = downToggle.makeToggle(34);
		downToggle.getState().set(true);
		downState = true;
			//set position
		downLabel.setLayoutX(20);
		downLabel.setLayoutY(400);
		downToggleBox.setLayoutX(228);
		downToggleBox.setLayoutY(400);
		
//		task-task parallelisation toggle switch
		Rectangle paraTTBack = new Rectangle(15, 420, 250, 23);
		paraTTBack.setFill(Color.WHITE);
		Label paraTTLabel = new Label("Run Tasks in Parallel");
		JFXToggle paraTT = new JFXToggle();
		Pane paraTTBox = paraTT.makeToggle(34);
		paraTT.getState().set(true);
		paraTTState = true;
			//set position
		paraTTLabel.setLayoutX(20);
		paraTTLabel.setLayoutY(423);
		paraTTBox.setLayoutX(228);
		paraTTBox.setLayoutY(423);
		
//		GUI-computation parallelisation toggle switch
		Rectangle paraGCBack = new Rectangle(15, 443, 250, 23);
		paraGCBack.setFill(Color.STEELBLUE);
		Label paraGCLabel = new Label("Run GUI in Parallel");
		paraGCLabel.setTextFill(Color.WHITE);
		JFXToggle paraGC = new JFXToggle();
		Pane paraGCBox = paraGC.makeToggle(34);
		paraGC.getState().set(true);
		paraGCState = true;
			//set position
		paraGCLabel.setLayoutX(20);
		paraGCLabel.setLayoutY(446);
		paraGCBox.setLayoutX(228);
		paraGCBox.setLayoutY(446);
		
//		download progress bar
		downProgress = new ProgressBar();
		downProgress.setLayoutX(280);
		downProgress.setLayoutY(135);
		downProgress.setPrefWidth(235);
		downProgress.setProgress(0F);
			//label
		downloadLabel = new Label("Waiting");
		downloadLabel.setLayoutX(280);
		downloadLabel.setLayoutY(155);
		
//		image library progress bar
		imgLibProgress = new ProgressBar();
		imgLibProgress.setLayoutX(280);
		imgLibProgress.setLayoutY(185);
		imgLibProgress.setPrefWidth(235);
		imgLibProgress.setProgress(0F);
			//label
		imgLibLabel = new Label("Waiting");
		imgLibLabel.setLayoutX(280);
		imgLibLabel.setLayoutY(205);
		
		// rgb library progress bar
		rgbLibProgress = new ProgressBar();
		rgbLibProgress.setLayoutX(280);
		rgbLibProgress.setLayoutY(235);
		rgbLibProgress.setPrefWidth(235);
		rgbLibProgress.setProgress(0F);
			//label
		rgbLibLabel = new Label("Waiting");
		rgbLibLabel.setLayoutX(280);
		rgbLibLabel.setLayoutY(255);
		
		// img grid progress bar
		imgGridProgress = new ProgressBar();
		imgGridProgress.setLayoutX(280);
		imgGridProgress.setLayoutY(285);
		imgGridProgress.setPrefWidth(235);
		imgGridProgress.setProgress(0F);
			//label
		imgGridLabel = new Label("Waiting");
		imgGridLabel.setLayoutX(280);
		imgGridLabel.setLayoutY(305);
		
//		mosaic build progress bar
		mosaicBuildProgress = new ProgressBar();
		mosaicBuildProgress.setLayoutX(280);
		mosaicBuildProgress.setLayoutY(335);
		mosaicBuildProgress.setPrefWidth(235);
		mosaicBuildProgress.setProgress(0F);
			//label
		mosaicBuildLabel = new Label("Waiting");
		mosaicBuildLabel.setLayoutX(280);
		mosaicBuildLabel.setLayoutY(355);
		
//		time values
		HBox timeBox = new HBox();
			//time list
		TimeList timeList = new TimeList();
		Pane timeListPane = new Pane();
		timeListPane = timeList.makeTimeList("Overall", "Download", "Reference", "Library", "RGB", "Mosaic");
			//time 1
		timeItem1 = new TimeList();
		Pane timeItem1Pane = new Pane();
		timeItem1Pane = timeItem1.makeTimeList();
			//time 2
		timeItem2 = new TimeList();
		Pane timeItem2Pane = new Pane();
		timeItem2Pane = timeItem2.makeTimeList();
			//time 3
		timeItem3 = new TimeList();
		Pane timeItem3Pane = new Pane();
		timeItem3Pane = timeItem3.makeTimeList();
			//time 4
		timeItem4 = new TimeList();
		Pane timeItem4Pane = new Pane();
		timeItem4Pane = timeItem4.makeTimeList();
			//time 5
		timeItem5 = new TimeList();
		Pane timeItem5Pane = new Pane();
		timeItem5Pane = timeItem5.makeTimeList();
			//time 6
		timeItem6 = new TimeList();
		Pane timeItem6Pane = new Pane();
		timeItem6Pane = timeItem6.makeTimeList();
			//time values set up
		timeBox.setPrefSize(966, 138);
		timeBox.setLayoutX(15);
		timeBox.setLayoutY(489);
//		timeBox.setBorder(new Border(new BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		timeBox.getChildren().addAll(timeListPane, timeItem1Pane, timeItem2Pane, timeItem3Pane, timeItem4Pane, timeItem5Pane, timeItem6Pane);
		
//		set actions on browse button click
		browse.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Select Reference Image");
				fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.bmp"));
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
				
				if (!downState) {
					downloadLabel.setText("Skipping downloads");
				} else {
					downloadLabel.setText("Waiting");
				}
				
				imgLibLabel.setText("Waiting");
				rgbLibLabel.setText("Waiting");
				imgGridLabel.setText("Waiting");
				mosaicBuildLabel.setText("Waiting");
				
				saveImageButton.setDisable(true);
				hboxOut.getChildren().remove(dispOut);
				initialiseProcessingObjects(false);
				
				shiftTimes();
				
				if (paraGCState) {
					if (paraTTState) {
						runComputations();				
					} else {
						runComputationsSequentially();
					}
				} else {
					runComputationsCompletelySequentially();
				}
			}
		});
		
//		saving image
		saveImageButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				FileChooser saveChooser = new FileChooser();
				saveChooser.setTitle("Save Image To...");
				saveChooser.getExtensionFilters().addAll(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.bmp"));
				saveToFile = saveChooser.showSaveDialog(stage);
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
		
//		set actions on task-task parallelisation toggle click
		paraTTBox.setOnMouseClicked(event -> {
			paraTT.getState().set(!paraTT.getStateBool());
			paraTTState = paraTT.getStateBool();
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
		//add task-task parallelisation toggle
		root.getChildren().addAll(paraTTBack, paraTTLabel, paraTTBox);
		//add GUI-computation parallelisation toggle
		root.getChildren().addAll(paraGCBack, paraGCLabel, paraGCBox);
		//add run computations button
		root.getChildren().add(runCompButton);
		//add save image to button
		root.getChildren().add(saveImageButton);
		//add download progress bar
		root.getChildren().addAll(downProgress, downloadLabel);
		//add image library progress bar
		root.getChildren().addAll(imgLibProgress, imgLibLabel);
		//add rgb library progress bar
		root.getChildren().addAll(rgbLibProgress, rgbLibLabel);
		//add img grid progress bar
		root.getChildren().addAll(imgGridProgress, imgGridLabel);
		//add tinder/substitution porgress bar
		root.getChildren().addAll(mosaicBuildProgress, mosaicBuildLabel);
		//add image output display to GUI
		root.getChildren().add(hboxOut);
		//add time values
		root.getChildren().add(timeBox);
		
		initialiseProcessingObjects(true);
		
		Scene scene = new Scene(root, width, height);

		latestTimes.put("overall", "0");
		latestTimes.put("download", "0");
		latestTimes.put("reference", "0");
		latestTimes.put("library", "0");
		latestTimes.put("rgb", "0");
		latestTimes.put("mosaic", "0");
		
		//refresh output display
		new AnimationTimer() {
			@Override
			public void handle(long arg0) {
				if (!mosaicBuilder.isFinished()) {
					++frameCounting;
					if (frameCounting % 1 == 0) {
						dispOut.setImage(mosaicBuilder.getOutputImage());
						hboxOut.getChildren().remove(dispOut);
						hboxOut.getChildren().add(dispOut);
						frameCounting = 0;
					}
				}
				//System.out.println(latestTimes.get("download"));
				updateLatest();
			}
		}.start();
	
		//display UI
		stage.setScene(scene);
        stage.show();
	}
	
	
	private void initialiseProcessingObjects(boolean firstStartup) {
		try {
			imageDownloader = new ImageDownloader(downProgress, downloadLabel, !downState);
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
		long startTime = System.currentTimeMillis();
		@Future
		int imageDownloadTask = imageDownloader.downloadRecentImages(Integer.parseInt(threadCount.getText()), this);
		
		@Gui(notifiedBy="imageDownloadTask")
		Void imageDownloadGuiUpdate = imageDownloader.postExecutionUpdate();
		
		
		// Process sub-images in directory
		@Future(depends="imageDownloadTask")
		Map<String, BufferedImage> imageLibraryResult = imageLibrary.readDirectory("photos", Double.parseDouble(libScale.getText()), Integer.parseInt(threadCount.getText()), this);
		
		@Gui(notifiedBy="imageLibraryResult")
		Void imgLibraryGuiUpdate = imageLibrary.postExecutionUpdate();
		
		
		// Calculate RGB values for sub-images in library
		@Future()
		Map<String, AvgRGB> rgbList = rgbLibrary.calculateRGB(imageLibraryResult, this);
		
		@Gui(notifiedBy="rgbList")
		Void rgbListGuiUpdate = rgbLibrary.postExecutionUpdate();
		
		
		// Calculate RGB values of cells for reference image
		@Future()
		int imageGridTask = imageGrid.createGrid(false, Integer.parseInt(gridWidth.getText()), Integer.parseInt(gridHeight.getText()), this);
		
		@Gui(notifiedBy="imageGridTask")
		Void imageGridGuiUpdate = imageGrid.postExecutionUpdate();
		
		
		// Create Photomosaic using the processed sub-images
		@AsyncCatch(throwables= {ImageTooBigException.class}, handlers= {"handleImageTooBig()"})
		@Future()
		int mosaicBuild = mosaicBuilder.createMosaic(imageLibrary, rgbList, imageGrid, Integer.parseInt(threadCount.getText()), 'R', this, startTime);
		
		@Gui(notifiedBy="mosaicBuild")
		Void mosaicBuildGuiUpdate = mosaicBuilder.postExecutionUpdate(dispOut, saveImageButton, runCompButton);
		
		
		@Future(depends="mosaicBuild")
		int print = printTime(startTime);
	}
	
	private void runComputationsSequentially() {
		long startTime = System.currentTimeMillis();
		@Future
		int imageDownloadTask = imageDownloader.downloadRecentImages(1, this);
		@Gui(notifiedBy="imageDownloadTask")
		Void imageDownloadGuiUpdate = imageDownloader.postExecutionUpdate();
		@Future(depends="imageDownloadTask")
		Map<String, BufferedImage> imageLibraryResult = imageLibrary.readDirectory("photos", Double.parseDouble(libScale.getText()), 1, this);	
		@Gui(notifiedBy="imageLibraryResult")
		Void imgLibraryGuiUpdate = imageLibrary.postExecutionUpdate();		
		@Future(depends="imageLibraryResult")
		Map<String, AvgRGB> rgbList = rgbLibrary.calculateRGB(imageLibraryResult, this);
		@Gui(notifiedBy="rgbList")
		Void rgbListGuiUpdate = rgbLibrary.postExecutionUpdate();
		@Future(depends="rgbList")
		int imageGridTask = imageGrid.createGrid(false, Integer.parseInt(gridWidth.getText()), Integer.parseInt(gridHeight.getText()), this);	
		@Gui(notifiedBy="imageGridTask")
		Void imageGridGuiUpdate = imageGrid.postExecutionUpdate();
		@AsyncCatch(throwables= {ImageTooBigException.class}, handlers= {"handleImageTooBig()"})
		@Future(depends="imageGridTask")
		int mosaicBuild = mosaicBuilder.createMosaic(imageLibrary, rgbList, imageGrid, 1, 'R', this, startTime);
		@Gui(notifiedBy="mosaicBuild")
		Void mosaicBuildGuiUpdate = mosaicBuilder.postExecutionUpdate(dispOut, saveImageButton, runCompButton);
	
		@Future(depends="mosaicBuild")
		int print = printTime(startTime);
	}
	
	
	private void runComputationsCompletelySequentially() {
		long startTime = System.currentTimeMillis();
		int imageDownloadTask = imageDownloader.downloadRecentImages(1, this);
		Void imageDownloadGuiUpdate = imageDownloader.postExecutionUpdate();
		Map<String, BufferedImage> imageLibraryResult = imageLibrary.readDirectory("photos", Double.parseDouble(libScale.getText()), 1, this);
		Void imgLibraryGuiUpdate = imageLibrary.postExecutionUpdate();
		Map<String, AvgRGB> rgbList = rgbLibrary.calculateRGB(imageLibraryResult, this);
		Void rgbListGuiUpdate = rgbLibrary.postExecutionUpdate();
		int imageGridTask = imageGrid.createGrid(false, Integer.parseInt(gridWidth.getText()), Integer.parseInt(gridHeight.getText()), this);
		Void imageGridGuiUpdate = imageGrid.postExecutionUpdate();
		try {
			int mosaicBuild = mosaicBuilder.createMosaic(imageLibrary, rgbList, imageGrid, 1, 'R', this, startTime);
		} catch (ImageTooBigException e) {
			handleImageTooBig();
		}
		
		Void mosaicBuildGuiUpdate = mosaicBuilder.postExecutionUpdate(dispOut, saveImageButton, runCompButton);

		@Future(depends="mosaicBuild")
		int print = printTime(startTime);
	}


	
	private void updateLatest() {
		timeItem1.setLabel("overall", latestTimes.get("overall"));
		timeItem1.setLabel("download", latestTimes.get("download"));
		timeItem1.setLabel("reference", latestTimes.get("reference"));
		timeItem1.setLabel("library", latestTimes.get("library"));
		timeItem1.setLabel("rgb", latestTimes.get("rgb"));
		timeItem1.setLabel("mosaic", latestTimes.get("mosaic"));
	}
	
	private void shiftTimes() {
		timeItem6.setLabel(timeItem5);
		timeItem5.setLabel(timeItem4);
		timeItem4.setLabel(timeItem3);
		timeItem3.setLabel(timeItem2);
		timeItem2.setLabel(timeItem1);
		latestTimes.put("overall", "InProgress");
		if (downState) {
			latestTimes.put("download", "InProgress");
		} else {
			latestTimes.put("download", "Skipped");
		}
		latestTimes.put("reference", "InProgress");
		latestTimes.put("library", "InProgress");
		latestTimes.put("rgb", "InProgress");
		latestTimes.put("mosaic", "InProgress");
	}
	
	@Override
	public void setTime(String key, long value) {
		synchronized(latestTimes){
			latestTimes.put(key, Long.toString(value));
		}
	}
	
	public int printTime(long startTime) {
		System.out.println("Time to finish: " + (System.currentTimeMillis() - startTime));
		return 1;
	}
	
	public void handleImageTooBig() {
		mosaicBuildLabel.setText("IMAGE TOO BIG, CHOOSE SMALLER SCALE OR BIGGER CELL SIZE");
	}
}
