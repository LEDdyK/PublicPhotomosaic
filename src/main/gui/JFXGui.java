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
	
	private ProgressBar imgLibProgress;
	private ProgressBar downProgress;
	private ProgressBar tinSubProgress;
	
	private Button saveImageButton;
	
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
		Button runComp = new Button("Run Computation");
			//set position
		runComp.setLayoutX(545);
		runComp.setLayoutY(400);
		
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
		display.setFitWidth(500);
		display.setFitHeight(500);
		display.setPreserveRatio(true);//TODO revert
			//set position and border
		HBox hbox = new HBox();
		hbox.setBorder(new Border(new BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		hbox.setLayoutX(15);
		hbox.setLayoutY(135);
		hbox.setPrefWidth(500);
		hbox.setPrefHeight(500);
		
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
		Label paraGCLabel = new Label("Run tasks in Parallel");
		JFXToggle paraGC = new JFXToggle();
		Pane paraGCBox = paraGC.makeToggle(34);
			//set position
		paraGCLabel.setLayoutX(20);
		paraGCLabel.setLayoutY(673);
		paraGCBox.setLayoutX(480);
		paraGCBox.setLayoutY(673);
		
//		download progress bar
		downProgress = new ProgressBar();
		downProgress.setLayoutX(545);
		downProgress.setLayoutY(15);
		downProgress.setProgress(0F);
		
//		image library progress bar
		imgLibProgress = new ProgressBar();
		imgLibProgress.setLayoutX(545);
		imgLibProgress.setLayoutY(55);
		imgLibProgress.setProgress(0F);
		
//		imageTinder and substitution progress bar
		tinSubProgress = new ProgressBar();
		tinSubProgress.setLayoutX(545);
		tinSubProgress.setLayoutY(95);
		tinSubProgress.setProgress(0F);
		
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
						hbox.getChildren().add(display);
					} catch (FileNotFoundException e) {
						//TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		
//		run computations
		runComp.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				imgLibProgress.setProgress(0);
				downProgress.setProgress(0);
				tinSubProgress.setProgress(0);
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
		root.getChildren().add(runComp);
		//add save image to button
		root.getChildren().add(saveImageButton);
		//add download progress bar
		root.getChildren().add(downProgress);
		//add image library progress bar
		root.getChildren().add(imgLibProgress);
		//add tinder/substitution porgress bar
		root.getChildren().add(tinSubProgress);
		//add image output display to GUI
		root.getChildren().add(hboxOut);
		
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
			imageDownloader = new ImageDownloader(downProgress);
			imageLibrary = new ImageLibrary(imgLibProgress);
			
			if (firstStartup) {
				imageGrid = new ImageGrid(null);
			} else {
				imageGrid = new ImageGrid(ImageIO.read(new File(refPath.getText())));
			}
			
			rgbLibrary = new RGBLibrary();
			mosaicBuilder = new MosaicBuilder(tinSubProgress);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void runComputations() {
		@Future
		int imageDownloadTask = imageDownloader.downloadRecentImages(Integer.parseInt(threadCount.getText()));

		@Future(depends="imageDownloadTask")
		Map<String, BufferedImage> imageLibraryResult = imageLibrary.readDirectory("photos", Double.parseDouble(libScale.getText()), Integer.parseInt(threadCount.getText()));	
					
		@Future()
		int imageGridTask = imageGrid.createGrid(false, Integer.parseInt(gridWidth.getText()), Integer.parseInt(gridHeight.getText()));	
		
		@Future()
		Map<String, AvgRGB> rgbList = rgbLibrary.calculateRGB(imageLibraryResult);
					
		@Future()
		int mosaicBuild = mosaicBuilder.createMosaic(imageLibrary, rgbList, imageGrid, Integer.parseInt(threadCount.getText()), 'R');
		
		@Gui(notifiedBy="mosaicBuild")
		Void guiUpdate = mosaicBuilder.displayOnGUI(dispOut, saveImageButton);
		
	}
	
	private void runComputationsSequentially() {
		@Future
		int imageDownloadTask = imageDownloader.downloadRecentImages(1);

		@Future(depends="imageDownloadTask")
		Map<String, BufferedImage> imageLibraryResult = imageLibrary.readDirectory("photos", Double.parseDouble(libScale.getText()), 1);	
					
		@Future(depends="imageLibraryResult")
		int imageGridTask = imageGrid.createGrid(false, Integer.parseInt(gridWidth.getText()), Integer.parseInt(gridHeight.getText()));	
		
		@Future(depends="imageGridTask")
		Map<String, AvgRGB> rgbList = rgbLibrary.calculateRGB(imageLibraryResult);
					
		@Future(depends="rgbList")
		int mosaicBuild = mosaicBuilder.createMosaic(imageLibrary, rgbList, imageGrid, 1, 'R');
		
		@Gui(notifiedBy="mosaicBuild")
		Void guiUpdate = mosaicBuilder.displayOnGUI(dispOut, saveImageButton);
	}
}
