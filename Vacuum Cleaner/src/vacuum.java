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

	// Class that makes the vacuum that interacts with the room from makeBoard
public class vacuum {
	public static Point coords;
	public static int direction;
	public static Vector<Integer> vect = new Vector<Integer>(11);
	public static int timeStep;
	public static int count = 0;
	public static int energy = (10 * (makeBoard.n * makeBoard.n));
	public static int score = 0;
	public static String action = "Start\t";
	public static String symbol;

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
		} else if(str.equals("2")) {
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
		// sets vacuum symbol on printed room
		makeBoard.room[coords.x][coords.y] = symbol;
		makeBoard.printRoom();
		// while loop that runs entire simulation
		while (energy > 0 && vect.get(6) != 1) {
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
				makeBoard.room[coords.x][coords.y] = symbol;
				checkSensors();
				makeBoard.printRoom();
				filePrint(ps);
			}
		}
		makeBoard.printRoom();
		move.close();
	}
	
	// exactly same method as nextMove() but skips to end of simulation
	public static void nextMove2() throws FileNotFoundException {
		PrintStream ps = new PrintStream(new File("prog1_log.txt"));
		printHeading(ps);
		Scanner move = new Scanner(System.in);
		setSymbol();
		makeBoard.room[coords.x][coords.y] = symbol;
		makeBoard.printRoom();
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
		makeBoard.printRoom();
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
			if (coords.y == makeBoard.n - 1) {
				vect.set(0, 1);
			}
			if (coords.x != makeBoard.n - 1) {
				if (makeBoard.room[coords.x + 1][coords.y] == "#") {
					vect.set(4, 1);
				} else if (makeBoard.room[coords.x + 1][coords.y] == "G") {
					vect.set(9, 1);
				}
			}
			if (coords.y != makeBoard.n - 1) {
				vect.set(0, 0);
				if (makeBoard.room[coords.x][coords.y + 1] == "#") {
					vect.set(2, 1);
				} else if (makeBoard.room[coords.x][coords.y + 1] == "G") {
					vect.set(7, 1);
				} else if (makeBoard.room[coords.x][coords.y + 1] == "X") {
					vect.set(0, 1);
				}
			}
			if (coords.y != 0) {
				if (makeBoard.room[coords.x][coords.y - 1] == "#") {
					vect.set(3, 1);
				} else if (makeBoard.room[coords.x][coords.y - 1] == "G") {
					System.out.println();
					vect.set(8, 1);
				}
			}
			if (coords.x != 0) {
				if (makeBoard.room[coords.x - 1][coords.y] == "#") {
					vect.set(5, 1);
				} else if (makeBoard.room[coords.x - 1][coords.y] == "G") {
					vect.set(10, 1);
				}
			}
		}
		if (direction == 2) {
			if (coords.x == makeBoard.n - 1) {
				vect.set(0, 1);
			}
			if (coords.x != makeBoard.n - 1) {
				vect.set(0, 0);
				if (makeBoard.room[coords.x + 1][coords.y] == "#") {
					vect.set(2, 1);
				} else if (makeBoard.room[coords.x + 1][coords.y] == "G") {
					vect.set(7, 1);
				} else if (makeBoard.room[coords.x + 1][coords.y] == "X") {
					vect.set(0, 1);
				}
			}
			if (coords.y != makeBoard.n - 1) {
				if (makeBoard.room[coords.x][coords.y + 1] == "#") {
					vect.set(5, 1);
				} else if (makeBoard.room[coords.x][coords.y + 1] == "G") {
					vect.set(10, 1);
				}
			}
			if (coords.y != 0) {
				if (makeBoard.room[coords.x][coords.y - 1] == "#") {
					vect.set(4, 1);
				} else if (makeBoard.room[coords.x][coords.y - 1] == "G") {
					vect.set(9, 1);
				}
			}
			if (coords.x != 0) {
				if (makeBoard.room[coords.x - 1][coords.y] == "#") {
					vect.set(3, 1);
				}
				if (makeBoard.room[coords.x - 1][coords.y] == "G") {
					vect.set(8, 1);
				}
			}
		}
		if (direction == 3) {
			if (coords.y == 0) {
				vect.set(0, 1);
			}
			if (coords.x != makeBoard.n - 1) {
				if (makeBoard.room[coords.x + 1][coords.y] == "#") {
					vect.set(5, 1);
				} else if (makeBoard.room[coords.x + 1][coords.y] == "G") {
					vect.set(10, 1);
				}
			}
			if (coords.y != makeBoard.n - 1) {
				if (makeBoard.room[coords.x][coords.y + 1] == "#") {
					vect.set(3, 1);
				} else if (makeBoard.room[coords.x][coords.y + 1] == "G") {
					vect.set(8, 1);
				}
			}
			if (coords.y != 0) {
				vect.set(0, 0);
				if (makeBoard.room[coords.x][coords.y - 1] == "#") {
					vect.set(2, 1);
				} else if (makeBoard.room[coords.x][coords.y - 1] == "G") {
					vect.set(7, 1);
				} else if (makeBoard.room[coords.x][coords.y - 1] == "X") {
					vect.set(0, 1);
				}
			}
			if (coords.x != 0) {
				if (makeBoard.room[coords.x - 1][coords.y] == "#") {
					vect.set(4, 1);
				} else if (makeBoard.room[coords.x - 1][coords.y] == "G") {
					vect.set(9, 1);
				}
			}
		}
		if (direction == 4) {
			if (coords.x == 0) {
				vect.set(0, 1);
			}
			if (coords.x != makeBoard.n - 1) {
				if (makeBoard.room[coords.x + 1][coords.y] == "#") {
					vect.set(3, 1);
				} else if (makeBoard.room[coords.x + 1][coords.y] == "G") {
					vect.set(8, 1);
				}
			}
			if (coords.y != makeBoard.n - 1) {
				if (makeBoard.room[coords.x][coords.y + 1] == "#") {
					vect.set(4, 1);
				} else if (makeBoard.room[coords.x][coords.y + 1] == "G") {
					vect.set(9, 1);
				}
			}
			if (coords.y != 0) {
				if (makeBoard.room[coords.x][coords.y - 1] == "#") {
					vect.set(5, 1);
				} else if (makeBoard.room[coords.x][coords.y - 1] == "G") {
					vect.set(10, 1);
				}
			}
			if (coords.x != 0) {
				vect.set(0, 0);
				if (makeBoard.room[coords.x - 1][coords.y] == "#") {
					vect.set(2, 1);
				} else if (makeBoard.room[coords.x - 1][coords.y] == "G") {
					vect.set(7, 1);
				} else if (makeBoard.room[coords.x - 1][coords.y] == "X") {
					vect.set(0, 1);
				}
			}
		}
	}

	// post-condition: vacuum cleaner turns to the left; energy and score - 1; action changes to "turn left"
	public static void turnLeft() {
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
		if ((makeBoard.room[coords.x][coords.y]) == "#") {
			makeBoard.room[coords.x][coords.y] = " ";
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

	// second main half of AI logic; moves the vacuum forward 1
	// post-condition: moves vacuum forward; energy and score -1; calls suck() if moves onto dirt
	public static void moveForward() {
		energy--;
		action = "Move Forward";
		score -= 1;
		if (direction == 1 && coords.y != makeBoard.n - 1) {
			coords.y++;
			if (makeBoard.room[coords.x][coords.y] == "#")
				suck();
			makeBoard.room[coords.x][coords.y - 1] = " ";
			makeBoard.room[coords.x][coords.y] = symbol;
			if (coords.y == makeBoard.n - 1) {
				vect.set(0, 1);
			}
		} else if (direction == 2 && coords.x != makeBoard.n - 1) {
			coords.x++;
			if (makeBoard.room[coords.x][coords.y] == "#")
				suck();
			makeBoard.room[coords.x - 1][coords.y] = " ";
			makeBoard.room[coords.x][coords.y] = symbol;
			if (coords.x == makeBoard.n - 1) {
				vect.set(0, 1);
			}
		} else if (direction == 3 && coords.y != 0) {
			coords.y--;
			if (makeBoard.room[coords.x][coords.y] == "#")
				suck();
			makeBoard.room[coords.x][coords.y + 1] = " ";
			makeBoard.room[coords.x][coords.y] = symbol;
			if (coords.y == 0) {
				vect.set(0, 1);
			}
		} else if (direction == 4 && coords.x != 0) {
			coords.x--;
			if (makeBoard.room[coords.x][coords.y] == "#")
				suck();
			makeBoard.room[coords.x + 1][coords.y] = " ";
			makeBoard.room[coords.x][coords.y] = symbol;
			if (coords.x == 0) {
				vect.set(0, 1);
			}
		}
		if (coords.x > 0 || coords.y > 0) {
			makeBoard.room[0][0] = "H";
		}
	}

	// post-condition: prints everything to the file except first line; score -1000 for file print if no energy
	public static void filePrint(PrintStream ps) throws FileNotFoundException {
		if (energy == 0) {
			score -= 1000;
		}
		ps.println(count + "    " + vect + "\t\t\t" + action + "\t" + score);
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

	// called once to set vect[0-10] to 0
	public static void setVect() {
		for (int i = 0; i < 11; i++) {
			vect.add(0);
		}
	}

	// post-condition: first line of output file is printed
	public static void printHeading(PrintStream ps) {
		ps.println("Time  <B  Du Df Db Dr Dl Gu Gf Gb Gr Gl>\t\tAction\t\tScore");
		ps.println("------------------------------------------- \t\t------ \t\t-----");
	}
}
