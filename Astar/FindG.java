
/*  Author: Brenden Davey
	Course: CIS 421 Artificial Intelligence
	Assignment 3
*/

import java.util.*;

// finds g(n)
public class FindG {

	Map<String, Map<String, int[]>> g = new HashMap<String, Map<String, int[]>>();
	String currentLocation;
	String destination;
	int distance;
	String line;
	int count;
	int roadQuality;
	int riskLevel;
	int winterTravel;
	int speed;
	String shortest;

	// parameter: Scanner sc - scanner on input file
	// pre-condition: scanner is given a file with correct formatting
	// post-condition: Map<String, Map<String>, Integer>> g is created, holding
	// each map of the destinates and costs
	public FindG(Scanner sc) {
		count = 0;
		shortest = "";
		// While there is another line in the file, process it
		while (sc.hasNextLine()) {
			Map<String, int[]> pairs = new HashMap<String, int[]>();
			// Make new map
			pairs.clear();

			currentLocation = sc.nextLine();

			line = sc.nextLine();
			// Second Scanner to process one line at a time
			Scanner lineScanner = new Scanner(line);
			// While there is more on the line, process it
			while (lineScanner.hasNext()) {
				destination = lineScanner.next();
				while (!lineScanner.hasNextInt()) {
					destination += " " + lineScanner.next();
				}
				int conditions[] = new int[4];

				// Adds each condition to a variable
				distance = lineScanner.nextInt();
				roadQuality = lineScanner.nextInt();
				riskLevel = lineScanner.nextInt();
				winterTravel = lineScanner.nextInt();
				// Adds each variable to an array
				conditions[0] = distance;
				conditions[1] = 100 - roadQuality;
				conditions[2] = riskLevel;
				conditions[3] = 25 - winterTravel;

				pairs.put(destination, conditions);
			}
			// sets pairs as the value for currentLocation
			g.put(currentLocation, pairs);
			count++;
		}
	}

	// param: String current - current location
	// param: FindH h - a map for the h(n) values,
	// param: String season - which season it is
	// param: String version - which version to run
	// pre-condition: g and h were created; season and version are valid
	// returns: an int - distance from parent to current node
	public int GetLowest(String current, FindH h, String season, String version) {
		int hValue = 0;
		int gValue = 0;
		int[] temp1 = new int[4];
		String shortest = " ";
		int lowest = 10000000;
		int lowestG = 0;
		for (Map.Entry<String, int[]> entry : g.get(current).entrySet()) {
			temp1 = entry.getValue();
			if ((entry.getKey()).equals("Iron Hills")) {
				hValue = 0;
				temp1 = entry.getValue();
			} else {
				temp1 = entry.getValue();
				if (version.equals("1")) {
					gValue = temp1[0];
					hValue = h.getDistance(entry.getKey());
				} else if (version.equals("2")) {
					gValue = (temp1[0] * 5 * (temp1[2] / 100)) + (3 * temp1[1]) + temp1[3];
					hValue = h.getDistance(entry.getKey());
				} else if (version.equals("3")) {
					if (season.equalsIgnoreCase("winter")) {
						gValue = (temp1[0] * 4 * (temp1[1] / 100)) + (4 * temp1[3]);
						hValue = h.getDistance(entry.getKey());
					} else {
						gValue = (temp1[0] * 4 * (temp1[1]) / 10) + (1 * temp1[3]);
						hValue = h.getDistance(entry.getKey());
					}
				}
			}
			if (hValue + gValue < lowest) {
				lowest = gValue + hValue;
				shortest = entry.getKey();
				lowestG = temp1[0];
			}
		}
		setShortest(shortest);
		return lowestG;
	}

	// setter for shortest
	// parameter: String shortest - shortest f(n)
	// post-condition: shortest has destination with smallest distance
	public void setShortest(String shortest) {
		this.shortest = shortest;
	}

	// getter for shortest
	// returns: string - destination that had the shortest f(n)
	public String getShortest() {
		return shortest;
	}

}
