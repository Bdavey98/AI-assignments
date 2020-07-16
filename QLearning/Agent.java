/*
Name: Brenden Davey
CIS 421 Artificial Intelligence
Assignment: 6 
*/

import java.awt.Point;
import java.util.*;

public class Agent {

    // fields
    Point location;
    Environment map;

    // constructor
    public Agent(Point start) {

        // starts at given location
        this.location = start;

    }

    // chooses action based on p-greedy
    // param: State state: state the action occurs from
    // returns: int - choice of movement in the form of an index
    public int choosePGreedyAction(State state) {
        double prob; // temporary probability variable
        int move = (int) (Math.random() * (7 + 1)); // temporary move choice
        if (Math.random() < QLearning.alpha) {
            while (!map.legalMove(location, move)) {

                move = (int) (Math.random() * (7 + 1));
                if (move > 3)
                    move++;
            }
            return move;
        }

        if (move > 3 && move < 7)
            move++;
        for (int i = 0; i < state.actions.length; i++) {
            if (map.legalMove(location, move)) {
                prob = state.actions[i] / state.getQSum();
                if (prob > Math.random())
                    return (move);
            }
            if (move++ == 7) // cycles to beginning of array
                move = 0;
        }

        // nothing has been selected, choose random legal action
        while (!map.legalMove(location, move)) {
            move = (int) (Math.random() * (7 + 1));
            if (move > 3)
                move++;
        }
        return move;
    }

    // finds action based on greedy selection
    // param: state State state: state in which the action occurs from
    // return: an int - choice of movement in the form of an index
    public int chooseGreedyAction(State state) {
        int max = 0; // temp variable

        // finds the action for an unvisited state that had the highest q
        for (int i = 1; i < state.actions.length; i++) {

            // new coordinates
            int newX = state.location.x + QLearning.indexToCoords(i).x;
            int newY = state.location.y + QLearning.indexToCoords(i).y;
            if (state.actions[i] > state.actions[max] && map.grid[newY][newX].symbol != 'X') {
                max = i; // finds largest unvisited legal
            }
        }

        // if move is illegal, choose another
        while (!map.legalMove(location, max)) {
            max = (int) (Math.random() * (7 + 1));
            if (max > 3)
                max++;
        }

        return max;
    }
}