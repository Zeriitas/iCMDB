package application;

import java.time.LocalDate;

public class Computer extends Device{
private String localOffice;

public String getLocalOffice() {
	return localOffice;
}

public void setLocalOffice(String localOffice) {
	this.localOffice = localOffice;
}

public Computer(int _id, String deviceName, String modelName, String serialNumber,
		application.Storagestatus storagestatus, String notes, LocalDate warrantyDate, String software,
		String localOffice) {
	super(_id, deviceName, modelName, serialNumber, storagestatus, notes, warrantyDate, software);
	this.localOffice = localOffice;
}

public Computer() {
}

@Override
public String toString() {
	return super.getDeviceName();


}


}




