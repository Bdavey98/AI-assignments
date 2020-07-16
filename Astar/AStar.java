
/*  Author: Brenden Davey
	Course: CIS 421 Artificial Intelligence
	Assignment 3
*/

import java.util.*;
import java.io.*;

// uses A* to find path from locations in a file
public class AStar {

	public static void main(String[] args) throws FileNotFoundException {
		int shortest = 0;
		int temp = 0;
		ArrayList<String> path = new ArrayList<String>();
		String version = args[0];

		// scanner for total.txt file to make h(n)
		Scanner reader = new Scanner(new FileInputStream("distance.txt"));
		FindH h = new FindH(reader);
		// scanner for point.txt file to make g(n)
		Scanner sc = new Scanner(new FileInputStream("point.txt"));
		FindG g = new FindG(sc);
		String current = "";

		// if an alternative start location was entered, start there
		if (args.length > 1) {
			for (int i = 1; i < args.length; i++) {
				if (i > 1)
					current += " ";
				current += args[i];
			}
		} else
			current = "Blue Mountains";

		String season = "summer\n";

		// if not version 1, ask season until valid season input
		if (!version.equals("1")) {
			Scanner in = new Scanner(System.in);
			System.out.println("What season are we traveling in? (Winter/Spring/Summer/Fall");
			season = in.nextLine();
			while (!(season.equalsIgnoreCase("summer") || season.equalsIgnoreCase("spring")
					|| season.equalsIgnoreCase("winter") || season.equalsIgnoreCase("fall"))) {
				System.out.println("Enter a valid season");
				System.out.println("What season are we traveling in? (Winter/Spring/Summer/Fall)");
				season = in.nextLine();
			}
		}
		path.add(current);
		System.out.print(current + " -> ");

		// loop to find path
		while (!current.equals("Iron Hills")) {
			shortest += g.GetLowest(current, h, season, version);
			current = g.getShortest();
			temp = shortest + h.getDistance(current);
			path.add(current);
			if (!current.equals("Iron Hills"))
				System.out.print(current + " -> ");
			else
				System.out.println(current);
		}
	}
}