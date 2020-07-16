import java.util.*;
import java.io.*;

public class NQueens {
  private static int n = 20; // SIZE OF CHESS BOARD: n * n
  private static ArrayList<NQueensSolution> population =
                                              new ArrayList<NQueensSolution>();

  // Initializes population of size 10n
  // Runs 1000 generations of genetic algorithm
  public static void main(String[] args) throws IOException {
    // Seed initial population
    for (int i = 0; i < n * 10; i ++) {
      int[] genotype = new int[n];
      Set<Integer> takenNumbers = new HashSet<Integer>();
      for (int j = 0; j < n; j++) {

        // Generate a random number between 1 and n
        int thisNum = Math.round(randomNumberGen(0, n - 1));

        // Keep regenerating until it's not already in this solution
        while (takenNumbers.contains(thisNum))
          thisNum = Math.round(randomNumberGen(0, n - 1));
        takenNumbers.add(thisNum);
        genotype[j] = thisNum;
      }
      population.add(new NQueensSolution(genotype));
    }

    String filename = "NQueensOutput.txt";
    FileWriter fw = new FileWriter(filename, true);
    // Process 1000 generations
    for (int i = 0; i < 100000; i++) {
      checkForSolution(population, i, fw);
      population = selectParents(population);
    }

    fw.write("1000 null\n");
    fw.close();
  }

  // params: ArrayList<NQueensSolution> population: current population
  //         int gen: current generation
  //         FileWriter fw: to print results to
  // Checks each NQueensSolution in the population to see if it solves N-Queens
  public static void checkForSolution(ArrayList<NQueensSolution> population,
                                    int gen, FileWriter fw) throws IOException {
    for (NQueensSolution sol : population) {
      if (sol.solution) {
        fw.write(gen + " " + Arrays.toString(sol.genotype) + "\n");
        fw.close();
        System.exit(0);
      }
    }
  }

  // params:  ArrayList<NQueensSolution> population: current population
  // returns: new population after mating and death of unfit (very morbid)
  public static ArrayList<NQueensSolution> selectParents(ArrayList<NQueensSolution> population) {
    int currentMember = 0;
    ArrayList<NQueensSolution> matingPool = new ArrayList<NQueensSolution>();
    while (currentMember < population.size() / 10) {
      // Pick top three
      NQueensSolution[] contenders = new NQueensSolution[3];
      for (int i = 0; i < 3; i++) {
        int ix = Math.round(randomNumberGen(0, population.size() - 1));
        contenders[i] = population.get(ix);
        
      }
//      System.out.println("zero: " + contenders[0]);
//      System.out.println("one: " + contenders[1]);
//      System.out.println("two: " + contenders[2]);
      Arrays.sort(contenders);
      matingPool.add(contenders[0]);
      currentMember++;
    }
    population = mate(matingPool, population);
    killWeaklings(population);
    return population;
  }

  // params: ArrayList<NQueensSolution> population: current population
  // postcondition: n individuals with lowest fitness are culled from population
  public static void killWeaklings(ArrayList<NQueensSolution> population) {
    Collections.sort(population, new NQueensComparator());
    for (int i = 0; i < population.size() / 10; i++)
      population.remove(population.size() - 1);
  }

  // params: matingPool: mating pool selected from selectParents()
  //         population: current population
  // returns: new population of size n + (10n)
  // offspring are produced, but weak are not killed yet
  public static ArrayList<NQueensSolution> mate(ArrayList<NQueensSolution>
                            matingPool, ArrayList<NQueensSolution> population) {
    List<NQueensSolution[]> parentPairs = new ArrayList<NQueensSolution[]>();
    // Pair by fitness
    Collections.sort(matingPool, new NQueensComparator());
    if (matingPool.size() % 2 == 1) matingPool.remove(matingPool.size() - 1);
    for (int i = 0; i < matingPool.size(); i+= 2) {
      NQueensSolution[] thisPair = {matingPool.get(i), matingPool.get(i + 1)};
      parentPairs.add(thisPair);
    }

    // for each pair of parents, cross genotypes
    for (NQueensSolution[] thisPair : parentPairs) {
      int crossPoint = Math.round(randomNumberGen(0, n));
      int[] gtype1 = crossArrays(thisPair[0].genotype, thisPair[1].genotype,
                                                                    crossPoint);
      int[] gtype2 = crossArrays(thisPair[1].genotype, thisPair[0].genotype,
                                                                    crossPoint);
      // 10% chance of mutation
      if (randomNumberGen(0, 9) == 0) mutate(gtype1);
      if (randomNumberGen(0, 9) == 0) mutate(gtype2);
      population.add(new NQueensSolution(gtype1));
      population.add(new NQueensSolution(gtype2));
    }
    return population;
  }

  // params: int[] a1: array to fill in first crossPoint values of return array
  //         int[] a2: array to fill in rest of values of return array
  //         int crossPoint: point at which to cross a1 and a2
  // returns: int[] containing first crossPoint values of a1, and remaining
  //          values from a2
  // precondition: a1.length() == a2.length(). should not be an issue
  public static int[] crossArrays(int[] a1, int[] a2, int crossPoint) {
    int[] child = new int[n];
    Set<Integer> takenNumbers = new HashSet<Integer>();
    for (int i = 0; i < crossPoint; i++) {
      child[i] = a1[i];
      takenNumbers.add(a1[i]);
    }
    int currentPosition = crossPoint;
    for (int i : a2) {
      if (currentPosition > n) return child;
      if (takenNumbers.contains(a2[i])) continue;
      else {
        child[currentPosition] = a2[i];
        takenNumbers.add(a2[i]);
        currentPosition++;
      }
    }
    return child;
  }

  // params: int[] genotype: genotype of the child to mutate
  // returns: int[] of new genotype with two values switched
  public static int[] mutate(int[] genotype) {
    int pos1 = Math.round(randomNumberGen(0, n - 1));
    int pos2 = -1;
    while (pos2 != pos1) pos2 = Math.round(randomNumberGen(0, n-1));
    int temp = genotype[pos1];
    genotype[pos1] = genotype[pos2];
    genotype[pos2] = temp;
    return genotype;
  }

  // params: int min: minimum
  //         int max: maximum (shocker)
  // returns: float randomly selected between min and max, uniformly
  public static float randomNumberGen(int min, int max) {
    Random r = new Random();
    float rawVal = r.nextFloat();
    return ((max - min) * (rawVal)) + min;
  }
}