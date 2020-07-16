/*
  Name: Brenden Davey
  CIS 421 Artificial Intelligence
  Assignment: 6 
*/

import java.util.*;
import java.awt.Point;

public class State {

  // fields
  double[] actions;
  int reward;
  int timesVisited;
  Point location;
  char symbol;
  Environment map;

  // constructor
  public State(int x, int y, int reward) {

    // sets all surrounding q values to optimistic 0.0
    this.actions = new double[9];
    this.location = new Point(x, y);
    this.symbol = '-';
    this.reward = reward;
    this.timesVisited = 0;
  }

  // returns the maximum possible q value obtained from possible actions
  // return: max q value
  public double getMaxQ() {
    double max = actions[0];
    for (int i = 1; i < actions.length; i++) {
      if (actions[i] > max) {
        max = actions[i];
      }
    }
    return max;
  }

  // returns the sum of all q value obtained from possible action
  // returns: sum of q values
  public double getQSum() {
    double sum = actions[0];
    for (int i = 1; i < actions.length; i++) {
      if (map.legalMove(this.location, i))
        sum += actions[i];
    }
    return sum;
  }

  // updates the q value for a certain action
  // param: int index - index of action for this node
  public void updateQ(int index, State s) {
    actions[index] += QLearning.alpha * (s.reward + (QLearning.gamma * s.getMaxQ()) - actions[index]);
  }
}