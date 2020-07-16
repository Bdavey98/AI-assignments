/*
 * Name: Brenden Davey
 * Course: CIS 421 Artificial Intelligence
 * Assignment: 1
 * Date: 9/19/2019
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class makeBoard2 {
	public static int n;
	public static int furniture;
	public static int dirt;
	public static String[][] room;

	// pre-condition: needs input file named input1.txt
	// post-condition: reads data from file
	public static void getData() throws FileNotFoundException {
		File file = new File("input1.txt");
		Scanner sc = new Scanner(file);

		if (sc.hasNext()) {
			n = sc.nextInt();
			furniture = sc.nextInt();
			dirt = sc.nextInt();
		}
		// sets the room layout from the file info
		room = new String[n][n];
		setInit();
		setGoal(sc);
		setFurniture(sc);
		setDirt(sc);
		sc.close();

	}
	// post-condition: the entire output to the terminal gets printed
	public static void printRoom() {
		System.out.println("Energy\t\tTime <B  Du Df Db Dr Dl Gu Gf Gb Gr Gl>\t\tAction\t\t\tScore");
		if(vacuum2.count < 100) {
		System.out.println(vacuum2.energy + "\t\t" + vacuum2.count + "    " + vacuum2.vect + "\t\t" + vacuum2.action
				+ "\t\t" + vacuum2.score);
		} else {
			System.out.println(vacuum2.energy + "\t\t" + vacuum2.count + "    " + vacuum2.vect + "\t" + vacuum2.action
					+ "\t\t" + vacuum2.score);
		}
		printTopBot();
		printRow();
	}
	// post-condition: prints the top and bottom lines of the room
	public static void printTopBot() {
		for (int i = 0; i < n; i++) {
			System.out.print("+");
			System.out.print("-");
		}
		System.out.print("+");
	}

	// post-condition: prints the rest of the room
	public static void printRow() {
		System.out.print("\n|");
		for (int j = n - 1; j >= 0; j--) {
			for (int i = 0; i < n; i++) {
				System.out.print(room[i][j]);

				if (i < n - 1) {
					System.out.print(" ");
				}
			}
			System.out.println("|");
			if (j > 0) {
				printSeparations();
			}
		}
		printTopBot();
		System.out.println();
	}

	// helper to row dividers
	public static void printSeparations() {
		for (int i = 0; i < n; i++) {
			System.out.print("+ ");
		}
		System.out.println("+");
		System.out.print("|");
	}
	// sets the room values to spaces before they get filled 
	public static void setInit() {
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				room[i][j] = " ";
			}
		}
	}

	// helper to set location of the goal from the file
	public static void setGoal(Scanner sc) {
		int x = 0;
		int y = 0;
		if (sc.hasNext()) {
			x = sc.nextInt();
			y = sc.nextInt();
		}
		room[x][y] = "G";
		room[0][0] = "H";
	}

	// helper to set location of furniture
	public static void setFurniture(Scanner sc) {
		int x = 0;
		int y = 0;
		int count = 0;
		while (count < furniture) {
			if (sc.hasNext()) {
				x = sc.nextInt();
				y = sc.nextInt();
			}
			room[x][y] = "X";
			count++;
		}
	}

	// helper to set location of dirt
	public static void setDirt(Scanner sc) {
		int x = 0;
		int y = 0;
		int count = 0;
		while (count < dirt) {
			if (sc.hasNext()) {
				x = sc.nextInt();
				y = sc.nextInt();
			}
			room[x][y] = "#";
			count++;
		}
	}

}
