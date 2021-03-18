package application;

import java.time.LocalDate;

public class Phone extends Device {

	private String number;
	private String manufacturer;
	public Phone() {
	}
	public String getNumber() {
		return number;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public void setNumber(int number) {
		this.number = Integer.toString(number);
	}
	public void setNumber(long number) {
		this.number = Long.toString(number);
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public Phone(int _id, String deviceName, String modelName, String serialNumber,
			application.Storagestatus storagestatus, String notes, LocalDate warrantyDate, String software,
			String number, String manufacturer) {
		super(_id, deviceName, modelName, serialNumber, storagestatus, notes, warrantyDate, software);
		this.number = number;
		this.manufacturer = manufacturer;
	}
	@Override
	public String toString() {
		return super.getDeviceName();
	}

	
}
