package application;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public abstract class Device implements Comparator <Device>{
	private int _id;
	private String deviceName;
	private String modelName;
	private String serialNumber;
	private Storagestatus Storagestatus; 
	private String notes;
	private LocalDate warrantyDate;
	private String software;
	private int userID;
	
	private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	
	
	public Device(int _id, String deviceName, String modelName, String serialNumber,
			Storagestatus storagestatus, String notes, LocalDate warrantyDate, String software, int userID) {
		this._id = _id;
		this.deviceName = deviceName;
		this.modelName = modelName;
		this.serialNumber = serialNumber;
		Storagestatus = storagestatus;
		this.notes = notes;
		this.warrantyDate = warrantyDate;
		this.software = software;
		this.userID = userID;
		
	}
	public int get_id() {
		return _id;
	}
	public String getDeviceName() {
		return deviceName.toUpperCase();
	}
	public String getModelName() {
		return modelName;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public String getStoragestatus() {
		return Storagestatus.toString();
	}
	public String getNotes() {
		return notes;
	}
	public String getWarrantyDate() {
		return warrantyDate.format(dateFormatter);
	}
	
	public LocalDate getWarrantyDateLocalDate() {
		return warrantyDate;
	}
	public String getSoftware() {
		return software;
	}
	public int getUserID() {
		return userID;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName.toUpperCase();
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public void setStoragestatus(Storagestatus storagestatus) {
		Storagestatus = storagestatus;
	}
	public void setStoragestatus(String storagestatus) {
		this.Storagestatus =application.Storagestatus.valueOf(storagestatus.toUpperCase());
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public void setWarrantyDate(LocalDate warrantyDate) {
		this.warrantyDate = warrantyDate;
	}
	public void setWarrantyDate(String warrantyDate) {
		this.warrantyDate = LocalDate.parse(warrantyDate, dateFormatter);
	}
	public void setSoftware(String software) {
		this.software = software;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public Device(int id,String deviceName, String modelName, String serialNumber, application.Storagestatus storagestatus,
			String notes, LocalDate warrantyDate, String software) { //Test purpose
		this._id = id;
		this.deviceName = deviceName;
		this.modelName = modelName;
		this.serialNumber = serialNumber;
		Storagestatus = storagestatus;
		this.notes = notes;
		this.warrantyDate = warrantyDate;
		this.software = software;
	}
	public Device() {
	}
	@Override
	public String toString() {
		return getDeviceName();
	}

	@Override
	public int compare(Device o1, Device o2) {
			return o1.getDeviceName().compareTo(o2.getDeviceName());
		
	}
}
