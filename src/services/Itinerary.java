package services;

import java.util.ArrayList;

import com.google.maps.model.Duration;
import com.google.maps.model.TravelMode;

public class Itinerary {
	private ArrayList<Integer> order;
	private ArrayList<Duration> times;
	private ArrayList<TravelMode> transportModes;
	public Itinerary(ArrayList<Integer> _order, ArrayList<Duration> _times, ArrayList<TravelMode> _transportModes) {
		order = _order;
		times = _times;
		transportModes = _transportModes;
	}
	public ArrayList<Integer> getOrder() {
		return order;
	}
	public void setOrder(ArrayList<Integer> order) {
		this.order = order;
	}
	public ArrayList<Duration> getTimes() {
		return times;
	}
	public void setTimes(ArrayList<Duration> times) {
		this.times = times;
	}
	public ArrayList<TravelMode> getTransportModes() {
		return transportModes;
	}
	public void setTransportModes(ArrayList<TravelMode> transportModes) {
		this.transportModes = transportModes;
	}
}
