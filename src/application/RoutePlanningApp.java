package application;

import static java.nio.file.Files.readAllBytes;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.lynden.gmapsfx.GoogleMapView;

import controllers.RoutingController;
import gui.OuterPane;
import gui.RouteTab;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import services.Planner;

public class RoutePlanningApp extends Application{
	protected Stage primaryStage;
	protected OuterPane outerPane;
	private RoutingController routeController;
	
	private static String getAPIKey() {
		try {
			String path = "src/services/APIKey";
			return new String(readAllBytes(Paths.get(path)));
		}
		catch(IOException e) {
			e.printStackTrace();
			return "No APIKey found";
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		String apikey = getAPIKey();
		this.primaryStage = primaryStage;
		this.outerPane = new OuterPane(primaryStage, apikey);
		outerPane.getMapPane().setMapView(new GoogleMapView());
		outerPane.getMapPane().addInitialisedListener();

		List<RadioButton> radioButtons = new ArrayList<RadioButton>();
		
		RadioButton rbA = new RadioButton("Any");
		rbA.setUserData("Any");
		radioButtons.add(rbA);
		
		RadioButton rbW = new RadioButton("Walking");
		rbW.setUserData("Walk");
		rbW.setSelected(true);
		radioButtons.add(rbW);
		RadioButton rbP = new RadioButton("Public Transport");
		rbP.setUserData("PublicTransport");
		radioButtons.add(rbP);
		
		RadioButton rbD = new RadioButton("Driving");
		rbD.setUserData("Driving");
		radioButtons.add(rbD);
		
		RadioButton rbB = new RadioButton("Bicycling");
		rbB.setUserData("Bicycling");
		radioButtons.add(rbB);
		
		RouteTab rt = new RouteTab("Routing");
		TextField startAddr = new TextField();
	    TextField destAddr = new TextField();
	    Button addDestinationButton = new Button("Add stops");
	    Button searchButton = new Button("Search");
		outerPane.setupOuterPane(rt, startAddr, destAddr,
				addDestinationButton, searchButton, radioButtons);
		
		Planner p = new Planner(apikey);
		routeController = new RoutingController(p, rt, radioButtons, searchButton, addDestinationButton, outerPane.getMapPane());

		Scene scene = new Scene(outerPane);
		scene.getStylesheets().add("html/routing.css");
		primaryStage.setTitle("Itinerary Planner");
	    primaryStage.setScene(scene);
	    primaryStage.show();
	}
	
	public static void showErrorAlert(String header, String content) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Invalid addresses");
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}
}
