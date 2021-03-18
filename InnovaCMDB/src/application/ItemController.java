package application;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.Comparator;
import java.util.Optional;

import javax.management.ObjectInstance;
import javax.swing.ImageIcon;
import javax.swing.event.ChangeListener;

import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ItemController {

	@FXML
	private AnchorPane itemWindowAnchorPaneID;

	@FXML
	private ToggleGroup deviceTypeRadioButtonID;

	@FXML
	private RadioButton deviceTypeComputerID;

	@FXML
	private RadioButton deviceTypeLaptopID;

	@FXML
	private RadioButton deviceTypePhoneID;

	@FXML
	private TextField deviceNameID;

	@FXML
	private TextField modelNameID;

	@FXML
	private TextField serialNumberID;

	@FXML
	private ToggleGroup storageTypeRadioButtonID;

	@FXML
	private RadioButton radioButtonInUseID;

	@FXML
	private RadioButton radioButtonInStorageID;

	@FXML
	private RadioButton radioButtonDefectID;

	@FXML
	private DatePicker warrantyDateID;

	@FXML
	private TextArea osAndKeyID;

	@FXML
	private ListView<User> listViewUsersID;

	@FXML
	private TextArea officeID;

	@FXML
	private TextField manufacturerID;

	@FXML
	private TextField phoneNumberID;

	@FXML
	private TextArea notesID;

	@FXML
	private Button addUserButtonID;

	@FXML
	private Button addButtonID;
	
	@FXML
	private Button saveChangesButtonID;

	private String storageStatus;
	
	
	
	//Gebruiken we om de gegevens te bewaren van een reference 
	//waarmee we bij SQLdata de verwijzing kunnen maken welke we willen veranderen
	private Device deviceObject;

	public ItemController() {

	}

	@FXML
	public void initialize() {
		SortedList<User> sortedUserList = new SortedList<User>(SQLData.getInstance().getUsers()
				, new Comparator<User>() {
			@Override
			public int compare(User o1, User o2) {
				return o1.getName().compareTo(o2.getName());
			}
			
		});
		Image image = new Image("https://i.imgur.com/B9r5t4W.png");
		ImageView imageView = new ImageView(image);
		addUserButtonID.setGraphic(imageView); //fx:id van button device template.
		listViewUsersID.setItems(sortedUserList);
		phoneNumberID.setDisable(true);
		manufacturerID.setDisable(true);
		listViewUsersID.setDisable(true);
		try {
		addButtonID.setDisable(true);
	} catch (Exception e) {
		//Doen we omdat de AddButton er niet is in de Edit window mode.
	}
	}

	/*
	 * Hierbij hebben we een methode gemaakt, onAction event van de 3 radiotypes
	 * Wanneer een wordt geselecteerd wordt de instantievariable storageStatus
	 * gewijzigt. Zodat de opslagstatus van de machine wordt gewijzigd. We gebruiken
	 * de instantievariable En roepen de set method later aan bij createDevice. We
	 * disable de listview voor users bij DEFECT EN STORED
	 */
	@FXML
	private void storageTypeSelected(ActionEvent e) {
	
		if (e.getSource().equals(radioButtonDefectID)) {
			storageStatus = "DEFECT";
			listViewUsersID.setDisable(true);
			listViewUsersID.getSelectionModel().clearSelection(); // Clear selection anders wordt een user alsnog toegewezen
			try  { addButtonID.setDisable(false);
			} catch(Exception i) {//Doen we omdat de AddButton er niet is in de Edit window mode.
			}
		} else if (e.getSource().equals(radioButtonInStorageID)) {
			storageStatus = "STORED";
			listViewUsersID.setDisable(true);
			listViewUsersID.getSelectionModel().clearSelection(); // Clear selection anders wordt een user alsnog
			try  { addButtonID.setDisable(false);
			} catch(Exception i) {//Doen we omdat de AddButton er niet is in de Edit window mode.
			}									
		} else if (e.getSource().equals(radioButtonInUseID)) {
			storageStatus = "USED";
			listViewUsersID.setDisable(false);
			try  { addButtonID.setDisable(false);
			} catch(Exception i) {//Doen we omdat de AddButton er niet is in de Edit window mode.
			}

		
		}	
		
	}

	private RadioButton storageTypeSelected(String storagetype) {
		switch (storagetype) {
		case "DEFECT":
			storageStatus = "DEFECT";
			return radioButtonDefectID;
		case "STORED":
			storageStatus = "STORED";
			return radioButtonInStorageID;
		case "USED":
			storageStatus = "USED";
			listViewUsersID.setDisable(false);
			return radioButtonInUseID;
		}
		return null;
	}

	@FXML
	private void deviceTypeSelected(ActionEvent e) {
		if (e.getSource().equals(deviceTypeComputerID)) {
			phoneNumberID.setDisable(true);
			manufacturerID.setDisable(true);
			officeID.setDisable(false);
		} else if (e.getSource().equals(deviceTypeLaptopID)) {
			manufacturerID.setDisable(false);
			phoneNumberID.setDisable(true);
			officeID.setDisable(false);
		} else if (e.getSource().equals(deviceTypePhoneID)) {
			phoneNumberID.setDisable(false);
			manufacturerID.setDisable(false);
			officeID.setDisable(true);
		}
	}
	
	private void deviceTypeSelected(String e) {
		switch(e.toUpperCase()) {
		case "COMPUTER":
			phoneNumberID.setDisable(true);
			manufacturerID.setDisable(true);
			officeID.setDisable(false);
			break;
		case "LAPTOP":
			manufacturerID.setDisable(false);
			phoneNumberID.setDisable(true);
			officeID.setDisable(false);
			break;
		case "PHONE":
			phoneNumberID.setDisable(false);
			manufacturerID.setDisable(false);
			officeID.setDisable(true);
		break;
		}
	}

	@FXML
	private void createDevice() {
		if (deviceTypeRadioButtonID.getSelectedToggle() == deviceTypeComputerID) {
			Computer device = new Computer();
			device.setDeviceName(deviceNameID.getText().trim());
			device.setModelName(modelNameID.getText());
			device.setSerialNumber(serialNumberID.getText());
			device.setStoragestatus(storageStatus);
			if (warrantyDateID.getValue() == null) {// Als er niks is ingevuld dan wordt de datum automatisch 01012000
				device.setWarrantyDate("01-01-2000");
			} else {
				device.setWarrantyDate(warrantyDateID.getValue());
			}
			device.setSoftware(osAndKeyID.getText());
			try { // Als er geen user is geselecteerd door de clearSelection methode boven aan in de code, dan
					// wordt 0 gegeven als ID.
					// IDS beginnen bij 1.
				device.setUserID(listViewUsersID.getSelectionModel().getSelectedItem().get_id());
			} catch (Exception e) {
				device.setUserID(0);
			}
			device.setLocalOffice(officeID.getText());
			device.setNotes(notesID.getText());
			SQLData.getInstance().createComputer(device);
			// Sturen de object door naar SQLData om daar verder de device aan te maken
			// SQL Data voert querys uit etc en check of t wel kan toegevoegd worden.
			Stage stage = (Stage) itemWindowAnchorPaneID.getScene().getWindow();// We sluiten het scherm
			stage.close();
		} else if (deviceTypeRadioButtonID.getSelectedToggle() == deviceTypeLaptopID) {
			Laptop device = new Laptop();
			device.setDeviceName(deviceNameID.getText().trim());
			device.setModelName(modelNameID.getText());
			device.setSerialNumber(serialNumberID.getText());
			device.setStoragestatus(storageStatus);
			if (warrantyDateID.getValue() == null) {// Als er niks is ingevuld dan wordt de datum automatisch 01012000
				device.setWarrantyDate("01-01-2000");
			} else {
				device.setWarrantyDate(warrantyDateID.getValue());
			}
			device.setSoftware(osAndKeyID.getText());
			try { 
				device.setUserID(listViewUsersID.getSelectionModel().getSelectedItem().get_id());
			} catch (Exception e) {
				device.setUserID(0);
			}
			device.setLocalOffice(officeID.getText());
			device.setManufacturer(manufacturerID.getText());
			device.setNotes(notesID.getText());
			SQLData.getInstance().createLaptop(device);
			Stage stage = (Stage) itemWindowAnchorPaneID.getScene().getWindow();// We sluiten het scherm
			stage.close();

		} else if (deviceTypeRadioButtonID.getSelectedToggle() == deviceTypePhoneID) {
			Phone device = new Phone();
			device.setDeviceName(deviceNameID.getText().trim());
			device.setModelName(modelNameID.getText());
			device.setSerialNumber(serialNumberID.getText());
			device.setStoragestatus(storageStatus);
			if (warrantyDateID.getValue() == null) { // Als er niks is ingevuld dan wordt de datum automatisch 01012000
				device.setWarrantyDate("01-01-2000"); 
			} else {
				device.setWarrantyDate(warrantyDateID.getValue());
			}
			device.setSoftware(osAndKeyID.getText());
			try {
				device.setUserID(listViewUsersID.getSelectionModel().getSelectedItem().get_id());
			} catch (Exception e) {
				device.setUserID(0);
			}
			device.setManufacturer(manufacturerID.getText());
			device.setNumber(phoneNumberID.getText());
			device.setNotes(notesID.getText());
		
			SQLData.getInstance().createPhone(device);
			Stage stage = (Stage) itemWindowAnchorPaneID.getScene().getWindow(); // We sluiten het scherm
			stage.close();
		}
	}

	/*
	 * Edit button methods
	 */
	public void editButtonClicked(Computer device) {
		try {
			deviceTypeRadioButtonID.selectToggle(deviceTypeComputerID);
			deviceTypeSelected("Computer");
			officeID.setText(device.getLocalOffice());
			deviceNameID.setText(device.getDeviceName());
			modelNameID.setText(device.getModelName());
			serialNumberID.setText(device.getSerialNumber());
			storageTypeRadioButtonID.selectToggle(storageTypeSelected(device.getStoragestatus()));
			//Roept een methode storageTypeSelected aan die een RadioButton terugkeert in een switch statement,
			//Er wordt gekeken naar de String waarde (.getStorageStatus) 
			//Bepaalde nodes worden dan ook gelijk aangezet weer.
			warrantyDateID.setValue(device.getWarrantyDateLocalDate());
			osAndKeyID.setText(device.getSoftware());
			listViewUsersID.getSelectionModel().select(SQLData.getInstance().findUser(device.getUserID()));
			notesID.setText(device.getNotes());
			deviceObject = device; //Gemaakt om referentie te behouden zodat we die naar SQLdata kunnen sturen.
		} catch (Exception e) {

		}
	}

	public void editButtonClicked(Laptop device) {
		try {
			deviceTypeRadioButtonID.selectToggle(deviceTypeLaptopID);
			deviceTypeSelected("Laptop");
			deviceNameID.setText(device.getDeviceName());
			modelNameID.setText(device.getModelName());
			serialNumberID.setText(device.getSerialNumber());
			storageTypeRadioButtonID.selectToggle(storageTypeSelected(device.getStoragestatus()));
			warrantyDateID.setValue(device.getWarrantyDateLocalDate());
			osAndKeyID.setText(device.getSoftware());
			listViewUsersID.getSelectionModel().select(SQLData.getInstance().findUser(device.getUserID()));
			notesID.setText(device.getNotes());
			officeID.setText(device.getLocalOffice());
			manufacturerID.setText(device.getManufacturer());
			deviceObject = device;

		} catch (Exception e) {
		}
	}

	public void editButtonClicked(Phone device) {
		try {
			deviceTypeSelected("Phone");
			deviceTypeRadioButtonID.selectToggle(deviceTypePhoneID);
			deviceNameID.setText(device.getDeviceName());
			modelNameID.setText(device.getModelName());
			serialNumberID.setText(device.getSerialNumber());
			storageTypeRadioButtonID.selectToggle(storageTypeSelected(device.getStoragestatus()));
			warrantyDateID.setValue(device.getWarrantyDateLocalDate());
			osAndKeyID.setText(device.getSoftware());
			listViewUsersID.getSelectionModel().select(SQLData.getInstance().findUser(device.getUserID()));
			notesID.setText(device.getNotes());
			phoneNumberID.setText(device.getNumber());
			manufacturerID.setText(device.getManufacturer());
			deviceObject=device;
		} catch (Exception e) {
		}
	}
	
	@FXML
	private void saveChangesClicked() {
		if (deviceTypeRadioButtonID.getSelectedToggle() == deviceTypeComputerID) {
			Computer device = new Computer();
			device.setDeviceName(deviceNameID.getText().trim());
			device.setModelName(modelNameID.getText());
			device.setSerialNumber(serialNumberID.getText());
			device.setStoragestatus(storageStatus); //Variable storageStatus wordt veranderd aan de hand van de togglebutton die is gekozen
			//De variable is een string die continue veranderd.
			
			/*De volgende code is commented out omdat de warrantyDateID uneditable is
			 * En al een standaard waarde heeft gekregen vanuit onze add Device methode.
			 */
//			if (warrantyDateID.getValue() == null) {// Als er niks is ingevuld dan wordt de datum automatisch 01012000
//				device.setWarrantyDate("01-01-2000");
//			} else {
			device.setWarrantyDate(warrantyDateID.getValue());
			device.setSoftware(osAndKeyID.getText());
			try { // Als er geen user is geselecteerd door de clearSelection methode boven dan
					// wordt 0 gegeven als ID. (null/niemand)
					// IDS beginnen bij 1. 
				device.setUserID(listViewUsersID.getSelectionModel().getSelectedItem().get_id());
			} catch (Exception e) {
				device.setUserID(0);
			}
			device.setLocalOffice(officeID.getText());
			device.setNotes(notesID.getText());
			SQLData.getInstance().editSelectedDevice((Computer)deviceObject, device);
			closeStage();
		} else if (deviceTypeRadioButtonID.getSelectedToggle() == deviceTypeLaptopID) {
			Laptop device = new Laptop();
			device.setDeviceName(deviceNameID.getText().trim());
			device.setModelName(modelNameID.getText());
			device.setSerialNumber(serialNumberID.getText());
			device.setStoragestatus(storageStatus);
			device.setWarrantyDate(warrantyDateID.getValue());
			device.setSoftware(osAndKeyID.getText());
			try { 
				device.setUserID(listViewUsersID.getSelectionModel().getSelectedItem().get_id());
			} catch (Exception e) {
				device.setUserID(0);
			}
			device.setLocalOffice(officeID.getText());
			device.setManufacturer(manufacturerID.getText());
			device.setNotes(notesID.getText());
			SQLData.getInstance().editSelectedDevice((Laptop)deviceObject, device);
			closeStage();
		} else if (deviceTypeRadioButtonID.getSelectedToggle() == deviceTypePhoneID) {
			Phone device = new Phone();
			device.setDeviceName(deviceNameID.getText().trim());
			device.setModelName(modelNameID.getText());
			device.setSerialNumber(serialNumberID.getText());
			device.setStoragestatus(storageStatus);
			device.setWarrantyDate(warrantyDateID.getValue());
			device.setSoftware(osAndKeyID.getText());
			try {
				device.setUserID(listViewUsersID.getSelectionModel().getSelectedItem().get_id());
			} catch (Exception e) {
				device.setUserID(0);
			}
			device.setManufacturer(manufacturerID.getText());
			device.setNumber(phoneNumberID.getText());
			device.setNotes(notesID.getText());
			SQLData.getInstance().editSelectedDevice((Phone)deviceObject, device);
			closeStage();
		}
	}

	// Werken met OK en Cancel butto;ns hier omdat FXML het niet support.
	@FXML
	public void showNewUserDialog() {
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("New User");
		dialog.setHeaderText("Add a user!");
		/*
		 * We maken een nieuwe dialog aan En we blokkeren input door de owner ervan te
		 * zetten bij dialog.initOwner(mainBorderPaneID.getScene().getWindow()); (Window
		 * is metaphore voor Stage) Nu we onze dialog instantie, wordt deze niet
		 * automatisch geladen in de UI. We moeten de FXML hier laden in de controller
		 * dezelfde manier als we doen in de main.java class
		 */
		// Wat we hieronder aangeven is, we pakken de Node mainBorderPaneID, we gaan
		// naar zijn scene toe, daarna
		// Naar zijn window/Stage en zeggen dit is de owner, aka je kan niks meer van
		// deze stage aanklikken.
		dialog.initOwner(itemWindowAnchorPaneID.getScene().getWindow());
		// Code voor main.java nadoen

		dialog.getDialogPane().getStylesheets().add(getClass().getResource("myStyle.css").toExternalForm());
		dialog.getDialogPane().getStyleClass().add("myStyle");
		FXMLLoader fxmlloader = new FXMLLoader();
		fxmlloader.setLocation(getClass().getResource("Dialog.fxml"));
		try {

			// We roepen de dialogPane zelf op en setten daarna de content ervan. wat onze
			// fxml is.
			dialog.getDialogPane().setContent(fxmlloader.load());

		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
		dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

		Optional<ButtonType> result = dialog.showAndWait(); // zet de input van de user in result.
		if (result.isPresent() && result.get() == ButtonType.OK) {
			// Code om de resultaten te processen wanneer op OK is gedrukt. check blz 49
			// section 13
			// We moeten eerst de controller getten van onze ToDoItemDialogue.fxml, daarna
			// kunnen we de methode
			// processresults aanroepen zodat de alles uitgevoerd.
			DialogController controller = fxmlloader.getController();
			controller.processResults();

		}
	}

	@FXML
	private void cancelButtonClicked() {
	closeStage();
	}
	
	private void closeStage() {
		Stage stage = (Stage) itemWindowAnchorPaneID.getScene().getWindow(); // We sluiten het scherm
		stage.close();
	}

}
