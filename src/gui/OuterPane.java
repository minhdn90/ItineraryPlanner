package gui;

import java.util.List;

import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class OuterPane extends BorderPane{
	private MapPane mapPane;
	private TabPane routeTabPane;
	public OuterPane(Stage aStage, String aKey) {
		this.mapPane = new MapPane(aStage, aKey);
	}
	public void setupOuterPane(RouteTab routeTab, TextField startAddr, TextField destAddr, Button addDestinationButton, Button searchButton, List<RadioButton> transportOptions) {
		routeTab.setupRouteTab(startAddr, destAddr, addDestinationButton, searchButton, transportOptions);
	    routeTabPane = new TabPane(routeTab);
	    routeTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
	    this.setRight(routeTabPane);
		this.setCenter(mapPane.getMapView());
		
	}
	public TabPane getRouteTabPane() {
		return routeTabPane;
	}
	public MapPane getMapPane() {
		return mapPane;
	}
}
