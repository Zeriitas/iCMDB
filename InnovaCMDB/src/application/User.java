package application;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class User {
	private int _id;
	private String name;
	private int deviceID;
	private int computerID;
	private int laptopID;
	private int phoneID;
	public int get_id() {
		return _id;
	}
	public String getName() {
		return name.toUpperCase();
	}
	public int getDeviceID() {
		return deviceID;
	}
	public int getComputerID() {
		return computerID;
	}
	public int getLaptopID() {
		return laptopID;
	}
	public int getPhoneID() {
		return phoneID;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public void setName(String name) {
		this.name = name.toUpperCase().trim();
	}
	public void setDeviceID(int deviceID) {
		this.deviceID = deviceID;
	}
	public void setComputerID(int computerID) {
		this.computerID = computerID;
	}
	public void setLaptopID(int laptopID) {
		this.laptopID = laptopID;
	}
	public void setPhoneID(int phoneID) {
		this.phoneID = phoneID;
	}
	public User( String name) {
		this.name = name.toUpperCase().trim();
	
	}
	public User() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return  name.toUpperCase();
	}
	
	
}