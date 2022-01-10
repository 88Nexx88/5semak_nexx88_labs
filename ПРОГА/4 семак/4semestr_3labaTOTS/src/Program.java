

import java.io.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;
//0 - 1 2
//1 - 1
//2 - 1

class Graph
{
    private int numVertices;
    private LinkedList<Integer> adjLists[];



    Graph(int vertices)
    {
        numVertices = vertices;
        adjLists = new LinkedList[vertices];

        for (int i = 0; i < vertices; i++)
            adjLists[i] = new LinkedList();
    }

    void addEdge(int src, int dest)
    {
        adjLists[src].add(dest);
    }

    void printGraph(){
        for(int i = 0; i < numVertices; i++){
            ListIterator<Integer> itr = adjLists[i].listIterator();
            System.out.print(i + " -> ");
            while(itr.hasNext()){
                StdOut.print(itr.next()+" ");
            }
            System.out.println();
        }
    }



    boolean[] ergodic;
    boolean[] used;

    void randomGraphOR(int vertices) throws FileNotFoundException {
        boolean[] ergodic = new boolean[numVertices];
        Vector<Integer> posLink = new Vector<>(vertices);
        for(int i = 0;i<vertices;i++)
            posLink.add(i, i);
        int count = 0;
        int dest;
        int link = 3;
        while(count != vertices){

            for(int i = 0; i<link;i++){
                if(posLink.size()==0) break;
                int numDest = StdRandom.uniform(0, posLink.size());
                dest = posLink.get(numDest);
                posLink.remove(numDest);
                addEdge(count, dest);

            }
            if(count==vertices-1) ;
            else {
                //StdOut.print(" "+link+" ");
            }
            count++;
            posLink.removeAllElements();
            int j = 0;
            for(int i = 0;i<vertices-1;i++) {
                if (i == count) j++;
                posLink.add(i, j);
                j++;
            }
        }
        used = new boolean[numVertices];
        dfs(0);

        count = 0;
        for(int i = 0; i < vertices; i++){
            if(ergodic[i] = true) count++;
        }


        if(count == numVertices) {
            for (int i = 0; i < vertices; i++) {
                ListIterator<Integer> itr = adjLists[i].listIterator();
                System.out.print(i + " -> ");
                while (itr.hasNext()) {
                    StdOut.print(itr.next() + " ");
                }
                System.out.println();
            }
            CSV(numVertices);
        }
        else randomGraphOR(numVertices);
    }
    void CSV(int vertices) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(new File("D:/lab3_TOTS.csv"));
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < vertices; i++){
            sb.append(i);

            ListIterator<Integer> itr = adjLists[i].listIterator();
            while(itr.hasNext()){
                sb.append(',');
                sb.append(itr.next());
            }
            sb.append('\n');
        }
        pw.write(sb.toString());
        pw.close();
        StdOut.println("Файл в формате csv -> D:/lab3_TOTS.csv");
    }


    public void dfs(int v) throws FileNotFoundException {
        int count = 0;
        int v_test = v;
        Stack<Integer> stack = new Stack();
        if (used[v]) {
            return;
        }
        stack.push(v);
        used[v] = true;
        while (!stack.isEmpty()) {
            v = stack.pop();
            for (int i = 0; i < adjLists[v].size(); i++) {
                int w = adjLists[v].get(i);

                if (used[w]) {
                    continue;
                }
                stack.push(w);
                used[w] = true;

            }
        }

        System.out.println();
        for (int i = 0; i < adjLists[v].size(); i++) {
            if(used[i]==true) count++;
            else randomGraphOR(numVertices);
        }
        if(v_test == numVertices-1) ergodic[v_test] = true;
        else
            if(count == numVertices) dfs(++v_test);



    }
    float[][] arrayGraph;
    public void doMatrix(int v, Graph g) {
        int count = 0;
        float sum = 0;
        arrayGraph = new float[v][v];
        for (int i = 0; i < numVertices; i++) {
            count = 0;
            sum = 0;
            ListIterator<Integer> itr = g.adjLists[i].listIterator();
            for (int j = 0; j < 3; j++) {
                if (itr.hasNext()) {
                    if (count == 2){
                        arrayGraph[i][itr.next()] = (1 - sum);
                    }
                    else {
                        int t = itr.next();
                        double weight = Math.random();
                        MathContext context = new MathContext(3, RoundingMode.HALF_UP);
                        BigDecimal result = new BigDecimal(weight, context);
                        weight = result.floatValue();
                        if(weight>0.5) weight = 1 - weight;
                        arrayGraph[i][t] = (float)weight;
                        sum += arrayGraph[i][t];
                        count++;
                    }
                }
            }

        }

        for (int i = 0; i < numVertices; i++) {
            //sum = 0;
            for (int j = 0; j < numVertices; j++) {
                StdOut.print(arrayGraph[i][j]+" ");
                //sum+=arrayGraph[i][j];
            }
            //StdOut.print( sum+" ");
            StdOut.print("\n");
        }
    }













}


public class Program {
    public static void main(String[] args) throws FileNotFoundException {

        StdOut.println("Number of vertexes: ");
        int vertices = StdIn.readInt();
        Graph g = new Graph(vertices);
        g.randomGraphOR(vertices);
        StdOut.println();
        //g.matrixPerehodov();
        g.doMatrix(vertices, g);

    }
}
