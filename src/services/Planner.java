package services;
import java.nio.file.Paths;
import org.joda.time.*;
import java.util.ArrayList;
import java.util.Scanner;

import com.google.maps.DirectionsApiRequest;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.Duration;
import com.google.maps.model.TravelMode;
import static java.nio.file.Files.readAllBytes;

import com.lynden.gmapsfx.service.directions.DirectionsRenderer;
import com.lynden.gmapsfx.service.directions.DirectionsRequest;
import com.lynden.gmapsfx.service.directions.DirectionsService;
import com.lynden.gmapsfx.service.directions.TravelModes;

import controllers.RoutingController;

import java.io.IOException;

import geography.Place;

import com.lynden.gmapsfx.javascript.object.DirectionsPane;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.GoogleMapView;

public class Planner {
	private MapGraph graph;
	private String apiKey;
	
	public Planner(String aKey) {
		// TODO Auto-generated constructor stub
		apiKey = aKey;
		graph = new MapGraph();
	}
	
	//private long get(String origin, String dest, long fastestDuration)
	
	public void createGraph(ArrayList<String> addresses, int selectedToggle) {
		String[] placesToGo = addresses.toArray(new String[0]);
		int nPlacesToGo = placesToGo.length;
		Duration minDurations[][] = new Duration[nPlacesToGo][nPlacesToGo];
		TravelMode fastestMode[][] = new TravelMode[nPlacesToGo][nPlacesToGo];
		ArrayList<TravelMode> possibleTravelModes = new ArrayList<TravelMode>();
		//DirectionsRoute fastestRoute[][] = new DirectionsRoute[nPlacesToGo][nPlacesToGo];
		switch (selectedToggle) {
		case RoutingController.WALKING:
			possibleTravelModes.add(TravelMode.WALKING);
			break;
		case RoutingController.BICYCLING:
			possibleTravelModes.add(TravelMode.BICYCLING);
			break;
		case RoutingController.DRIVING:
			possibleTravelModes.add(TravelMode.DRIVING);
			break;
		case RoutingController.TRANSIT:
			possibleTravelModes.add(TravelMode.TRANSIT);
			break;
		case RoutingController.ANY:
			for(TravelMode tm : TravelMode.values()) {
				possibleTravelModes.add(tm);
			}
			break;
		default:
			break;	
		}
		for(TravelMode tm : possibleTravelModes) {
			if(tm == TravelMode.UNKNOWN) continue;
			GeoApiContext context = new GeoApiContext.Builder().apiKey(apiKey).build();
			DistanceMatrixApiRequest distanceMatrixReq
										= DistanceMatrixApi.newRequest(context);
			distanceMatrixReq.origins(placesToGo);
			distanceMatrixReq.destinations(placesToGo);
			distanceMatrixReq.mode(tm);
			try {
				DistanceMatrix distanceMatrix = distanceMatrixReq.await();
				for (int i = 0; i < nPlacesToGo; ++i) {
					for (int j = 0; j < nPlacesToGo; ++j) {
						if (i != j) {
							if(i == nPlacesToGo - 1 && j == 0) {
								minDurations[i][j] = new Duration();
								minDurations[i][j].inSeconds = 0;
								fastestMode[i][j] = TravelMode.UNKNOWN;
							}
							else if (minDurations[i][j] == null
								|| minDurations[i][j].inSeconds > distanceMatrix.rows[i].elements[j].duration.inSeconds) {
								minDurations[i][j] = distanceMatrix.rows[i].elements[j].duration;
								fastestMode[i][j] = tm;
							}
						}
					}
				}
			}catch (ApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		/*try {
			for (int i = 0; i < placesToGo.length; ++i) {
				for (int j = 0; j < placesToGo.length; ++j) {
					if(i !=j ) {
						GeoApiContext context = new GeoApiContext.Builder().apiKey(apiKey).build();
						DirectionsApiRequest directionReq = new DirectionsApiRequest(context);
						directionReq.origin(placesToGo[i]);
						directionReq.destination(placesToGo[j]);
						directionReq.mode(fastestMode[i][j]);
						DirectionsResult dirResults = directionReq.await();
						fastestRoute[i][j] = dirResults.routes[0];
					}
				}
			}
			
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		graph.setFastestTimes(minDurations);
		graph.setFastestTransport(fastestMode);
		//graph.setFastestDirections(fastestRoute);
	}
	
	public Itinerary createPlan() {
		return graph.getItinerary();
	}
	
}
