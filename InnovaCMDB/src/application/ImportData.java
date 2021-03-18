package application;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ImportData {

	@FXML
	private AnchorPane anchorPaneID;

	@FXML
	private RadioButton deviceTypeID;

	@FXML
	private RadioButton userTypeID;

	@FXML
	private Button deviceTemplateButtonID;

	@FXML
	private Button userTemplateButtonID;

	@FXML
	private ToggleGroup importTypeRadioButtonID;

	public ImportData() {

	}

	/*
	 * De user kiest welke type data hij wil importeren.
	 * 
	 * User template data. We beginnen met een scanner.nextLine dit gaat naar de
	 * eerste rij toe. We voeren elke x scanner.NextLine uit omdat we alle namen in
	 * een nieuwe /n regel hebben in de format.
	 * 
	 * Device is wat pittiger
	 */
	@FXML
	private void okButtonClicked() throws Exception {
		if (userTypeID.isSelected() == true) {
			FileChooser chooser = new FileChooser();
			chooser.setTitle("Opening user template!");
			chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text", "*.txt"));
			File file = chooser.showOpenDialog(anchorPaneID.getScene().getWindow()); // Main window niet beschikbaar			
			if(file==null) {	
			} else if (file!=null) {
				Scanner scanner = new Scanner(new File(file.getPath()));//scanner bestands path geven als leesinput
			while (scanner.hasNextLine()) {
				SQLData.getInstance().createUser(scanner.nextLine().toUpperCase().trim());
			
			}
			Alert alert = new Alert(AlertType.INFORMATION,
					"Done! Please refer to the imported data in the application. (Remember, duplicate data will not be imported)");
			alert.getDialogPane().getStylesheets().add(getClass().getResource("myStyle.css").toExternalForm());
			alert.getDialogPane().getStyleClass().add("myStyle");
			alert.show();
			cancelButtonClicked();// Closing window, process is done.
			}
		} else if (deviceTypeID.isSelected() == true) {
			/*
			 * Eerst openen we de filechooser en kiezen we de import file. Hierna checken we
			 * de eerste line. Is er wat ? ja dan checken we welke type het is. We willen
			 * nog niet de volgende line skippen Maar met hasnext checken. Als dit correct
			 * is en aansluit op "COMPUTER" dan slaan we de type over met next. De rest vult de informatie in en aan.
			 * 
			 * Let op de if statement binnen om te checken of er een username is aangegeven of 0
			 * Als er 0 is aangegeven dan wordt nextInt aangeroepen en wordt er geen user bij de device declared
			 * Als er een username is gegeven dan checkt de findUser methode met de String door de userlist via een for loop.
			 * Zoniet? Dan roepen we de createUser methode aan binnen findUser.
			 * De findUser methode roept zichzelf dan weer aan return de number van de user die we net hebben aangemaakt.
			 * 
			 * De rest is scanner.next om regels te skippen etc.
			 */
			FileChooser chooser = new FileChooser();
			chooser.setTitle("Opening device template!");
			// De extension is de save type, we hebben de 2e parameter verwijderd anders
			// krijgen we dubbel .txt
			chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text", "*.txt"));
			
			File file = chooser.showOpenDialog(anchorPaneID.getScene().getWindow()); //Mainwindow niet beschikbaar maken
			if(file==null) { //Als de user canceld krijgen we een nullpointer error. Dit willen we voorkomen
				//Dus we doen niks, en overwrite de error
			} else if (file!=null) {
			Scanner scanner = new Scanner(new File(file.getPath()));// Scanner de geselecteerde importdata path lezen.
			
			try {
				while (scanner.hasNextLine()) {
					if (scanner.hasNext("COMPUTER")) { //Eerste regel is COMPUTER.
						/*
						 * Test purposes om te zien waar de scanner was. Het was heel verwarrend.!
						 */
						scanner.next(); // SKIPPING THE TYPE want dat is confirmed door de if.
						Device device = new Computer();
						device.setDeviceName(scanner.next().toUpperCase().trim());
						device.setModelName(scanner.next());
						device.setSerialNumber(scanner.next());
						device.setStoragestatus(scanner.next());
						device.setNotes(scanner.next());
						String dateAndTime = scanner.next().replaceAll("/", "-");// Excel maakt er 01/01/2000 vandaar de methode om het te vervangen
						device.setWarrantyDate(dateAndTime);
						device.setSoftware(scanner.next());
						if (scanner.hasNextInt() == true) {
							device.setUserID(scanner.nextInt());
						} else if (scanner.hasNext()) {
							device.setUserID(SQLData.getInstance().findUser(scanner.next())); 
						}
						((Computer) device).setLocalOffice(scanner.next());
						scanner.next();
						scanner.nextLong();
						scanner.nextLine();
						SQLData.getInstance().createComputer((Computer) device);
						// Laptop
					} else if (scanner.hasNext("LAPTOP")) {
						scanner.next(); // SKIPPING THE TYPE.
						Device device = new Laptop();
						device.setDeviceName(scanner.next().toUpperCase().trim());
						device.setModelName(scanner.next());
						device.setSerialNumber(scanner.next());
						device.setStoragestatus(scanner.next());
						device.setNotes(scanner.next());
						String dateAndTime = scanner.next().replaceAll("/", "-");// Excel maakt er 01/01/2000 vandaar de methode om het te vervangen
						device.setWarrantyDate(dateAndTime);
						device.setSoftware(scanner.next());
						if (scanner.hasNextInt() == true) {
							device.setUserID(scanner.nextInt());
						} else if (scanner.hasNext()) {
							device.setUserID(SQLData.getInstance().findUser(scanner.next()));
							//trying to look for the user, if it doesnt exist in the app.
							//The user will get a popup asking if they want to create the user.
						}
						((Laptop) device).setLocalOffice(scanner.next());
						((Laptop) device).setManufacturer(scanner.next());
						scanner.nextLong();
						scanner.nextLine();
						SQLData.getInstance().createLaptop((Laptop) device);

						// Phone
					} else if (scanner.hasNext("PHONE")) {
						scanner.next(); // SKIPPING THE TYPE
						Device device = new Phone();
						device.setDeviceName(scanner.next().toUpperCase().trim());
						device.setModelName(scanner.next());
						device.setSerialNumber(scanner.next());
						device.setStoragestatus(scanner.next());
						device.setNotes(scanner.next());
						String dateAndTime = scanner.next().replaceAll("/", "-"); // Excel maakt er 01/01/2000 vandaar de methode om het te vervangen
						device.setWarrantyDate(dateAndTime);
						device.setSoftware(scanner.next());
						if (scanner.hasNextInt() == true) {
							device.setUserID(scanner.nextInt());
						} else if (scanner.hasNext()) {
							device.setUserID(SQLData.getInstance().findUser(scanner.next()));
							//trying to look for the user, if it doesnt exist in the app.
							//The user will get a popup asking if they want to create the user.
						}
						scanner.next();
						scanner.next();
						((Phone) device).setNumber(scanner.nextLong());
						scanner.nextLine();
						SQLData.getInstance().createPhone((Phone) device);

					}
				}
				scanner.close();
			} catch (Exception e) {
				scanner.close();
			}
			// Showing window that the process is done
			Alert alert = new Alert(AlertType.INFORMATION,
					"Done! Please refer to the imported data in the application. (Remember, duplicate data will not be imported)");
			alert.getDialogPane().getStylesheets().add(getClass().getResource("myStyle.css").toExternalForm());
			alert.getDialogPane().getStyleClass().add("myStyle");
			alert.show();
			cancelButtonClicked();// Closing window, process is done.
			}
		}

		

	}

	@FXML
	private void cancelButtonClicked() {
		Stage stage = (Stage) anchorPaneID.getScene().getWindow();
		stage.close();
	}

	/*
	 * Met de initialize zetten we alvast de template icons van button nodes.
	 */
	@FXML
	public void initialize() {
		Image image = new Image("https://i.imgur.com/7VvXFky.png");
		ImageView imageView = new ImageView(image);
		deviceTemplateButtonID.setGraphic(imageView); // fx:id van button device template.
		Image image2 = new Image("https://i.imgur.com/7VvXFky.png");
		ImageView imageView2 = new ImageView(image2);
		userTemplateButtonID.setGraphic(imageView2);

	}

	/*
	 * deviceTemplateButtonClicked is linked aan de button die een template download
	 * Je kiest waar je de template download en zet hem dan daar neer.
	 * 
	 */
	@FXML
	private void deviceTemplateButtonClicked() {
		try {
			FileChooser chooser = new FileChooser();
			chooser.setTitle("Save Application import data format, Import into Excel and seperate with comma!");
			// De extension is de save type, we hebben de 2e parameter verwijderd anders
			// krijgen we dubbel .txt
			chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text", "*"));
			chooser.setInitialFileName("deviceimportformat.txt"); // Specifieke naam instellen bij de txt file die we
																	// opslaan
			File file = chooser.showSaveDialog(anchorPaneID.getScene().getWindow()); // Main window niet beschikbaar
																						// maken
			if (file.getPath() != null) {
				System.out.println(file.getPath());

				File myfile = new File(file.getPath());
				// We maken de nieuwe file aan op de locatie met die naam
				myfile.createNewFile();
				// We gaan schrijven in de file.
				FileWriter myWriter = new FileWriter(file.getPath());
				// De write methode schrijven we alvast de columnnames.
				myWriter.write(
						"Device Type, Device Name, Model Name, Serial Number, Storage status, Notes, Warranty Date,Operating Software, UserID, Local Office, Manufacturer, Phone Number, < DELETE THIS ROW!!! DO NOT IMPORT WITH HEADER "
								+ "NULL MEANS NOT EMPTY. NOT NULL MEANS NOT EMPTY. INSERT . or something atleast. Otherwise the import will not work and the app will crash. \n"
								+ "COMPUTER/LAPTOP/PHONE,notnull,notnull,notnull,USED/STORED/DEFECT,notnull,dd-mm-yyyy if unknown input 01-01-2000,notnull,EXACT NAME OF USER OR 0,notnull,notnull,notnull, DELETE THIS ROW ASWELL \n"
								+ "COMPUTER,	Device-00002,	.,	.,	USED,	.,	10/12/2020,	.,	A.DAM,	.,	.,	55353,\n"
								+ "LAPTOP,	LAPTOP02323,	.,	.,	STORED,	.,	01/01/2000,	.,	J.APPLESEED,	.,	.,	535353,\n"
								+ "PHONE,	IPHONE5454,	.,	.,	DEFECT,	.,	03/03/2010,	.,	0,	.,	.,	53535353,		\n"
								+ "");
				myWriter.flush();
				myWriter.close();
				Alert alert = new Alert(AlertType.INFORMATION,
						"Step 1.Import the .TXT file into Excel/Google Sheets and seperate columns with comma! \n"
								+ "Step 2. Fill in the template and DELETE the upperheader row \n"
								+ "Step 3. Save the Excel/Google Sheet as a TXT with the seperation as tabs, option is given when in Excel with Save as... (There should be no comma`s, just tabs between username.)  \n"
								+ "Step 4. Import the TXT file into the Application. \n"
								+ "STRONG ADVICE TO BACKUP DE DATABASE FILE !",ButtonType.OK);
				/*
				 * We maken een image object aan en zetten het bestand erin. Daarna voegen we de
				 * image toe aan de alert via de setGraphic methode
				 */
				Image image = new Image(
						"https://cdn.extendoffice.com/images/stories/doc-excel/split-text/doc-split-text-to-rows-columns-4.png");
				ImageView imageView = new ImageView(image);
				alert.setGraphic(imageView);

				alert.getDialogPane().getStylesheets().add(getClass().getResource("myStyle.css").toExternalForm());
				alert.getDialogPane().getStyleClass().add("myStyle");
				alert.show();
			} else {
				
			}

		} catch (Exception e) {
			// cancelled save dialog.
		}

	}

	/*
	 * userTemplateButtonClicked is linked aan de button die een template download
	 * Je kiest waar je de template download en zet hem dan daar neer. we creeren
	 * eerst een nieuwe bestand met File = new File(en de gekozen path met de
	 * Filechooser class); Hierna schrijven we in de bestaande file de template uit.
	 */
	@FXML
	private void userTemplateButtonClicked() {
		try {
			FileChooser chooser = new FileChooser();
			chooser.setTitle("Save Application import data format, Import into Excel and seperate with comma!");
			// De extension is de save type, we hebben de 2e parameter verwijderd anders
			// krijgen we dubbel .txt
			chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text", "*"));
			chooser.setInitialFileName("importformat.txt"); // Specifieke naam instellen bij de txt file die we opslaan
			File file = chooser.showSaveDialog(anchorPaneID.getScene().getWindow()); // Main window niet beschikbaar
																						// maken
			if (file.getPath() != null) {
				System.out.println(file.getPath());

				File myfile = new File(file.getPath());
				// We maken de nieuwe file aan op de locatie met die naam
				myfile.createNewFile();
				// We gaan schrijven in de file.
				FileWriter myWriter = new FileWriter(file.getPath());
				// De write methode schrijven we alvast de columnnames.
				myWriter.write("UserID,   DELETE THIS HEADER ROW COMPLETELY . ONLY FILL IN THE FIRST FIRST COLUMN LIKE SHOWN!!! \n"
						+ "j.appleseed, \n" + "a.dam,\n" + "g.norman,\n" + "r.ravenclaw,\n");
				myWriter.flush();
				myWriter.close();
				Alert alert = new Alert(AlertType.INFORMATION,
						"Step 1.Import the .TXT file into Excel/Google Sheets and seperate columns with comma! \n"
								+ "Step 2. Fill in the template and DELETE the upperheader row \n"
								+ "Step 3. Save the Excel/Google Sheet as a TXT with the seperation as tabs, option is given when in Excel with Save as... (There should be no comma`s, just tabs between username.)  \n"
								+ "Step 4. Import the TXT file into the Application. \n"
								+ "STRONG ADVICE TO BACKUP DE DATABASE FILE !");
				/*
				 * We maken een image object aan en zetten het bestand erin. Daarna voegen we de
				 * image toe aan de alert via de setGraphic methode
				 */
				Image image = new Image(
						"https://cdn.extendoffice.com/images/stories/doc-excel/split-text/doc-split-text-to-rows-columns-4.png");
				ImageView imageView = new ImageView(image);
				alert.setGraphic(imageView);

				alert.getDialogPane().getStylesheets().add(getClass().getResource("myStyle.css").toExternalForm());
				alert.getDialogPane().getStyleClass().add("myStyle");
				alert.show();
			} else {
			}
			

		} catch (Exception e) {
			// cancelled save dialog.
		}

	}

//	if (!listUsersID.getSelectionModel().isEmpty()) {		
//		try {
//			User selecteduser = listUsersID.getSelectionModel().getSelectedItem();
//			Dialog<ButtonType> dialog = new Dialog<>();
//			dialog.setTitle("Edit existing user");
//			dialog.setHeaderText(" The selected user you are editing " + selecteduser.getName());
//			dialog.initOwner(borderPaneID.getScene().getWindow());
//			FXMLLoader fxmlloader = new FXMLLoader();
//			fxmlloader.setLocation(getClass().getResource("EditUserDialog.fxml"));
//			try {
//				dialog.getDialogPane().setContent(fxmlloader.load());
//			} catch (IOException e) {
//				System.out.println(e.getMessage());
//				e.printStackTrace();
//			}
//			dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
//			dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
//			dialog.getDialogPane().getStylesheets().add(getClass().getResource("myStyle.css").toExternalForm());
//			dialog.getDialogPane().getStyleClass().add("myStyle");
//			Optional<ButtonType> result = dialog.showAndWait();
//			if (result.isPresent() && result.get() == ButtonType.OK) {
//				DialogController controller = fxmlloader.getController();
//				controller.processResults(selecteduser);
//			}
//
//		} catch (Exception e) {

//	private void importSelectedData(String path) {
////		FileChooser chooser = new FileChooser();
////		chooser.setTitle("Selecting existing .TXT data file !");
////		chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("txt", "*.txt"));
////		File file = chooser.showOpenDialog(borderPaneID.getScene().getWindow());
////		if (file.getPath()!=null) {
////			ImportData importdata = new ImportData();
////			System.out.println(file.getPath());
////			importdata.importSelectedData(file.getPath());
//	}

}
