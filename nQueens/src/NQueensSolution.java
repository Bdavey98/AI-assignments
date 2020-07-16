import java.awt.Point;
import java.util.ArrayList;

public class NQueensSolution implements Comparable<NQueensSolution> {
  public float fitness;   // fitness of this solution: 1 / (conflicts + 0.00001)
  public int[] genotype;  // array in which genotype[i] == j is position (i, j)
  public boolean solution; // does this solution solve NQueens? (conflicts == 0)

  public NQueensSolution(int[] inGenotype) {
    genotype = inGenotype;
    fitness = checkFitness();
  }

  // returns: fitness of this population: 1 / (conflicts + 0.00001)
  // also sets boolean solution accordingly
  public float checkFitness() {
    ArrayList<Point> conflictingPairs = new ArrayList<Point>();

    // This nested loop checks all possible (i, j) pairs without repetition
    for (int i = 0; i < genotype.length; i++) {
      for (int j = i+1; j < genotype.length; j++) {

        // initialize x and y so we can do some manipulation without messing
        // with the loop
        int x = i;
        int y = j;
        if (checkForConflict(x, y)) {
          // to make sure we don't count the same pair twice
          if (y < x) {
            int temp = x;
            x = y;
            y = temp;
          }
          // if my list doesn't contain this pair, add it
          if (!conflictingPairs.contains(new Point(genotype[x], genotype[y])))
            conflictingPairs.add(new Point(genotype[x], genotype[y]));
          }
        }
    }
    // how many elements are in my list?
    int conflicts = conflictingPairs.size();
    solution = conflicts == 0;
    return 1 / (float) (conflicts + 0.00001);
  }

  // params: int q1: queen 1
  //         int q2: queen 2
  // returns: boolean true if these two queens are in conflict, else false
  public boolean checkForConflict(int q1, int q2) {
    int deltaRow = Math.abs(q1 - q2);
    int deltaColumn = Math.abs(genotype[q1] - genotype[q2]);
    return deltaRow == deltaColumn;
  }

  // params: NQueensSolution other: NQueensSolution to compare to
  // returns: negative number if this solution's fitness < other's fitness
  //          positive number if this solution's fitness > other's fitness
  //          zero if this solution's fitness == other's fitness
  // Comparator method for sorting purposes 
  public int compareTo(NQueensSolution other) {
    if (other == null) return -1;
    return Math.round(other.fitness - this.fitness);
  }
}