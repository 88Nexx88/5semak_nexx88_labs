// Graph<V, E> where V is the type of the vertices
// and E is the type of the edges

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
/*
class Program {
    /*
    public static void main(String[] args) {
        Graph<Integer, String> g = new SparseMultigraph<Integer, String>();
        // Add some vertices. From above we defined these to be type Integer.
        g.addVertex((Integer) 1);
        g.addVertex((Integer) 2);
        g.addVertex((Integer) 3);
        // Add some edges. From above we defined these to be of type String
        // Note that the default is for undirected edges.
        g.addEdge("Edge-A", 1, 2); // Note that Java 1.5 auto-boxes primitives
        g.addEdge("Edge-B", 2, 3);
        // Let's see what we have. Note the nice output from the
        // SparseMultigraph<V,E> toString() method
        System.out.println("The graph g = " + g.toString());
        // Note that we can use the same nodes and edges in two different graphs.
        Graph<Integer, String> g2 = new SparseMultigraph<Integer, String>();
        g2.addVertex((Integer) 1);
        g2.addVertex((Integer) 2);
        g2.addVertex((Integer) 3);
        g2.addEdge("Edge-A", 1, 3);
        g2.addEdge("Edge-B", 2, 3, EdgeType.DIRECTED);
        g2.addEdge("Edge-C", 3, 2, EdgeType.DIRECTED);
        g2.addEdge("Edge-P", 2, 3); // A parallel edge
        System.out.println("The graph g2 = " + g2.toString());
        DirectedSparseMultigraph<MyNode, MyLink> gr = new DirectedSparseMultigraph<>();
        // Create some MyNode objects to use as vertices
        MyNode n1 = new MyNode(1);
        MyNode n2 = new MyNode(2);
        MyNode n3 = new MyNode(3);
        MyNode n4 = new MyNode(4);
        MyNode n5 = new MyNode(5); // note n1-n5 declared elsewhere.
        // Add some directed edges along with the vertices to the graph
        gr.addEdge(new MyLink(2.0, 48),n1, n2, EdgeType.DIRECTED); // This method
        gr.addEdge(new MyLink(2.0, 48),n2, n3, EdgeType.DIRECTED);
        gr.addEdge(new MyLink(3.0, 192), n3, n5, EdgeType.DIRECTED);
        gr.addEdge(new MyLink(2.0, 48), n5, n4, EdgeType.DIRECTED); // or we can use
        gr.addEdge(new MyLink(2.0, 48), n4, n2); // In a directed graph the
        gr.addEdge(new MyLink(2.0, 48), n3, n1); // first node is the source
        gr.addEdge(new MyLink(10.0, 48), n2, n5);// and the second the destination

        public static void main(String[] args) {
            SimpleGraphView sgv = new SimpleGraphView(); //We create our graph in here
            // The Layout<V, E> is parameterized by the vertex and edge types
            Layout<Integer, String> layout = new CircleLayout(sgv.g);
            layout.setSize(new Dimension(300,300)); // sets the initial size of the space
            // The BasicVisualizationServer<V,E> is parameterized by the edge types
            BasicVisualizationServer<Integer,String> vv =
                    new BasicVisualizationServer<Integer,String>(layout);
            vv.setPreferredSize(new Dimension(350,350)); //Sets the viewing area size

            JFrame frame = new JFrame("Simple Graph View");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(vv);
            frame.pack();
            frame.setVisible(true);
        }

}

class MyNode {
    int id; // good coding practice would have this as private
    public MyNode(int id) {
        this.id = id;
    }
    public String toString() { // Always a good idea for debuging
        return "V"+id; // JUNG2 makes good use of these.
    }
}

class MyLink {
    double capacity; // should be private
    double weight; // should be private for good practice
    int id;
    int edgeCount = 0;

    public MyLink(double weight, double capacity) {
        this.id = edgeCount++; // This is defined in the outer class.
        this.weight = weight;
        this.capacity = capacity;
    }

    public String toString() { // Always good for debugging
        return "E" + id;
    }
}
 */