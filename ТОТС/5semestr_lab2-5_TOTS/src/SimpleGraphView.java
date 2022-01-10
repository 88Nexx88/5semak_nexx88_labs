import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import org.apache.commons.collections15.Transformer;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ThermometerPlot;
import org.jfree.data.xy.DefaultXYDataset;

import org.jfree.chart.plot.DefaultDrawingSupplier;

import javax.swing.*;
//import java.awt.List;
import java.awt.*;
import java.time.Duration;
import java.time.Instant;
import java.util.Queue;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;

import static java.lang.String.format;


public class SimpleGraphView {


    public Graph<Integer, String> graph;

    public int[][] graph_networkAdjMtr;
    public Router[] routers_list;
    public int count_edges = 0;

    public SimpleGraphView() {
        graph = new SparseMultigraph<>();
    }

    public Router[] getRouters_list() {
        return routers_list;
    }

    public void makeNetwork(String[] args) {
        int key = Integer.parseInt(args[0]);
        switch (key) {
            case (0):
                randomGraphNEOR(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
                break;
            case (1):
                randomCircle(Integer.parseInt(args[1]));
                break;
            case (2):
                randomStar(Integer.parseInt(args[1]));
                break;
        }
        Layout<Integer, String> layout = new CircleLayout(graph);
        layout.setSize(new Dimension(600, 600));
        BasicVisualizationServer<Integer, String> vv =
                new BasicVisualizationServer<Integer, String>(layout);
        vv.setPreferredSize(new Dimension(1150, 950));
        // Setup up a new vertex to paint transformer...
        Transformer<Integer, Paint> vertexPaint = new Transformer<Integer, Paint>() {
            public Paint transform(Integer i) {
                return Color.WHITE;
            }
        };
        // Set up a new stroke Transformer for the edges
        float dash[] = {10.0f};
        /*
        final Stroke edgeStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
        Transformer<String, Stroke> edgeStrokeTransformer =
                new Transformer<String, Stroke>() {
                    public Stroke transform(String s) {
                        return edgeStroke;
                    }
                };

         */
        vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
        //vv.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer);
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        //vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
        vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);

        JFrame frame = new JFrame("Simple Graph View");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv);
        frame.pack();
        frame.setVisible(true);

    }

    void randomStar(int vertices) {
        graph = new SparseMultigraph<>();
        graph_networkAdjMtr = new int[vertices][vertices];
        for (int i = 0; i < vertices; i++) {
            graph.addVertex(i);
        }
        int source = StdRandom.uniform(1, vertices);
        for (int i = 0; i < vertices; i++) {
            if (i == source) {
                continue;
            }
            String str = "";
            str = source + "-" + i;
            graph.addEdge(str, source, i, EdgeType.UNDIRECTED);
            graph_networkAdjMtr[source][i] = 1;
            graph_networkAdjMtr[i][source] = 1;
            count_edges++;
        }
    }

    void randomCircle(int vertices) {

        routers_list = new Router[vertices];
        for (int i = 1; i < vertices + 1; i++) {
            routers_list[i - 1] = new Router(i, routers_list);
        }

        graph = new SparseMultigraph<>();
        graph_networkAdjMtr = new int[vertices][vertices];
        for (int i = 1; i < vertices; i++) {
            graph.addVertex(i);
        }
        for (int i = 1; i < vertices; i++) {
            String str = "";
            if (i == vertices - 1) {
                str = i + "-" + 1;
                graph.addEdge(str, i, 1, EdgeType.UNDIRECTED);
                graph_networkAdjMtr[i][1] = 1;
                routers_list[i].adjRouter.add(routers_list[1]);
                count_edges++;
                routers_list[1].adjRouter.add(routers_list[i]);
                count_edges++;
            } else {
                str = i + "-" + i + 1;
                graph.addEdge(str, i, i + 1, EdgeType.UNDIRECTED);
                graph_networkAdjMtr[i][i + 1] = 1;
                routers_list[i].adjRouter.add(routers_list[i + 1]);
                count_edges++;
                routers_list[i + 1].adjRouter.add(routers_list[i]);
                count_edges++;
            }
        }
    }

    void randomGraphNEOR(int vertices, float lambda) {
        graph = new SparseMultigraph<>();

        graph_networkAdjMtr = new int[vertices][vertices];
        routers_list = new Router[vertices];
        for (int i = 1; i < vertices + 1; i++) {
            routers_list[i - 1] = new Router(i, routers_list);
        }

        for (int i = 1; i < vertices; i++) {
            graph.addVertex(i);
        }
        Vector<Integer> posLink = new Vector<>(vertices);
        for (int i = 1; i < vertices; i++)
            posLink.add(i - 1, i);
        int count = 1;
        int dest;
        int link = getPoisson(lambda);
        while (count != vertices) {
            int decount = graph.getSuccessorCount(count);
            for (int i = 1; i < link - decount; i++) {
                if (posLink.size() == 0) break;
                int numDest = StdRandom.uniform(0, posLink.size());
                dest = posLink.get(numDest);
                if (dest == count) {
                    continue;
                }
                posLink.remove(numDest);
                String str = count + "-" + dest;
                graph.addEdge(str, count, dest, EdgeType.UNDIRECTED);
                graph_networkAdjMtr[count][dest] = 1;
                count_edges++;
                routers_list[count].adjRouter.add(routers_list[dest]);
                graph_networkAdjMtr[dest][count] = 1;
                count_edges++;
                routers_list[dest].adjRouter.add(routers_list[count]);

            }
            if (count != vertices - 1)
                link = getPoisson(lambda);
                //StdOut.print(" "+link+" ");
            count++;
            posLink.removeAllElements();
            for (int i = 1; i < vertices - count; i++) {
                posLink.add(i - 1, count + i);
            }
            if (link > posLink.size()) {
                link = posLink.size();
            }
        }


        //}
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


class Router {
    Router[] routers;
    PriorityBlockingQueue<Package> router_queue;
    PriorityQueue<Package> router_do;
    int max_size_queue = 15;////////////////////////////////////////////////////////////////////////////////////
    int delay_router = 2;/////////////////////////////////////////////////////////\\\
    int name;
    ArrayList<Router> adjRouter;
    List<ArrayList<Integer>> paths;
    Router[][] network;
    List<Package>  removed_packed = new ArrayList<>();
    int diedPackage;
    double averageTime = 0.0;
    int finishPacage = 0;

    boolean stop = false;
    boolean allPaths = false;

    ExecutorService service;
    ExecutorService servicePaths;

    List<String> procces = new ArrayList<>();

    public Router(int name, Router[] routers) {
        this.router_queue = new PriorityBlockingQueue<Package>(1, new Comparator<Package>() {
            @Override
            public int compare(Package o1, Package o2) {
                return (o1.priority > o2.priority) ? 1 : -1;
            }
        });

        this.router_do = new PriorityQueue<>(new Comparator<Package>() {
            @Override
            public int compare(Package o1, Package o2) {
                return (o1.priority > o2.priority) ? 1 : -1;
            }
        });
        this.name = name;
        this.adjRouter = new ArrayList<>();
        this.routers = routers;
        this.diedPackage = 0;
        network = new Router[routers.length][routers.length];
        paths = new ArrayList<>();
    }

    public void setNetwork() {
        for (int i = 0; i < adjRouter.size(); i++) {
            network[name - 1][adjRouter.get(i).name - 1] = adjRouter.get(i);
        }
    }

    public void findPathsNetwork(int count_edges) {
        allPaths = true;

        servicePaths = Executors.newFixedThreadPool(1);
        Runnable task = () -> {
            setNetwork();
            while (allPaths) {
                try {
                    Thread.currentThread().sleep(1000);
                    int count = 0;
                    for (int i = 0; i < adjRouter.size(); i++) {
                        Router r = adjRouter.get(i);
                        for (int i1 = 1; i1 < r.network.length; i1++) {
                            for (int i2 = 1; i2 < r.network[i1].length; i2++) {
                                if (r.network[i1][i2] != null) network[i1][i2] = r.network[i1][i2];
                            }
                        }
                    }
                    for (int i1 = 1; i1 < network.length; i1++) {
                        for (int i2 = 1; i2 < network[i1].length; i2++) {
                            if (network[i1][i2] != null) count++;
                        }
                    }
                    //System.out.printf("%d - %d - %d", name-1, count, count_edges);
                    if (count == count_edges) allPaths = false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }



            /*
            System.out.println("_______________________________________________" + (name - 1));
            for (int i = 1; i < network.length; i++) {
                for (int j = 1; j < network[i].length; j++) {
                    if (network[i][j] != null)
                        System.out.print(network[i][j].name - 1 + " ");
                    else System.out.print("- ");

                }
                System.out.println();
            }

             */


        };

        servicePaths.submit(task);


    }

    synchronized ArrayList<Integer> findPaths(Package p){
        int from = p.from;
        int to = p.to;
        for(ArrayList<Integer> list : paths){
            //System.out.println(from+" "+to+"    "+list.get(0)+"  "+list.get(list.size()-1)+"  "+name);
            if(list.get(0) == from && list.get(list.size()-1) == to){
                return list;
            }
        }
        return null;
    }
    public void processingRouter() {
        service = Executors.newFixedThreadPool(1);
        Runnable task = () -> {
            while (true && stop) {
                while (!router_queue.isEmpty() && stop) {
                    Queue<Package> ls0 = new LinkedList<>();
                    Queue<Package> ls1 = new LinkedList<>();
                    Queue<Package> ls2 = new LinkedList<>();
                    //System.out.println(router_queue.size()+" &@&@# "+ name);
                    String s = "";
                    while(!router_queue.isEmpty()){
                        s+=router_queue.peek().priority+" ";
                        if(router_queue.peek().priority == 0) ls0.add(router_queue.poll());
                        else if(router_queue.peek().priority == 1) ls1.add(router_queue.poll());
                        else ls2.add(router_queue.poll());
                    }
                    if(ls0.size() != 0) router_do.add(ls0.poll());
                    if(ls1.size() != 0) router_do.add(ls1.poll());
                    if(ls2.size() != 0) router_do.add(ls2.poll());

                    while(router_do.size() < 5){
                        if(ls0.size() > 0) router_do.add(ls0.poll());
                        else if(ls1.size() > 0) router_do.add(ls1.poll());
                        else if(ls2.size() > 0) router_do.add(ls2.poll());
                        else break;
                    }
                    while (ls0.size() != 0){
                        router_queue.add(ls0.poll());
                    }
                    while (ls1.size() != 0){
                        router_queue.add(ls1.poll());
                    }
                    while (ls2.size() != 0){
                        router_queue.add(ls2.poll());
                    }
                    procces.add(s);
                    s = "";
                    for(Package p : router_do){
                        s+=(p.priority+ " ");
                    }
                    procces.add(s);

                    while(!router_do.isEmpty()) {
                        Package currentPackage = router_do.poll();
                        //System.out.println(name+" !1! "+ currentPackage.from +" "+ currentPackage.to);
                        try {
                            if (currentPackage == null) {
                                continue;
                            }
                            Thread.currentThread().sleep(1000);
                            //System.out.println(index_current+" "+ (path.size() - 1) + " "+currentPackage.from+" "+currentPackage.to+" "+name);
                            if (name == currentPackage.to) {
                                finishPacage++;
                                Instant finish = Instant.now();
                                long time = Duration.between(currentPackage.start, finish).toSeconds();
                                averageTime += time;
                                System.out.printf("Router %d has accepted package |%s| Time - %d \n", name, pathToString(currentPackage.path), time);
                            } else {
                                currentPackage.from = name;
                                ArrayList<Integer> path = findPaths(currentPackage);
                                //System.out.println(name + " " + path.toString() + " " + currentPackage.from + " " + currentPackage.to);
                                int index_current = path.indexOf(name);
                                for (int i = 0; i < routers.length; i++) {
                                    if (routers[i].name == path.get(index_current + 1)) {
                                        System.out.printf("Router %d sent to Router %d package |%s|\n", name, routers[i].name, pathToString(currentPackage.path));
                                        routers[i].router_queue.add(currentPackage);
                                        if (!routers[i].checkSize()) {
                                            //System.out.println("DIED" + routers[i].router_queue.size() + " " + routers[i].name);
                                            List<Package> list1 = new ArrayList<>();
                                            for (int j = 0; j < routers[i].max_size_queue; j++) {
                                                list1.add(routers[i].router_queue.poll());
                                            }
                                            Package del = routers[i].router_queue.poll();
                                            routers[i].router_queue.clear();
                                            System.out.printf("Router %d overloaded - package |%s| died\n", routers[i].name, pathToString(del.path));
                                            routers[i].diedPackage++;
                                            for (Package pac : list1) {
                                                routers[i].router_queue.add(pac);
                                            }

                                        }
                                        break;
                                    }
                                }
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                try {
                    Thread.currentThread().sleep(1000 * delay_router);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        service.submit(task);

    }

    public boolean checkSize() {
        if (router_queue.size() > max_size_queue) return false;
        else return true;
    }

    private String pathToString(ArrayList<Integer> path) {
        String p = "";
        for (Integer i : path) {
            p += i + " ";
        }
        return p;
    }


    //public void setPaths(ArrayList<Integer> path){
    //    this.path = path;
    //}


}


class Package {
    int from;
    int to;
    int delay;
    ArrayList<Integer> path;
    Instant start;
    Integer priority;

    public Package(int from, int to) {
        this.from = from;
        this.to = to;
        this.delay = 2;
        path = new ArrayList<>();
        this.path.add(from);
        this.path.add(to);
        this.start = Instant.now();
    }

    public void setPath(ArrayList<Integer> path) {
        this.path.clear();
        this.path = path;
    }
}

class Processing {
    static List<ArrayList<Integer>> paths;

    public static void main(String[] args) throws InterruptedException {
        SimpleGraphView graphView = new SimpleGraphView();
        graphView.makeNetwork(args);


        paths = new ArrayList<>();

        int count = 0;
        for (int i = 0; i < Integer.parseInt(args[1]); i++) {
            for (int j = 0; j < Integer.parseInt(args[1]); j++) {
                //System.out.print(graphView.graph_networkAdjMtr[i][j] + " ");
                if (graphView.graph_networkAdjMtr[i][j] == 1)
                    count++;
            }
            //System.out.println();
        }
        int[][] fGraph = new int[count][3];
        count = 0;
        for (int i = 1; i < Integer.parseInt(args[1]); i++) {
            for (int j = 1; j < Integer.parseInt(args[1]); j++) {
                if (graphView.graph_networkAdjMtr[i][j] == 1) {
                    fGraph[count][2] = graphView.graph_networkAdjMtr[i][j];
                    fGraph[count][1] = j;
                    fGraph[count][0] = i;
                    count++;
                }
            }
        }

        floydWarshall(fGraph, Integer.parseInt(args[1]) - 1);
        ExecutorService service = Executors.newFixedThreadPool(1);

        for(int i = 0; i < graphView.routers_list.length; i++){
            graphView.routers_list[i].stop = true;
            graphView.routers_list[i].processingRouter();
        }


        System.out.println(graphView.count_edges);

        for (int i = 1; i < graphView.routers_list.length; i++) {
            List<ArrayList<Integer>> p_listRouter = new ArrayList<>();
            for(ArrayList<Integer> p : paths){
                if(p.get(0) == i+1) p_listRouter.add(p);
            }
            graphView.routers_list[i].paths = p_listRouter;

            graphView.routers_list[i].findPathsNetwork(graphView.count_edges);
        }

        List<Double> sum_list = new ArrayList<Double>();
        List<Integer> died_list = new ArrayList<Integer>();
        List<Double> timeList = new ArrayList<Double>();

        var ref = new Object() {
            boolean stop = true;
        };

        Runnable task = () -> {

            while (ref.stop) {

                try {
                    Thread.currentThread().sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                double sum = 0;
                int died = 0;
                double time = 0;
                int number = 0;
                Router[] list = graphView.getRouters_list();
                for(int i = 0; i < list.length; i++) {
                    //System.out.println(list[i].name + " " + list[i].router_queue.size());
                    sum += list[i].router_queue.size();
                    died += list[i].diedPackage;
                    if (list[i].finishPacage > 0) {
                        number += list[i].finishPacage;
                        time += list[i].averageTime;
                    }
                }

                System.out.println("System Info: average size = " + (sum)/(graphView.routers_list.length-1));
                System.out.println("System Info: died package = " + died);
                if(number > 0)
                    System.out.println("System Info: average time  = " + time/number);
                else
                    System.out.println("System Info: average time  = " + time);
                System.out.println("System Info: finish package  = " + number);
                sum_list.add(sum/(graphView.routers_list.length-1));
                died_list.add(died);
                if(number > 0)
                    timeList.add(time/number);
                else timeList.add(0.0);
            }

        };

        service.submit(task);

        int intensiv = 1;
        int time = 100 * intensiv;
        while (time != 0) {
            Thread.sleep(1000 / intensiv);
            int from = StdRandom.uniform(1, Integer.parseInt(args[1]));
            int to = StdRandom.uniform(1, Integer.parseInt(args[1]));
            if (from == to) to = StdRandom.uniform(to, Integer.parseInt(args[1]));
            if (from == to) to = StdRandom.uniform(1, to + 1);
            Package p = new Package(from, to);
            p.priority = StdRandom.uniform(0, 3);
            /*
            for (int i = 0; i < paths.size(); i++) {
                if ((paths.get(i).get(0) == from) && (paths.get(i).get(paths.get(i).size() - 1) == to)) {
                    p.setPath(paths.get(i));
                }
            }
            if (p.path == null) System.out.println(from + " " + to);
            else

             */

            System.out.printf("Package |%s| generated in Router %d, finish in Router %d\n", pathToString(p.path), from, to);

            Router[] list = graphView.getRouters_list();
            for (int i = 0; i < list.length; i++) {
                if (list[i].name == from) {
                    list[i].router_queue.add(p);
                    if (!list[i].checkSize()){
                        //System.out.println("DIED " + list[i].router_queue.size()+ " "+list[i].name);
                        List<Package> list1 = new ArrayList<>();
                        for(int j = 0; j < list[i].max_size_queue; j++){
                            list1.add(list[i].router_queue.poll());
                        }
                        Package del = list[i].router_queue.poll();
                        System.out.printf("Router %d overloaded - package |%s| died\n", list[i].name, pathToString(del.path));
                        list[i].diedPackage++;
                        list[i].router_queue.clear();
                        for(Package pac : list1){
                            list[i].router_queue.add(pac);
                        }
                    }
                }
            }

            time--;
        }


        Router[] list = graphView.getRouters_list();
        for (int i = 0; i < list.length; i++) {
            System.out.println("Router " + list[i].name + " turn off");

            list[i].stop = false;
            list[i].service.shutdownNow();
        }
        ref.stop = false;
        service.shutdownNow();
        /*
        for(Router r : graphView.getRouters_list()){
            for(String s : r.procces){
                System.out.println("!"+r.name+"!"+" "+s);
            }
        }

         */


        DefaultXYDataset xyDataset = new DefaultXYDataset();
        double[][] series = new double[2][sum_list.size()];
        for (int i = 0; i < sum_list.size(); i++) {
            series[0][i] = (double) i + 1;
            series[1][i] = sum_list.get(i);
        }
        String str = "Time/size-";
        xyDataset.addSeries(str, series);

        JFreeChart chart = ChartFactory.createXYLineChart("TOTS", "Time", "Size",
                xyDataset,
                PlotOrientation.VERTICAL,
                true, true, true);
        Plot plot = chart.getPlot();
        plot.setDrawingSupplier(new ChartDrawingSupplier());

        JFrame frame =
                new JFrame("Average size queue");
        frame.setLayout(new FlowLayout());
        frame.getContentPane().add(new ChartPanel(chart));

        frame.pack();
        frame.setSize(700, 500);
        frame.show();


        xyDataset = new DefaultXYDataset();
        series = new double[2][died_list.size()];
        for (int i = 0; i < died_list.size(); i++) {
            series[0][i] = (double) i + 1;
            series[1][i] = died_list.get(i);
        }
        str = "Time/died-";
        xyDataset.addSeries(str, series);

        chart = ChartFactory.createXYLineChart("TOTS", "Time", "Died",
                xyDataset,
                PlotOrientation.VERTICAL,
                true, true, true);
        plot = chart.getPlot();
        plot.setDrawingSupplier(new ChartDrawingSupplier());

        frame =
                new JFrame("Died package");
        frame.setLayout(new FlowLayout());
        frame.getContentPane().add(new ChartPanel(chart));

        frame.pack();
        frame.setSize(700, 500);
        frame.show();


        xyDataset = new DefaultXYDataset();
        series = new double[2][timeList.size()];
        for (int i = 0; i < died_list.size(); i++) {
            series[0][i] = (double) i + 1;
            series[1][i] = timeList.get(i);
        }
        str = "Time/averageTime-";
        xyDataset.addSeries(str, series);

        chart = ChartFactory.createXYLineChart("TOTS", "Time", "averageTime",
                xyDataset,
                PlotOrientation.VERTICAL,
                true, true, true);
        plot = chart.getPlot();
        plot.setDrawingSupplier(new ChartDrawingSupplier());

        frame =
                new JFrame("Died package");
        frame.setLayout(new FlowLayout());
        frame.getContentPane().add(new ChartPanel(chart));

        frame.pack();
        frame.setSize(700, 500);
        frame.show();






    }

    static private String pathToString(ArrayList<Integer> path) {
        String p = "";
        for (Integer i : path) {
            p += i + " ";
        }
        return p;
    }

    static void floydWarshall(int[][] weights, int numVertices) {

        double[][] dist = new double[numVertices][numVertices];
        for (double[] row : dist)
            Arrays.fill(row, Double.POSITIVE_INFINITY);

        for (int[] w : weights) {
            dist[w[0] - 1][w[1] - 1] = w[2];
        }

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
        /*
        System.out.println("Результат работы алгоритма: ");
        for (int i = 0; i < next.length; i++) {
            for (int j = 0; j < next.length; j++) {
                if (i == j) System.out.print(" *  ");
                else {
                    if ((int) dist[i][j] == 2147483647) System.out.printf(" N  ");
                    else System.out.printf(format, (int) dist[i][j]);
                }

            }
            System.out.println();
        }

         */
        //System.out.println("Пути: ");
        for (int i = 0; i < next.length; i++) {
            for (int j = 0; j < next.length; j++) {
                if (i != j) {
                    int u = i + 1;
                    int v = j + 1;
                    ArrayList<Integer> path_ = new ArrayList<>();
                    String path = format("%s", u);
                    path_.add(u);
                    do {
                        u = next[u - 1][v - 1];
                        path += " -> " + u;
                        path_.add(u);
                    } while (u != v);
                    if ((int) dist[i][j] == 2147483647) path += " - Путей нет";
                    else path += format("  [%d]", (int) dist[i][j]);
                    //System.out.println(path);
                    paths.add(path_);
                }
            }
        }

    }


}

class ChartDrawingSupplier extends DefaultDrawingSupplier {

    public Paint[] paintSequence;
    public int paintIndex;
    public int fillPaintIndex;

    {
        paintSequence = new Paint[]{
                new Color(227, 26, 28),
                new Color(000, 102, 204),
                new Color(000, 000, 000),
                new Color(102, 51, 0),
                new Color(156, 136, 48),
                new Color(153, 204, 102),
        };
    }

    @Override
    public Paint getNextPaint() {
        Paint result
                = paintSequence[paintIndex % paintSequence.length];
        paintIndex++;
        return result;
    }


    @Override
    public Paint getNextFillPaint() {
        Paint result
                = paintSequence[fillPaintIndex % paintSequence.length];
        fillPaintIndex++;
        return result;
    }


}