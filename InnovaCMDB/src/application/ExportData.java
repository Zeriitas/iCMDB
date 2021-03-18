package application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

public class ExportData {

public  void exportData(String path) throws IOException {
		//We krijgen de file path binnen en maken een nieuwe myFile variable.
		File myfile =new File(path);
		//We maken de nieuwe file aan op de locatie met die naam
		myfile.createNewFile();
		//We gaan schrijven in de file.
		FileWriter myWriter = new FileWriter(path);
		SQLData.getInstance().queryDevices(); //query devices om alles te vernieuwen
		List <Device> myDevices = new ArrayList<>(SQLData.getInstance().getDevices());
		//We maken een list vanuit de getDevices.
		//De write methode schrijven we alvast de columnnames.
		myWriter.write("Device Name,Model Name, Serial Number, Storage Status, Notes, Warranty Date, Operating Software, UserID/Name, Local Office, Manufacturer, Phone Number, \n");
		for(Device device : myDevices) {
			try { //We schrijven onder de kolommen en seperate het met commas.
			myWriter.append(device.getDeviceName() + ", " +
							device.getModelName() + ", " + 
							device.getSerialNumber() + ", " + 
							device.getStoragestatus() + ", " + 
							device.getNotes() + ", " + 
							device.getWarrantyDate() + ", " + 
							device.getSoftware() + ", " +
							SQLData.getInstance().findUsername(device.getUserID()) + ", ");
							//De correcte , seperation gebruikt per column en \n om naar een nieuwe regel te gaan
							if (device.getClass() == Computer.class) {
								myWriter.append(((Computer)device).getLocalOffice() + ", , , \n");
							} else if (device.getClass() == Laptop.class) {
								myWriter.append(((Laptop)device).getLocalOffice() + ", " + 
										((Laptop)device).getManufacturer() + ", , \n");
							} else if(device.getClass() == Phone.class) {
								myWriter.append(", " + ((Phone)device).getManufacturer() + ", " + 
										((Phone)device).getNumber() + ", \n");
							}					
			} catch (Exception e) {
			}
		}
		//We flushen de write en sluiten hem
		myWriter.flush();
		myWriter.close();
		
		/*
		 * We geven een alert aan de user!
		 */
		Alert alert = new Alert(AlertType.INFORMATION, "Import the .TXT file into Excel/Google Sheets and seperate columns with comma!",
				ButtonType.OK);
		/*
		 * We maken een image object aan en zetten het bestand erin.
		 * Daarna voegen we de image toe aan de alert via de setGraphic methode
		 */
		Image image = new Image("https://cdn.extendoffice.com/images/stories/doc-excel/split-text/doc-split-text-to-rows-columns-4.png");
		ImageView imageView = new ImageView(image);
		alert.setGraphic(imageView);
		
		alert.getDialogPane().getStylesheets().add(getClass().getResource("myStyle.css").toExternalForm());
		alert.getDialogPane().getStyleClass().add("myStyle");
		alert.show();
	
	

	
}
}

