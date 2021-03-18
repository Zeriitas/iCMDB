package application;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

import com.sun.glass.ui.Screen;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.fxml.FXMLLoader;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws IOException {
//		try {

//			Parent loginScreen = FXMLLoader.load(getClass().getResource("Login.fxml"));
//			Scene loginScene = new Scene(loginScreen);
//			System.out.println(primaryStage.getRenderScaleX());

		// primaryStage.setResizable(false);
		// 1e stap in de applicatie is een bestaande database selecteren of een nieuwe
		// maken.

		/*
		 * Database controller object aanmaken en draw methode uitvoeren De methode
		 * roept de fxml loader aan.
		 */
		try {

			DatabaseController dbcontroller = new DatabaseController();
			dbcontroller.draw();

			Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
			Scene scene = new Scene(root, 1920, 1080);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {

		}

	}

	public static void main(String[] args) {
		launch(args);

	}
}
