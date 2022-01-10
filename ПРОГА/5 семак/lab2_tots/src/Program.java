import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class Program {
}

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
                return Color.GREEN;
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
        for (int i = 1; i <= vertices; i++) {
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
            if (count == vertices - 1) ;
            else {
                link = getPoisson(lambda);
                //StdOut.print(" "+link+" ");
            }
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