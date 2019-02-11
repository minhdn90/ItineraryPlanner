package gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import geography.Place;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class RouteTab extends Tab{
	private ToggleGroup group;
    public ToggleGroup getGroup() {
		return group;
	}
    private TextField startAddr;
    private TextField endAddr;
    private Button searchButton;
    private Button addStopButton;
    private static int midStopCounter = 0;
	private static final int MAX_MID_STOPS = 5;
	private Map<Integer, Place> placeMap;
	public ArrayList<Place> getPlaceList() {
		ArrayList<Place> validPlaceList = new ArrayList<Place>();
		for (Place p : placeMap.values()) {
			validPlaceList.add(p);
		}
		return validPlaceList;
	}
	private void setupToggle(List<RadioButton> radioButtons) {
		for(RadioButton rb : radioButtons) {
			rb.setToggleGroup(group);
		}
	}
	/*
	private ArrayList<String> getRawAddressList(){
		ArrayList<String> addressList = new ArrayList<String>();
		VBox tabContent = (VBox) this.getContent();
		for(Node n : tabContent.getChildren()) {
			if(n instanceof HBox) {
				String userSearchText = ((TextField)(((HBox)n).getChildren()
										.get(1)))
										.getText();
				String searchText = userSearchText.strip();
				if (!searchText.isEmpty())
					addressList.add(searchText);
			}
		}
		return addressList;
	}*/
	private void updatePlaceList(String address, int whichToBeUpdated) {
		Place p = new Place(address);
		placeMap.put(whichToBeUpdated, p);
	}
	
	public RouteTab(String tabName) {
		super(tabName);
		group = new ToggleGroup();
		placeMap = new HashMap<Integer, Place>();
	}
	public void setupRouteTab(TextField startAddr, TextField destAddr,
								Button addDestinationButton, Button searchButton,
								List<RadioButton> transportOptions) {
		VBox v = new VBox();
		HBox startBox = new HBox();
		startAddr.setOnKeyPressed(ke ->
	    {
            if (ke.getCode().equals(KeyCode.ENTER)) {
            	updatePlaceList(startAddr.getText(), 0);
            	startAddr.setText(placeMap.get(0).getName());
            }
	    });
	    startBox.getChildren().add(new Label("Start:"));
	    startBox.getChildren().add(startAddr);
	    startBox.setSpacing(20);
	    HBox destinationBox = new HBox();
	    destAddr.setOnKeyPressed(ke ->
	    {
            if (ke.getCode().equals(KeyCode.ENTER)) {
            	updatePlaceList(destAddr.getText(), 6);
            	destAddr.setText(placeMap.get(6).getName());
            }
	    });
	    destinationBox.getChildren().add(new Label("Dest:"));
	    destinationBox.getChildren().add(destAddr);	    
	    destinationBox.setSpacing(20);
	    v.getChildren().add(startBox);
	    v.getChildren().add(destinationBox);
	    v.getChildren().add(addDestinationButton);
	    v.getChildren().add(searchButton);
	    setupToggle(transportOptions);
	    for (RadioButton rb : transportOptions) {
	    	v.getChildren().add(rb);
	    }
	    this.setContent(v);
	}
	public void addAStop(HBox aBox) {
		if(midStopCounter == MAX_MID_STOPS) return;
		int textFieldPosition = midStopCounter + 1;
		((VBox)this.getContent()).getChildren().add(textFieldPosition, aBox);
		TextField tf = (TextField)((aBox).getChildren()).get(1);
		tf.setOnKeyPressed(ke ->
	    {
            if (ke.getCode().equals(KeyCode.ENTER)) {
            	updatePlaceList(tf.getText(), textFieldPosition);
            	tf.setText(placeMap.get(textFieldPosition).getName());
            }
	    });
		++midStopCounter;
	}
	/*public ArrayList<String> getDestinationList(){
		ArrayList<String> addressList = new ArrayList<String>();
		VBox tabContent = (VBox) this.getContent();
		for(Node n : tabContent.getChildren()) {
			if(n instanceof HBox) {
				addressList.add(((TextField)(((HBox)n).getChildren().get(1))).getText());
			}
		}
		return addressList;
	}*/
}
