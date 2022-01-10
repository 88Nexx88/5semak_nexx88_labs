// Java-реализация алгоритма Дейкстры
// используя очередь приоритетов

import java.io.*;
import java.util.*;

public class DPQ {
    private int dist[];
    private Set<Integer> settled;
    private PriorityQueue<Node> pq;
    private int V;
    List<List<Node> > adj;


    public DPQ(int V)

    {
        this.V = V;
        dist = new int[V];
        settled = new HashSet<Integer>();
        pq = new PriorityQueue<Node>(V, new Node());
    }



    // Функция для алгоритма Дейкстры
    public void dijkstra(List<List<Node> > adj, int src)
    {
        this.adj = adj;
        for (int i = 0; i < V; i++)
            dist[i] = Integer.MAX_VALUE;
        // Добавить исходный узел в приоритетную очередь
        pq.add(new Node(src, 0));
        // Расстояние до источника равно 0
        dist[src] = 0;
        while (settled.size() != V) {
            // удаляем узел минимального расстояния
            // из очереди приоритетов
            if(pq.isEmpty()) break;
            int u = pq.remove().node;
            // добавляем узел, расстояние которого
            // доработано
            settled.add(u);
            e_Neighbours(u);
        }
    }
    // Функция для обработки всех соседей
    // пройденного узла
    private void e_Neighbours(int u)
    {
        int edgeDistance = -1;
        int newDistance = -1;
        // Все соседи v
        for (int i = 0; i < adj.get(u).size(); i++) {
            Node v = adj.get(u).get(i);
            // Если текущий узел еще не был обработан
            if (!settled.contains(v.node)) {
                edgeDistance = v.cost;
                newDistance = dist[u] + edgeDistance;
                // Если новое расстояние дешевле в стоимости
                if (newDistance < dist[v.node]) {
                    dist[v.node] = newDistance;
                }
                // Добавить текущий узел в очередь
                pq.add(new Node(v.node, dist[v.node]));
            }
        }
    }




    static int istok_network;
    static int stok_network;
    public static List<List<Node>> generationGraph(int vertices, int max_flow, List<List<Node>> adj){
        int link = 0;
        int count = 0;
        int max_edge = vertices*(vertices-1)/2;
        int istok = StdRandom.uniform(vertices-1);
        int stok = StdRandom.uniform(vertices-1);
        if(istok == stok)
            if(stok+1<vertices-1) stok++;
            else istok--;
        for(int i = 0; i<vertices; i++) {
            if(vertices < max_edge)
                link = StdRandom.uniform(1, vertices/2);
            else
                if(i!=stok && i==vertices-1) {
                    if(vertices-2 <= max_edge) link = vertices-2;
                    else
                        if(max_edge>0) link = max_edge;
                        else link = 1;
                }
                else{
                    if(vertices*(vertices-1)/2 - count > max_edge) max_edge = vertices*(vertices-1)/2 - count;
                }


            max_edge -= link;
            if(i == stok)  link = 0;

            boolean[] used = new boolean[vertices];
            for(int j = 0; j<link;j++){
                int dest = StdRandom.uniform(0, vertices);

                if(used[dest] == true||dest == istok||dest==i) {
                    for(int k = i+1; k < vertices; k++)
                        if(used[k]==false && k!=istok) {
                            dest = k;
                            break;
                        }
                    if(used[dest] == true) continue;
                }
                if(dest == istok) continue;
                used[dest] = true;
                int weight = StdRandom.uniform(1, max_flow);
                adj.get(i).add(new Node(dest, weight));
                count++;
            }

        }
        istok_network = istok;
        stok_network = stok;
        return adj;


    }

    public static void CSV(int vertices, List<List<Node>> adj) throws FileNotFoundException {

        PrintWriter pw = new PrintWriter(new File("D:/lab7.csv"));
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < vertices; i++){
            ListIterator<Node> itr = adj.get(i).listIterator();
            StdOut.println();
            while(itr.hasNext()) {
                Node node = itr.next();
                StdOut.print("from "+i +" to " +node.node +" throughput capacity "+ node.cost+"; ");
                sb.append(i+","+node.node+","+node.cost);

                if(i == vertices-1) {
                    ;
                }
                else
                    sb.append('\n');
            }
            if(i == stok_network) {
                StdOut.print("from "+i);
                sb.append(stok_network);
                sb.append("\n");
            }


        }
        pw.write(sb.toString());
        pw.close();
        StdOut.println("Файл в формате csv -> D:/lab5.csv");
    }



    // Код драйвера
    public static void main(String[] args) throws IOException {
        int key = Integer.parseInt(args[0]);//argument na rejim raboti
        switch (key) {
            case (1):
                StdOut.println("Number of vertexes: ");
                int vertices = StdIn.readInt();
                StdOut.println("Max throughput capacity: ");
                int max_flow = StdIn.readInt();
                List<List<Node> > adj = new ArrayList<List<Node> >();
                for (int i = 0; i < vertices; i++) {
                    List<Node> item = new ArrayList<Node>();
                    adj.add(item);
                }
                List<List<Node> > graph = generationGraph(vertices, max_flow, adj);
                CSV(vertices, graph);
                System.out.println(istok_network+" "+stok_network);
                DPQ dpq = new DPQ(vertices);
                dpq.dijkstra(adj, istok_network);

                if(dpq.dist[stok_network] == Integer.MAX_VALUE) System.out.println("Исток и сток не связанны");
                else {
                    System.out.println("The shorted path from node :");
                    System.out.println(istok_network + " to " + stok_network + " is "
                            + dpq.dist[stok_network]);
                    int[][] graph_matrix = new int[vertices][vertices];
                    for (int i = 0; i < vertices; i++) {
                        ListIterator<Node> itr = adj.get(i).listIterator();
                        while (itr.hasNext()) {
                            Node node = itr.next();
                            graph_matrix[i][node.node] = node.cost;
                        }
                    }
                    int check = stok_network;
                    int k = 0;
                    Vector<Integer> result = new Vector<>(0);
                    result.add(stok_network);
                    while (true) {
                        for (int i = 0; i < vertices; i++) {
                            if (dpq.dist[check] - graph_matrix[i][check] == dpq.dist[i] && graph_matrix[i][check] != 0) {
                                check = i;
                                k++;
                                break;
                            }
                        }
                        if (check == istok_network) {
                            result.add(check);
                            break;
                        } else result.add(check);

                        if (k > vertices * (vertices - 1) / 2) break;
                    }
                    for (int i = result.size()-1; i >=0; i--) {
                        System.out.print(result.get(i) + " ");
                    }
                }



                break;
            case (2):
                String path = args[1];

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
                List<List<Node> > adj_file = new ArrayList<List<Node> >();
                for (int i = 0; i < v; i++) {
                    List<Node> item = new ArrayList<Node>();
                    adj_file.add(item);
                }
                //System.out.println(v);
                String line = reader.readLine();
                while(true) {
                    while (line != null) {
                        String[] link_ = line.split(",");
                        if(link_.length==1) ;
                        else {
                            if(Integer.parseInt(link_[2]) <= 0) {
                                System.out.println("Граф имеет отрицательный цикл :( ");
                                System.exit(0);
                            }
                            adj_file.get(Integer.parseInt(link_[0])).add(new Node(Integer.parseInt(link_[1]), Integer.parseInt(link_[2])));
                        }
                        line = reader.readLine();
                    }
                    break;
                }

                boolean[] istok_file = new boolean[v];
                int stok_file = -1;
                for(int i = 0; i < v; i++){
                    ListIterator<Node> itr = adj_file.get(i).listIterator();
                    if(itr.hasNext()==false) stok_file = i;
                    while(itr.hasNext()){
                        Node node = itr.next();
                        if(istok_file[node.node]==false) {
                            istok_file[node.node] = true;
                        }
                    }
                }
                int source = 0;
                System.out.println("The program has found source: ");
                for(int i = 0; i<v;i++) {
                    if(stok_file==-1) {
                        StdOut.println(" Нет стока");
                        break;
                    }
                    if (istok_file[i] == false) {
                        source = i;
                        StdOut.println(i + " - исток");

                    }
                }
                StdOut.println(stok_file + " - сток");
                System.out.println("Select a source: ");
                int source1 = StdIn.readInt();



                for(int i = 0; i < v; i++){
                    ListIterator<Node> itr = adj_file.get(i).listIterator();
                    while(itr.hasNext()){
                        Node node = itr.next();
                        StdOut.print("from "+i +" to " +node.node +" throughput capacity "+ node.cost+"; ");
                    }

                    if(i!=19)
                        StdOut.println();
                    else {
                        StdOut.println("from "+ stok_file);
                    }

                }
                DPQ dpq_file = new DPQ(v);
                dpq_file.dijkstra(adj_file, source1);
                if(dpq_file.dist[stok_file] == Integer.MAX_VALUE) System.out.println("Исток и сток не связанны");
                else {
                    System.out.println("The shorted path from node :");
                    System.out.println(source1 + " to " + stok_file + " is "
                            + dpq_file.dist[stok_file]);
                    int[][] graph_matrix = new int[v][v];
                    for (int i = 0; i < v; i++) {
                        ListIterator<Node> itr = adj_file.get(i).listIterator();
                        while (itr.hasNext()) {
                            Node node = itr.next();
                            graph_matrix[i][node.node] = node.cost;
                        }
                    }
                    int check = stok_file;
                    int k = 0;
                    Vector<Integer> result = new Vector<>(0);
                    result.add(check);
                    while (true) {
                        for (int i = 0; i < v; i++) {
                            if (dpq_file.dist[check] - graph_matrix[i][check] == dpq_file.dist[i] && graph_matrix[i][check] != 0) {
                                check = i;
                                k++;
                                break;
                            }
                        }
                        if (check == source1) {
                            result.add(check);
                            break;
                        } else result.add(check);

                        if (k > v * (v - 1) / 2) break;
                    }
                    for (int i = result.size()-1; i >=0; i--) {
                        if(i==0) System.out.print(result.get(i));
                        else System.out.print(result.get(i) + "->");
                    }
                }
                break;
        }

    }
    /*
    int key = Integer.parseInt(args[0]);//argument na rejim raboti
        switch (key) {
            case (1):
                StdOut.println("Number of vertexes: ");
                int vertices = StdIn.readInt();
                StdOut.println("Max throughput capacity: ");
                int max_flow = StdIn.readInt();
                StdOut.println("Max number of links: ");
                int link = StdIn.readInt();
                List<Edge>[] g = generationGraph(vertices, max_flow, link);
                //StdOut.println();
                boolean[] istok = new boolean[vertices];
                int stok = 0;
                List<Edge>[] graph = createGraph(vertices);
                for(int i = 0; i < vertices; i++){
                    ListIterator<Edge> itr = g[i].listIterator();
                    if(itr.hasNext()==false) stok = i;
                    while(itr.hasNext()){
                        Edge edge = itr.next();
                        if(istok[edge.t]==false) {
                            istok[edge.t] = true;
                        }
                        addEdge(graph, i, edge.t, edge.cap);
                    }
                }
                int istok_graph = 0;
                int count = 0;
                for(int i = 0; i<vertices;i++){
                    if(istok[i]==false) {
                        if(count == 0) {
                            istok_graph = i;
                            StdOut.println(i+" - исток");
                            istok_network = i;
                            StdOut.println(stok+" - сток");
                            stok_network = stok;
                            count++;
                        }
                        else {
                            //addEdge(g, i+1, i, StdRandom.uniform(max_flow));
                            //addEdge(graph, i+1, i, StdRandom.uniform(max_flow));
                        }
                    }

                }
                for(int i = 0; i < vertices; i++){
                    ListIterator<Edge> itr = g[i].listIterator();
                    while(itr.hasNext()){
                        Edge edge = itr.next();
                        StdOut.print("from "+i +" to " +edge.t +" throughput capacity "+ edge.cap+"; ");
                    }
                    if(i!=stok)
                        StdOut.println();
                    else {
                        StdOut.println("from "+ stok);
                    }
                }
                PrintWriter pw = new PrintWriter(new File("D:/lab5.csv"));
                StringBuilder sb = new StringBuilder();
                for(int i = 0; i < vertices; i++){
                    ListIterator<Edge> itr = g[i].listIterator();
                    while(itr.hasNext()) {
                        Edge edge = itr.next();
                        sb.append(i+","+edge.t+","+edge.cap);

                        if(i == g.length-1&&itr.hasNext()==false) {
                            ;
                        }
                        else  sb.append('\n');
                    }
                    if(i == stok_network) {
                        sb.append(stok_network);
                        sb.append("\n");
                    }


                }
                pw.write(sb.toString());
                pw.close();
                StdOut.println("Файл в формате csv -> D:/lab5.csv");
                System.out.println();
                System.out.println("Results of algoritm: " + maxFlow(graph, istok_graph, stok));
                break;

            case(2):
                String path = args[1];

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
                List<Edge>[] graphFromFile = createGraph(v);
                List<Edge>[] graphForDinic = createGraph(v);
                //System.out.println(v);
                String line = reader.readLine();
                while(true) {
                    while (line != null) {
                        String[] link_ = line.split(",");
                        if(link_.length==1) ;
                        else {
                            addEdge_(graphFromFile,Integer.parseInt(link_[0]), Integer.parseInt(link_[1]), Integer.parseInt(link_[2]));
                            addEdge(graphForDinic,Integer.parseInt(link_[0]), Integer.parseInt(link_[1]), Integer.parseInt(link_[2]));
                        }
                        line = reader.readLine();
                    }
                    break;
                }

                boolean[] istok_file = new boolean[v];
                int stok_file = -1;
                for(int i = 0; i < v; i++){
                    ListIterator<Edge> itr = graphFromFile[i].listIterator();
                    if(itr.hasNext()==false) stok_file = i;
                    while(itr.hasNext()){
                        Edge edge = itr.next();
                        if(istok_file[edge.t]==false) {
                            istok_file[edge.t] = true;
                        }
                    }
                }
                int source = 0;
                System.out.println("The program has found source: ");
                for(int i = 0; i<v;i++) {
                    if(stok_file==-1) {
                        StdOut.println(" Нет стока");
                        break;
                    }
                    if (istok_file[i] == false) {
                        source = i;
                        StdOut.println(i + " - исток");

                    }
                }
                StdOut.println(stok_file + " - сток");
                System.out.println("Select a source: ");
                int source1 = StdIn.readInt();

                for(int i = 0; i < v; i++){
                    ListIterator<Edge> itr = graphFromFile[i].listIterator();
                    while(itr.hasNext()){
                        Edge edge = itr.next();
                        StdOut.print("from "+i +" to " +edge.t +" throughput capacity "+ edge.cap+"; ");
                    }
                    if(i!=stok_file)
                        StdOut.println();
                    else {
                        StdOut.println("from "+ stok_file);
                    }
                }
                System.out.println("Results of algoritm: " + maxFlow(graphForDinic, source1, stok_file));

                break;

        }
     */
}


// Класс для представления узла на графике
class Node implements Comparator<Node> {
    public int node;
    public int cost;
    public Node() {
    }
    public Node(int node, int cost)

    {
        this.node = node;
        this.cost = cost;
    }

    @Override
    public int compare(Node node1, Node node2)
    {
        if (node1.cost < node2.cost)
            return -1;
        if (node1.cost > node2.cost)
            return 1;
        return 0;
    }
}