import java.util.*;

public class NQueensComparator implements Comparator<NQueensSolution> {
  // Comparator method for NQueensSolution based on fitness
  @Override
  public int compare(NQueensSolution s1, NQueensSolution s2) {
    if (s1.fitness < s2.fitness) return 1;
    else if (s2.fitness < s1.fitness) return -1;
    else return 0;
  }
}