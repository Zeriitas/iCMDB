package application;

import java.io.IOException;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class DialogController {
	
	
	@FXML
	private TextField textfieldID;
	
	@FXML
	public void processResults() {
		String username = textfieldID.getText().toUpperCase().trim();
			SQLData.getInstance().createUser(username);
		
			
		
		
		
		}
	
	public void  processResults(User selecteduser) {
		String username = textfieldID.getText().toUpperCase().trim();
		SQLData.getInstance().editSelectedUser(username, selecteduser);
	}
	
	}
