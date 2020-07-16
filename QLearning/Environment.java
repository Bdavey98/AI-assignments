/*
  Name: Brenden Davey
  CIS 421 Artificial Intelligence
  Assignment: 6
*/

import java.util.*;
import java.io.*;
import java.awt.Point;

public class Environment {

  // fields
  State[][] grid; // map of the environment
  Agent agent; // agent
  State escapeLocation; // escape state
  State[] trolls; // states with trolls
  State[] ponies; // states with ponies
  State[] obstructions; // states with obstructions

  // constructor
  // param: File f - file to read environment from
  public Environment(File f) throws FileNotFoundException {

    // scanner to read file
    Scanner fs = new Scanner(f);

    // creates map
    int size = fs.nextInt(); // size of board (size x size)
    grid = new State[size][size];

    // create all state action pairs
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[i].length; j++) {
        grid[j][i] = new State(i, j, 2);
      }
    }

    // sets actions you cant take
    for (State[] a : grid) {
      for (State s : a) {
        for (int i = 0; i < s.actions.length; i++) {
          if (!legalMove(s.location, i)) {
            s.actions[i] = -1;
          }
        }
      }
    }

    // gets amount of trolls
    trolls = new State[fs.nextInt()];
    // gets amount of ponies
    ponies = new State[fs.nextInt()];

    // coords for escape location
    int x = fs.nextInt();
    int y = fs.nextInt();
    escapeLocation = grid[y][x];
    grid[y][x].symbol = 'E'; // sets printed character
    grid[y][x].reward = 15; // sets reward

    // places ponies
    for (int i = 0; i < ponies.length; i++) {
      x = fs.nextInt();
      y = fs.nextInt();
      ponies[i] = grid[y][x];
      grid[y][x].symbol = 'P'; // sets printed character
      grid[y][x].reward = 10; // sets reward
    }

    // places obstructions, handles if there are none
    ArrayList<State> a = new ArrayList<State>();
    int temp = fs.nextInt();
    String line = fs.nextLine();
    if (temp != -1) { // ensures there are obstructions
      Scanner scan = new Scanner(line);
      a.add(grid[scan.nextInt()][temp]);
      while (scan.hasNext()) {
        x = scan.nextInt();
        y = scan.nextInt();
        a.add(grid[y][x]);
      }
    }
    a.trimToSize();
    obstructions = new State[a.size()];
    obstructions = a.toArray(obstructions); // stores list as array
    for (State obstr : obstructions) {
      obstr.symbol = 'O';
    }

    // places trolls
    for (int i = 0; i < trolls.length; i++) {
      x = fs.nextInt();
      y = fs.nextInt();
      trolls[i] = grid[y][x];
      grid[y][x].symbol = 'T'; // sets printed character
      grid[y][x].reward = -15; // sets reward
    }

    // agent starts at random location that is not occupied space
    do {
      agent = new Agent(
          new Point((int) Math.random() * ((size - 1) - 2) + 1, (int) Math.random() * ((size - 1) - 2) + 1));
    } while (grid[agent.location.y][agent.location.x].symbol != '-');
  }

  // resets the environments symbols back to what they started as
  public void reset() {

    // resets agent location
    agent.location = new Point((int) (Math.random() * ((grid.length - 1) - 2)) + 1,
        (int) (Math.random() * ((grid.length - 1) - 2)) + 1);

    // resets all characters
    for (State[] a : this.grid) {
      for (State s : a) {
        s.symbol = '-';
        s.timesVisited = 0;
      }
    }
    escapeLocation.symbol = 'E';
    for (State s : ponies) {
      s.symbol = 'P';
    }
    for (State s : trolls) {
      s.symbol = 'T';
    }
    for (State s : obstructions) {
      s.symbol = 'O';
    }
  }

  // function to give a visual interpretation of the environment
  // returns: String representation of this environment
  public String printEnv() {
    String temp = "";

    // adds top line
    temp += "##"; // top left wall
    for (int i = 0; i < this.grid.length / 2; i++) {
      temp += " ## "; // top wall
    }
    temp += " ##\n"; // top right wall

    // all internal lines
    char swapc = grid[agent.location.y][agent.location.x].symbol; // swaps
    grid[agent.location.y][agent.location.x].symbol = 'B';
    for (State[] a : this.grid) {
      temp += "##"; // right wall
      for (State s : a) {
        temp += " " + s.symbol; // space
      }
      temp += " ##\n"; // left wall
    }

    // reverts swap
    grid[agent.location.y][agent.location.x].symbol = swapc;

    // adds bottom line
    temp += "##"; // bottom left wall
    for (int i = 0; i < this.grid.length / 2; i++) {
      temp += " ## "; // lower wall
    }
    temp += " ##"; // bottom right wall
    return temp;
  }

  public void printStateQ() {
    for (int i = 0; i < grid.length; i++) {
      for (int k = 0; k < 3; k++) {
        for (int j = 0; j < grid.length; j++) {
          System.out.print("[");
          for (int l = 0; l < 3; l++) {
            if (Math.round(grid[i][j].actions[l + (3 * k)]) > -10) {
              System.out.print(" ");
            }
            System.out.print(" " + Math.round(grid[i][j].actions[l + (3 * k)]) + " ");
            if (Math.round(grid[i][j].actions[l + (3 * k)]) > -1
                && (Math.round(grid[i][j].actions[l + (3 * k)]) < 10)) {
              System.out.print(" ");
            }
          }
          System.out.print("]");
        }
        System.out.println();
      }
      System.out.println();
    }
  }

  // helper method to ensure that an action is legal
  // param: Point currLocation: current location
  // param: int action: direction of the action
  // returns boolean: if move is legal or not
  public boolean legalMove(Point currLocation, int action) {

    // new coordinates
    int newX = currLocation.x + QLearning.indexToCoords(action).x;
    int newY = currLocation.y + QLearning.indexToCoords(action).y;

    // check if location is within environment
    if (newX > -1 && newX < grid.length && newY > -1 && newY < grid.length) {

      // if not current location, has not been visited, and there are no obstacles
      return action != 4 && grid[newY][newX].symbol != 'O';
    }
    return false;
  }
}