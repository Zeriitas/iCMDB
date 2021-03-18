package application;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import javafx.stage.Stage;

public class JUnitTest {


	@Test
	public void test1() {
		SQLData.getInstance().open();
	}
	
	@Test
	public void test2() {
		Computer computer = new Computer();
		computer.setDeviceName("Test-001");
		computer.setModelName("TestModel001");
		computer.setStoragestatus("STORED");
		computer.setSerialNumber("00000-00000-00000");
		computer.setWarrantyDate("01-01-2000");
		computer.setSoftware("11111-11111-11111");
		computer.setNotes("Test");
		computer.setUserID(0);
		computer.setLocalOffice("22222-22222-22222");
		System.out.println(computer);
		SQLData.getInstance().createComputer(computer);
	}

}
