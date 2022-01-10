import static java.lang.String.format;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FloydWarshall {

    public static void main(String[] args) throws IOException {
        String path = args[0];

        File file = new File(path);
        FileReader csv = new FileReader(file);
        FileReader csv1 = new FileReader(file);
        BufferedReader reader = new BufferedReader(csv);
        BufferedReader reader1 = new BufferedReader(csv1);
        String line1 = reader1.readLine();
        int v = 0;
        int count_line = 0;
        while (line1 != null) {
            String[] link_ = line1.split(",");
            v = Integer.parseInt(link_[0]);
            count_line++;
            line1 = reader1.readLine();
        }
        //v++;
        int[][] weights = new int[count_line][3];
        int[][] matrix = new int[v+1][v+1];
        //System.out.println(v);
        String line = reader.readLine();
        while(true) {
            int i = 0;
            while (line != null) {
                String[] link_ = line.split(",");
                    if (Integer.parseInt(link_[2]) <= 0) {
                        System.out.println("Граф имеет отрицательный цикл :( ");
                        System.exit(0);
                    }
                    matrix[Integer.parseInt(link_[0])][Integer.parseInt(link_[1])] = Integer.parseInt(link_[2]);
                    weights[i][0] = Integer.parseInt(link_[0]);
                    weights[i][1] = Integer.parseInt(link_[1]);
                    weights[i][2] = Integer.parseInt(link_[2]);
                    i++;
                    line = reader.readLine();
            }
            break;
        }
        /*
        int[][] weights = {
                {2, 1, 4},
                {1, 3, 2},
                {2, 3, 3},
                {3, 4, 2},
                {4, 2, 1}};
        int v = 4;

         */
        /*


         */
        System.out.println("Граф из файла: ");
        for (int k = 1; k < v+1; k++){
            for (int i = 1; i < v+1; i++) {
                System.out.printf("%d ",matrix[k][i]);
            }
            System.out.println();
        }
        floydWarshall(weights, v);
    }

    static void floydWarshall(int[][] weights, int numVertices) {

        double[][] dist = new double[numVertices][numVertices];
        for (double[] row : dist)
            Arrays.fill(row, Double.POSITIVE_INFINITY);

        for (int[] w : weights)
            dist[w[0] - 1][w[1] - 1] = w[2];

        int[][] next = new int[numVertices][numVertices];
        for (int i = 0; i < next.length; i++) {
            for (int j = 0; j < next.length; j++)
                if (i != j)
                    next[i][j] = j + 1;
        }

        for (int k = 0; k < numVertices; k++)
            for (int i = 0; i < numVertices; i++)
                for (int j = 0; j < numVertices; j++)
                    if (dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                        next[i][j] = next[i][k];
                    }

        printResult(dist, next);
    }

    static void printResult(double[][] dist, int[][] next) {
        int maxLength = 2;
        String format = "%" + maxLength + "d  ";
        System.out.println("Результат работы алгоритма: ");
        for (int i = 0; i < next.length; i++) {
            for (int j = 0; j < next.length; j++) {
                if(i == j) System.out.print(" *  ");
                else {
                    if((int)dist[i][j] == 2147483647) System.out.printf(" N  ");
                    else System.out.printf(format, (int)dist[i][j]);
                }

            }
            System.out.println();
        }
        System.out.println("Пути: ");
        for (int i = 0; i < next.length; i++) {
            for (int j = 0; j < next.length; j++) {
                if (i != j) {
                    int u = i + 1;
                    int v = j + 1;
                    String path = format("%s", u);
                    do {
                            u = next[u - 1][v - 1];
                            path += " -> " + u;
                    } while (u != v);
                    if((int)dist[i][j] == 2147483647) path+= " - Путей нет";
                    else path+= format("  [%d]", (int)dist[i][j]);
                    System.out.println(path);
                }
            }
        }
        /*
            System.out.println("pair     dist    path");
                    for (int i = 0; i < next.length; i++) {
                        for (int j = 0; j < next.length; j++) {
                            if (i != j) {
                                int u = i + 1;
                                int v = j + 1;
                                String path = format("%d -> %d    %2d     %s", u, v,
                                        (int) dist[i][j], u);
                                do {
                                    u = next[u - 1][v - 1];
                                    path += " -> " + u;
                                } while (u != v);
                                System.out.println(path);
                            }
                        }
                    }
 */
    }
}

