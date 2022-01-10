import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import org.apache.commons.collections15.Transformer;
import edu.uci.ics.jung.visualization.renderers.Renderer;


import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

/*
Условие 3 ЛР по БОСу
Создание программного модуля для анализа графов доступов по расширенной модели Take-Grant.
Модуль должен уметь достраивать недостающие ребра по де-юре и де-факто правилам, определять мосты и находить tg-связные подграфы.
Входные и выходные данные в формате CSV.
 */
public class Program {
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static String[][] matrix;

    public static void main(String[] args) throws IOException, CloneNotSupportedException {
        System.out.println(RED + "FILE -> " + RESET);
        getConfig("config");
        List<String[]> list_com = new ArrayList<>();
        //list_com.add(new String[]{"bridge", "0", "2"});
        list_com.add(new String[]{"tglink", "0", "1"});
        list_com.add(new String[]{"deure", "0", "1"});
        list_com.add(new String[]{"defacto", "0", "1"});
        printGraph(vertex);
        showGraph(vertex, "FILE");
        for (int i = 0; i < list_com.size(); i++) {
            String key = list_com.get(i)[0];
            switch (key) {
                case "bridge":
                    int k = Integer.parseInt(list_com.get(i)[1]);
                    int j = Integer.parseInt(list_com.get(i)[2]);
                    System.out.println("__________________________________________________________________________");
                    System.out.println(RED + "BRIDGE -> " + RESET);
                    System.out.printf("Мост между %s - %s = %b\n", vertex.get(k).getName(), vertex.get(j).getName(), checkBridge(vertex.get(k), vertex.get(j)) || checkBridge(vertex.get(j), vertex.get(k)));
                    break;
                case "deure":
                    System.out.println(RED + "\nDE URE-> " + RESET);
                    deure();
                    showGraph(vertexDeUre, "DE URE");
                    break;
                case "defacto":
                    System.out.println(RED + "\nDE FACTO-> " + RESET);
                    defacto();
                    showGraph(vertexDeFacto, "DE FACTO");
                    break;
                case "tglink":
                    System.out.println(RED + "TG-link -> " + RESET);
                    tg_links();
                    break;
            }
        }
        /*
        System.out.println("МОСТЫ: ");
        for(int i = 0; i < vertex.size()-1;i++){
            for(int j = 1; j < vertex.size();j++){
                System.out.printf("Мост между %s - %s = %b\n", vertex.get(i).getName(), vertex.get(j).getName(), checkBridge(vertex.get(i), vertex.get(j)));
            }
        }

         */

    }

    public static List<Edge> edgeMap = new ArrayList<>();
    public static List<Vertex> vertex = new ArrayList<>();
    public static List<Vertex> vertexDeUre = new ArrayList<>();
    public static List<Vertex> vertexDeFacto = new ArrayList<>();

    private static void getConfig(String configPath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(configPath));
        int size = Integer.parseInt(reader.readLine());
        for (int i = 0; i < size; i++) {
            String[] entity = reader.readLine().split(" ", 2);
            vertex.add(new Vertex(entity[0], entity[1]));
            vertexDeUre.add(new Vertex(entity[0], entity[1]));
            vertexDeFacto.add(new Vertex(entity[0], entity[1]));
        }
        while (reader.ready()) {
            String str = reader.readLine();
            String[] edge = str.split(":");
            String[] vertexes = edge[0].split("-");
            List<String> l = new ArrayList<>();
            String[] permissions = edge[1].split(",");
            for (int i = 0; i < permissions.length; i++) {
                Edge e = new Edge();
                for (int j = 0; j < vertex.size(); j++) {
                    if (vertex.get(j).getName().equals(vertexes[1])) {
                        e.setEdge(permissions[i], vertex.get(j));
                        break;
                    }
                }
                edgeMap.add(e);
                vertex.get(findIdByName(vertex, vertexes[0])).setLinksEdge(e);
                vertexDeUre.get(findIdByName(vertex, vertexes[0])).setLinksEdge(e);
                vertexDeFacto.get(findIdByName(vertex, vertexes[0])).setLinksEdge(e);
            }
        }
        reader.close();

    }

    private static void printGraph(List<Vertex> vertex) {
        System.out.println("__________________________________________________________________________");
        int key = 0;
        if (vertex.equals(vertexDeUre) || vertex.equals(vertexDeFacto)) key++;
        matrix = new String[vertex.size()][vertex.size()];
        for (int i = 0; i < vertex.size(); i++) {
            System.out.print(RED + vertex.get(i).getName() + RESET + "  : ");
            for (int j = 0; j < vertex.get(i).getLinksEdge().size(); j++) {
                for (int j1 = 0; j1 < vertex.get(i).getLinksEdge().get(j).getLinks().size(); j1++) {
                    System.out.print(vertex.get(i).getName() + " --" + vertex.get(i).getLinksEdge().get(j).getRights().get(j1) + "-->"
                            + vertex.get(i).getLinksEdge().get(j).getLinks().get(j1).getName() + " ; ");
                    if (key == 0)
                        matrix[i][vertex.indexOf(vertex.get(i).getLinksEdge().get(j).getLinks().get(j1))] = vertex.get(i).getLinksEdge().get(j).getRights().get(j1);
                }
            }
            System.out.println();
        }
        /*
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if (matrix[i][j] == null) System.out.print("- ");
                else System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }

         */

    }

    public static void showGraph(List<Vertex> listVertex, String name) {
        Graph<String, String> graph = new SparseMultigraph<>();
        ;
        for (Vertex v : listVertex) {
            graph.addVertex(v.getName());
        }

        for (Vertex v : listVertex) {
            for (Edge e : v.getLinksEdge()) {
                graph.addEdge(v.getName() + "-" + e.getRights().get(0).toUpperCase() + "->" + e.getLinks().get(0).getName(), v.getName(), e.getLinks().get(0).getName(), EdgeType.DIRECTED);
            }
        }
        Layout<String, String> layout = new CircleLayout(graph);
        layout.setSize(new Dimension(600, 600));
        BasicVisualizationServer<String, String> vv =
                new BasicVisualizationServer<String, String>(layout);
        vv.setPreferredSize(new Dimension(1150, 950));


        Transformer<String, Paint> vertexPaint = new Transformer<String, Paint>() {
            public Paint transform(String i) {
                return Color.GREEN;
            }
        };


        final Stroke edgeStroke = new BasicStroke(0.0f);
        Transformer<String, Stroke> edgeStrokeTransformer =
                new Transformer<String, Stroke>() {
                    public Stroke transform(String s) {
                        return edgeStroke;
                    }
                };

        vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
        vv.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer);
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
        vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);

        JFrame frame = new JFrame(name);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv);
        frame.pack();
        frame.setVisible(true);

    }

    private static int findIdByName(List<Vertex> list, String name) {
        int id = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().equals(name)) {
                id = i;
                break;
            }
        }
        return id;
    }

    private static void defacto() {
        //1 A-r->B -> A<-w-B
        //2 A-w->B -> A<-r-B

        //spy A-r->B-r->C -> A<-w-C

        //find A-w->B-w->C -> A<-r-C

        //post A-r->B<-w-C -> A-r->C A<-w-C

        //pass A<-w-B-r->C -> A-r->C A<-w-C
        for (Vertex v : vertexDeUre) {
            for (Edge e : v.getLinksEdge()) {
                if (e.getRights().get(0).equals("r")) {
                    Vertex ver = e.getLinks().get(0);
                    Edge newE = new Edge();
                    newE.setEdge("w", v);
                    for (Vertex deFacto : vertexDeFacto) {
                        if (deFacto.getName().equals(ver.getName())) deFacto.setLinksEdge(newE);
                    }
                }
                if (e.getRights().get(0).equals("w")) {
                    Vertex ver = e.getLinks().get(0);
                    Edge newE = new Edge();
                    newE.setEdge("r", v);
                    for (Vertex deFacto : vertexDeFacto) {
                        if (deFacto.getName().equals(ver.getName())) deFacto.setLinksEdge(newE);
                    }
                }
                //spy
                if (e.getRights().get(0).equals("r")) {
                    for (Vertex vertexSpy : e.getLinks()) {
                        for (Edge edgeSpy : vertexSpy.getLinksEdge()) {
                            //spy
                            if (edgeSpy.getRights().get(0).equals("r")) {
                                Vertex ver = edgeSpy.getLinks().get(0);
                                Edge newE = new Edge();
                                newE.setEdge("w", v);
                                for (Vertex deFacto : vertexDeFacto) {
                                    if (deFacto.getName().equals(ver.getName())) deFacto.setLinksEdge(newE);
                                }
                                newE = new Edge();
                                newE.setEdge("r", ver);
                                for (Vertex deFacto : vertexDeFacto) {
                                    if (deFacto.getName().equals(v.getName())) deFacto.setLinksEdge(newE);
                                }

                            }
                        }
                    }
                }

                //find
                if (e.getRights().get(0).equals("w")) {
                    for (Vertex vertexSpy : e.getLinks()) {
                        for (Edge edgeSpy : vertexSpy.getLinksEdge()) {
                            //spy
                            if (edgeSpy.getRights().get(0).equals("w")) {
                                Vertex ver = edgeSpy.getLinks().get(0);
                                Edge newE = new Edge();
                                newE.setEdge("r", v);
                                for (Vertex deFacto : vertexDeFacto) {
                                    if (deFacto.getName().equals(ver.getName())) deFacto.setLinksEdge(newE);
                                }
                                newE = new Edge();
                                newE.setEdge("w", ver);
                                for (Vertex deFacto : vertexDeFacto) {
                                    if (deFacto.getName().equals(v.getName())) deFacto.setLinksEdge(newE);
                                }

                            }
                        }
                    }
                }
                //post

                if (e.getRights().get(0).equals("r")) {

                    for (Vertex verPost : vertexDeUre) {
                        for (Edge edgePost : verPost.getLinksEdge()) {
                            if (edgePost.getRights().get(0).equals("w") && edgePost.getLinks().get(0).getName().equals(e.getLinks().get(0).getName())) {
                                Edge newE = new Edge();
                                newE.setEdge("w", v);
                                for (Vertex deFacto : vertexDeFacto) {
                                    if (deFacto.getName().equals(verPost.getName())) deFacto.setLinksEdge(newE);
                                }
                                newE = new Edge();
                                newE.setEdge("r", verPost);
                                for (Vertex deFacto : vertexDeFacto) {
                                    if (deFacto.getName().equals(v.getName())) deFacto.setLinksEdge(newE);
                                }
                            }
                        }
                    }

                }
                //pass
                if (e.getRights().get(0).equals("r")) {
                    for (Edge ePass : v.getLinksEdge()) {
                        if (ePass.getRights().get(0).equals("w")) {
                            Edge newE = new Edge();
                            newE.setEdge("r", e.getLinks().get(0));// s1 -r-> s3
                            for (Vertex deFacto : vertexDeFacto) {
                                if (deFacto.getName().equals(ePass.getLinks().get(0).getName()))
                                    deFacto.setLinksEdge(newE);
                            }

                            newE = new Edge();
                            newE.setEdge("w", ePass.getLinks().get(0));// s3 -w-> s1
                            for (Vertex deFacto : vertexDeFacto) {
                                if (deFacto.getName().equals(e.getLinks().get(0).getName())) deFacto.setLinksEdge(newE);
                            }
                        }
                    }

                }


            }

        }


        printGraph(vertexDeFacto);

    }

    private static void deure() {

        for (Vertex v : vertex) {
            for (Edge e : v.getLinksEdge()) {
                if (e.getRights().get(0).equals("t")) {
                    Vertex ver = e.getLinks().get(0);
                    for (Edge verE : ver.getLinksEdge()) {
                        if (!verE.getRights().get(0).equals("t") && !verE.getRights().get(0).equals("g")) {
                            Edge newE = new Edge();
                            newE.setEdge(verE.getRights().get(0), verE.getLinks().get(0));
                            for (Vertex deUre : vertexDeUre) {
                                if (deUre.getName().equals(v.getName())) deUre.setLinksEdge(newE);
                            }
                            for (Vertex deFacto : vertexDeFacto) {
                                if (deFacto.getName().equals(v.getName())) deFacto.setLinksEdge(newE);
                            }
                        }
                    }
                }

                if (e.getRights().get(0).equals("g")) {
                    Vertex ver = e.getLinks().get(0);
                    for (Edge verE : v.getLinksEdge()) {
                        if (!verE.getRights().get(0).equals("t") && !verE.getRights().get(0).equals("g")) {
                            if (verE.equals(e)) continue;
                            Edge newE = new Edge();
                            newE.setEdge(verE.getRights().get(0), verE.getLinks().get(0));
                            for (Vertex deUre : vertexDeUre) {
                                if (deUre.getName().equals(ver.getName())) deUre.setLinksEdge(newE);
                            }
                            for (Vertex deFacto : vertexDeFacto) {
                                if (deFacto.getName().equals(ver.getName())) deFacto.setLinksEdge(newE);
                            }
                        }
                    }
                }
            }
        }
        printGraph(vertexDeUre);
    }


    private static boolean checkBridge(Vertex x, Vertex y) {
        //white black red blue green

        boolean[] visit = new boolean[vertex.size()];
        HashMap<String, String> color = new HashMap<>();
        for (Vertex v : vertex) {
            color.put(v.getName(), "white");
        }
        Stack<Vertex> v = new Stack<>();
        v.push(x);
        color.replace(x.getName(), "gray");
        while (!v.isEmpty()) {
            Vertex cur = v.pop();
            visit[vertex.indexOf(cur)] = true;
            if (!color.get(y.getName()).equals("white")) {
                return true;
            }
            for (Edge edge : cur.getLinksEdge()) {
                for (Vertex vertex1 : edge.getLinks()) {
                    //System.out.println(edge.getLinks().size() + " " + vertex1.getName() + " " + edge.getRights().size());
                    if (visit[vertex.indexOf(vertex1)] == true)
                        break;
                    v.push(vertex1);

                    int checkV_id = vertex1.getLinksEdge().indexOf(cur);
                    int count = 0;
                    for (Edge e : vertex1.getLinksEdge()) {
                        if (e.getLinks().get(0).getName().equals(cur.getName())) checkV_id = count;
                        count++;
                    }
                    if (color.get(vertex1.getName()).equals("white") && (color.get(cur.getName()).equals("gray") || (color.get(cur.getName()).equals("red")))) {
                        if (edge.getRights().get(0).equals("t")) {
                            color.replace(vertex1.getName(), "red");
                        } else if (edge.getRights().get(0).equals("g") || (checkV_id != 0 && vertex1.getLinksEdge().get(checkV_id).getRights().get(0).equals("g"))) {
                            color.replace(vertex1.getName(), "green");
                        }

                    }
                    if (color.get(vertex1.getName()).equals("white") && (color.get(cur.getName()).equals("gray") || (color.get(cur.getName()).equals("green")) ||
                            (color.get(cur.getName()).equals("blue")))) {
                        System.out.println("!");
                        if (vertex1.getLinksEdge().get(checkV_id).getRights().get(0).equals("t"))
                            color.replace(vertex1.getName(), "red");
                    }

                }
            }


        }
        return false;
    }


    static boolean[] used;

    static List<TGlink> tglinks_graph;

    public static void dfs(Vertex v, String[][] transMatrix) {
        Stack<Vertex> stack = new Stack();
        List<Vertex> linkT = new ArrayList<>();
        List<Vertex> linkG = new ArrayList<>();
        if (used[vertex.indexOf(v)]) {
            return;
        }
        stack.push(v);
        used[vertex.indexOf(v)] = true;
        while (!stack.isEmpty()) {
            v = stack.pop();
            //System.out.print(v.getName() + " ");

            for (int i = 0; i < transMatrix[vertex.indexOf(v)].length; i++) {
                if (transMatrix[vertex.indexOf(v)][i] != null) {
                    Vertex cur = vertex.get(i);
                    if (linkT.indexOf(v) == -1 && transMatrix[vertex.indexOf(v)][i].equals("t")) {
                        linkT.add(v);
                    }
                    if (linkG.indexOf(v) == -1 && transMatrix[vertex.indexOf(v)][i].equals("g")) {
                        linkG.add(v);
                    }
                    if (used[vertex.indexOf(cur)]) {
                        continue;
                    }
                    stack.push(cur);
                    used[vertex.indexOf(cur)] = true;

                }
            }

        }
        if (!linkT.isEmpty()) {
        /*
            System.out.print("T ");
            for (Vertex ver : linkT) {
                System.out.print(ver.getName() + " ");
            }
            System.out.println();


         */
            int count = 0;
            for (Vertex ch : linkT) {
                for (Vertex ch1 : linkT) {
                    if (matrix[vertex.indexOf(ch)][vertex.indexOf(ch1)] != null && matrix[vertex.indexOf(ch)][vertex.indexOf(ch1)].equals("t"))
                        count++;
                }
            }

            if (count >= linkT.size())
                tglinks_graph.add(new TGlink(linkT, "t"));
        }
        if (!linkG.isEmpty()) {
            /*
            System.out.print("G ");
            for (Vertex ver : linkG) {
                System.out.print(ver.getName() + " ");
            }
            System.out.println();



             */

            int count = 0;
            for (Vertex ch : linkG) {
                for (Vertex ch1 : linkG) {
                    if (matrix[vertex.indexOf(ch)][vertex.indexOf(ch1)] != null && matrix[vertex.indexOf(ch)][vertex.indexOf(ch1)].equals("g"))
                        count++;
                }
            }
            if (count == linkG.size())
                tglinks_graph.add(new TGlink(linkG, "g"));
        }
    }

    static private Stack<Vertex> stack;
    static private boolean[] timeInOut;

    public static void tg_links() throws FileNotFoundException, CloneNotSupportedException {
        kasurajuAlgoritm();
        for (TGlink tg : tglinks_graph) {
            if (tg.list_link.size() == 1) continue;
            System.out.printf("TG-подграф типа %s -> ", tg.type);
            for (Vertex v : tg.list_link) {
                System.out.print(v.getName() + " ");
            }
            System.out.println();
        }
    }

    private static void kasurajuAlgoritm() throws FileNotFoundException, CloneNotSupportedException {
        tglinks_graph = new ArrayList<>();


        used = new boolean[vertex.size()];
        timeInOut = new boolean[vertex.size()];
        stack = new Stack();
        int v = StdRandom.uniform(vertex.size());
        Vertex vertex1 = (vertex.get(v).clone());

        findComponent(vertex.get(v));
        /*
        for (Vertex ver : stack) {
            System.out.print(ver.getName() + " ");
        }

         */

        while (stack.size() != vertex.size()) {
            for (Vertex ver : vertex) {
                if (timeInOut[vertex.indexOf(ver)] == false) vertex1 = ver;
            }
            findComponent(vertex1);
        }
        /*
        System.out.println();
        for (Vertex ver : stack) {
            System.out.print(ver.getName() + " ");
        }
        System.out.println();
        //System.out.println("\n" + stack.pop().getName());
        /*

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if (matrix[i][j] != null)
                    System.out.print(matrix[i][j] + " ");
                else System.out.print("- ");
            }
            System.out.println();
        }

         */
        String[][] transMatrix = new String[vertex.size()][vertex.size()];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                transMatrix[j][i] = matrix[i][j];
            }
        }
        /*
        System.out.println("________________________________________");
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if (transMatrix[i][j] != null)
                    System.out.print(transMatrix[i][j] + " ");
                else System.out.print("- ");
            }
            System.out.println();
        }

         */


        while (!stack.isEmpty()) {
            dfs(stack.pop(), transMatrix);
        }

    }

    public static void findComponent(Vertex v) {
        timeInOut[vertex.indexOf(v)] = true;
        for (Edge e : v.getLinksEdge()) {
            //System.out.println(e.getLinks().get(0).getName()+" <- "+v.getName());
            if (timeInOut[vertex.indexOf(e.getLinks().get(0))] == false) {
                timeInOut[vertex.indexOf(e.getLinks().get(0))] = true;
                findComponent(e.getLinks().get(0));
            }

        }
        stack.push(v);
    }


}

class Edge {
    private List<String> Rights;
    private List<Vertex> links;

    public Edge() {
        this.Rights = new ArrayList<>();
        this.links = new ArrayList<>();
    }

    public void setEdge(String right, Vertex link) {
        Rights.add(right);
        links.add(link);
    }

    public List<String> getRights() {
        return Rights;
    }

    public void setRights(List<String> rights) {
        Rights = rights;
    }

    public List<Vertex> getLinks() {
        return links;
    }

    public void setLinks(List<Vertex> links) {
        this.links = links;
    }

    @Override
    protected Edge clone() throws CloneNotSupportedException {

        return (Edge) super.clone();
    }
}

class Vertex implements Cloneable {
    private String name;
    private String type;
    private List<Edge> linksEdge;

    public Vertex(String name, String type) {
        this.name = name;
        this.type = type;
        linksEdge = new ArrayList<>();
    }

    public void setLinksEdge(Edge edge) {
        linksEdge.add(edge);
    }

    public List<Edge> getLinksEdge() {
        return linksEdge;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    protected Vertex clone() throws CloneNotSupportedException {
        return (Vertex) super.clone();
    }
}

class TGlink {
    public List<Vertex> list_link;
    public String type;

    public TGlink(List<Vertex> l, String type) {
        this.list_link = l;
        this.type = type;
    }
}
