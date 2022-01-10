import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

public class Hungarian {

    private int numRows;
    private int numCols;

    private boolean[][] primes;
    private boolean[][] stars;
    private boolean[] rowsCovered;
    private boolean[] colsCovered;
    private int[][] costs;

    public Hungarian(int theCosts[][]) {
        costs = theCosts;
        numRows = costs.length;
        numCols = costs[0].length;

        primes = new boolean[numRows][numCols];
        stars = new boolean[numRows][numCols];

        // Инициализация массивов с покрытием строк/столбцов
        rowsCovered = new boolean[numRows];
        colsCovered = new boolean[numCols];
        for (int i = 0; i < numRows; i++) {
            rowsCovered[i] = false;
        }
        for (int j = 0; j < numCols; j++) {
            colsCovered[j] = false;
        }
        // Инициализация матриц
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                primes[i][j] = false;
                stars[i][j] = false;
            }
        }
    }

    public int[][] execute() {
        subtractRowColMins();

        this.findStars(); // O(n^2)
        this.resetCovered(); // O(n);
        this.coverStarredZeroCols(); // O(n^2)

        while (!allColsCovered()) {
            int[] primedLocation = this.primeUncoveredZero(); // O(n^2)

            // It's possible that we couldn't find a zero to prime, so we have to induce some zeros so we can find one to prime
            if (primedLocation[0] == -1) {
                this.minUncoveredRowsCols(); // O(n^2)
                primedLocation = this.primeUncoveredZero(); // O(n^2)
            }

            // is there a starred 0 in the primed zeros row?
            int primedRow = primedLocation[0];
            int starCol = this.findStarColInRow(primedRow);
            if (starCol != -1) {
                // cover ther row of the primedLocation and uncover the star column
                rowsCovered[primedRow] = true;
                colsCovered[starCol] = false;
            } else { // otherwise we need to find an augmenting path and start over.
                this.augmentPathStartingAtPrime(primedLocation);
                this.resetCovered();
                this.resetPrimes();
                this.coverStarredZeroCols();
            }
        }

        return this.starsToAssignments(); // O(n^2)

    }

    /*
     * the starred 0's in each column are the assignments.
     * O(n^2)
     */
    public int[][] starsToAssignments() {
        int[][] toRet = new int[numCols][];
        for (int j = 0; j < numCols; j++) {
            toRet[j] = new int[] {
                    this.findStarRowInCol(j), j
            }; // O(n)
        }
        return toRet;
    }

    /*
     * resets prime information
     */
    public void resetPrimes() {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                primes[i][j] = false;
            }
        }
    }


    /*
     * resets covered information, O(n)
     */
    public void resetCovered() {
        for (int i = 0; i < numRows; i++) {
            rowsCovered[i] = false;
        }
        for (int j = 0; j < numCols; j++) {
            colsCovered[j] = false;
        }
    }

    /*
     * get the first zero in each column, star it if there isn't already a star in that row
     * cover the row and column of the star made, and continue to the next column
     * O(n^2)
     */
    public void findStars() {
        boolean[] rowStars = new boolean[numRows];
        boolean[] colStars = new boolean[numCols];

        for (int i = 0; i < numRows; i++) {
            rowStars[i] = false;
        }
        for (int j = 0; j < numCols; j++) {
            colStars[j] = false;
        }

        for (int j = 0; j < numCols; j++) {
            for (int i = 0; i < numRows; i++) {
                if (costs[i][j] == 0 && !rowStars[i] && !colStars[j]) {
                    stars[i][j] = true;
                    rowStars[i] = true;
                    colStars[j] = true;
                    break;
                }
            }
        }
    }

    /*
     * Finds the minimum uncovered value, and adds it to all the covered rows then
     * subtracts it from all the uncovered columns. This results in a cost matrix with
     * at least one more zero.
     */
    private void minUncoveredRowsCols() {
        // find min uncovered value
        int minUncovered = Integer.MAX_VALUE;
        for (int i = 0; i < numRows; i++) {
            if (!rowsCovered[i]) {
                for (int j = 0; j < numCols; j++) {
                    if (!colsCovered[j]) {
                        if (costs[i][j] < minUncovered) {
                            minUncovered = costs[i][j];
                        }
                    }
                }
            }
        }

        // add that value to all the COVERED rows.
        for (int i = 0; i < numRows; i++) {
            if (rowsCovered[i]) {
                for (int j = 0; j < numCols; j++) {
                    costs[i][j] = costs[i][j] + minUncovered;

                }
            }
        }

        // subtract that value from all the UNcovered columns
        for (int j = 0; j < numCols; j++) {
            if (!colsCovered[j]) {
                for (int i = 0; i < numRows; i++) {
                    costs[i][j] = costs[i][j] - minUncovered;
                }
            }
        }
    }

    /*
     * Finds an uncovered zero, primes it, and returns an array
     * describing the row and column of the newly primed zero.
     * If no uncovered zero could be found, returns -1 in the indices.
     * O(n^2)
     */
    private int[] primeUncoveredZero() {
        int[] location = new int[2];

        for (int i = 0; i < numRows; i++) {
            if (!rowsCovered[i]) {
                for (int j = 0; j < numCols; j++) {
                    if (!colsCovered[j]) {
                        if (costs[i][j] == 0) {
                            primes[i][j] = true;
                            location[0] = i;
                            location[1] = j;
                            return location;
                        }
                    }
                }
            }
        }

        location[0] = -1;
        location[1] = -1;
        return location;
    }

    /*
     * Starting at a given primed location[0=row,1=col], we find an augmenting path
     * consisting of a primed , starred , primed , ..., primed. (note that it begins and ends with a prime)
     * We do this by starting at the location, going to a starred zero in the same column, then going to a primed zero in
     * the same row, etc, until we get to a prime with no star in the column.
     * O(n^2)
     */
    private void augmentPathStartingAtPrime(int[] location) {
        // Make the arraylists sufficiently large to begin with
        ArrayList < int[] > primeLocations = new ArrayList < int[] > (numRows + numCols);
        ArrayList < int[] > starLocations = new ArrayList < int[] > (numRows + numCols);
        primeLocations.add(location);

        int currentRow = location[0];
        int currentCol = location[1];
        while (true) { // add stars and primes in pairs
            int starRow = findStarRowInCol(currentCol);
            // at some point we won't be able to find a star. if this is the case, break.
            if (starRow == -1) {
                break;
            }
            int[] starLocation = new int[] {
                    starRow, currentCol
            };
            starLocations.add(starLocation);
            currentRow = starRow;

            int primeCol = findPrimeColInRow(currentRow);
            int[] primeLocation = new int[] {
                    currentRow, primeCol
            };
            primeLocations.add(primeLocation);
            currentCol = primeCol;
        }

        unStarLocations(starLocations);
        starLocations(primeLocations);
    }


    /*
     * Given an arraylist of  locations, star them
     */
    private void starLocations(ArrayList < int[] > locations) {
        for (int k = 0; k < locations.size(); k++) {
            int[] location = locations.get(k);
            int row = location[0];
            int col = location[1];
            stars[row][col] = true;
        }
    }

    /*
     * Given an arraylist of starred locations, unstar them
     */
    private void unStarLocations(ArrayList < int[] > starLocations) {
        for (int k = 0; k < starLocations.size(); k++) {
            int[] starLocation = starLocations.get(k);
            int row = starLocation[0];
            int col = starLocation[1];
            stars[row][col] = false;
        }
    }


    /*
     * Given a row index, finds a column with a prime. returns -1 if this isn't possible.
     */
    private int findPrimeColInRow(int theRow) {
        for (int j = 0; j < numCols; j++) {
            if (primes[theRow][j]) {
                return j;
            }
        }
        return -1;
    }




    /*
     * Given a column index, finds a row with a star. returns -1 if this isn't possible.
     */
    public int findStarRowInCol(int theCol) {
        for (int i = 0; i < numRows; i++) {
            if (stars[i][theCol]) {
                return i;
            }
        }
        return -1;
    }


    public int findStarColInRow(int theRow) {
        for (int j = 0; j < numCols; j++) {
            if (stars[theRow][j]) {
                return j;
            }
        }
        return -1;
    }

    // looks at the colsCovered array, and returns true if all entries are true, false otherwise
    private boolean allColsCovered() {
        for (int j = 0; j < numCols; j++) {
            if (!colsCovered[j]) {
                return false;
            }
        }
        return true;
    }

    /*
     * sets the columns covered if they contain starred zeros
     * O(n^2)
     */
    private void coverStarredZeroCols() {
        for (int j = 0; j < numCols; j++) {
            colsCovered[j] = false;
            for (int i = 0; i < numRows; i++) {
                if (stars[i][j]) {
                    colsCovered[j] = true;
                    break; // break inner loop to save a bit of time
                }
            }
        }
    }

    private void subtractRowColMins() {
        for (int i = 0; i < numRows; i++) { //for each row
            int rowMin = Integer.MAX_VALUE;
            for (int j = 0; j < numCols; j++) { // grab the smallest element in that row
                if (costs[i][j] < rowMin) {
                    rowMin = costs[i][j];
                }
            }
            for (int j = 0; j < numCols; j++) { // subtract that from each element
                costs[i][j] = costs[i][j] - rowMin;
            }
        }

        for (int j = 0; j < numCols; j++) { // for each col
            int colMin = Integer.MAX_VALUE;
            for (int i = 0; i < numRows; i++) { // grab the smallest element in that column
                if (costs[i][j] < colMin) {
                    colMin = costs[i][j];
                }
            }
            for (int i = 0; i < numRows; i++) { // subtract that from each element
                costs[i][j] = costs[i][j] - colMin;
            }
        }
    }

    public static int[][] generationDvudolniyGraph(int vertices){
        int[][] graph = new int[vertices][vertices];
        for(int i = 0; i < vertices; i++){
            for(int j = 0; j < vertices; j++){
                graph[i][j] = StdRandom.uniform(1, 21);
            }
        }
        return graph;
    }
    static void CSV(int[][] graph) throws FileNotFoundException {

        PrintWriter pw = new PrintWriter(new File("D:/lab6.csv"));
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < graph[0].length; i++){
            for(int j = 0; j < graph[0].length; j++){
                sb.append(graph[i][j]);
                if(j==graph[0].length-1) ;
                else sb.append(',');
            }
            sb.append('\n');
        }

        pw.write(sb.toString());
        pw.close();
        StdOut.println("Файл в формате csv -> D:/lab6.csv");
    }
    static int max = 30;
    public static void main(String[] args) throws IOException {
        int keys = Integer.parseInt(args[0]);
        switch (keys) {
            case (0):
                System.out.print("Введите кол-во вершин: ");
                int key = StdIn.readInt();
                int[][] matrix = generationDvudolniyGraph(key);
                int vertices = matrix.length;
                if (vertices <= max) {
                    System.out.println("Двудольный граф: ");
                    for (int i = 0; i < vertices; i++) {
                        System.out.print("\t    ");
                        for (int j = 0; j < vertices; j++) {
                            System.out.print(matrix[i][j] + " ");
                        }
                        System.out.println();
                    }
                }
                int[][] original = new int[vertices][vertices];
                for (int i = 0; i < vertices; i++) {
                    original[i] = Arrays.copyOf(matrix[i], matrix[i].length);
                }
                for (int i = 0; i < vertices; i++) {
                    for (int j = 0; j < vertices; j++) {
                        matrix[i][j] = matrix[i][j] * -1;
                    }
                }
                System.out.println("Результат работы алгоритма: ");
                int[][] result = new Hungarian(matrix).execute();
                System.out.println("Номер строки/столбца");
                for (int i = 0; i < vertices; i++) {
                    System.out.println("\t    " + result[i][0] + "\t\t" + result[i][1]);
                }

                if (vertices <= max) {
                    System.out.println("Результат: ");
                    for (int i = 0; i < vertices; i++) {
                        System.out.print("\t    ");
                        for (int j = 0; j < vertices; j++) {
                            int checker = 0;
                            for (int k = 0; k < vertices; k++)
                                if (result[k][0] == i && result[k][1] == j) {
                                    System.out.print("[" + original[i][j] + "] ");
                                    checker++;
                                    break;
                                }
                            if (checker == 1) ;
                            else System.out.print(original[i][j] + " ");
                        }
                        System.out.println();
                    }
                    int sum = 0;
                    for (int i = 0; i < vertices; i++) {
                        for (int j = 0; j < vertices; j++) {
                            for (int k = 0; k < vertices; k++)
                                if (result[k][0] == i && result[k][1] == j) {
                                    sum += original[i][j];
                                    break;
                                }

                        }
                    }
                    System.out.println("Сумма: "+sum);
                }
                CSV(original);
                break;
            case (1):
                String path = args[1];

                File file = new File(path);
                FileReader csv = new FileReader(file);
                FileReader csv1 = new FileReader(file);
                BufferedReader reader = new BufferedReader(csv);
                BufferedReader reader1 = new BufferedReader(csv1);
                String line1 = reader1.readLine();
                int v = 0;
                while (line1 != null) {
                   v++;
                   line1 = reader1.readLine();
                }
                int[][] graph = new int[v][v];
                String line = reader.readLine();
                v = 0;
                while(true) {
                    while (line != null) {
                        String[] link_ = line.split(",");
                        for (int i = 0; i < link_.length;i++){
                            graph[v][i] = Integer.parseInt(link_[i]);
                        }
                        v++;
                        line = reader.readLine();
                    }
                    break;
                }
                if (graph[0].length <= max) {
                    System.out.println("Двудольный граф: ");
                    for (int i = 0; i < graph[0].length; i++) {
                        System.out.print("\t    ");
                        for (int j = 0; j < graph[0].length; j++) {
                            System.out.print(graph[i][j] + " ");
                        }
                        System.out.println();
                    }
                }
                int[][] original_file = new int[v][v];
                for (int i = 0; i < graph[0].length; i++) {
                    original_file[i] = Arrays.copyOf(graph[i], graph[i].length);
                }
                for (int i = 0; i < graph[0].length; i++) {
                    for (int j = 0; j < graph[0].length; j++) {
                        graph[i][j] = graph[i][j] * -1;
                    }
                }
                System.out.println("Результат работы алгоритма: ");
                int[][] result_file = new Hungarian(graph).execute();
                System.out.println("Номер строки/столбца");
                for (int i = 0; i < graph[0].length; i++) {
                    System.out.println("\t    " + result_file[i][0] + "\t\t" + result_file[i][1]);
                }

                if (graph[0].length <= max) {
                    System.out.println("Результат: ");
                    for (int i = 0; i < graph[0].length; i++) {
                        System.out.print("\t    ");
                        for (int j = 0; j < graph[0].length; j++) {
                            int checker = 0;
                            for (int k = 0; k < graph[0].length; k++)
                                if (result_file[k][0] == i && result_file[k][1] == j) {
                                    System.out.print("[" + original_file[i][j] + "] ");
                                    checker++;
                                    break;
                                }
                            if (checker == 1) ;
                            else System.out.print(original_file[i][j] + " ");
                        }
                        System.out.println();
                    }
                    int sum = 0;
                    for (int i = 0; i < graph[0].length; i++) {
                        for (int j = 0; j < graph[0].length; j++) {
                            for (int k = 0; k < graph[0].length; k++)
                                if (result_file[k][0] == i && result_file[k][1] == j) {
                                    sum += original_file[i][j];
                                    break;
                                }

                        }
                    }
                    System.out.println("Сумма: "+sum);
                }


                break;
        }
    }

}
