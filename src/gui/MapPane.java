package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.google.maps.model.Duration;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;
import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.MapTypeIdEnum;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;
import com.lynden.gmapsfx.service.directions.DirectionStatus;
import com.lynden.gmapsfx.service.directions.DirectionsRenderer;
import com.lynden.gmapsfx.service.directions.DirectionsRequest;
import com.lynden.gmapsfx.service.directions.DirectionsResult;
import com.lynden.gmapsfx.service.directions.DirectionsService;
import com.lynden.gmapsfx.service.directions.DirectionsServiceCallback;
import com.lynden.gmapsfx.service.directions.TravelModes;

import controllers.RoutingController;
import geography.Place;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MapPane extends TabPane implements MapComponentInitializedListener, DirectionsServiceCallback, Initializable {
	private String apikey;
	protected GoogleMapView mapView;
	public GoogleMapView getMapView() {
		return mapView;
	}

	public void setMapView(GoogleMapView mapView) {
		this.mapView = mapView;
		this.mapView.setKey(apikey);
	}
	private Stage primaryStage;
	protected GoogleMap map;
	protected DirectionsService directionsService;
    
    public MapPane (Stage aStage, String akey) {
    	this.primaryStage = aStage;
    	this.apikey = akey;
    }

	@Override
	public void mapInitialized() {
		// TODO Auto-generated method stub
		MapOptions mapOptions = new MapOptions();
	    mapOptions.center(new LatLong(51.505054, -0.420994))
	       .mapType(MapTypeIdEnum.ROADMAP)
	       //maybe set false
	       .mapTypeControl(true)
	       .overviewMapControl(false)
	       .panControl(true)
	       .rotateControl(false)
	       .scaleControl(false)
	       .streetViewControl(false)
	       .zoom(14)
	       .zoomControl(true);
	    map = mapView.createMap(mapOptions);
	    setupJSAlerts(mapView.getWebview());
	    directionsService = new DirectionsService();
	}
	
	public void addInitialisedListener() {
		mapView.addMapInitializedListener(this);
	}
	
	private void addMarkers(ArrayList<Place> orderedPlaces) {
		final String labels[] = {"A", "B", "C", "D", "E", "F", "G"};
		for (int i = 0; i < orderedPlaces.size(); ++i) {
			final MarkerOptions markerOptions = new MarkerOptions();
			markerOptions.label(labels[i])
						 .position(orderedPlaces.get(i).getCoordinate());
			final Marker m = new Marker(markerOptions);
			map.addMarker(m);
		}
	}
	
	public void displayPlan(ArrayList<Place> orderedPlaces, ArrayList<TravelMode> travelModes) {
		for (int i = 0; i < orderedPlaces.size()-1; ++i) {
			LatLong latlongOrigin = orderedPlaces.get(i).getCoordinate();
			LatLong latlongDestination = orderedPlaces.get(i+1).getCoordinate();
			DirectionsRequest dirReq = new DirectionsRequest(latlongOrigin,
															 latlongDestination,
															 convertFromGGTravelMode(travelModes.get(i)),
															 false);
			DirectionsRenderer dirRenderer = new DirectionsRenderer(true, map, mapView.getDirec());
			dirRenderer.setSuppressMarkers(true);
			directionsService.getRoute(dirReq, this, dirRenderer);
        }
		addMarkers(orderedPlaces);
	}

	private TravelModes convertFromGGTravelMode(TravelMode travelMode) {
		// TODO Auto-generated method stub
		switch(travelMode) {
		case WALKING:
			return TravelModes.WALKING;
		case BICYCLING:
			return TravelModes.BICYCLING;
		case DRIVING:
			return TravelModes.DRIVING;
		case TRANSIT:
			return TravelModes.TRANSIT;
		default :
			return null;
		}
	}

	@Override
	public void directionsReceived(DirectionsResult arg0, DirectionStatus arg1) {
		// TODO Auto-generated method stub
	}
	
	public void setupJSAlerts(WebView webView) {
	    webView.getEngine().setOnAlert( e -> {
	        Stage popup = new Stage();
	        popup.initOwner(primaryStage);
	        popup.initStyle(StageStyle.UTILITY);
	        popup.initModality(Modality.WINDOW_MODAL);

	        StackPane content = new StackPane();
	        content.getChildren().setAll(
	          new Label(e.getData())
	        );
	        content.setPrefSize(200, 100);

	        popup.setScene(new Scene(content));
	        popup.showAndWait();
	    });
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		mapView.addMapInitializedListener(this);
	}
}
