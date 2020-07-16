
/*
 * Name: Brenden Davey
 * Course: CIS 421 Artificial Intelligence
 * Assignment: 1
 * Date: 9/19/2019
 */

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.Vector;

// Class that makes the vacuum that interacts with the room from makeBoard2
public class vacuum2 {
	public static Point coords;
	public static int direction;
	public static Vector<Integer> vect = new Vector<Integer>(11);
	public static int timeStep;
	public static int count = 0;
	public static int energy = (10 * (makeBoard2.n * makeBoard2.n));
	public static int score = 0;
	public static String action = "Start\t";
	public static String symbol;
	public static int counter2 = 0;
	public static Vector<String> vect2 = new Vector<String>(3);

	// method that everything that happens in the class goes through
	// pre-condition: a board must be made
	// post-condition: vector is set and zeroed ready for nextMove() to be called
	// parameters: String str; 1 if you want to step through, 2 if you want to skip
	public static void makeHappen(String str) throws FileNotFoundException {
		setVect();
		// sets the starting point; must be less than makeBoard.n in x and y
		coords = new Point(0, 0);
		zeroVector();
		direction = 1;
		// condition for step through or skip
		if (str.equals("1")) {
			nextMove();
		} else if (str.equals("2")) {
			nextMove2();
		}
	}

	// decides which move comes next if you want to step through
	// pre-condition: starting point must be set
	// post-condition: entire process of room cleaning is run to completion
	// post-condition: created output file
	public static void nextMove() throws FileNotFoundException {
		// sets printstream to output file
		PrintStream ps = new PrintStream(new File("prog1_log.txt"));
		// prints the first line of the output file
		printHeading(ps);
		// user input scanner
		Scanner move = new Scanner(System.in);
		setSymbol();
		makeBoard2.room[coords.x][coords.y] = symbol;
		makeBoard2.printRoom();
		// while loop that runs entire simulation
		while (energy > 0 && vect.get(6) != 1) {
			System.out.println(vect2);
			setSymbol();
			count++;
			checkSensors();
			System.out.print("Enter space: ");
			String s = move.nextLine();
			if (s.equals("")) {
				if (vect.get(1) == 1) {
					suck();
				} else if (vect.get(2) == 1) {
					moveForward();
				} else if (vect.get(4) == 1) {
					turnRight();
				} else if (vect.get(5) == 1) {
					turnLeft();
				} else if (vect.get(3) == 1) {
					turnRight();

				} else if (vect.get(7) == 1) {
					moveForward();
					off();
				} else if (vect.get(9) == 1) {
					turnRight();
				} else if (vect.get(10) == 1) {
					turnLeft();
				} else if (vect.get(8) == 1) {
					turnLeft();
				} else if (vect.get(0) == 0) {
					int rand = (int) (Math.random() * 8);
					if ((vect2.get(0).equals("right") || vect2.get(0).equals("left"))
							&& (vect2.get(1).equals("right") || vect2.get(1).equals("left"))) {
						moveForward();
					} else if (rand < 5) {
						moveForward();
					} else if (rand == 6 && !(vect2.get(0).equals("right"))) {
						turnLeft();
					} else if (!(vect2.get(0).equals("left"))) {
						turnRight();
					}
				} else {
					int rand = (int) (Math.random() * 2);
					if (rand == 0 && !(vect2.get(0).equals("right"))) {
						turnLeft();
					} else if (rand == 1 && !(vect2.get(0).equals("left"))) {
						turnRight();
					}
				}
				setSymbol();
				makeBoard2.room[coords.x][coords.y] = symbol;
				checkSensors();
				makeBoard2.printRoom();
				filePrint(ps);
			}
		}
		makeBoard2.printRoom();
		move.close();
	}

	// exactly same method as nextMove() but skips to end of simulation
	public static void nextMove2() throws FileNotFoundException {
		PrintStream ps = new PrintStream(new File("prog1_log.txt"));
		printHeading(ps);
		Scanner move = new Scanner(System.in);
		setSymbol();
		makeBoard2.room[coords.x][coords.y] = symbol;
		makeBoard2.printRoom();
		System.out.println("Initial room\n");
		while (energy > 0 && vect.get(6) != 1) {
			setSymbol();
			count++;
			checkSensors();
			if (vect.get(1) == 1) {
				suck();
			} else if (vect.get(2) == 1) {
				moveForward();
			} else if (vect.get(4) == 1) {
				turnRight();
			} else if (vect.get(5) == 1) {
				turnLeft();
			} else if (vect.get(3) == 1) {
				turnRight();

			} else if (vect.get(7) == 1) {
				moveForward();
				off();
			} else if (vect.get(9) == 1) {
				turnRight();
			} else if (vect.get(10) == 1) {
				turnLeft();
			} else if (vect.get(8) == 1) {
				turnLeft();
			} else if (vect.get(0) == 0) {
				int rand = (int) (Math.random() * 8);
				if (rand < 5) {
					moveForward();
				} else if (rand == 6) {
					turnLeft();
				} else {
					turnRight();
				}
			} else {
				int rand = (int) (Math.random() * 2);
				if (rand == 0) {
					turnLeft();
				} else {
					turnRight();
				}
			}
			setSymbol();
			checkSensors();
			filePrint(ps);
		}
		makeBoard2.printRoom();
		System.out.println("Final room\n");
		move.close();
	}

	// sets symbol based on direction vacuum is facing
	// post-condition: symbol is open mouth toward direction it is facing
	public static void setSymbol() {
		if (direction == 1) {
			symbol = "V";
		} else if (direction == 2) {
			symbol = "<";
		} else if (direction == 3) {
			symbol = "^";
		} else if (direction == 4) {
			symbol = ">";
		}
	}

	// sets entire percept vector
	// post-condition: percept vector will be wiped from last step and refilled with new data
	public static void checkSensors() {
		zeroVector();
		if (direction == 1) {
			if (coords.y == makeBoard2.n - 1) {
				vect.set(0, 1);
			}
			if (coords.x != makeBoard2.n - 1) {
				if (makeBoard2.room[coords.x + 1][coords.y] == "#") {
					vect.set(4, 1);
				} else if (makeBoard2.room[coords.x + 1][coords.y] == "G") {
					vect.set(9, 1);
				}
			}
			if (coords.y != makeBoard2.n - 1) {
				vect.set(0, 0);
				if (makeBoard2.room[coords.x][coords.y + 1] == "#") {
					vect.set(2, 1);
				} else if (makeBoard2.room[coords.x][coords.y + 1] == "G") {
					vect.set(7, 1);
				} else if (makeBoard2.room[coords.x][coords.y + 1] == "X") {
					vect.set(0, 1);
				}
			}
			if (coords.y != 0) {
				if (makeBoard2.room[coords.x][coords.y - 1] == "#") {
					vect.set(3, 1);
				} else if (makeBoard2.room[coords.x][coords.y - 1] == "G") {
					System.out.println();
					vect.set(8, 1);
				}
			}
			if (coords.x != 0) {
				if (makeBoard2.room[coords.x - 1][coords.y] == "#") {
					vect.set(5, 1);
				} else if (makeBoard2.room[coords.x - 1][coords.y] == "G") {
					vect.set(10, 1);
				}
			}
		}
		if (direction == 2) {
			if (coords.x == makeBoard2.n - 1) {
				vect.set(0, 1);
			}
			if (coords.x != makeBoard2.n - 1) {
				vect.set(0, 0);
				if (makeBoard2.room[coords.x + 1][coords.y] == "#") {
					vect.set(2, 1);
				} else if (makeBoard2.room[coords.x + 1][coords.y] == "G") {
					vect.set(7, 1);
				} else if (makeBoard2.room[coords.x + 1][coords.y] == "X") {
					vect.set(0, 1);
				}
			}
			if (coords.y != makeBoard2.n - 1) {
				if (makeBoard2.room[coords.x][coords.y + 1] == "#") {
					vect.set(5, 1);
				} else if (makeBoard2.room[coords.x][coords.y + 1] == "G") {
					vect.set(10, 1);
				}
			}
			if (coords.y != 0) {
				if (makeBoard2.room[coords.x][coords.y - 1] == "#") {
					vect.set(4, 1);
				} else if (makeBoard2.room[coords.x][coords.y - 1] == "G") {
					vect.set(9, 1);
				}
			}
			if (coords.x != 0) {
				if (makeBoard2.room[coords.x - 1][coords.y] == "#") {
					vect.set(3, 1);
				}
				if (makeBoard2.room[coords.x - 1][coords.y] == "G") {
					vect.set(8, 1);
				}
			}
		}
		if (direction == 3) {
			if (coords.y == 0) {
				vect.set(0, 1);
			}
			if (coords.x != makeBoard2.n - 1) {
				if (makeBoard2.room[coords.x + 1][coords.y] == "#") {
					vect.set(5, 1);
				} else if (makeBoard2.room[coords.x + 1][coords.y] == "G") {
					vect.set(10, 1);
				}
			}
			if (coords.y != makeBoard2.n - 1) {
				if (makeBoard2.room[coords.x][coords.y + 1] == "#") {
					vect.set(3, 1);
				} else if (makeBoard2.room[coords.x][coords.y + 1] == "G") {
					vect.set(8, 1);
				}
			}
			if (coords.y != 0) {
				vect.set(0, 0);
				if (makeBoard2.room[coords.x][coords.y - 1] == "#") {
					vect.set(2, 1);
				} else if (makeBoard2.room[coords.x][coords.y - 1] == "G") {
					vect.set(7, 1);
				} else if (makeBoard2.room[coords.x][coords.y - 1] == "X") {
					vect.set(0, 1);
				}
			}
			if (coords.x != 0) {
				if (makeBoard2.room[coords.x - 1][coords.y] == "#") {
					vect.set(4, 1);
				} else if (makeBoard2.room[coords.x - 1][coords.y] == "G") {
					vect.set(9, 1);
				}
			}
		}
		if (direction == 4) {
			if (coords.x == 0) {
				vect.set(0, 1);
			}
			if (coords.x != makeBoard2.n - 1) {
				if (makeBoard2.room[coords.x + 1][coords.y] == "#") {
					vect.set(3, 1);
				} else if (makeBoard2.room[coords.x + 1][coords.y] == "G") {
					vect.set(8, 1);
				}
			}
			if (coords.y != makeBoard2.n - 1) {
				if (makeBoard2.room[coords.x][coords.y + 1] == "#") {
					vect.set(4, 1);
				} else if (makeBoard2.room[coords.x][coords.y + 1] == "G") {
					vect.set(9, 1);
				}
			}
			if (coords.y != 0) {
				if (makeBoard2.room[coords.x][coords.y - 1] == "#") {
					vect.set(5, 1);
				} else if (makeBoard2.room[coords.x][coords.y - 1] == "G") {
					vect.set(10, 1);
				}
			}
			if (coords.x != 0) {
				vect.set(0, 0);
				if (makeBoard2.room[coords.x - 1][coords.y] == "#") {
					vect.set(2, 1);
				} else if (makeBoard2.room[coords.x - 1][coords.y] == "G") {
					vect.set(7, 1);
				} else if (makeBoard2.room[coords.x - 1][coords.y] == "X") {
					vect.set(0, 1);
				}
			}
		}
	}

	// post-condition: vacuum cleaner turns to the left; energy and score - 1; action changes to "turn left"
	public static void turnLeft() {
		vect2.set(2, vect2.get(1));
		vect2.set(1, vect2.get(0));
		vect2.set(0, "left");
		energy--;
		action = "Turn Left";
		score -= 1;
		if (direction == 1) {
			direction = 4;
		} else {
			direction--;
		}
	}
	
	// post-condition: vacuum cleaner turns to the right; energy and score -1; action changes to "turn right"
	public static void turnRight() {
		vect2.set(2, vect2.get(1));
		vect2.set(1, vect2.get(0));
		vect2.set(0, "right");
		energy--;
		action = "Turn Right";
		score -= 1;
		if (direction == 4) {
			direction = 1;
		} else {
			direction++;
		}
	}
	
	// post-condition: energy -1 and score + 99 (100 - 1) "#" removed from room; action changed
	public static void suck() {
		energy--;
		score -= 1;
		if ((makeBoard2.room[coords.x][coords.y]) == "#") {
			makeBoard2.room[coords.x][coords.y] = " ";
			action = "Vacuum\t";
			score += 100;
		}
	}

	// post-condition: vect[6] set to 1 escaping while loop in nextMove(); energy and score -1
	public static void off() {
		energy--;
		score--;
		vect.set(6, 1);
	}
	
	// vector to store last 3 actions
	public static void actionsVector() {

	}

	// second main half of AI logic; moves the vacuum forward 1
		// post-condition: moves vacuum forward; energy and score -1; calls suck() if moves onto dirt
	public static void moveForward() {
		vect2.set(2, vect2.get(1));
		vect2.set(1, vect2.get(0));
		vect2.set(0, "forward");
		energy--;
		action = "Move Forward";
		score -= 1;
		if (direction == 1 && coords.y != makeBoard2.n - 1) {
			coords.y++;
			if (makeBoard2.room[coords.x][coords.y] == "#")
				suck();
			makeBoard2.room[coords.x][coords.y - 1] = " ";
			makeBoard2.room[coords.x][coords.y] = symbol;
			if (coords.y == makeBoard2.n - 1) {
				vect.set(0, 1);
			}
		} else if (direction == 2 && coords.x != makeBoard2.n - 1) {
			coords.x++;
			if (makeBoard2.room[coords.x][coords.y] == "#")
				suck();
			makeBoard2.room[coords.x - 1][coords.y] = " ";
			makeBoard2.room[coords.x][coords.y] = symbol;
			if (coords.x == makeBoard2.n - 1) {
				vect.set(0, 1);
			}
		} else if (direction == 3 && coords.y != 0) {
			coords.y--;
			if (makeBoard2.room[coords.x][coords.y] == "#")
				suck();
			makeBoard2.room[coords.x][coords.y + 1] = " ";
			makeBoard2.room[coords.x][coords.y] = symbol;
			if (coords.y == 0) {
				vect.set(0, 1);
			}
		} else if (direction == 4 && coords.x != 0) {
			coords.x--;
			if (makeBoard2.room[coords.x][coords.y] == "#")
				suck();
			makeBoard2.room[coords.x + 1][coords.y] = " ";
			makeBoard2.room[coords.x][coords.y] = symbol;
			if (coords.x == 0) {
				vect.set(0, 1);
			}
		}
		if (coords.x > 0 || coords.y > 0) {
			makeBoard2.room[0][0] = "H";
		}
	}

	// post-condition: prints everything to the file except first line; score -1000 for file print if no energy
	public static void filePrint(PrintStream ps) throws FileNotFoundException {
		if (energy == 0) {
			score -= 1000;
		}
		if (count < 10) {
			ps.println(" " + count + "    " + vect + "\t\t\t" + action + "\t" + score);
		} else if (count < 100) {
			ps.println(count + "    " + vect + "\t\t\t" + action + "\t" + score);
		} else {
			ps.println(count + "   " + vect + "\t\t\t" + action + "\t" + score);
		}
	}

	// post-condition: resets vector values to zero except the goal reached position
	public static void zeroVector() {
		boolean end = false;
		if (vect.get(6) == 1) {
			end = true;
		}
		for (int i = 1; i < 11; i++) {
			vect.set(i, 0);
		}
		if (end) {
			vect.set(6, 1);
		}
	}

	// called once to set vect[0-10] to 0 and vect2 set it to empty strings
	public static void setVect() {
		for (int i = 0; i < 11; i++) {
			vect.add(0);
			if (i < 3) {
				vect2.add("");
			}
		}
	}

	// post-condition: first line of output file is printed
	public static void printHeading(PrintStream ps) {
		ps.println("Time  <B  Du Df Db Dr Dl Gu Gf Gb Gr Gl>\t\tAction\t\tScore");
		ps.println("------------------------------------------- \t\t------ \t\t-----");
	}
}
