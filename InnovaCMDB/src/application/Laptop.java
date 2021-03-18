package application;

import java.time.LocalDate;

public class Laptop extends Device {
private String manufacturer;
private String localOffice;
public String getManufacturer() {
	return manufacturer;
}
public String getLocalOffice() {
	return localOffice;
}
public void setManufacturer(String manuFacturer) {
	this.manufacturer = manuFacturer;
}
public void setLocalOffice(String localOffice) {
	this.localOffice = localOffice;
}
public Laptop(int _id, String deviceName, String modelName, String serialNumber,
		application.Storagestatus storagestatus, String notes, LocalDate warrantyDate, String software,
		String manufacturer, String localOffice) {
	super(_id, deviceName, modelName, serialNumber, storagestatus, notes, warrantyDate, software);
	this.manufacturer = manufacturer;
	this.localOffice = localOffice;
}
public Laptop() {
	// TODO Auto-generated constructor stub
}
@Override
public String toString() {
	return super.getDeviceName();
}






}
