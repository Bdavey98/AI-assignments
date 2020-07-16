/*
 * Name: Brenden Davey
 * Course: CIS 421 Artificial Intelligence
 * Assignment: 1
 * Date: 9/19/2019
 */

import java.io.FileNotFoundException;

public class cleanerMain {
	// calls 2 methods from other classes to do all the work
	// command-line arguments: args[0] 1 for first version 2 for second; args[1] 1 to step through 2 to skip to end
	public static void main(String[] args) throws FileNotFoundException {
		String args1 = args[1];
		if (args[0].equals("1")) {
			 makeBoard.getData();
			 vacuum.makeHappen(args1);
		}
		if (args[0].equals("2")) {
			makeBoard2.getData();
			vacuum2.makeHappen(args1);
		}

	}
}
