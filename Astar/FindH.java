
/*  Author: Brenden Davey
	Course: CIS 421 Artificial Intelligence
	Assignment 3
*/

import java.util.*;

// class to find h(n)
public class FindH {

	Map<String, Integer> h = new HashMap<String, Integer>();
	String location;
	int distance;

	// parameter: Scanner sc - scanner on input file
	// pre-condition: scanner is given a file with correct formatting
	// post-condition: Map<String, Integer>> h is created, holding
	// each map of the locations and cost to destination
	public FindH(Scanner sc) {
		while (sc.hasNextLine()) {
			location = sc.next();
			while (!sc.hasNextInt()) {
				location += " " + sc.next();
			}
			distance = sc.nextInt();
			h.put(location, distance);
		}
	}

	// parameter: String current which is the current location on the path
	// returns: an int - the distance from the current location to the destination
	public int getDistance(String current) {
		if (current.equals("Iron Hills"))
			return 0;
		return h.get(current);

	}

}