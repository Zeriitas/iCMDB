package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class SQLData {
//	public static final String DB_NAME = "InnovaCMDB.db"; old, niet nodig met dbpath en database selecting
	
	//Stringbuilder ipv String omdat strings constants zijn
	private StringBuilder DBpath = new StringBuilder(); //ALWAYS EMPTY
	private StringBuilder CONNECTION_SB = new StringBuilder("jdbc:sqlite:"); //("jdbc:sqlite:"); ALWAYS MINIMAL!
	//Laat de parameter empty normaal voor productie.
	private StringBuilder CONNECTION_STRING = new StringBuilder(); //"jdbc:sqlite:C:\\Users\\Fatih\\Eclipse-workspace-fatih\\InnovaCMDB\\InnovaCMDB.db"); //Hardcode path for testing, empty is production
	
	
	public static final String TABLE_DEVICES = "Devices";
	public static final String COLUMN_DEVICES_ID = "_id";
	public static final String COLUMN_DEVICES_DEVICENAME = "deviceName";
	public static final String COLUMN_DEVICES_MODELNAME = "modelName";
	public static final String COLUMN_DEVICES_SERIALNUMBER = "serialNumber";
	public static final String COLUMN_DEVICES_STORAGESTATUS = "storageStatus";
	public static final String COLUMN_DEVICES_NOTES = "notes";
	public static final String COLUMN_DEVICES_WARRANTYDATE = "warrantyDate";
	public static final String COLUMN_DEVICES_OSSOFTWARE = "osSoftware";
	public static final String COLUMN_DEVICES_USER = "userID";
	public static final int INDEX_DEVICES_ID = 1;
	public static final int INDEX_DEVICES_DEVICENAME = 2;
	public static final int INDEX_DEVICES_MODELNAME = 3;
	public static final int INDEX_DEVICES_SERIALNUMBER = 4;
	public static final int INDEX_DEVICES_STORAGESTATUS = 5;
	public static final int INDEX_DEVICES_NOTES = 6;
	public static final int INDEX_DEVICES_WARRANTYDATE = 7;
	public static final int INDEX_DEVICES_OSSOFTWARE = 8;
	public static final int INDEX_DEVICES_USER = 9;

	public static final String TABLE_COMPUTERS = "Computers";
	public static final String COLUMN_COMPUTERS_LOCALOFFICE = "localOffice";
	public static final int INDEX_COMPUTERS_LOCALOFFICE = 10;

	public static final String TABLE_LAPTOPS = "Laptops";
	public static final String COLUMN_LAPTOPS_LOCALOFFICE = "localOffice";
	public static final String COLUMN_LAPTOPS_MANUFACTURER = "manufacturer";
	public static final int INDEX_LAPTOPS_LOCALOFFICE = 10;
	public static final int INDEX_LAPTOPS_MANUFACTURER = 11;

	public static final String TABLE_PHONES = "Phones";
	public static final String COLUMN_PHONES_MANUFACTURER = "manufacturer";
	public static final String COLUMN_PHONES_PHONENUMBER = "number";
	public static final int INDEX_PHONES_MANUFACTURER = 10;
	public static final int INDEX_PHONES_PHONENUMBER = 11;

	public static final String TABLE_USERS = "Users";
	public static final String COLUMN_USERS_ID = "_id";
	public static final String COLUMN_USERS_NAME = "name";
	public static final String COLUMN_USERS_DEVICE = "deviceID";
	public static final String COLUMN_USERS_COMPUTER = "computerID";
	public static final String COLUMN_USERS_PHONE = "phoneID";
	public static final String COLUMN_USERS_LAPTOP = "laptopID";
	public static final int INDEX_USERS_ID = 1;
	public static final int INDEX_USERS_NAME = 2;
	public static final int INDEX_USERS_DEVICE = 3;
	public static final int INDEX_USERS_COMPUTER = 4;
	public static final int INDEX_USERS_PHONE = 5;
	public static final int INDEX_USERS_LAPTOP = 6;

	public static final int ORDER_BY_NONE = 1;
	public static final int ORDER_BY_ASC = 2;
	public static final int ORDER_BY_DESC = 3;

	public ObservableList<User> getUsers() {
		return users;
	}

	public ObservableList<Device> getDevices() {
		return devices;
	}

	public ObservableList<Computer> getComputers() {
		return computers;
	}

	public ObservableList<Laptop> getLaptops() {
		return laptops;
	}

	public ObservableList<Phone> getPhones() {
		return phones;
	}

	//Om te laten zien waar de path is bij import.
	public StringBuilder getDBpath() {
		return DBpath;
	}

	/*
	 * Belangrijk om met 1 instantie van deze database te werken, ofc wegens
	 * veiligheid en zodat er geen kopies van zijn Wanneer we altijd via een static
	 * manier referenties maken of methodes aanroepen dan worden de wijzigingen Aan
	 * de SQLData class zijn variables etc gelijk toegepast over de enige instantie
	 * Dit is een belangrijk concept voor Databinding. Je moet via static referentes
	 * werken in 1 instantie.
	 */
	public static SQLData getInstance() {
		return instance;
	}

	private SQLData() {
	}
	
	private void changeConnection(String string) {
		this.DBpath.append(string);
		CONNECTION_STRING.append(CONNECTION_SB);
		CONNECTION_STRING.append(DBpath);
	}
	
	public  void selectDB(String string) {
		changeConnection(string);
	}
	
	/*
	 * createDB wordt aangeroepen,
	 * We roepen changeConnection method aan, hierbij bouwen we de CONNECTION STRING op.
	 * We hebben dit veranderd in StringBuilder omdat string immutable is en altijd null bleef.
	 * Hierna voeren we de statements uit om de tables aan te maken met de juiste columns.
	 */
	public void createDB(String string) {
		changeConnection(string);
		try {
			conn = DriverManager.getConnection(CONNECTION_STRING.toString());
			Statement statement = conn.createStatement();
			 statement.execute("CREATE TABLE \"Computers\" (\r\n"
			 		+ "	\"_id\"	INTEGER,\r\n"
			 		+ "	\"deviceName\"	TEXT,\r\n"
			 		+ "	\"modelName\"	TEXT,\r\n"
			 		+ "	\"serialNumber\"	TEXT,\r\n"
			 		+ "	\"storageStatus\"	TEXT,\r\n"
			 		+ "	\"notes\"	TEXT,\r\n"
			 		+ "	\"warrantyDate\"	TEXT,\r\n"
			 		+ "	\"osSoftware\"	TEXT,\r\n"
			 		+ "	\"userID\"	INTEGER,\r\n"
			 		+ "	\"localOffice\"	TEXT,\r\n"
			 		+ "	PRIMARY KEY(\"_id\")\r\n"
			 		+ ")");
			 statement.execute("CREATE TABLE \"Devices\" (\r\n"
			 		+ "	\"_id\"	INTEGER,\r\n"
			 		+ "	\"deviceName\"	TEXT,\r\n"
			 		+ "	\"modelName\"	TEXT,\r\n"
			 		+ "	\"serialNumber\"	TEXT,\r\n"
			 		+ "	\"storageStatus\"	TEXT,\r\n"
			 		+ "	\"notes\"	TEXT,\r\n"
			 		+ "	\"warrantyDate\"	TEXT,\r\n"
			 		+ "	\"osSoftware\"	TEXT,\r\n"
			 		+ "	\"userID\"	INTEGER,\r\n"
			 		+ "	PRIMARY KEY(\"_id\")\r\n"
			 		+ ")");
			 statement.execute("CREATE TABLE \"Laptops\" (\r\n"
			 		+ "	\"_id\"	INTEGER,\r\n"
			 		+ "	\"deviceName\"	TEXT,\r\n"
			 		+ "	\"modelName\"	TEXT,\r\n"
			 		+ "	\"serialNumber\"	TEXT,\r\n"
			 		+ "	\"storageStatus\"	TEXT,\r\n"
			 		+ "	\"notes\"	TEXT,\r\n"
			 		+ "	\"warrantyDate\"	TEXT,\r\n"
			 		+ "	\"osSoftware\"	TEXT,\r\n"
			 		+ "	\"userID\"	INTEGER,\r\n"
			 		+ "	\"localOffice\"	TEXT,\r\n"
			 		+ "	\"manufacturer\"	TEXT,\r\n"
			 		+ "	PRIMARY KEY(\"_id\")\r\n"
			 		+ ")");
			 statement.execute("CREATE TABLE \"Phones\" (\r\n"
			 		+ "	\"_id\"	INTEGER,\r\n"
			 		+ "	\"deviceName\"	TEXT,\r\n"
			 		+ "	\"modelName\"	TEXT,\r\n"
			 		+ "	\"serialNumber\"	TEXT,\r\n"
			 		+ "	\"storageStatus\"	TEXT,\r\n"
			 		+ "	\"notes\"	TEXT,\r\n"
			 		+ "	\"warrantyDate\"	TEXT,\r\n"
			 		+ "	\"osSoftware\"	TEXT,\r\n"
			 		+ "	\"userID\"	TEXT,\r\n"
			 		+ "	\"manufacturer\"	TEXT,\r\n"
			 		+ "	\"number\"	TEXT,\r\n"
			 		+ "	PRIMARY KEY(\"_id\")\r\n"
			 		+ ")");
			 statement.execute("CREATE TABLE \"Users\" (\r\n"
			 		+ "	\"_id\"	INTEGER,\r\n"
			 		+ "	\"name\"	TEXT,\r\n"
			 		+ "	\"deviceID\"	INTEGER,\r\n"
			 		+ "	\"computerID\"	INTEGER,\r\n"
			 		+ "	\"phoneID\"	INTEGER,\r\n"
			 		+ "	\"laptopID\"	INTEGER,\r\n"
			 		+ "	PRIMARY KEY(\"_id\")\r\n"
			 		+ ")");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

	private ObservableList<Device> devices;
	private ObservableList<Computer> computers;
	private ObservableList<Laptop> laptops;
	private ObservableList<Phone> phones;
	private ObservableList<User> users;

	private static SQLData instance = new SQLData();
	// Methode zodat een gebruiker een connectie kan maken met de datbase.
	private Connection conn;


	public boolean open() {
		try {
			conn = DriverManager.getConnection(CONNECTION_STRING.toString());
			loadData(); //Draait eenmaling bij open om alle informatie te laden.

			return true;
		} catch (SQLException e) {
			System.out.println("Couldnt get connection to database" + e.getMessage());
			return false;
		}
	}

	public void close() {
		try {
			if (conn != null) {
				conn.close();

			}
		} catch (SQLException e) {
			System.out.println("Couldnt close connection" + e.getMessage());

		}
	}

	public void loadData() {
		queryUsers();
		queryComputers();
		queryLaptops();
		queryPhones();
		queryDevices();
	}

	public void queryDevices() {
			this.devices = FXCollections.observableArrayList();
			this.devices.addAll(this.computers);
			this.devices.addAll(this.laptops);
			this.devices.addAll(this.phones);
			
			}

	
	// Wanneer we een try catch clause maken met resources als parameters worden de
			// resources automatisch aan het einde gesloten.
			
	public void queryComputers() {
		try (Statement statement = conn.createStatement();
				ResultSet results = statement.executeQuery("SELECT * FROM " + TABLE_COMPUTERS)) {

			this.computers = FXCollections.observableArrayList();
			while (results.next()) {
				Computer device = new Computer();
				device.set_id(results.getInt(INDEX_DEVICES_ID));
				device.setDeviceName(results.getString(INDEX_DEVICES_DEVICENAME));
				device.setModelName(results.getString(INDEX_DEVICES_MODELNAME));
				device.setSerialNumber(results.getString(INDEX_DEVICES_SERIALNUMBER));
				device.setStoragestatus(results.getString(INDEX_DEVICES_STORAGESTATUS));
				device.setNotes(results.getString(INDEX_DEVICES_NOTES));
				device.setWarrantyDate(results.getString(INDEX_DEVICES_WARRANTYDATE));
				device.setSoftware(results.getString(INDEX_DEVICES_OSSOFTWARE));
				device.setUserID(results.getInt(INDEX_DEVICES_USER));
				device.setLocalOffice(results.getString(INDEX_COMPUTERS_LOCALOFFICE));
				computers.add(device);
				
			}

		} catch (SQLException e) {
			System.out.println("QUERY FAILED " + e.getMessage());
		}/*
		 * We catchen hier de query. In de finally block catchen we de results.close die
		 * we proberen te sluiten En in de andere block catchen we de statement.close
		 * die we proberen te sluiten Individueel in een try catch block is beter omdat
		 * we dan weten welke specifiek is gefaald.
		 */
		

	}
	

	

	public void queryPhones() {
		try (Statement statement = conn.createStatement();
				ResultSet results = statement.executeQuery("SELECT * FROM " + TABLE_PHONES)) {

			this.phones = FXCollections.observableArrayList();
			while (results.next()) {
				Phone device = new Phone();
				device.set_id(results.getInt(INDEX_DEVICES_ID));
				device.setDeviceName(results.getString(INDEX_DEVICES_DEVICENAME));
				device.setModelName(results.getString(INDEX_DEVICES_MODELNAME));
				device.setSerialNumber(results.getString(INDEX_DEVICES_SERIALNUMBER));
				device.setStoragestatus(results.getString(INDEX_DEVICES_STORAGESTATUS));
				device.setNotes(results.getString(INDEX_DEVICES_NOTES));
				device.setWarrantyDate(results.getString(INDEX_DEVICES_WARRANTYDATE));
				device.setSoftware(results.getString(INDEX_DEVICES_OSSOFTWARE));
				device.setUserID(results.getInt(INDEX_DEVICES_USER));
				device.setManufacturer(results.getString(INDEX_PHONES_MANUFACTURER));
				device.setNumber(results.getString(INDEX_PHONES_PHONENUMBER));
				this.phones.add(device);

			}

		} catch (SQLException e) {
			System.out.println("QUERY FAILED " + e.getMessage());
		}
	

	}

	public void queryLaptops() {
		try (Statement statement = conn.createStatement();
				ResultSet results = statement.executeQuery("SELECT * FROM " + TABLE_LAPTOPS)) {

			this.laptops = FXCollections.observableArrayList();
			while (results.next()) {
				Laptop device = new Laptop();
				device.set_id(results.getInt(INDEX_DEVICES_ID));
				device.setDeviceName(results.getString(INDEX_DEVICES_DEVICENAME));
				device.setModelName(results.getString(INDEX_DEVICES_MODELNAME));
				device.setSerialNumber(results.getString(INDEX_DEVICES_SERIALNUMBER));
				device.setStoragestatus(results.getString(INDEX_DEVICES_STORAGESTATUS));
				device.setNotes(results.getString(INDEX_DEVICES_NOTES));
				device.setWarrantyDate(results.getString(INDEX_DEVICES_WARRANTYDATE));
				device.setSoftware(results.getString(INDEX_DEVICES_OSSOFTWARE));
				device.setUserID(results.getInt(INDEX_DEVICES_USER));
				device.setLocalOffice(results.getString(INDEX_LAPTOPS_LOCALOFFICE));
				device.setManufacturer(results.getString(INDEX_LAPTOPS_MANUFACTURER));
				laptops.add(device);
			}

		} catch (SQLException e) {
			System.out.println("QUERY FAILED " + e.getMessage());

		}
	

	}
	public void queryUsers() {
		try (Statement statement = conn.createStatement();
				ResultSet results = statement.executeQuery("SELECT * FROM " + TABLE_USERS)) {

			this.users = FXCollections.observableArrayList();
			while (results.next()) {
				User user = new User();
				user.set_id(results.getInt(INDEX_USERS_ID));
				user.setName(results.getString(INDEX_USERS_NAME));
				user.setDeviceID(results.getInt(INDEX_USERS_DEVICE));
				user.setComputerID(results.getInt(INDEX_USERS_COMPUTER));
				user.setPhoneID(results.getInt(INDEX_USERS_PHONE));
				user.setLaptopID(results.getInt(INDEX_USERS_LAPTOP));
				users.add(user);
			}

		} catch (SQLException e) {
			System.out.println("QUERY FAILED " + e.getMessage());

		}
	
	}
	
	/*
	 * user methods
	 * 
	 * 
	 * 
	 * 
	 */
	
	

	public void createUser(String username) {
		if (!queryUserList(username.trim())) {
			try {
				//Add the item to the list as last,
				//We doen dit om de reden dat als er iets misgaat in de SQL statement we liever niet de item toevoegen
				User user = new User(username);
				user.set_id(idIteratorUsers());
				Statement statement = conn.createStatement();
				statement.execute(
						"INSERT INTO " + TABLE_USERS + " (" + COLUMN_USERS_NAME + " ) " + "VALUES('" + user.getName() + "')");
				this.users.add(user);
			} catch (SQLException e) {
				System.out.println("QUERY FAILED " + e.getMessage());
				e.printStackTrace();
			}

		}
	}
	private boolean queryUserList(String username) {
		// System.out.println(" User 0 in array ID = " + this.users.get(0).get_id() + "
		// User 1 in array id = " + this.users.get(1).get_id());

		for (User user : this.users) {
			if ((user.getName().equalsIgnoreCase(username)) || (username.isEmpty())) {//isempty werkde en null niet
				Alert alert = new Alert(AlertType.ERROR, "User already exists or the field was empty ...",
						ButtonType.OK);
				alert.getDialogPane().getStylesheets().add(getClass().getResource("myStyle.css").toExternalForm());
				alert.getDialogPane().getStyleClass().add("myStyle");
				alert.show();
				return true;
			}
		}
		return false;

	}

	private int findUser(User user) {
		for (int i = 0; i < this.users.size(); i++) {
			if (this.users.get(i).get_id() == (user.get_id())) {
				return i;
			}

		}
		return -1;
	}
	
	public int findUser(String string) {
		for(User user : this.users) {
			if(user.getName().equals(string.toUpperCase())) {
				return user.get_id();
			} 
			
			
		} createUser(string.toUpperCase());
		return findUser(string);
	}
	
	public User findUser(int id) {
		for (User user : this.users) {
			if((user.get_id() == id)) {
				return user;
			}

		}
		return null;
	}
	public String findUsername(int id) {
		for (User user : this.users) {
			if((user.get_id() == id)) {
				return user.getName();
			}

		}
		return null;
	}

	public void editSelectedUser(String newusername, User selecteduser) {
		if (!queryUserList(newusername)) {
			String selectedusername = selecteduser.getName(); // Voor efficientie, databinding duurde telang.
			int i = findUser(selecteduser);
			this.users.get(i).setName(newusername);
			try {

				Statement statement = conn.createStatement();
				statement.execute("UPDATE " + TABLE_USERS + " SET " + COLUMN_USERS_NAME + "='" + newusername
						+ "' WHERE " + COLUMN_USERS_NAME + "='" + selectedusername + "'");

			} catch (SQLException e) {
				System.out.println("QUERY FAILED " + e.getMessage());
			}
		}
	}
	
	private void setUserDevice(Computer computer) {
		User selectedUser = findUser(computer.getUserID()); //We vinden eerst de geselecteerde user, en returnen de referentie.
		selectedUser.setComputerID(computer.get_id()); //dan zetten we de computerID van de user met de computerobject zijn field.
	
				try {
					Statement statement =conn.createStatement();
					statement.execute("UPDATE " + TABLE_USERS + " SET " + COLUMN_USERS_COMPUTER + "=" + computer.get_id() 
						+ " WHERE " + COLUMN_USERS_NAME + "='" + selectedUser.getName() + "'");
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
		
	}
	
	private void deleteUserDevice(Computer computer) {
		for(User user : this.users) {
			if(user.getComputerID() == computer.get_id()) {
				user.setComputerID(0);;
				try {
					Statement statement =conn.createStatement();
				statement.execute("UPDATE " + TABLE_USERS + " SET " + COLUMN_USERS_COMPUTER + "=" + 0 
				+ " WHERE " + COLUMN_USERS_COMPUTER + "=" + computer.get_id() + "");
					
				} catch (SQLException e) {
					System.out.println(e);
				}
			}
			
		}
	}
	
	

	private void setUserDevice(Laptop laptop) {
		User selectedUser = findUser(laptop.getUserID());
		
		selectedUser.setLaptopID(laptop.get_id());
	
				try {
					Statement statement =conn.createStatement();
					statement.execute("UPDATE " + TABLE_USERS + " SET " + COLUMN_USERS_LAPTOP + "=" + laptop.get_id() 
						+ " WHERE " + COLUMN_USERS_NAME + "='" + selectedUser.getName() + "'");
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
		
	}
	private void deleteUserDevice(Laptop laptop) {
		for(User user : this.users) {
			if(user.getLaptopID() == laptop.get_id()) {
				user.setLaptopID(0);;
				try {
					Statement statement =conn.createStatement();
				statement.execute("UPDATE " + TABLE_USERS + " SET " + COLUMN_USERS_LAPTOP + "=" + 0 
				+ " WHERE " + COLUMN_USERS_LAPTOP + "=" + laptop.get_id() + "");
					
				} catch (SQLException e) {
					System.out.println(e);
				}
			}
			
		}
	}
	
	private void setUserDevice(Phone phone) {
		User selectedUser = findUser(phone.getUserID());
		selectedUser.setPhoneID(phone.get_id());
	
				try {
					Statement statement =conn.createStatement();
					statement.execute("UPDATE " + TABLE_USERS + " SET " + COLUMN_USERS_PHONE + "=" + phone.get_id() 
						+ " WHERE " + COLUMN_USERS_NAME + "='" + selectedUser.getName() + "'");
				
				} catch (SQLException e) {
					
					System.out.println(e.getMessage());
				}
		
	}
	
	private void deleteUserDevice(Phone phone) {
		for(User user : this.users) {
			if(user.getPhoneID() == phone.get_id()) {
				user.setPhoneID(0);;
				try {
					Statement statement =conn.createStatement();
				statement.execute("UPDATE " + TABLE_USERS + " SET " + COLUMN_USERS_PHONE + "=" + 0 
				+ " WHERE " + COLUMN_USERS_PHONE + "=" + phone.get_id() + "");
					
				} catch (SQLException e) {
					System.out.println(e);
				}
			}
			
		}
	}
	 
/*
* Computer methods
* 
* 
* 
* 
*/
	
	public void createComputer(Computer computer) {
		if (!queryComputerList(computer.getDeviceName())) { //Als de computer al bestaat op een andere positie met de zelfde naam is het true
			//Zoniet dan is het false en gaat de code door.
			try {
				//Add the item to the list as last,
				//We doen dit om de reden dat als er iets misgaat in de SQL statement we liever niet de item toevoegen
				computer.set_id(idIteratorComputers()); //Pakt de juiste id door naar de de laatste item te gaan in de List.
				Statement statement = conn.createStatement();
				statement.execute(
						"INSERT INTO " + TABLE_COMPUTERS + 
						" (" + COLUMN_DEVICES_DEVICENAME + ", " +
						COLUMN_DEVICES_MODELNAME + ", " + 
						COLUMN_DEVICES_SERIALNUMBER + ", " + 
						COLUMN_DEVICES_STORAGESTATUS + ", " +
						COLUMN_DEVICES_WARRANTYDATE + ", " +
						COLUMN_DEVICES_OSSOFTWARE + ", " +
						COLUMN_DEVICES_NOTES + ", " + 
						COLUMN_DEVICES_USER + ", " +
						COLUMN_COMPUTERS_LOCALOFFICE + " ) " + 
						"VALUES('" + computer.getDeviceName() + "', '" +
						computer.getModelName() + "', '" + 
						computer.getSerialNumber() + "', '" +
						computer.getStoragestatus() + "', '" +
						computer.getWarrantyDate() + "', '" +
						computer.getSoftware() + "', '" +
						computer.getNotes() + "', " +
						computer.getUserID() + ", '" + //0 if no user selected
						computer.getLocalOffice() + "')");
				this.computers.add(computer);
				if(computer.getUserID() != 0) {
					setUserDevice(computer);
				}
			} catch (SQLException e) {
				System.out.println("QUERY FAILED " + e.getMessage());
				e.printStackTrace();
			}

		}
	}
	
	private boolean queryComputerList(String devicename) {
		for (Computer computer : this.computers) {
			if ((computer.getDeviceName().equalsIgnoreCase(devicename)) || (devicename.isEmpty())) {//isempty werkde en null niet
				Alert alert = new Alert(AlertType.ERROR, "Device name is empty or already exists ...",
						ButtonType.OK);
				alert.getDialogPane().getStylesheets().add(getClass().getResource("myStyle.css").toExternalForm());
				alert.getDialogPane().getStyleClass().add("myStyle");
				alert.show();
				return true;
			}
		}
		return false;

	}
	//Nieuwe methode voor het editen van een computer die ook checkt of het wel NIET een andere ID is.
	//Anders kan je de huidige niet wijzigen door alleen de naam niet te veranderen.
	private boolean queryComputerList(Computer device) {
		for (Computer computer : this.computers) {
			if ((computer.getDeviceName().equalsIgnoreCase(device.getDeviceName())) && (computer.get_id() != device.get_id()) || (device.getDeviceName().isEmpty())) {//isempty werkde en null niet
				Alert alert = new Alert(AlertType.ERROR, "Device name is empty or already exists ...",
						ButtonType.OK);
				alert.getDialogPane().getStylesheets().add(getClass().getResource("myStyle.css").toExternalForm());
				alert.getDialogPane().getStyleClass().add("myStyle");
				alert.show();
				return true;
			}
		}
		return false;

	}
	
	public void editSelectedDevice(Computer olddevice, Computer newdevice) {
		newdevice.set_id(olddevice.get_id()); //ID van oude device geven aan nieuwe device.
		//Zo kunnen we ook van de nieuwe queryComputerList gebruik maken die zoekt op naam en nog is of de ID anders is.
		if (!queryComputerList(newdevice)) { //Als de computer al bestaat op een andere positie met de zelfde naam is het true
			//Zoniet dan is het false en gaat de code door.
			if(olddevice.getUserID() != 0) { //eerst verwijderen we de huidige device van de geselecteerde persoon in de userlist
				//en in de SQL database zetten we hem ook op 0, later in de code voegen we de nieuwe geselecteerde user toe ALS die er is.
				deleteUserDevice(olddevice);
			}
			try {
			Statement statement  = conn.createStatement();
			statement.execute("UPDATE " + TABLE_COMPUTERS + 
					" SET " + COLUMN_DEVICES_DEVICENAME +
					"='" + newdevice.getDeviceName() +
					"', " + COLUMN_DEVICES_MODELNAME +
					"='" + newdevice.getModelName() +
					"', " + COLUMN_DEVICES_SERIALNUMBER +
					"='" + newdevice.getSerialNumber() +
					"', " + COLUMN_DEVICES_STORAGESTATUS +
					"='" + newdevice.getStoragestatus() +
					"', " +  COLUMN_DEVICES_NOTES +
					"='" + newdevice.getNotes() +
					"', " + COLUMN_DEVICES_WARRANTYDATE +
					"='" + newdevice.getWarrantyDate() +
					"', " + COLUMN_DEVICES_OSSOFTWARE +
					"='" + newdevice.getSoftware() +
					"', " + COLUMN_DEVICES_USER +
					"=" + newdevice.getUserID() +
					", " + COLUMN_COMPUTERS_LOCALOFFICE +
					"='" + newdevice.getLocalOffice() +
					"' WHERE " + COLUMN_DEVICES_ID +
					" = " + olddevice.get_id());
			int index = findComputer(olddevice); //De computer vinden in de list, deze slaan we op in index 
			// Deze gebruiken we laten bij de computers.set methode om de geslecteerde device te vervangen met nieuwe item
			if(index < 0) {
				Alert alert = new Alert(AlertType.ERROR, "Something went wrong please restart the app...",
						ButtonType.OK);
				alert.getDialogPane().getStylesheets().add(getClass().getResource("myStyle.css").toExternalForm());
				alert.getDialogPane().getStyleClass().add("myStyle");
				alert.show();
			} else { this.computers.set(index, newdevice);
			
			if(newdevice.getUserID() != 0) { //methode uitvoeren om user te vervangen
				setUserDevice(newdevice);
			}		
			}
			} catch (Exception e) {
 			System.out.println(e.getMessage());
 		}
	}
	}
	
	private int findComputer(Computer selectedcomputer) {
		for(int i=0; i < this.computers.size(); i++) {
			if(this.computers.get(i).get_id() == selectedcomputer.get_id()) {
				return i;
			}
		} return -1;
	}
/*
* Laptop methods
* 
* 
* 
* 
*/
	
	
	public void createLaptop(Laptop laptop) {
		if (!queryLaptopsList(laptop.getDeviceName())) {
			try {
				//Add the item to the list as last,
				//We doen dit om de reden dat als er iets misgaat in de SQL statement we liever niet de item toevoegen
				laptop.set_id(idIteratorLaptops());
				Statement statement = conn.createStatement();
				statement.execute(
						"INSERT INTO " + TABLE_LAPTOPS + 
						" (" + COLUMN_DEVICES_DEVICENAME + ", " +
						COLUMN_DEVICES_MODELNAME + ", " + 
						COLUMN_DEVICES_SERIALNUMBER + ", " + 
						COLUMN_DEVICES_STORAGESTATUS + ", " +
						COLUMN_DEVICES_WARRANTYDATE + ", " +
						COLUMN_DEVICES_OSSOFTWARE + ", " +
						COLUMN_DEVICES_NOTES + ", " + 
						COLUMN_DEVICES_USER + ", " +
						COLUMN_LAPTOPS_LOCALOFFICE + ", " +
						COLUMN_LAPTOPS_MANUFACTURER + " ) " + 
						"VALUES('" + laptop.getDeviceName() + "', '" +
						laptop.getModelName() + "', '" + 
						laptop.getSerialNumber() + "', '" +
						laptop.getStoragestatus() + "', '" +
						laptop.getWarrantyDate() + "', '" +
						laptop.getSoftware() + "', '" +
						laptop.getNotes() + "', " +
						laptop.getUserID() + ", '" + 
						laptop.getLocalOffice() +  "', '" +
						laptop.getManufacturer() + "')");
					this.laptops.add(laptop);
					if(laptop.getUserID() != 0) {
						setUserDevice(laptop);
					}
			} catch (SQLException e) {
				System.out.println("QUERY FAILED " + e.getMessage());
				e.printStackTrace();
		
			}

		}
	}
	private boolean queryLaptopsList(String devicename) {
		for (Laptop laptop : this.laptops) {
			if ((laptop.getDeviceName().equalsIgnoreCase(devicename)) || (devicename.isEmpty())) { //isempty werkde en null niet
				Alert alert = new Alert(AlertType.ERROR, "Device name is empty or already exists ...",
						ButtonType.OK);
				alert.getDialogPane().getStylesheets().add(getClass().getResource("myStyle.css").toExternalForm());
				alert.getDialogPane().getStyleClass().add("myStyle");
				alert.show();
				return true;
			}
		}
		return false;

	}
	private boolean queryLaptopsList(Laptop device) {
		for (Laptop laptop : this.laptops) {
			if ((laptop.getDeviceName().equalsIgnoreCase(device.getDeviceName())) && (laptop.get_id() != device.get_id()) || (device.getDeviceName().isEmpty())) { //isempty werkde en null niet
				Alert alert = new Alert(AlertType.ERROR, "Device name is empty or already exists ...",
						ButtonType.OK);
				alert.getDialogPane().getStylesheets().add(getClass().getResource("myStyle.css").toExternalForm());
				alert.getDialogPane().getStyleClass().add("myStyle");
				alert.show();
				return true;
			}
		}
		return false;

	}
	public void editSelectedDevice(Laptop olddevice, Laptop newdevice) {
		newdevice.set_id(olddevice.get_id()); //ID van oude device geven aan nieuwe device.
		//Zo kunnen we ook van de nieuwe queryComputerList gebruik maken die zoekt op naam en nog is of de ID anders is.
		if(olddevice.getUserID() != 0) {
			deleteUserDevice(olddevice);
		}
		if (!queryLaptopsList(newdevice)) {
			try {
			Statement statement  = conn.createStatement();
			statement.execute("UPDATE " + TABLE_LAPTOPS + 
					" SET " + COLUMN_DEVICES_DEVICENAME +
					"='" + newdevice.getDeviceName() +
					"', " + COLUMN_DEVICES_MODELNAME +
					"='" + newdevice.getModelName() +
					"', " + COLUMN_DEVICES_SERIALNUMBER +
					"='" + newdevice.getSerialNumber() +
					"', " + COLUMN_DEVICES_STORAGESTATUS +
					"='" + newdevice.getStoragestatus() +
					"', " +  COLUMN_DEVICES_NOTES +
					"='" + newdevice.getNotes() +
					"', " + COLUMN_DEVICES_WARRANTYDATE +
					"='" + newdevice.getWarrantyDate() +
					"', " + COLUMN_DEVICES_OSSOFTWARE +
					"='" + newdevice.getSoftware() +
					"', " + COLUMN_DEVICES_USER +
					"=" + newdevice.getUserID() +
					", " + COLUMN_LAPTOPS_LOCALOFFICE +
					"='" + newdevice.getLocalOffice() +
					"', " + COLUMN_LAPTOPS_MANUFACTURER +
					"='" + newdevice.getManufacturer() +
					"' WHERE " + COLUMN_DEVICES_ID +
					" = " + olddevice.get_id());
			int index = findLaptop(olddevice);
			if(index < 0) {
				Alert alert = new Alert(AlertType.ERROR, "Something went wrong please restart the app...",
						ButtonType.OK);
				alert.getDialogPane().getStylesheets().add(getClass().getResource("myStyle.css").toExternalForm());
				alert.getDialogPane().getStyleClass().add("myStyle");
				alert.show();
			} else { this.laptops.set(index, newdevice);
			//Id van device moet nog gegeven worden aan nieuwe device zijn gegevens.
			}
			} catch (Exception e) {
 			System.out.println(e.getMessage());
 		}
	}
	}
	private int findLaptop(Laptop selectedlaptop) {
		for(int i=0; i < this.laptops.size(); i++) {
			if(this.laptops.get(i).get_id() == selectedlaptop.get_id()) {
				return i;
			}
		} return -1;
	}
	
	/*
	 * 
	 * Phone methods
	 * 
	 */
	
	public void createPhone(Phone phone) {
		if (!queryPhonesList(phone.getDeviceName())) {
			try { 
				//Add the item to the list as last,
				//We doen dit om de reden dat als er iets misgaat in de SQL statement we liever niet de item toevoegen
				phone.set_id(idIteratorPhones());
				Statement statement = conn.createStatement();
				statement.execute(
						"INSERT INTO " + TABLE_PHONES + 
						" (" + COLUMN_DEVICES_DEVICENAME + ", " +
						COLUMN_DEVICES_MODELNAME + ", " + 
						COLUMN_DEVICES_SERIALNUMBER + ", " + 
						COLUMN_DEVICES_STORAGESTATUS + ", " +
						COLUMN_DEVICES_WARRANTYDATE + ", " +
						COLUMN_DEVICES_OSSOFTWARE + ", " +
						COLUMN_DEVICES_NOTES + ", " + 
						COLUMN_DEVICES_USER + ", " +
						COLUMN_PHONES_MANUFACTURER + ", " +
						COLUMN_PHONES_PHONENUMBER + " ) " + 
						"VALUES('" + phone.getDeviceName() + "', '" +
						phone.getModelName() + "', '" + 
						phone.getSerialNumber() + "', '" +
						phone.getStoragestatus() + "', '" +
						phone.getWarrantyDate() + "', '" +
						phone.getSoftware() + "', '" +
						phone.getNotes() + "', " +
						phone.getUserID() + ", '" + 
						phone.getManufacturer() + "', '" +
						phone.getNumber() + "')");
				this.phones.add(phone);
				if(phone.getUserID() != 0) {
					setUserDevice(phone);
				}
			} catch (SQLException e) {
				System.out.println("QUERY FAILED " + e.getMessage());
				e.printStackTrace();
			} finally {
				
		}  
			
	}
}
	
	private boolean queryPhonesList(String devicename) {
		for (Phone phone: this.phones) {
			if ((phone.getDeviceName().equalsIgnoreCase(devicename)) || (devicename.isEmpty())) {//isempty werkde en null niet
				Alert alert = new Alert(AlertType.ERROR, "Device name is empty or already exists ...",
						ButtonType.OK);
				alert.getDialogPane().getStylesheets().add(getClass().getResource("myStyle.css").toExternalForm());
				alert.getDialogPane().getStyleClass().add("myStyle");
				alert.show();
				return true;
			}
		}
		return false;

	}
	private boolean queryPhonesList(Phone device) {
		for (Phone phone: this.phones) {
			if ((phone.getDeviceName().equalsIgnoreCase(device.getDeviceName())) && (phone.get_id() != device.get_id())|| (device.getDeviceName().isEmpty())) {//isempty werkde en null niet
				Alert alert = new Alert(AlertType.ERROR, "Device name is empty or already exists ...",
						ButtonType.OK);
				alert.getDialogPane().getStylesheets().add(getClass().getResource("myStyle.css").toExternalForm());
				alert.getDialogPane().getStyleClass().add("myStyle");
				alert.show();
				return true;
			}
		}
		return false;

	}
	public void editSelectedDevice(Phone olddevice, Phone newdevice) {
		newdevice.set_id(olddevice.get_id()); //ID van oude device geven aan nieuwe device.
		//Zo kunnen we ook van de nieuwe queryComputerList gebruik maken die zoekt op naam en nog is of de ID anders is.
		if(olddevice.getUserID() != 0) {
			deleteUserDevice(olddevice);
		}
		if (!queryPhonesList(newdevice)) {
			try {
			Statement statement  = conn.createStatement();
			statement.execute("UPDATE " + TABLE_PHONES + 
					" SET " + COLUMN_DEVICES_DEVICENAME +
					"='" + newdevice.getDeviceName() +
					"', " + COLUMN_DEVICES_MODELNAME +
					"='" + newdevice.getModelName() +
					"', " + COLUMN_DEVICES_SERIALNUMBER +
					"='" + newdevice.getSerialNumber() +
					"', " + COLUMN_DEVICES_STORAGESTATUS +
					"='" + newdevice.getStoragestatus() +
					"', " +  COLUMN_DEVICES_NOTES +
					"='" + newdevice.getNotes() +
					"', " + COLUMN_DEVICES_WARRANTYDATE +
					"='" + newdevice.getWarrantyDate() +
					"', " + COLUMN_DEVICES_OSSOFTWARE +
					"='" + newdevice.getSoftware() +
					"', " + COLUMN_DEVICES_USER +
					"=" + newdevice.getUserID() +
					", " + COLUMN_PHONES_MANUFACTURER +
					"='" + newdevice.getManufacturer() +
					"', " + COLUMN_PHONES_PHONENUMBER +
					"='" + newdevice.getNumber() +
					"' WHERE " + COLUMN_DEVICES_ID +
					" = " + olddevice.get_id());
			int index = findPhone(olddevice);
			if(index < 0) {
				Alert alert = new Alert(AlertType.ERROR, "Something went wrong please restart the app...",
						ButtonType.OK);
				alert.getDialogPane().getStylesheets().add(getClass().getResource("myStyle.css").toExternalForm());
				alert.getDialogPane().getStyleClass().add("myStyle");
				alert.show();
			} else { this.phones.set(index, newdevice);
			//Id van device moet nog gegeven worden aan nieuwe device zijn gegevens.
			}
			} catch (Exception e) {
 			System.out.println(e.getMessage());
 		}
	}
	}
	private int findPhone(Phone selectedphone) {
		for(int i=0; i < this.phones.size(); i++) {
			if(this.phones.get(i).get_id() == selectedphone.get_id()) {
				return i;
			}
		} return -1;
	}

	/*
	 * 
	 * device finder voor context menu
	 */

	
	public Device deviceFinder(String devicename) {
		for(Computer computer : this.computers) {
			if(computer.getDeviceName().equals(devicename)) {
				return computer;
			}
		}
			for (Laptop laptop : this.laptops) {
				if(laptop.getDeviceName().equals(devicename)) {
					return laptop;
				}
			}
			for (Phone phone : this.phones) {
				if(phone.getDeviceName().equals(devicename)) {
					return phone;
				}
		} return null;
	}
	/* Waarom maken we een IDIterator die de id van de laatste entree pakt?
	 * De reden hiervoor is omdat als we nieuwe users toevoegen in de database 
	 * Deze wel daar de correctie ID hebben maar niet in de actuele List. 
	 * Wat voor duplicate IDS kan zorgen in de list en dan bugs kunnen ontstaan.
	 */
	private int idIteratorUsers() {
		//belangrijk bij de methode de listIterator de parameter van de list size aantegeven anders krijg je een error.
		ListIterator<User> iterator = this.users.listIterator(this.users.size());
		if (iterator.hasPrevious()) { //als er geen previous entree is dan geven we de nummer 1 id.
			return iterator.previous().get_id() + 1;
		} else {
			return 1;
		}
		
	}
	
	private int idIteratorComputers() {
		ListIterator<Computer> iterator = this.computers.listIterator(this.computers.size());
		if (iterator.hasPrevious()) {
			return iterator.previous().get_id() + 1;
		} else {
			return 1;
		}
	}
	
	private int idIteratorLaptops() {
		ListIterator<Laptop> iterator = this.laptops.listIterator(this.laptops.size());
		if (iterator.hasPrevious()) {
			return iterator.previous().get_id() + 1;
		} else {
			return 1;
		}
	}
	
	private int idIteratorPhones() {
		ListIterator<Phone> iterator = this.phones.listIterator(this.phones.size());
		if (iterator.hasPrevious()) {
			return iterator.previous().get_id() + 1;
		} else {
			return 1;
		}
	}

	public void deleteSelectedItem(User selecteduser) {
		this.users.remove(selecteduser);
		try {

			Statement statement = conn.createStatement();
			statement.execute(
					"DELETE FROM " + TABLE_USERS + " WHERE " + COLUMN_USERS_NAME + "='" + selecteduser.getName() + "'");
			statement.execute("UPDATE " + TABLE_COMPUTERS + " SET " + COLUMN_DEVICES_USER + "=" + 0 
						+ " WHERE " + COLUMN_DEVICES_USER + "=" + selecteduser.get_id() + "");
			statement.execute("UPDATE " + TABLE_LAPTOPS + " SET " + COLUMN_DEVICES_USER + "=" + 0 
					+ " WHERE " + COLUMN_DEVICES_USER + "=" + selecteduser.get_id() + "");
			statement.execute("UPDATE " + TABLE_PHONES + " SET " + COLUMN_DEVICES_USER + "=" + 0 
					+ " WHERE " + COLUMN_DEVICES_USER + "=" + selecteduser.get_id() + "");
		} catch (SQLException e) {
			System.out.println("QUERY FAILED " + e.getMessage());
		}
	}

	public void deleteSelectedItem(Computer selectedcomputer) {
		this.computers.remove(selectedcomputer);
		try {

			Statement statement = conn.createStatement();
			statement.execute("DELETE FROM " + TABLE_COMPUTERS + " WHERE " + COLUMN_DEVICES_DEVICENAME + "='"
					+ selectedcomputer.getDeviceName() + "'");
			statement.execute("UPDATE " + TABLE_USERS + " SET " + COLUMN_USERS_COMPUTER + "=" + 0 
					+ " WHERE " + COLUMN_USERS_COMPUTER + "=" + selectedcomputer.get_id() + "");
		} catch (SQLException e) {
			System.out.println("QUERY FAILED " + e.getMessage());
		}
	}

	public void deleteSelectedItem(Laptop selectedlaptop) {
		this.laptops.remove(selectedlaptop);
		try {

			Statement statement = conn.createStatement();
			statement.execute("DELETE FROM " + TABLE_LAPTOPS + " WHERE " + COLUMN_DEVICES_DEVICENAME + "='"
					+ selectedlaptop.getDeviceName() + "'");
			statement.execute("UPDATE " + TABLE_USERS + " SET " + COLUMN_USERS_LAPTOP + "=" + 0 
					+ " WHERE " + COLUMN_USERS_LAPTOP + "=" + selectedlaptop.get_id() + "");
		} catch (SQLException e) {
			System.out.println("QUERY FAILED " + e.getMessage());
		}
	}

	public void deleteSelectedItem(Phone selectedphone) {
		this.phones.remove(selectedphone);
		try {

			Statement statement = conn.createStatement();
			statement.execute("DELETE FROM " + TABLE_PHONES + " WHERE " + COLUMN_DEVICES_DEVICENAME + "='"
					+ selectedphone.getDeviceName() + "'");
			statement.execute("UPDATE " + TABLE_USERS + " SET " + COLUMN_USERS_PHONE + "=" + 0 
					+ " WHERE " + COLUMN_USERS_PHONE + "=" + selectedphone.get_id() + "");
		} catch (SQLException e) {
			System.out.println("QUERY FAILED " + e.getMessage());
		}
	}

	
}

//public ObservableList<Device> queryDevices(int sortorder) {
//// Wanneer we een try catch clause maken met resources als parameters worden de
//// resources automatisch aan het einde gesloten.
//StringBuilder sb = new StringBuilder("SELECT * FROM ");
//sb.append(TABLE_DEVICES);
//if (sortorder != ORDER_BY_NONE) { // Normaal geen sortorder
//	sb.append(" ORDER BY ");
//	sb.append(COLUMN_DEVICES_DEVICENAME);
//	sb.append(" COLLATE NOCASE ");
//	if (sortorder == ORDER_BY_DESC) { // De sort order kan gezet worden op DESC
//		sb.append("DESC");
//	} else { // De default is automatisch de ASC sort order
//		sb.append("ASC");
//	}
//
//}
//try (Statement statement = conn.createStatement(); ResultSet results = statement.executeQuery(sb.toString())) {
//	// Statement.executeQuery, geven we de String van de query die we willen
//	// uitvoeren
//	ObservableList<Device> devices = FXCollections.observableArrayList();
//	while (results.next()) {
//		Device device = new Device();
//		device.set_id(results.getInt(INDEX_DEVICES_ID));
//		device.setDeviceName(results.getString(INDEX_DEVICES_DEVICENAME));
//		device.setModelName(results.getString(INDEX_DEVICES_MODELNAME));
//		device.setSerialNumber(results.getString(INDEX_DEVICES_SERIALNUMBER));
//		device.setStoragestatus(results.getString(INDEX_DEVICES_STORAGESTATUS));
//		device.setNotes(results.getString(INDEX_DEVICES_NOTES));
//		device.setWarrantyDate(results.getString(INDEX_DEVICES_WARRANTYDATE));
//		device.setSoftwareID(results.getString(INDEX_DEVICES_OSSOFTWARE));
//		device.setUserID(results.getInt(INDEX_DEVICES_USER));
//		devices.add(device);
//	}
//	return devices;
//
//} catch (SQLException e) {
//	System.out.println("QUERY FAILED " + e.getMessage());
//	return null;
//	/*
//	 * We catchen hier de query. In de finally block catchen we de results.close die
//	 * we proberen te sluiten En in de andere block catchen we de statement.close
//	 * die we proberen te sluiten Individueel in een try catch block is beter omdat
//	 * we dan weten welke specifiek is gefaald.
//	 */
//}
//
//}
