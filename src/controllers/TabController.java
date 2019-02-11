package controllers;

import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TabController {
	static final int MAX_MID_STOPS = 5;
	Tab routeTab;
	static int midStopCounter = 0;
	public TabController(Tab aTab) {
		routeTab = aTab;
	}
	public void addAStop(HBox aBox) {
		if(midStopCounter == MAX_MID_STOPS) return;
		((VBox)routeTab.getContent()).getChildren().add(midStopCounter + 1, aBox);
		++midStopCounter;
	}
	public Tab getRouteTab() {
		return routeTab;
	}
}
