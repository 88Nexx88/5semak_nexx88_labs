import java.io.*;
import java.util.*;

public class Program {
    static int max_color = 0;
    public static void main(String[] args) throws IOException {
        String path = args[0];

        File file = new File(path);
        FileReader csv = new FileReader(file);
        FileReader csv1 = new FileReader(file);
        BufferedReader reader = new BufferedReader(csv);
        BufferedReader reader1 = new BufferedReader(csv1);
        String line1 = reader1.readLine();
        int v = 0;
        while (line1 != null) {
            String[] link_ = line1.split(",");
            v = Integer.parseInt(link_[0]);
            line1 = reader1.readLine();
        }
        v++;
        boolean[] isVisited = new boolean[v];
        List<Edge>[] graph = createGraph(v);
        String line = reader.readLine();
        while(true) {
            int i = 0;
            while (line != null) {
                String[] link_ = line.split(",");
                addEdge(graph, Integer.parseInt(link_[0]), Integer.parseInt(link_[1]));
                line = reader.readLine();
            }
            break;
        }
        for(int i = 0; i < graph.length; i++){
            System.out.println("Вершина "+i+" степень "+graph[i].size());
        }
        System.out.println();
        /*
        addEdge(graph, 0, 1);
        addEdge(graph, 0, 3);
        addEdge(graph, 1, 0);
        addEdge(graph, 1, 2);
        addEdge(graph, 1, 3);
        addEdge(graph, 2, 1);
        addEdge(graph, 2, 3);
        addEdge(graph, 2, 4);
        addEdge(graph, 3, 0);
        addEdge(graph, 3, 1);
        addEdge(graph, 3, 2);
        addEdge(graph, 3, 4);
        addEdge(graph, 4, 2);
        addEdge(graph, 4, 3);

         */
        edgeColor(4, graph, isVisited);
        System.out.println("Результат в формате ГРАНЬ/ЦВЕТ");
        for(int i = 0; i < graph.length;i++){
            for(int j = 0; j < graph[i].size();j++){
                System.out.println(i+"->"+graph[i].get(j).t+" / "+graph[i].get(j).color);
            }
        }
        Vector<Integer> checker = new Vector<>(0);
        checker.addElement(0);
        //graph[0].get(1).setColor(graph[0].get(0).color);
        boolean check = false;
        for(int i = 0; i < graph.length;i++){
            for(int j = 0; j < graph[i].size();j++){
                if(checker.contains(graph[i].get(j).color)) {
                    check = true;
                }
                checker.addElement(graph[i].get(j).color);
            }
            checker.clear();
        }
        if(check)  System.out.println("Ошибка, повторяющийся цвет :( данил исправляй");


        System.out.println("Хроматический индекс графа: "+max_color);





        if(Integer.parseInt(args[1]) == 1) {
            System.out.println("Кол-во вершин: ");
            int vertices = StdIn.readInt();
            System.out.println("Лямда: ");
            int lymbda = StdIn.readInt();
            randomGraphOR(vertices, lymbda);
        }

    }

    public static void edgeColor(int ptr, List<Edge>[] graph, boolean[] isVisited){
        Queue<Integer> q = new LinkedList<>();
        int c = 0;
        Vector<Integer> colored = new Vector<>(0);
        colored.addElement(0);
        if (isVisited[ptr])
            return;
        //for(int i = 0; i < isVisited.length; i++){
        //    System.out.print(isVisited[i]+" ");
        //}
        //System.out.println();
        isVisited[ptr] = true;
        //System.out.print(ptr+"->"+graph[ptr].get(0).t+" "+graph[ptr].get(0).color);
        //System.out.println(isVisited[ptr]);
        /*
        0 edge1 1 color 1 2 4  edge2 3
        1 edge1  edge2
         */
        for(int i = 0; i < graph[ptr].size();i++){
            if(graph[ptr].get(i).color != -1)
                colored.addElement(graph[ptr].get(i).color);
        }
        /*
        for(int i = 0; i < graph[ptr].size();i++){
            for(int j = 0; j < graph[graph[ptr].get(i).t].size();j++) {
                if(graph[graph[ptr].get(i).t].get(j).color != -1 && colored.contains(graph[graph[ptr].get(i).t].get(j).color) != true)
                    colored.addElement(graph[graph[ptr].get(i).t].get(j).color);
            }
        }

         */
        System.out.println(ptr+"- "+colored);
        for(int i = 0; i < graph[ptr].size();i++) {
            if(!isVisited[graph[ptr].get(i).t])
                q.add(graph[ptr].get(i).t);
            if(graph[ptr].get(i).color == -1) {

                while (colored.contains(c) == true)
                    c++;

                graph[ptr].get(i).setColor(c);
                int r = graph[ptr].get(i).t;
                int cheker = 0;
                for(int l = 0; l < graph[r].size(); l++){
                    if(graph[r].get(l).color == c) cheker = 1;
                }
                for (int j = 0; j < graph[r].size(); j++)
                    if (graph[r].get(j).t == ptr && graph[r].get(j).color == -1 && cheker == 0)
                            graph[r].get(j).setColor(c);

                colored.addElement(c);
                c++;

            }
        }
        while(!q.isEmpty()){
            int temp = q.poll();
            edgeColor(temp, graph, isVisited);
        }


    }
    static class Edge {
        int t;
        int color;
        public Edge(int t) {
            this.t = t;
            this.color = -1;

        }
        void setColor(int c){
            this.color = c;
            if(c > max_color) max_color = c;
        }
    }


    public static List<Edge>[] createGraph(int nodes) {
        List<Edge>[] graph = new List[nodes];
        for (int i = 0; i < nodes; i++)
            graph[i] = new ArrayList<>();
        return graph;
    }

    public static void addEdge(List<Edge>[] graph, int s, int t) {
        graph[s].add(new Edge(t));
    }


    static void CSV(List<Edge>[] g) throws FileNotFoundException {

        PrintWriter pw = new PrintWriter(new File("D:/lab9.csv"));
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < g.length; i++){
            ListIterator<Edge> itr = g[i].listIterator();
            while(itr.hasNext()) {
                Edge edge = itr.next();
                StdOut.print("from "+i +" to " +edge.t);
                sb.append(i+","+edge.t);

                sb.append('\n');
            }
        }
        pw.write(sb.toString());
        pw.close();
        StdOut.println("Файл в формате csv -> D:/lab9.csv");
    }

    static List<Edge>[] randomGraphOR(int vertices, float lambda) throws FileNotFoundException {
            List<Edge>[] graph = createGraph(vertices);

            int count = 0;
            int s = 0;
            int dest;
            int link = getPoisson(lambda);
            while(count != vertices){
                for(int i = 0; i<link;i++){
                    dest = StdRandom.uniform(vertices);
                    if(dest == count) break;
                    for(int j = 0; j < graph[count].size();j++){
                        if(graph[count].get(j).t == dest) s = 1;
                    }
                    if(s == 1) break;
                    addEdge(graph, count, dest);
                    addEdge(graph, dest, count);

                }
                s = 0;
                if(count==vertices-1) ;
                else {
                    link = getPoisson(lambda);
                    //StdOut.print(" "+link+" ");
                }
                count++;
            }


        CSV(graph);
        return graph;
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
}





