package services;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.Duration;
import com.google.maps.model.TravelMode;

public class MapGraph {
	// Java program to print all permutations of a 
	// given string. 
	private class Permutation 
	{ 
		/*public static void main(String[] args) 
		{ 
			String str = "ABC"; 
			int n = str.length(); 
			Permutation permutation = new Permutation(); 
			permutation.permute(str, 0, n-1); 
		}*/
		private ArrayList<String> permutationList;
		Permutation(){
			permutationList = new ArrayList<String>();
		}
		public ArrayList<String> getPermutationList() {
			return permutationList;
		}
		/** 
		* permutation function 
		* @param str string to calculate permutation for 
		* @param l starting index 
		* @param r end index 
		*/
		private void permute(String str, int l, int r) 
	    { 
	        if (l == r) 
	            System.out.println(str); 
	        else
	        { 
	            for (int i = l; i <= r; i++) 
	            { 
	                str = swap(str,l,i); 
	                permute(str, l+1, r); 
	                str = swap(str,l,i); 
	            } 
	        } 
	    } 

		/** 
		* Swap Characters at position 
		* @param a string value 
		* @param i position 1 
		* @param j position 2 
		* @return swapped string 
		*/
		private String swap(String a, int i, int j) 
		{ 
			char temp; 
			char[] charArray = a.toCharArray(); 
			temp = charArray[i] ; 
			charArray[i] = charArray[j]; 
			charArray[j] = temp; 
			return String.valueOf(charArray); 
		} 

	}

	private static Duration[][] fastestTimes;
    private static long optimalDuration = Long.MAX_VALUE;
    private static String optimalPath;
    private DirectionsRoute[][] fastestDirections;
	private TravelMode[][] fastestTransport;
    
	private static long procedure(int initial, int vertices[], String path, long costUntilHere) {
        // We concatenate the current path and the vertex taken as initial
        path = path + Integer.toString(initial);
        int length = vertices.length;
        long newCostUntilHere;

        // Exit case, if there are no more options to evaluate (last node)
        if (length == 0) {
            newCostUntilHere = costUntilHere + fastestTimes[initial][0].inSeconds;

            // If its cost is lower than the stored one
            if (newCostUntilHere < optimalDuration){
            	optimalDuration = newCostUntilHere;
                optimalPath = path;
            }
            return (fastestTimes[initial][0].inSeconds);
        }

        // If the current branch has higher cost than the stored one: stop traversing
        else if (costUntilHere > optimalDuration){
            return 0;
        }

        // Common case, when there are several nodes in the list
        else {
            int[][] newVertices = new int[length][(length - 1)];
            long costCurrentNode, costChild;
            long bestCost = Long.MAX_VALUE;
            // For each of the nodes of the list
            for (int i = 0; i < length; i++) {
                // Each recursion new vertices list is constructed
                for (int j = 0, k = 0; j < length; j++, k++) {
                    // The current child is not stored in the new vertices array
                    if (j == i) {
                        k--;
                        continue;
                    }
                    newVertices[i][k] = vertices[j];
                }

                // Cost of arriving the current node from its parent
                costCurrentNode = fastestTimes[initial][vertices[i]].inSeconds;

                // Here the cost to be passed to the recursive function is computed
                newCostUntilHere = costCurrentNode + costUntilHere;

                // RECURSIVE CALLS TO THE FUNCTION IN ORDER TO COMPUTE THE COSTS
                costChild = procedure(vertices[i], newVertices[i], path, newCostUntilHere);

                // The cost of every child + the current node cost is computed
                long totalCost = costChild + costCurrentNode;

                // Finally we select from the minimum from all possible children costs
                if (totalCost < bestCost) {
                    bestCost = totalCost;
                }
            }
            return (bestCost);
        }
    }
	
	private String createInputString(int nPlaces) {
		String tmp = "";
		for(int i = 0; i < nPlaces; ++i) {
			tmp += Integer.toString(i);
		}
		return tmp;
	}
	
	private long calculateTravelTime(String s) {
		long travelTime = 0;
		for(int i=0, j=1; j < s.length(); ++i, ++j) {
			travelTime += fastestTimes[i][i].inSeconds;
		}
		return travelTime;
	}
	
	private HashMap<Long, String> rankPermutations(ArrayList<String> permutationList){
		HashMap<Long, String> rankings = new HashMap<Long, String>();
		for(String s : permutationList) {
			rankings.put(calculateTravelTime(s), s);
		}
		return rankings;
	}
	
	public Duration[][] getFastestTimes(){
		return fastestTimes;
	}
	public void setFastestTimes(Duration[][] _fastestTimes) {
		fastestTimes = _fastestTimes;
	}
	public DirectionsRoute[][] getFastestDirections(){
		return fastestDirections;
	}
	public void setFastestDirections(DirectionsRoute[][] _fastestDirection) {
		fastestDirections = _fastestDirection;
	}
	public TravelMode[][] getFastestTransport(){
		return fastestTransport;
	}
	public void setFastestTransport(TravelMode[][] _fastestTransport) {
		fastestTransport = _fastestTransport;
	}
	public Itinerary getItinerary() {
		ArrayList<Duration> times = new ArrayList<Duration>();
		ArrayList<TravelMode> transportModes = new ArrayList<TravelMode>();
		int size = fastestTimes[0].length;
		String path = "";
		int[] vertices = new int[size - 1];
		// Filling the initial vertices array with the proper values
        for (int i = 1; i < size; i++) {
            vertices[i - 1] = i;
        }
        procedure(0, vertices, path, 0);
		
        ArrayList<Integer> order = new ArrayList<Integer>();
        for (int i = 0; i < optimalPath.length(); ++i) {
        	order.add(optimalPath.charAt(i) - '0');
        }
        for (int i = 0; i < order.size()-1; ++i) {
        	times.add( fastestTimes[order.get(i)][order.get(i+1)] );
        	transportModes.add(fastestTransport[order.get(i)][order.get(i+1)]);
        }
        return new Itinerary(order, times, transportModes);
	}
	
	public HashMap<Long, Itinerary> getRankedItineraries(){
		HashMap<Long, Itinerary> rankedItineraries = new HashMap<Long, Itinerary>();
		Permutation p = new Permutation();
		int nPlaces = fastestTimes[0].length;
		createInputString(nPlaces);
		p.permute(createInputString(nPlaces), 1, nPlaces-2);
		HashMap<Long, String> itineraryMap = rankPermutations(p.getPermutationList());
		for (Long time : itineraryMap.keySet()) {
			ArrayList<Duration> times = new ArrayList<Duration>();
			ArrayList<TravelMode> transportModes = new ArrayList<TravelMode>();
			ArrayList<Integer> order = new ArrayList<Integer>();
	        for (int i = 0; i < itineraryMap.get(time).length(); ++i) {
	        	order.add(itineraryMap.get(time).charAt(i) - '0');
	        }
	        for (int i = 0; i < order.size()-1; ++i) {
	        	times.add( fastestTimes[order.get(i)][order.get(i+1)] );
	        	transportModes.add(fastestTransport[order.get(i)][order.get(i+1)]);
	        }
	        rankedItineraries.put(time, new Itinerary(order, times, transportModes));
		}
		return rankedItineraries;
	}
}
