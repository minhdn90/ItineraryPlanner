package controllers;

import org.joda.time.*;

import com.google.maps.model.Duration;
import com.google.maps.model.TravelMode;
import java.util.ArrayList;
import java.util.List;

import application.RoutePlanningApp;
import geography.Place;
import gui.MapPane;
import gui.RouteTab;
import services.Itinerary;
import services.Planner;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.*;

public class RoutingController {
	public static final int TRANSIT = 2;
    public static final int WALKING = 1;
    public static final int DRIVING = 3;
    public static final int BICYCLING = 4;
    public static final int ANY = 5;
    
    private int selectedToggle = WALKING;
    
	private Planner planner;
    private ToggleGroup group;

	private TextField startAddr;
    private TextField endAddr;
    private Button searchButton;
    private Button addStopButton;
    private MapPane mapPane;
    private RouteTab routeTab;
    
    public RoutingController(Planner p, RouteTab rt, List<RadioButton> transportOptions, Button aSearchButton, Button aAddStopButton, MapPane mp) {
    	// save parameters
    	this.planner = p;
    	this.searchButton = aSearchButton;
    	this.addStopButton = aAddStopButton;
    	this.group = rt.getGroup();
    	this.mapPane = mp;
    	this.routeTab = rt;
    	setupSearchButton();
    	setupAddStopButton();
    	setupLabels();
    	setupToggle();
    }

	private void setupToggle() {
		// TODO Auto-generated method stub
		group.selectedToggleProperty().addListener( li -> {
            if(group.getSelectedToggle().getUserData().equals("Walk")) {
            	selectedToggle = WALKING;
            }
            else if(group.getSelectedToggle().getUserData().equals("PublicTransport")) {
            	selectedToggle = TRANSIT;
            }
            else if(group.getSelectedToggle().getUserData().equals("Driving")) {
            	selectedToggle = DRIVING;
            }
            else if(group.getSelectedToggle().getUserData().equals("Bicycling")) {
            	selectedToggle = BICYCLING;
            }
            else if(group.getSelectedToggle().getUserData().equals("Any")) {
            	selectedToggle = ANY;
            }
            else {
            	System.err.println("Invalid radio button selection");
            }
    	});
	}

	private void setupLabels() {
		// TODO Auto-generated method stub
		
	}

	private void setupAddStopButton() {
		// TODO Auto-generated method stub
		addStopButton.setOnAction(e -> {
			TextField stopAddr = new TextField();
			HBox aStop = new HBox();
			aStop.getChildren().add(new Label("Stop:"));
			aStop.getChildren().add(stopAddr);	    
			aStop.setSpacing(20);
			routeTab.addAStop(aStop);
		});
	}

	private void setupSearchButton() {
		// TODO Auto-generated method stub
		searchButton.setOnAction(e -> {
			ArrayList<Place> originalPlaceList = routeTab.getPlaceList();
        	ArrayList<String> originalAddressList = new ArrayList<String>();
        	for(Place p : originalPlaceList) {
        		originalAddressList.add(p.getAddress());
        	}
        	if(!originalAddressList.get(0).isEmpty()
        		&& !originalAddressList.get(originalAddressList.size()-1).isEmpty()) {
	        	planner.createGraph(originalAddressList, selectedToggle);
	        	Itinerary it = planner.createPlan();
	        	ArrayList<Integer> order = it.getOrder();
	    		ArrayList<Duration> howlong = it.getTimes();
	    		ArrayList<TravelMode> travelModes = it.getTransportModes();
	        	//ArrayList<String> orderedAddressList = new ArrayList<String>();
	        	ArrayList<Place> orderedPlaceList = new ArrayList<Place>();
	        	for(int i : order) {
	        		//orderedAddressList.add(originalAddressList.get(i));
	        		orderedPlaceList.add(originalPlaceList.get(i));
	        	}
	        	mapPane.displayPlan(orderedPlaceList, travelModes);
            }
            else {
            	RoutePlanningApp.showErrorAlert("Route Display Error", "Make sure to choose points for both start and destination.");
            }
		});
	}
}