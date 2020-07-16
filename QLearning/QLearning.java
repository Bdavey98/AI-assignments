/*
  Name: Brenden Davey
  CIS 421 Artificial Intelligence
  Assignment: 6 
*/

import java.io.*;
import java.util.*;
import java.awt.Point;

public class QLearning {

  // variables
  public static final double alpha = 0.1; // learning rate
  public static final double gamma = 0.5; // discount Factor
  public static final int epochs = 10000; // number of iterations

  // param - args[0]: name of file with environment information
  public static void main(String[] args) throws FileNotFoundException {

    // creates map and assigns useful pointers
    Environment map = createEnvironment(args[0]);
    Agent agent = map.agent;

    for (int i = 0; i < epochs; i++) {
      trial(map, agent, false);
    }

    // final greedy tour
    trial(map, agent, true);
  }

  // function that runs through a single epoch
  // param: Environment map: environment to run on
  // param: Agent agent - agent in environment
  // param: boolean greedy - whether it is a greedy choice or not
  // post-condition: a single epoch is ran
  private static void trial(Environment map, Agent agent, boolean greedy) throws FileNotFoundException {

    // printstream to print to file
    PrintStream ps = new PrintStream(new File("output.txt"));

    // resets map
    map.reset();

    // prints final run before greedy choosing
    if (greedy) {
      System.out.println("Initial Environment");
      System.out.println(map.printEnv());
      ps.println("Initial Environment");
      ps.println(map.printEnv());
    }

    // number of ponies rescued
    int rescued = 0;

    // total cumulative points
    int reward = 0;

    // breaks loop if our agent has been killed
    boolean dead = false;

    // runs algorithms until agent is on goal location or dies
    while (!agent.location.equals(map.escapeLocation.location) && !dead) {

      int move;

      // chooses next move based off of greed
      if (greedy) {
        move = agent.chooseGreedyAction(map.grid[agent.location.y][agent.location.x]);
      } else {
        move = agent.choosePGreedyAction(map.grid[agent.location.y][agent.location.x]);
      }
      int dx = indexToCoords(move).x;
      int dy = indexToCoords(move).y;

      // updates the previous q value
      map.grid[agent.location.y][agent.location.x].updateQ(move,
          map.grid[agent.location.y + dy][agent.location.x + dx]);

      // moves the agent
      agent.location.translate(dx, dy);

      // increments count for how many times spot has been visited
      map.grid[agent.location.y][agent.location.x].timesVisited++;

      // counts the reward of moving to this spot
      reward += map.grid[agent.location.y][agent.location.x].reward;

      // checks if the agent is on a pony
      for (int i = 0; i < map.ponies.length; i++) {
        if (map.ponies[i].location.equals(agent.location)) {
          rescued++;
        }
      }

      // checks if the agent is on a troll
      for (int i = 0; i < map.trolls.length; i++) {
        if (map.trolls[i].location.equals(agent.location)) {
          dead = true;
        }
      }

      // sets the current location's symbol value to X
      map.grid[agent.location.y][agent.location.x].symbol = 'X';
    }

    // prints final run after greedy choosing
    if (greedy) {
      System.out.print("\nFinal environment:");
      System.out.println("\n" + map.printEnv());
      System.out.println("rescued ponies: " + rescued);
      System.out.println("total reward: " + reward + "\n");
      ps.print("\nFinal environment");
      ps.println("\n" + map.printEnv());
      ps.println("rescued ponies: " + rescued);
      ps.println("total reward: " + reward + "\n");
    }
  }

  // creates the Environment for the simulation
  // param: String filename - name of file with environment information
  // throws: FileNotFoundException
  private static Environment createEnvironment(String filename) throws FileNotFoundException {
    File f = new File(filename);
    if (f.exists()) {
      Environment temp = new Environment(f);
      temp.agent.map = temp;
      for (State[] a : temp.grid) {
        for (State s : a) {
          s.map = temp;
        }
      }
      return temp;
    } else {
      throw new FileNotFoundException();
    }
  }

  // helper method to convert coordinates of move to index of action array
  // param: Point - vector for changes in state
  // returns: int index - index of the direction
  public static int coordsToIndex(int dx, int dy) {
    return (4 + dx + (3 * dy));
  }

  // helper method to convert index of action array to coordinates of move
  // param: int index: index of the direction
  // returns: Point - vector of the change in state
  public static Point indexToCoords(int index) { // works
    return new Point((index % 3) - 1, (index / 3) - 1);
  }
}
