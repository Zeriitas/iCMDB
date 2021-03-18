package application;

import java.io.File;
import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class DatabaseController {

	@FXML
	private AnchorPane anchorPaneID;

	public DatabaseController() {

	}

	//Wordt aangeroepen vanuit de main methode wanneer we beginnen 
	public void draw() {
		try {
			FXMLLoader fxml = new FXMLLoader(getClass().getResource("DatabaseSelector.fxml"));
			Parent root = (Parent) fxml.load();
			DatabaseController dbcontroller = fxml.getController();
			Stage stage = new Stage();
			stage.setTitle("Create or Select Database");
			stage.setScene(new Scene(root));
			stage.showAndWait();
		} catch (Exception e) {
			// TODO Auto-generated catch block
		
		
		}
	}

	public void initialize() {

	}

	/*
	 * Button CreateNew Database clicked wordt dit aangeroepen
	 * We maken chooser object,(een scherm op items te kiezen op de computer)
	 * We roepen showSaveDialog, en nemen we de filepath op in de file object
	 * Dan maken we een nieuwe file met createNewFile();
	 */
	@FXML
	private void createNewClicked() {
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Creating a new database file!");
		// De extension is de save type, we hebben de 2e parameter verwijderd anders
		// krijgen we dubbel .txt
		chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("dbfile", "*.db"));
		chooser.setInitialFileName("myDatabase"); // Specifieke naam instellen bij de txt file die we opslaan
		File file = chooser.showSaveDialog(anchorPaneID.getScene().getWindow()); // Main window niet beschikbaar maken
		if (file != null) {
		try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block

		}
		 // Het path is de gekozen direction. Als deze niet leeg is sturen we het door
			try {
				SQLData.getInstance().createDB(file.getPath());
				
			} catch (Exception e) {
			}
			finally {Stage stage = (Stage)anchorPaneID.getScene().getWindow();
			stage.close();
		}
		} else {
			//do nothing surpress nullpointer error, user canceld.
			
		}
		
		
	}
	/*
	 * Button CreateNew Database clicked wordt dit aangeroepen
	 * We maken chooser object,(een scherm op items te kiezen op de computer)
	 * We roepen showOpenDialog, en we kiezen een object
	 * Dan maken we een nieuwe file met createNewFile();
	 */
	@FXML
	private void selectExistingClicked() {
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Selecting a existing database file!");
		chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("dbfile", "*.db"));
		File file = chooser.showOpenDialog(anchorPaneID.getScene().getWindow());
		if (file!=null) {
			
		try {
			SQLData.getInstance().selectDB(file.getPath());		
		} catch (Exception e) {
			
		}
		finally {Stage stage = (Stage)anchorPaneID.getScene().getWindow();
		stage.close();
		}
		} else {
			//do nothing, user canceled
		}
	}
}
