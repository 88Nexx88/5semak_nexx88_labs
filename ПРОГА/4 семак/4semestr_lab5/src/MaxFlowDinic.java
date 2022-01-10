
import java.io.*;
import java.util.*;

public class MaxFlowDinic {
    public static List<Edge>[] transport_network;
    public static int istok_network;
    public static int stok_network;
    static class Edge {
        int t, rev, cap, f;

        public Edge(int t, int rev, int cap) {
            this.t = t;
            this.rev = rev;
            this.cap = cap;
        }
    }

    public static List<Edge>[] createGraph(int nodes) {
        List<Edge>[] graph = new List[nodes];
        for (int i = 0; i < nodes; i++)
            graph[i] = new ArrayList<>();
        return graph;
    }

    public static void addEdge(List<Edge>[] graph, int s, int t, int cap) {
        graph[s].add(new Edge(t, graph[t].size(), cap));
        graph[t].add(new Edge(s, graph[s].size() - 1, cap));
    }

    public static void addEdge_(List<Edge>[] graph, int s, int t, int cap) {
        graph[s].add(new Edge(t, graph[t].size(), cap));
        //graph[t].add(new Edge(s, graph[s].size() - 1, cap));
    }

    static boolean dinicBfs(List<Edge>[] graph, int src, int dest, int[] dist) {
        Arrays.fill(dist, -1);
        dist[src] = 0;
        int[] Q = new int[graph.length];
        int sizeQ = 0;
        Q[sizeQ++] = src;
        for (int i = 0; i < sizeQ; i++) {
            int u = Q[i];
            for (Edge e : graph[u]) {
                if (dist[e.t] < 0 && e.f < e.cap) {
                    dist[e.t] = dist[u] + 1;
                    Q[sizeQ++] = e.t;
                }
            }
        }
        return dist[dest] >= 0;
    }

    static int dinicDfs(List<Edge>[] graph, int[] ptr, int[] dist, int dest, int u, int f) {
        if (u == dest)
            return f;
        for (; ptr[u] < graph[u].size(); ++ptr[u]) {
            Edge e = graph[u].get(ptr[u]);
            if (dist[e.t] == dist[u] + 1 && e.f < e.cap) {
                int df = dinicDfs(graph, ptr, dist, dest, e.t, Math.min(f, e.cap - e.f));
                if (df > 0) {
                    e.f += df;
                    graph[e.t].get(e.rev).f -= df;
                    return df;
                }
            }
        }
        return 0;
    }

    public static int maxFlow(List<Edge>[] graph, int src, int dest) {
        int flow = 0;
        int[] dist = new int[graph.length];
        while (dinicBfs(graph, src, dest, dist)) {
            int[] ptr = new int[graph.length];
            while (true) {
                int df = dinicDfs(graph, ptr, dist, dest, src, Integer.MAX_VALUE);
                if (df == 0)
                    break;
                flow += df;
            }
        }
        return flow;
    }

    static void CSV() throws FileNotFoundException {

        PrintWriter pw = new PrintWriter(new File("D:/lab5.csv"));
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < transport_network.length; i++){
            ListIterator<Edge> itr = transport_network[i].listIterator();
            if(itr.hasNext()) {
                Edge edge = itr.next();
                StdOut.print("from "+i +" to " +edge.t +" throughput capacity "+ edge.cap+"; ");
                sb.append(i+","+edge.t+","+edge.cap);

                if(i == transport_network.length-1) {
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
    }


    public static List<Edge>[] generationGraph(int vertices, int max_flow, int sred_link){
        List<Edge>[] graph = createGraph(vertices);
        int istok = StdRandom.uniform(vertices-1);
        int stok = StdRandom.uniform(vertices-1);
        if(istok == stok)
            if(stok+1<vertices-1) stok++;
            else istok--;
        for(int i = 0; i<vertices; i++) {
            int link = StdRandom.uniform(1, sred_link);
            if(i==istok && link<vertices) link = StdRandom.uniform(sred_link-1, sred_link);
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
                addEdge_(graph, i, dest, weight);
            }

        }
        /*
        for(int i = 0; i < vertices; i++){
            ListIterator<Edge> itr = graph[i].listIterator();
            while(itr.hasNext()){
                Edge edge = itr.next();
                StdOut.print("from "+i +" to " +edge.t +" weight "+ edge.cap+"; ");
            }
            if(i!=stok)
                StdOut.println();
            else {
                StdOut.println("from "+ stok);
            }
        }

         */
        return graph;


    }





    // Usage example
    public static void main(String[] args) throws IOException {
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
    }
}
