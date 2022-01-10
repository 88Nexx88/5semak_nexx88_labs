

import java.io.*;
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
    void randomGraphNEOR(int vertices, float lambda, int metka){
        //for(int i = 0; i<100;i++) {
        //int vertices = StdRandom.uniform(1, 15);
        //Graph newGraph = new Graph(vertices);
        Vector<Integer> posLink = new Vector<>(vertices);
        for(int i = 0;i<vertices;i++)
            posLink.add(i, i);
        int count = 0;
        int sum = 0;
        int dest;
        int link = getPoisson(lambda);
        if(link==0) link+=metka;
        while(count != vertices){
            ListIterator<Integer> itr = adjLists[count].listIterator();
            int decount = 0;
            if(itr.hasNext())
                while (itr.hasNext()) {
                    decount++;
                    itr.next();
                }
            for(int i = 0; i<link-decount;i++){
                if(posLink.size()==0) break;
                int numDest = StdRandom.uniform(0, posLink.size());
                dest = posLink.get(numDest);
                posLink.remove(numDest);
                addEdge(count, dest);
                if (count == dest)
                        ;
                else addEdge(dest, count);
            }
            if(count==vertices-1) ;
            else {
                link = getPoisson(lambda);
                if (link == 0) link += metka;
                //StdOut.print(" "+link+" ");
            }
            count++;
            posLink.removeAllElements();
            for(int i = 0;i<vertices-count;i++)
                posLink.add(i, count+i);
            if(link>posLink.size()) {
                link = posLink.size();
            }
        }

        for(int i = 0; i < vertices; i++){
            ListIterator<Integer> itr = adjLists[i].listIterator();
            System.out.print(i + " -> ");
            while(itr.hasNext()){
                StdOut.print(itr.next()+" ");
                sum++;
            }
            System.out.println();
        }
        StdOut.println(sum/(float)vertices);

        //}
    }

    void randomGraphOR(int vertices, float lambda, int metka){
        Vector<Integer> posLink = new Vector<>(vertices);
        for(int i = 0;i<vertices;i++)
            posLink.add(i, i);
        int count = 0;
        int sum = 0;
        int dest;
        int link = getPoisson(lambda);
        if(link==0) link+=metka;
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
                link = getPoisson(lambda);
                if (link == 0) link += metka;
                //StdOut.print(" "+link+" ");
            }
            count++;
            posLink.removeAllElements();
            for(int i = 0;i<vertices;i++)
                posLink.add(i, i);
        }

        for(int i = 0; i < vertices; i++){
            ListIterator<Integer> itr = adjLists[i].listIterator();
            System.out.print(i + " -> ");
            while(itr.hasNext()){
                StdOut.print(itr.next()+" ");
                sum++;
            }
            System.out.println();
        }
        StdOut.println(sum/(float)vertices);
    }

    void CSV(int vertices) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(new File("D:/test.csv"));
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
        StdOut.println("Файл в формате csv -> D:/test.csv");
    }


    public static int getPoisson(double lambda) {
        double L = Math.exp(-lambda);
        double p = 1.0;
        int k = 0;

        do {
            k++;
            p *= Math.random();
        } while (p > L);

        return k - 1;
    }

        //
        //0 5 3 4 1 2
        boolean[] used;
        StringBuilder sb;


        public void dfs(int v, Graph g){
            int count = 0;
            Stack<Integer> stack = new Stack();
            if (used[v]) {
                return;
            }
            stack.push(v);
            used[v] = true;
            while (!stack.isEmpty()) {
                v = stack.pop();
                StdOut.print(v+" ");
                sb.append(v);
                sb.append(',');
                for (int i = 0; i < g.adjLists[v].size(); i++) {
                    int w = g.adjLists[v].get(i);

                    if (used[w]) {
                        continue;
                    }
                    stack.push(w);
                    used[w] = true;

                }
            }
            sb.delete(sb.length()-1,sb.length());
            sb.append("\n");
            System.out.println();
        }

    private Stack<Integer> stack;
    private boolean[] timeInOut;
        public void kasurajuAlgoritm(int vertices, String path) throws FileNotFoundException {

            sb = new StringBuilder();
            used = new boolean[vertices];
            timeInOut = new boolean[vertices];
            stack = new Stack();

            int v = StdRandom.uniform(vertices);
            findComponent(v);
            while(stack.size()!=vertices){
                for(int i=0;i<vertices;i++){
                    if(timeInOut[i]==false)  v = i;
                }
                findComponent(v);
            }

            Graph tranGraph = new Graph(vertices);
            for(int i = 0; i < vertices; i++) {
                ListIterator<Integer> itr = adjLists[i].listIterator();
                while (itr.hasNext()) {
                    tranGraph.adjLists[itr.next()].add(i);
                }
            }

            while(!stack.isEmpty()) {
                dfs(stack.pop(), tranGraph);
            }
            PrintWriter pw = new PrintWriter(new File(path));
            pw.write(sb.toString());
            pw.close();
            StdOut.println("A file with the results in the format csv -> "+path);
        }

        public void findComponent(int v){
            timeInOut[v] = true;
            for (int i = 0; i < adjLists[v].size(); i++) {
                int w = adjLists[v].get(i);
                if(timeInOut[w] == false){
                    findComponent(w);
                }
            }
            stack.push(v);
        }







}




public class Program {
    public static void main(String args[]) throws IOException {
        int key = Integer.parseInt(args[0]);//argument na rejim raboti
        switch (key){
            case(1):
                StdOut.println("Ориентированный (1)/ Неориентированный  (2): ");
                int k1 = StdIn.readInt();
                if(k1 == 1) {

                        StdOut.println("Введите кол-во вершин: ");
                        int vertices = StdIn.readInt();
                        StdOut.println("Введите сред степень вершин: ");
                        float sredPow = StdIn.readFloat();
                        Graph g = new Graph(vertices);
                        StdOut.println("Флаг на 0 связей. Введите (0) или (1): ");
                        int metka = StdIn.readInt();
                        g.randomGraphOR(vertices, sredPow, metka);
                        g.CSV(vertices);
                            StdOut.println();



                }
                else {

                    StdOut.println("Введите кол-во вершин: ");
                    int vertices = StdIn.readInt();
                    StdOut.println("Введите сред степень вершин: ");
                    float sredPow = StdIn.readFloat();
                    Graph g = new Graph(vertices);
                    StdOut.println("Флаг на 0 связей. Введите (0) или (1): ");
                    int metka = StdIn.readInt();
                    g.randomGraphNEOR(vertices, sredPow, metka);
                    g.CSV(vertices);
                }
                break;
            case(2):
                StdOut.println("Ориентированный (1)/ Неориентированный  (2): ");
                int k2 = StdIn.readInt();
                if(k2 == 1) {
                    StdOut.println("Введите кол-во вершин: ");
                    int vertices = StdIn.readInt();
                    Graph q = new Graph(vertices);
                    for (int i = 0; i < vertices; i++) {
                        StdOut.printf("Введите кол-во связей для вершины %d \n", i);
                        int countOflink = StdIn.readInt();
                        StdOut.printf("Введите связи для вершины %d \n", i);
                        for (int j = 0; j < countOflink; j++) {
                            StdOut.printf("Из %d в ", i);
                            int dest = StdIn.readInt();

                            q.addEdge(i, dest);
                        }

                    }
                    q.printGraph();
                    q.CSV(vertices);

                }
                else
                {
                    StdOut.println("Введите кол-во вершин: ");
                    int vertices = StdIn.readInt();
                    Graph q = new Graph(vertices);
                    for (int i = 0; i < vertices; i++) {
                        StdOut.printf("Введите кол-во связей для вершины %d \n", i);
                        int countOflink = StdIn.readInt();
                        StdOut.printf("Введите связи для вершины %d \n", i);
                        for (int j = 0; j < countOflink; j++) {
                            StdOut.printf("Из %d в ", i);
                            int dest = StdIn.readInt();

                            q.addEdge(i, dest);
                        }

                    }
                    q.printGraph();
                    q.CSV(vertices);
                }
                break;
            case(3):
                String path = args[1];//argument na path k file
                File file = new File(path);
                FileReader csv = new FileReader(file);
                BufferedReader reader = new BufferedReader(csv);
                String line = reader.readLine();
                int count = 0;

                while (line != null) {
                    count++;
                    line = reader.readLine();
                }

                Graph g = new Graph(count);
                FileReader csv1 = new FileReader(file);
                BufferedReader reader1 = new BufferedReader(csv1);
                String line1 = reader1.readLine();
                while (line1 != null) {
                    String[] link = line1.split(",");
                    for(int i = 1;i<link.length;i++) {
                        g.addEdge(Integer.parseInt(link[0]), Integer.parseInt(link[i]));
                    }
                    line1 = reader1.readLine();
                }
                StdOut.println("Graph in file: ");
                g.printGraph();
                StdOut.println("Result of the algoritm: ");
                g.kasurajuAlgoritm(count, args[2]);
                /*
                StdOut.println("BFS: ");
                for(int j = 0;j<count;j++) {
                    StdOut.printf("Результат обхода, если за начало взять вершину-%d: ", j);
                    g.bfs(j, count);
                    StdOut.println();
                }
                StdOut.println("DFS: ");
                for(int j = 0;j<count;j++) {
                    StdOut.printf("Результат обхода, если за начало взять вершину-%d: ", j);
                    g.dfs(j, count);
                    StdOut.println();
                }

                 */
                break;
            default:
                StdOut.println("Error ");
        }

    }
}

