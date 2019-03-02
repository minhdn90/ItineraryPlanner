package geography;

import static java.nio.file.Files.readAllBytes;

import java.io.IOException;
import java.nio.file.Paths;
import com.google.maps.*;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.PlaceDetails;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;
import com.lynden.gmapsfx.javascript.object.LatLong;

import org.joda.time.LocalTime;

public class Place {
	private String name;
	public String getName() {
		return name;
	}
	private String placeID;
	private String address;
	public String getAddress() {
		return address;
	}
	private LatLong coordinate;
	public LatLong getCoordinate() {
		return coordinate;
	}
	private LocalTime timeOpen;
	private LocalTime timeClosed;
	private static String getAPIKey() {
		try {
			String apikey = new String(readAllBytes(Paths.get("src/services/APIKey")));
			return apikey;
		}
		catch(IOException e) {return "No API Key";}
	}
	public Place(String textSearch) {
		GeoApiContext.Builder contextBuilder = new GeoApiContext.Builder();
		contextBuilder.apiKey(getAPIKey());
		TextSearchRequest searchRequest = new TextSearchRequest(contextBuilder.build());
		searchRequest.query(textSearch);
		try {
			PlacesSearchResponse rsp = searchRequest.await();
			PlacesSearchResult resultPlace = rsp.results[0];
			placeID = resultPlace.placeId;
			PlaceDetailsRequest placeDetailsRequest = new PlaceDetailsRequest(contextBuilder.build());
			placeDetailsRequest.placeId(placeID);
			PlaceDetails details = placeDetailsRequest.await();
			address = details.formattedAddress;
			name = details.name;
			coordinate = new LatLong(details.geometry.location.lat,
									 details.geometry.location.lng);
			/*if(resultPlace.openingHours != null)
				timeOpen = resultPlace.openingHours.periods[0].open.time;
			if(resultPlace.openingHours != null)
				timeClosed = resultPlace.openingHours.periods[0].close.time;*/
		} catch (ApiException e) {
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
}
