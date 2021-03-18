package application;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController {

	@FXML 
	private BorderPane borderPaneID;

	@FXML
	private TextField searchFieldID;

	@FXML
	private ListView<User> listUsersID;

	@FXML
	private ListView <Device> listDevicesID;

	@FXML
	private ListView<Computer> listComputersID;

	@FXML
	private ListView<Laptop> listLaptopsID;

	@FXML
	private ListView<Phone> listPhonesID; 

	@FXML
	private ListView searchResultsID;

	@FXML
	private TextField deviceNameTextFieldID;

	@FXML
	private TextField modelNameTextFieldID;

	@FXML
	private TextField serialNumberTextFieldID;

	@FXML
	private TextField storageStatusTextFieldID;

	@FXML
	private TextArea notesTextAreaID;

	@FXML
	private TextField userTextFieldID;

	@FXML
	private TextArea localOfficeTextAreaID;

	@FXML
	private TextArea osKeyTextAreaID;

	@FXML
	private TextField warrantyDateTextFieldID;

	@FXML
	private TextField manufacturerTextFieldID;

	@FXML
	private TextField phoneNumberTextFieldID;
	
	@FXML
	private Accordion accordionID;
	
	
	@FXML
	private TitledPane titledComputerPaneID;
	
	@FXML
	private TitledPane titledLaptopPaneID;
	
	@FXML
	private TitledPane titledPhonePaneID;
	
	@FXML 
	private TitledPane titledUserPaneID;
	
	@FXML
	private ContextMenu listContextMenuDevicesID;
	
	@FXML
	private ContextMenu listContextMenuUsersID;
	
	@FXML
	private ContextMenu listContextMenuComputersID;
	
	@FXML
	private ContextMenu listContextMenuLaptopsID;
	
	@FXML
	private ContextMenu listContextMenuPhonesID;
	
	@FXML
	private TitledPane titledDevicePaneID;
	
	@FXML
	private FilteredList<Device> predicateDeviceList;

	
	public void initialize() {
		
	
	
	
		
		SQLData.getInstance().open();
		//We maken een sorted list en wrappen deze in de ListView. op line 124
		SortedList<Computer> sortedComputerList = new SortedList<Computer>(SQLData.getInstance().getComputers()
				, new Comparator<Computer>() {
			@Override //Innerclass die compare methode inzet, <Computer> betekend dat we het eigenlijk in Computer hebben gezet
			//We vergelijken de namen
			public int compare(Computer o1, Computer o2) {
				return o1.getDeviceName().compareTo(o2.getDeviceName());
			}
			
		});
		SortedList<User> sortedUserList = new SortedList<User>(SQLData.getInstance().getUsers()
				, new Comparator<User>() {
			@Override
			public int compare(User o1, User o2) {
				return o1.getName().compareTo(o2.getName());
			}
			
		});
		SortedList<Laptop> sortedLaptopList = new SortedList<Laptop>(SQLData.getInstance().getLaptops()
				, new Comparator<Laptop>() {
			@Override
			public int compare(Laptop o1, Laptop o2) {
				return o1.getDeviceName().compareTo(o2.getDeviceName());
			}
			
		});
		SortedList<Phone> sortedPhoneList = new SortedList<Phone>(SQLData.getInstance().getPhones()
				, new Comparator<Phone>() {
			@Override
			public int compare(Phone o1, Phone o2) {
				return o1.getDeviceName().compareTo(o2.getDeviceName());
			}
			
		});
		
	
		
		SortedList<Device> sortedDeviceList = new SortedList<Device>(SQLData.getInstance().getDevices()
				, new Comparator<Device>() {
			@Override
			public int compare(Device o1, Device o2) {
				return o1.getDeviceName().compareTo(o2.getDeviceName());
			}
			
		});
		
	
	
		/*
		 * De list Setters nadata alle items in de Observable list zijn gesorteerd op ASC en dan wrappen in de ListView.
		 */
		listUsersID.setItems(sortedUserList);
		listComputersID.setItems(sortedComputerList);
		listLaptopsID.setItems(sortedLaptopList);
		listPhonesID.setItems(sortedPhoneList);
		listDevicesID.setItems(sortedDeviceList);
	

		
		/*
		 * ContextMenu die we aanmaken met een eventlistener voor mousebuttons die clicked zijn
		 */	
		/*
		 * Method voor event handling double click on all devices, die je brengt naar de item van de listview waar het thuishoort
		 * Wanneer je dubbel klikt dan regelen de andere methodes de verwerking van de itemselection als normaal
		 * Als normaal als in selected in hun eige listview van specifieke devices.
		 */
		listDevicesID.setOnMouseClicked(new EventHandler <MouseEvent>() { //method voor het clicken op een item in listDevices
		//Dit is een static innerclass die de setOnMouseClicked event override.
			@Override //Nieuwe eventhandler gemaakt voor mouse kliks
		public void handle(MouseEvent click) {
			if(click.getClickCount() ==2) {
				Device selecteddevice =listDevicesID.getSelectionModel().getSelectedItem();
				if(selecteddevice.getClass() == Computer.class) {
					listComputersID.getSelectionModel().select((Computer)selecteddevice);
					listComputersClicked();
				} else if (selecteddevice.getClass() == Laptop.class) {
					listLaptopsID.getSelectionModel().select((Laptop) selecteddevice);
					listLaptopsClicked();
				} else if (selecteddevice.getClass() == Phone.class) {
					listPhonesID.getSelectionModel().select((Phone) selecteddevice);
					listPhonesClicked();
				}listDevicesID.getSelectionModel().clearSelection();
				/*
				 * Als er een RightClick plaatsvind in de mouseaction 
				 * dan wordt de volgende methode uitgevoerd.
				 * We pakken eerst de geselecteerde item die we hebben aangeklikt met right button
				 * Dan maken we een context menu aaan en vullen hem met de gebruikersnaam van degene die het toestel heeft
				 * We maken weer een Action Event dat wanneer iemand drukt op de contextmenu dat er dan het volgende wordt uitgevoerd
				 * Ga naar de user in de listuserID, request focus en open de titled pane., clear de selected oude item.
				 */
			} if ((click.getButton() == MouseButton.SECONDARY) && (listDevicesID.getSelectionModel().getSelectedItem() != (null))) {
				try {
				Device selecteddevice = listDevicesID.getSelectionModel().getSelectedItem();
				listDevicesID.getSelectionModel().clearSelection(); //Zodat de selectedItem wordt gecleared 
				//Anders ontstaat het probleem dat we steeds de selected item vast hebben en overal kunnen rightclicken wat raar is
				listContextMenuDevicesID = new ContextMenu();
				MenuItem selectDeviceItem = new MenuItem(SQLData.getInstance().findUsername(selecteddevice.getUserID()));  //Vind username met userID
				listContextMenuDevicesID.getItems().addAll(selectDeviceItem);
				listDevicesID.setContextMenu(listContextMenuDevicesID);
				selectDeviceItem.setOnAction(new EventHandler<ActionEvent>() { //Dubbelle anonymous innerclass.
					@Override
					public void handle(ActionEvent event) {
						listUsersID.getSelectionModel().select(SQLData.getInstance().findUser(selecteddevice.getUserID()));
						listUsersClicked();
					}
				});
			} catch(Exception e) {
				
			}
			} else if ((click.getButton() == MouseButton.SECONDARY) && (listDevicesID.getSelectionModel().getSelectedItem() == (null))) {
				listContextMenuDevicesID = new ContextMenu();
			MenuItem nothing = new MenuItem();
			listContextMenuDevicesID.getItems().setAll(nothing);
			listDevicesID.setContextMenu(listContextMenuDevicesID);
			}
		}
		});
		 
		/*
		 * QueryDevices eerst om de lijst de herladen volledig
		 * We pakken de geselecteerde item en clearen dan de selection. We gebruiken de geslecteerde user om de predicatelist te vullen
		 * We zoeken naar de devices die die geselecteerde user id bevatten en zetten ze in de filteredlist.
		 * Deze list wrappen we in de contextMenu als items met hun namen via de for loop. Ook binden we per item een EventHandler
		 * Voor elke item als er dus geclick wordt op. Zoeken we het in deviceFinder(zoekt in all devices). En selecteerd de computer
		 * De computer wordt geopend in de andere list, hierna voeren we de listDevicesClicked() methodes uit.
		 * Als er geen listuser item is geselecteer is de contextmenu leeg.
		 */
		listUsersID.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click) {
				try {
				if ((click.getButton() == MouseButton.SECONDARY) && (listUsersID.getSelectionModel().getSelectedItem() != (null))) {
					queryAllDevices();
					User selecteduser = listUsersID.getSelectionModel().getSelectedItem();
					listUsersID.getSelectionModel().clearSelection();
					listContextMenuUsersID = new ContextMenu();
					MenuItem emptydevice = new MenuItem();
					listContextMenuUsersID.getItems().add(emptydevice);
					listUsersID.setContextMenu(listContextMenuUsersID);
				predicateDeviceList = new FilteredList<Device>(SQLData.getInstance().getDevices(), new Predicate<Device>() {
					@Override
					public boolean test(Device device) {
						return device.getUserID() == selecteduser.get_id();
					}
				});	
						listContextMenuUsersID = new ContextMenu(); 
						//Dit is verplaatst buiten de for loop, eerst zat het in de for loop.
						//Waardoor we steeds een nieuwe aanmaakte en ervoor zorgde datie leeg werd gehaald.
						//Nu is de for loop perfect en hebben we een menu wat items een functionaliteit geeft.
						
					for (int i =0; i < predicateDeviceList.size(); i++) {
						MenuItem device = new MenuItem(predicateDeviceList.get(i).getDeviceName());
						listContextMenuUsersID.getItems().add(device);
						device.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
									Device selecteddevice = SQLData.getInstance().deviceFinder(device.getText());
									if (selecteddevice.getClass() == Computer.class) {
										listComputersID.getSelectionModel().select((Computer)selecteddevice);
										listComputersClicked();
									} else if (selecteddevice.getClass() == Laptop.class) {
										listLaptopsID.getSelectionModel().select((Laptop)selecteddevice);
										titledLaptopPaneID.setExpanded(true);
										listLaptopsClicked();
									} else if (selecteddevice.getClass() == Phone.class) {
										listPhonesID.getSelectionModel().select((Phone)selecteddevice);
										titledPhonePaneID.setExpanded(true);
										listPhonesClicked();
									}
							}			
						});	
					}
						listUsersID.setContextMenu(listContextMenuUsersID);	
				} else if ((click.getButton() == MouseButton.SECONDARY) && (listUsersID.getSelectionModel().getSelectedItem() == (null))) {
					listContextMenuUsersID = new ContextMenu();
				MenuItem nothing = new MenuItem();
				listContextMenuUsersID.getItems().setAll(nothing);
				listUsersID.setContextMenu(listContextMenuUsersID);
				}
			} catch(Exception e) {
				
			}}
		});
	
		/*
		 * We maken een nieuwe eventhandler
		 * De click parameter maakt niet uit hoe je hem schrijf.
		 * De eventhandler is van het type MouseEvent.
		 * Wat we eerst doen is checken of er is rightclicked && een item is geselecteerd && of de item een userID heeft boven 0
		 * Zowel dan zetten we de contextmenu voor diegene met de User die eraan gekoppeld is. We gebruikken de naam van diegene
		 * Die weergeven in de selectie. Als die geklikt is dan openen we de selectionmodel van ListUsersID en selecteren de user van de computer 
		 * We voeren de listUserclicked uit, wat alle andere lists deselect en focust op de user list.
		 * De else if zorgt ervoor dat we de rest van de items een lege contextmenu geven, anders dragen ze het over.
		 * Het is belangrijk de section altijd te clearen
		 */
		listComputersID.setOnMouseClicked(new EventHandler<MouseEvent>() {		
			@Override
			public void handle(MouseEvent click) {
				try { //Zetten het in een trycatch clausule
				if((click.getButton() == MouseButton.SECONDARY) && (listComputersID.getSelectionModel().getSelectedItem() != (null) && 
						(listComputersID.getSelectionModel().getSelectedItem().getUserID() > 0))) {		
					User user = SQLData.getInstance().findUser(listComputersID.getSelectionModel().getSelectedItem().getUserID());
					listContextMenuComputersID = new ContextMenu();
					MenuItem selecteddevice = new MenuItem(user.getName());
					listContextMenuComputersID.getItems().addAll(selecteddevice);
					listComputersID.setContextMenu(listContextMenuComputersID);
					listComputersID.getSelectionModel().clearSelection();
					selecteddevice.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
						listUsersID.getSelectionModel().select(user);
						listUsersClicked();
						}
					});
					
					
				}else if ((click.getButton() == MouseButton.SECONDARY)) {
						listContextMenuComputersID = new ContextMenu();
					MenuItem nothing = new MenuItem();
					listContextMenuComputersID.getItems().setAll(nothing);
					listComputersID.setContextMenu(listContextMenuComputersID);
					listComputersID.getSelectionModel().clearSelection();
					}
				} catch (Exception e) {
				}
			}
		});
		listLaptopsID.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent click) {
				try {
				if((click.getButton() == MouseButton.SECONDARY) && (listLaptopsID.getSelectionModel().getSelectedItem() != (null) && 
						(listLaptopsID.getSelectionModel().getSelectedItem().getUserID() > 0))) {
					User user = SQLData.getInstance().findUser(listLaptopsID.getSelectionModel().getSelectedItem().getUserID());
					listLaptopsID.getSelectionModel().clearSelection();
					listContextMenuLaptopsID = new ContextMenu();
					MenuItem selecteddevice = new MenuItem(user.getName());
					listContextMenuLaptopsID.getItems().add(selecteddevice);
					listLaptopsID.setContextMenu(listContextMenuLaptopsID);
					selecteddevice.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
						listUsersID.getSelectionModel().select(user);
						listUsersClicked();
						}
					});
				
					} else if ((click.getButton() == MouseButton.SECONDARY) && (listLaptopsID.getSelectionModel().getSelectedItem() == (null))) {
						listContextMenuLaptopsID = new ContextMenu();
					MenuItem nothing = new MenuItem();
					listContextMenuLaptopsID.getItems().setAll(nothing);
					listLaptopsID.setContextMenu(listContextMenuLaptopsID);
					}
				
			}catch (Exception e) {
				
			}
			}
		});
		listPhonesID.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent click) {
				try {
				if((click.getButton() == MouseButton.SECONDARY) && (listPhonesID.getSelectionModel().getSelectedItem() != (null) && 
						(listPhonesID.getSelectionModel().getSelectedItem().getUserID() > 0))) {
					User user = SQLData.getInstance().findUser(listPhonesID.getSelectionModel().getSelectedItem().getUserID());
					listPhonesID.getSelectionModel().clearSelection();
					listContextMenuPhonesID = new ContextMenu();
					MenuItem selecteddevice = new MenuItem(user.getName());
					listContextMenuPhonesID.getItems().add(selecteddevice);
					listPhonesID.setContextMenu(listContextMenuPhonesID);
					selecteddevice.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
						listUsersID.getSelectionModel().select(user);
						listUsersClicked();
						}
					});
					} else if ((click.getButton() == MouseButton.SECONDARY) && (listPhonesID.getSelectionModel().getSelectedItem() == (null))) {
						listContextMenuPhonesID = new ContextMenu();
					MenuItem nothing = new MenuItem();
					listContextMenuPhonesID.getItems().setAll(nothing);
					listPhonesID.setContextMenu(listContextMenuPhonesID);
					}
				
			} catch(Exception e) {
			}
			}
			
		});
	
		
		listComputersID.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Computer>() {
			//Change listener is executed whenever something changes.
			@Override //overriding static inner class method changed. anders moeten we hem implementeren in ToDoItem class
			//
			public void changed(ObservableValue<? extends Computer> observable, Computer oldValue, Computer newValue) {
				if(newValue!= null) { //Als we iets hebben aangedrukt en niet niks
					/* Eerst verkrijgen we de volledige listViewID selection, wanneer we kiezen voor een item
					 * in de listView model in onze UI, roepen we de getSelectedItem method aan
					 * Deze pakt de geselecteerde item, De reden dat de eerste item geselecteerd wordt is omdat
					 * We in onze code verderop listViewID.getSelectionModel().selectFirst(); aanroepen
					 * wordt de changed method aangeroepen door de ChangeListener, als er wat veranderd dan execute deze
					 * We pakken de geselecteerde item en zetten het in variable item
				     * De variable item zn details pakken we en zetten we weer in todoItemDetailedID fx:id
					 */
					Computer selecteddevice = listComputersID.getSelectionModel().getSelectedItem();
					deviceNameTextFieldID.setText(selecteddevice.getDeviceName());
					modelNameTextFieldID.setText(selecteddevice.getModelName());
					serialNumberTextFieldID.setText(selecteddevice.getSerialNumber());
					storageStatusTextFieldID.setText(selecteddevice.getStoragestatus());
					notesTextAreaID.setText(selecteddevice.getNotes());
					userTextFieldID.setText(SQLData.getInstance().findUsername(selecteddevice.getUserID()));
					localOfficeTextAreaID.setText(selecteddevice.getLocalOffice());
					osKeyTextAreaID.setText(selecteddevice.getSoftware());
					warrantyDateTextFieldID.setText(selecteddevice.getWarrantyDate());			
					manufacturerTextFieldID.setText(null); //clear de fields sinds ze deze niet gebruiken
					phoneNumberTextFieldID.setText(null);
					
			
				}
				
			}
		});
		
		listLaptopsID.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Laptop>() {
			public void changed(ObservableValue<? extends Laptop> observable, Laptop oldValue, Laptop newValue) {
				if(newValue!= null) {
					Laptop selecteddevice = listLaptopsID.getSelectionModel().getSelectedItem();
					deviceNameTextFieldID.setText(selecteddevice.getDeviceName());
					modelNameTextFieldID.setText(selecteddevice.getModelName());
					serialNumberTextFieldID.setText(selecteddevice.getSerialNumber());
					storageStatusTextFieldID.setText(selecteddevice.getStoragestatus());
					notesTextAreaID.setText(selecteddevice.getNotes());
					userTextFieldID.setText(SQLData.getInstance().findUsername(selecteddevice.getUserID())); //null if 0/geen
					localOfficeTextAreaID.setText(selecteddevice.getLocalOffice());
					osKeyTextAreaID.setText(selecteddevice.getSoftware());
					warrantyDateTextFieldID.setText(selecteddevice.getWarrantyDate());	
					manufacturerTextFieldID.setText(selecteddevice.getManufacturer());
					phoneNumberTextFieldID.setText(null);
			
					
				}
				
			}
		});
		
		listPhonesID.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Phone>() {
			public void changed(ObservableValue<? extends Phone> observable, Phone oldValue, Phone newValue) {
				if(newValue!= null) {
					Phone selecteddevice = listPhonesID.getSelectionModel().getSelectedItem();
					deviceNameTextFieldID.setText(selecteddevice.getDeviceName());
					modelNameTextFieldID.setText(selecteddevice.getModelName());
					serialNumberTextFieldID.setText(selecteddevice.getSerialNumber());
					storageStatusTextFieldID.setText(selecteddevice.getStoragestatus());
					notesTextAreaID.setText(selecteddevice.getNotes());
					userTextFieldID.setText(SQLData.getInstance().findUsername(selecteddevice.getUserID()));
					localOfficeTextAreaID.setText(null);
					osKeyTextAreaID.setText(selecteddevice.getSoftware());
					warrantyDateTextFieldID.setText(selecteddevice.getWarrantyDate());	
					manufacturerTextFieldID.setText(selecteddevice.getManufacturer());
					phoneNumberTextFieldID.setText(selecteddevice.getNumber());
					
					
				}
				
			}
		});
	}
	
	private void queryAllDevices() {//Methode om ALL Devices TitltedPane te herlade,
		//Wordt aangeroepen wanneer we de titledpane openen van alle devices.
		SQLData.getInstance().queryDevices();
		SortedList<Device> sortedDeviceList = new SortedList<Device>(SQLData.getInstance().getDevices()
				, new Comparator<Device>() {
			@Override
			public int compare(Device o1, Device o2) {
				return o1.getDeviceName().compareTo(o2.getDeviceName());
			}
			
		});
		listDevicesID.setItems(sortedDeviceList);
	}
	
	

	
	@FXML
	public void addDeviceButtonClicked() {
		try {
			FXMLLoader fxml = new FXMLLoader(getClass().getResource("ItemWindow.fxml"));
			Parent root =(Parent) fxml.load();
		
		Stage stage = new Stage();
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(borderPaneID.getScene().getWindow());
		stage.setTitle("Add or edit existing");
		stage.setScene(new Scene(root));
		stage.show();
		
		
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	}
	
	
	@FXML
	public void searchRectangleClicked() {
		searchFieldID.requestFocus();
	
	}

	// Zelfde methode staat in ItemController.
	// Verwijst naar DialogController voor de informatie die wordt processed.
	@FXML
	public void addUserButtonClicked() {
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("New User window");
		dialog.setHeaderText("Add a user!");
		dialog.initOwner(borderPaneID.getScene().getWindow());
		FXMLLoader fxmlloader = new FXMLLoader();
		fxmlloader.setLocation(getClass().getResource("Dialog.fxml"));
		try {
			dialog.getDialogPane().setContent(fxmlloader.load());
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
		dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
		dialog.getDialogPane().getStylesheets().add(getClass().getResource("myStyle.css").toExternalForm());
		dialog.getDialogPane().getStyleClass().add("myStyle");
	
		Optional<ButtonType> result = dialog.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			DialogController controller = fxmlloader.getController();
			controller.processResults();

		}
	}
	
	
	
	
	@FXML
	public void editButtonClicked() {
		if (!listComputersID.getSelectionModel().isEmpty()) {
			try {
				FXMLLoader fxml = new FXMLLoader(getClass().getResource("EditItemWindow.fxml"));
				Parent root =(Parent) fxml.load();
				ItemController itemcontroller = fxml.getController(); //Vergeten te gebruiken je moet de fxml controller laden
				Computer selectedcomputer = listComputersID.getSelectionModel().getSelectedItem();
				itemcontroller.editButtonClicked(selectedcomputer);
			Stage stage = new Stage();
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(borderPaneID.getScene().getWindow());
			stage.setTitle("edit existing");
			stage.setScene(new Scene(root));
			stage.show();
			
			
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			
		} else if (!listLaptopsID.getSelectionModel().isEmpty()) {
			try {
				FXMLLoader fxml = new FXMLLoader(getClass().getResource("EditItemWindow.fxml"));
				Parent root =(Parent) fxml.load();
				ItemController itemcontroller = fxml.getController();
				Laptop selectedlaptop = listLaptopsID.getSelectionModel().getSelectedItem();
				itemcontroller.editButtonClicked(selectedlaptop);
			Stage stage = new Stage();
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(borderPaneID.getScene().getWindow());
			stage.setTitle("edit existing");
			stage.setScene(new Scene(root));
			stage.show();		
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		} else if (!listPhonesID.getSelectionModel().isEmpty()) {
			try {
			FXMLLoader fxml = new FXMLLoader(getClass().getResource("EditItemWindow.fxml"));
			Parent root =(Parent) fxml.load();
			ItemController itemcontroller = fxml.getController();
			Phone selectedphone = listPhonesID.getSelectionModel().getSelectedItem();
			itemcontroller.editButtonClicked(selectedphone);
			Stage stage = new Stage();
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(borderPaneID.getScene().getWindow());
			stage.setTitle("edit existing");
			stage.setScene(new Scene(root));
			stage.show();		
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		if (!listUsersID.getSelectionModel().isEmpty()) {		
		try {
			User selecteduser = listUsersID.getSelectionModel().getSelectedItem();
			Dialog<ButtonType> dialog = new Dialog<>();
			dialog.setTitle("Edit existing user");
			dialog.setHeaderText(" The selected user you are editing " + selecteduser.getName());
			dialog.initOwner(borderPaneID.getScene().getWindow());
			FXMLLoader fxmlloader = new FXMLLoader();
			fxmlloader.setLocation(getClass().getResource("EditUserDialog.fxml"));
			try {
				dialog.getDialogPane().setContent(fxmlloader.load());
			} catch (IOException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
			dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
			dialog.getDialogPane().getStylesheets().add(getClass().getResource("myStyle.css").toExternalForm());
			dialog.getDialogPane().getStyleClass().add("myStyle");
			Optional<ButtonType> result = dialog.showAndWait();
			if (result.isPresent() && result.get() == ButtonType.OK) {
				DialogController controller = fxmlloader.getController();
				controller.processResults(selecteduser);
			}

		} catch (Exception e) {

		}
	}
	
}
	@FXML
	public void deleteButtonClicked() {
		
		User user = listUsersID.getSelectionModel().getSelectedItem();
		Computer computer = listComputersID.getSelectionModel().getSelectedItem();
		Laptop laptop = listLaptopsID.getSelectionModel().getSelectedItem();
		Phone phone = listPhonesID.getSelectionModel().getSelectedItem();
		try {
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("Edit existing user");
		dialog.setHeaderText(" Warning!  you are deleting an item");
		dialog.initOwner(borderPaneID.getScene().getWindow());
		
		dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
		dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
		dialog.getDialogPane().getStylesheets().add(getClass().getResource("myStyle.css").toExternalForm());
		dialog.getDialogPane().getStyleClass().add("myStyle");
		Optional<ButtonType> result = dialog.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {

			if (user != null) {
				
				SQLData.getInstance().deleteSelectedItem(user);
			} else if (computer != null) {
				SQLData.getInstance().deleteSelectedItem(computer);
			} else if (laptop != null) {
				SQLData.getInstance().deleteSelectedItem(laptop);
			} else if (phone != null) {
				SQLData.getInstance().deleteSelectedItem(phone);
			}
		}
		} catch (Exception e) {

		}
		queryAllDevices();
	}
	
		
	
	// Methods when certain listviews are clicked, will desedlect other listview
	// selected items
	// to prvent problem of multiple selected items to be edited at once.
	// Try catch voor de nullpointer.
	@FXML
	private void listUsersClicked() {
		try {
			listDevicesID.getSelectionModel().clearSelection();
			listComputersID.getSelectionModel().clearSelection();
			listLaptopsID.getSelectionModel().clearSelection();
			listPhonesID.getSelectionModel().clearSelection();
			titledUserPaneID.requestFocus();
			titledUserPaneID.setExpanded(true);
			listUsersID.requestFocus();
		} catch (Exception e) {
		}
	}
	@FXML
	private void listDevicesClicked() {
		try {
			
			listUsersID.getSelectionModel().clearSelection();
			listComputersID.getSelectionModel().clearSelection();
			listLaptopsID.getSelectionModel().clearSelection();
			listPhonesID.getSelectionModel().clearSelection();
			titledDevicePaneID.requestFocus();
			titledDevicePaneID.setExpanded(true);
			listDevicesID.requestFocus();
			queryAllDevices();
		} catch (Exception e) {
		}
	}
	@FXML
	private void listComputersClicked() {
		try {
			listUsersID.getSelectionModel().clearSelection();
			listDevicesID.getSelectionModel().clearSelection();
			listLaptopsID.getSelectionModel().clearSelection();
			listPhonesID.getSelectionModel().clearSelection();
			listDevicesID.getSelectionModel().clearSelection();
			titledComputerPaneID.requestFocus();
			titledComputerPaneID.setExpanded(true);
			listComputersID.requestFocus();
		} catch (Exception e) {
		}
	
	}
	@FXML
	private void listLaptopsClicked() {
		try {
			listUsersID.getSelectionModel().clearSelection();
			listDevicesID.getSelectionModel().clearSelection();
			listComputersID.getSelectionModel().clearSelection();
			listPhonesID.getSelectionModel().clearSelection();
			listDevicesID.getSelectionModel().clearSelection();
			titledLaptopPaneID.requestFocus();
			titledLaptopPaneID.setExpanded(true);
			listLaptopsID.requestFocus();
		} catch (Exception e) {
		}
	}
	
	@FXML
	private void listPhonesClicked() {
		try {
			listUsersID.getSelectionModel().clearSelection();
			listDevicesID.getSelectionModel().clearSelection();
			listComputersID.getSelectionModel().clearSelection();
			listLaptopsID.getSelectionModel().clearSelection();
			listDevicesID.getSelectionModel().clearSelection();
			titledPhonePaneID.requestFocus();
			titledPhonePaneID.setExpanded(true);
			listPhonesID.requestFocus();
		} catch (Exception e) {
		}
	}
	
	
	@FXML
	private void quitButtonClicked() {
		SQLData.getInstance().close();
		Platform.exit();
	}
	
	@FXML
	private void exportButtonClicked() {
		try {
			//Maken nieuwe filechooser aan. we geven de title. bij setTitle
		FileChooser chooser = new FileChooser(); 
		chooser.setTitle("Save Application Data, Import into Excel and seperate with comma!");
		//De extension is de save type, we hebben de 2e parameter verwijderd anders krijgen we dubbel .txt
		chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text", "*"));
		chooser.setInitialFileName("I-CMDB-Export.txt"); //Specifieke naam instellen bij de txt file die we opslaan
		File file = chooser.showSaveDialog(borderPaneID.getScene().getWindow()); //Main window niet beschikbaar maken
		if(file.getPath() != null) { //Het path is de gekozen direction. Als deze niet leeg is sturen we het door
		ExportData exportdata = new ExportData();
			exportdata.exportData(file.getPath());
		} else  {
			//nothing will be done since its cancelled
		}

	} catch (Exception e) {
		//nullpointer exception
	}	
	}
	
	@FXML
	private void importButtonClicked() {
		try {
			FXMLLoader fxml = new FXMLLoader(getClass().getResource("ImportData.fxml"));
			Parent root =(Parent) fxml.load();
		Stage stage = new Stage();
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(borderPaneID.getScene().getWindow());
		stage.setTitle("Choose Import Datatype");
		stage.setScene(new Scene(root));
		stage.show();
		Alert warning = new Alert(AlertType.INFORMATION,
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
		warning.setGraphic(imageView);

		warning.getDialogPane().getStylesheets().add(getClass().getResource("myStyle.css").toExternalForm());
		warning.getDialogPane().getStyleClass().add("myStyle");
		warning.show();
		
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
