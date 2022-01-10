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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class Program {
    static public void main(String[] args) {
        discretionaryModel dm = new discretionaryModel();
        int index = 0;
        dm.addSub("root", "System");
        while (true) {
            boolean log = false;
            StdOut.println("login type");
            String[] str_login = StdIn.readLine().split(" ");
            String login = str_login[0];
            String type_login = str_login[1];
            if(login != "") {
                for (int i = 0; i < dm.sub.size(); i++) {
                    if (dm.sub.get(i).name.equals(login))
                        index = 1;
                }
                if(index == 0) {
                    dm.addSub(login, type_login);
                    dm.setGraph("System", type_login);
                    log = true;
                }

            }
            while (log) {
                StdOut.println("command arguments\n" +
                        "createObj nameObj type\n" +
                        "deleteObj nameObj\n" +
                        "addRule rule nameObj nameSub\n" +
                        "removeRule rule nameObj nameSub\n" +
                        "show\n" +
                        "logout\n" +
                        "graph" +
                        "test");
                String str= StdIn.readLine();
                String[] keys = str.split(" ");
                switch (keys[0]) {
                    case ("createObj"):
                        String c_obj= keys[1];
                        String type = keys[2];
                        String type_parent = "";
                        int index_sub = 0;
                        for (int i = 0; i < dm.sub.size(); i++) {
                            if (dm.sub.get(i).name.equals(login)) {
                                type_parent = dm.sub.get(i).type;
                                index_sub = i;
                            }
                        }
                        dm.addObj(c_obj, type);
                        int index_obj = 0;
                        for (int i = 0; i < dm.obj.size(); i++) {
                            if (dm.obj.get(i).name.equals(c_obj)) {
                                index_obj = i;
                            }
                        }
                        //if(!type_parent.equals(type))
                            dm.setGraph(type_parent, type);
                        dm.addRight("rwo", index_sub, index_obj, login, c_obj, "root");
                        break;
                    case ("deleteObj"):
                        String d_obj= keys[1];
                        dm.removeObj(d_obj);
                        break;
                    case ("addRule"):
                        String rul= keys[1];
                        String nameObj= keys[2];
                        String nameSub= keys[3];

                        index_sub = 0;
                        for (int i = 0; i < dm.sub.size(); i++) {
                            if (dm.sub.get(i).name.equals(login))
                                index_sub = i;
                        }
                        index_obj = 0;
                        for (int i = 0; i < dm.obj.size(); i++) {
                            if (dm.obj.get(i).name.equals(login))
                                index_obj = i;
                        }

                        dm.addRight(rul, index_sub, index_obj, nameSub, nameObj, login);
                        break;
                    case ("removeRule"):
                        rul= keys[1];
                        nameObj= keys[2];
                        nameSub= keys[3];

                        index_sub = 0;
                        for (int i = 0; i < dm.sub.size(); i++) {
                            if (dm.sub.get(i).name.equals(nameSub))
                                index_sub = i;
                        }
                        index_obj = 0;
                        for (int i = 0; i < dm.obj.size(); i++) {
                            if (dm.obj.get(i).name.equals(nameObj))
                                index_obj = i;
                        }

                        dm.removeRight(index_sub, index_obj, rul.toCharArray(), login);
                        break;
                    case ("logout"):
                        log = false;
                        break;
                    case ("show"):
                        System.out.print("      ");
                        for (int j = 0; j < dm.obj.size(); j++) {
                            System.out.print(dm.obj.get(j).name+""+dm.obj.get(j).type + "      ");
                        }
                        System.out.println();
                        for (int i = 1; i < dm.sub.size(); i++) {
                            System.out.print(dm.sub.get(i).name + " "+dm.sub.get(i).type+" ");
                            for (int j = 0; j < dm.obj.size(); j++) {
                                Access a = dm.getRight(i, j);
                                if (a != null) System.out.print(a.r + "" + a.w +""+a.o+ "      ");
                                else System.out.print("         ");
                            }
                            System.out.println();
                        }
                        System.out.println();
                        break;
                    case ("graph"):
                        Layout<String, String> layout = new CircleLayout(dm.graph);
                        layout.setSize(new Dimension(600,600));
                        BasicVisualizationServer<String,String> vv =
                                new BasicVisualizationServer<String,String>(layout);
                        vv.setPreferredSize(new Dimension(1150,950));
                        // Setup up a new vertex to paint transformer...
                        Transformer<String,Paint> vertexPaint = new Transformer<String,Paint>() {
                            public Paint transform(String i) {
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
                        break;
                    case ("test"):
                        dm.removeSub(login);
                        test(dm);
                        break;
                    default:
                        break;
                }


            }
        }
    }
    static void test(discretionaryModel dm){
        System.out.println("!");
        dm.addSub("Admin", "v");
        dm.addSub("User", "u");
        dm.addObj("File", "v");
        dm.addObj("File0", "u");
        dm.addRight("rwo", dm.sub.indexOf("Admin"), dm.obj.indexOf("File"), "Admin", "File", "root");
        dm.addRight("rw-", dm.sub.indexOf("User"), dm.obj.indexOf("File"), "User", "File", "root");
        dm.addRight("rwo", dm.sub.indexOf("Admin"), dm.obj.indexOf("File0"), "Admin", "File0", "root");
        dm.addRight("rw-", dm.sub.indexOf("User"), dm.obj.indexOf("File0"), "User", "File0", "root");
        System.out.print("      ");
        for (int j = 0; j < dm.obj.size(); j++) {
            System.out.print(dm.obj.get(j) + " ");
        }
        System.out.println();
        for (int i = 0; i < dm.sub.size(); i++) {
            System.out.print(dm.sub.get(i) + " ");
            for (int j = 0; j < dm.obj.size(); j++) {
                Access a = dm.getRight(i, j);
                if (a != null) System.out.print(a.r + "" + a.w +""+a.o+ "      ");
                else System.out.print("         ");
            }
            System.out.println();
        }

        //проверка
        System.out.println();
        System.out.println("remove [rw-] User File0 <- Admin");
        System.out.print(dm.removeRight(2, 1, "rw-".toCharArray(), "Admin")); // удаление права
        System.out.print("  ");
        for (int j = 0; j < dm.obj.size(); j++) {
            System.out.print(dm.obj.get(j) + " ");
        }
        System.out.println();
        for (int i = 0; i < dm.sub.size(); i++) {
            System.out.print(dm.sub.get(i) + " ");
            for (int j = 0; j < dm.obj.size(); j++) {
                Access a = dm.getRight(i, j);
                if (a != null) System.out.print(a.r + "" + a.w +""+a.o+ "      ");
                else System.out.print("         ");
            }
            System.out.println();
        }

        System.out.println();
        System.out.println("add [-w-] User File0 <- Admin");
        System.out.print(dm.addRight("-w-", 2, 1, "User", "File0", "Admin"));
        System.out.print("  ");
        for (int j = 0; j < dm.obj.size(); j++) {
            System.out.print(dm.obj.get(j) + " ");
        }
        System.out.println();
        for (int i = 0; i < dm.sub.size(); i++) {
            System.out.print(dm.sub.get(i) + " ");
            for (int j = 0; j < dm.obj.size(); j++) {
                Access a = dm.getRight(i, j);
                if (a != null) System.out.print(a.r + "" + a.w +""+a.o+ "      ");
                else System.out.print("         ");
            }
            System.out.println();
        }

        System.out.println();
        System.out.println("remove [rw-] User File <- Admin");
        System.out.print(dm.removeRight(2, 0, "rw-".toCharArray(), "Admin")); // удаление права
        System.out.print("  ");
        for (int j = 0; j < dm.obj.size(); j++) {
            System.out.print(dm.obj.get(j) + " ");
        }
        System.out.println();
        for (int i = 0; i < dm.sub.size(); i++) {
            System.out.print(dm.sub.get(i) + " ");
            for (int j = 0; j < dm.obj.size(); j++) {
                Access a = dm.getRight(i, j);
                if (a != null) System.out.print(a.r + "" + a.w +""+a.o+ "      ");
                else System.out.print("         ");
            }
            System.out.println();
        }

        System.out.println();
        System.out.println("add [rw-] User File <- Admin");
        System.out.print(dm.addRight("rw-", 2, 0, "User", "File", "Admin")); // add права
        System.out.print("  ");
        for (int j = 0; j < dm.obj.size(); j++) {
            System.out.print(dm.obj.get(j) + " ");
        }
        System.out.println();
        for (int i = 0; i < dm.sub.size(); i++) {
            System.out.print(dm.sub.get(i) + " ");
            for (int j = 0; j < dm.obj.size(); j++) {
                Access a = dm.getRight(i, j);
                if (a != null) System.out.print(a.r + "" + a.w +""+a.o+ "      ");
                else System.out.print("         ");
            }
            System.out.println();
        }

        System.out.println();
        System.out.println("remove [rw-] User File <- User");
        System.out.print(dm.removeRight(2, 1, "rw-".toCharArray(), "User")); // удаление права
        System.out.print("  ");
        for (int j = 0; j < dm.obj.size(); j++) {
            System.out.print(dm.obj.get(j) + " ");
        }
        System.out.println();
        for (int i = 0; i < dm.sub.size(); i++) {
            System.out.print(dm.sub.get(i) + " ");
            for (int j = 0; j < dm.obj.size(); j++) {
                Access a = dm.getRight(i, j);
                if (a != null) System.out.print(a.r + "" + a.w +""+a.o+ "      ");
                else System.out.print("         ");
            }
            System.out.println();
        }


        System.out.println();
        System.out.println("add [rw-] User File0 <- User");
        System.out.print(dm.addRight("rw-", 1, 1, "User", "File0", "User")); // add права
        System.out.print("  ");
        for (int j = 0; j < dm.obj.size(); j++) {
            System.out.print(dm.obj.get(j) + " ");
        }
        System.out.println();
        for (int i = 0; i < dm.sub.size(); i++) {
            System.out.print(dm.sub.get(i) + " ");
            for (int j = 0; j < dm.obj.size(); j++) {
                Access a = dm.getRight(i, j);
                if (a != null) System.out.print(a.r + "" + a.w +""+a.o+ "      ");
                else System.out.print("         ");
            }
            System.out.println();
        }

        System.out.println();
        System.out.println("add [rw-] User Dr Disk <- Admin");
        System.out.print(dm.addRight("rw-", 1, 1, "User", "Disk drive0", "Admin")); // add права
        System.out.print("  ");
        for (int j = 0; j < dm.obj.size(); j++) {
            System.out.print(dm.obj.get(j) + " ");
        }
        System.out.println();
        for (int i = 0; i < dm.sub.size(); i++) {
            System.out.print(dm.sub.get(i) + " ");
            for (int j = 0; j < dm.obj.size(); j++) {
                Access a = dm.getRight(i, j);
                if (a != null) System.out.print(a.r + "" + a.w +""+a.o+ "      ");
                else System.out.print("         ");
            }
            System.out.println();
        }
    }
}

class discretionaryModel{
    public Graph<String, String> graph = new SparseMultigraph<>();;

    public void setGraph(String parent, String child){
        String str = parent+" - "+child;
        graph.addEdge(str, parent, child, EdgeType.DIRECTED);
    }

    public class Obj{
        String name;
        String type;
        public Obj(String name, String type){
            this.type = type;
            this.name = name;
        }
    }

    public class Sub{
        String name;
        String type;
        public Sub(String name, String type){
            this.type = type;
            this.name = name;
        }
    }
    public Vector<Obj> obj;
    public Vector<Sub> sub;
    public ArrayList<Access> ListRight;
    public int sizeObj;
    public int sizeSub;
    public discretionaryModel(){
        this.obj = new Vector<Obj>();
        this.sub = new Vector<Sub>();
        this.sizeObj = 0;
        this.sizeSub = 0;
        ListRight = new ArrayList<>();
    }
    public void addObj(String obj_, String type){
        obj.add(sizeObj, new Obj(obj_, type));
        //addRight("rwo", 0, index, "root", obj_, "root");
        sizeObj++;
    }
    public void removeObj(String obj_){
        //System.out.println(obj_);
        for (int i = 0; i < ListRight.size(); i++) {
            if(ListRight.get(i).nameObj.equals(obj_)){
                //System.out.println(i+" "+obj.get(i));
                obj.remove(i);
                removeRight(ListRight.get(i).indexSub, ListRight.get(i).indexObj, new char[]{ListRight.get(i).r, ListRight.get(i).w, ListRight.get(i).o}, "root");
            }
        }
        sizeObj--;
    }
    public void addSub(String sub_, String type){
        sub.add(sizeSub, new Sub(sub_, type));
        sizeSub++;
    }
    public void removeSub(String sub_){
        int index = 0;
        for(int i = 0; i < obj.size(); i++) {
            if(sub.get(i).name.equals(sub_))
                index = i;
        }
        sub.remove(index);
        sizeSub--;
    }
    public boolean addRight(String right, int isub_, int iobj_, String nameSub, String nameObj, String whoAdd){
        //Проверка на наличие в матрице суб и объекта
        //System.out.printf("%s, %d, %d, %s, %s, %s\b", right, isub_, iobj_, nameSub, nameObj, whoAdd);
        int check = 0;
        for (int i = 0; i < sub.size(); i++) {
            if (sub.get(i).name.equals(nameSub)) check++;
        }
        for (int i = 0; i < obj.size(); i++) {
            if (obj.get(i).name.equals(nameObj)) check++;
        }

        //Проверка на наличия права на добавление
        if(!(whoAdd == "root")) {
            int c = 0;
            for (int i = 0; i < ListRight.size(); i++) {
                if (ListRight.get(i).o == 'o' && ListRight.get(i).nameObj.equals(nameObj) && ListRight.get(i).nameSub.equals(whoAdd)) {
                    c++;
                }

            }
            if (c == 0) {
                return false;
            }

        }

        char[] rt = right.toCharArray();
        if (check == 2) {
            for (int i = 0; i < ListRight.size(); i++) {
                //Проверка на наличие в ячейке существуещего права...если есть удаляем / если нет дальше / если удалить не получилось ошибка
                if (ListRight.get(i).indexSub == isub_ && ListRight.get(i).indexObj == iobj_ ) {
                    removeRight(isub_, iobj_, new char[]{ListRight.get(i).r, ListRight.get(i).w, ListRight.get(i).o}, whoAdd);
                }
            }
            //Добавление права
            Access ac = new Access(rt[0], rt[1], rt[2],  isub_, iobj_, nameSub, nameObj);
            ListRight.add(ac);
            return true;
        }
        else return false;

    }
    public Access getRight(int isub_, int iobj_){
        for(int i = 0; i < ListRight.size();i++){
            if(ListRight.get(i).indexSub == isub_ && ListRight.get(i).indexObj == iobj_){
                return ListRight.get(i);
            }
        }
        return null;

    }
    public boolean removeRight(int isub_, int iobj_, char[] right, String whoRemove){
        //System.out.println(isub_+" "+iobj_+" "+whoRemove+" "+Arrays.toString(right));
        for(int i = 0; i < ListRight.size();i++){
            //System.out.println(ListRight.get(i).indexSub+" "+ListRight.get(i).indexObj+" "+ListRight.get(i).nameSub+" "+ListRight.get(i).o+" "+ListRight.get(i).alfa);
            if(ListRight.get(i).indexSub == isub_ && ListRight.get(i).indexObj == iobj_ && ListRight.get(i).r == right[0]
                    && ListRight.get(i).w == right[1] && ListRight.get(i).o == right[2]){
                for(int j = 0; j < ListRight.size();j++){
                    if(ListRight.get(j).o == 'o' && ListRight.get(j).nameSub == whoRemove) {
                        ListRight.remove(i);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

class Access{
    char r, w, o;
    int indexSub;
    int indexObj;
    String nameSub;
    String nameObj;
    public Access(char r, char w, char o, int iSub, int iObj, String nameSub, String nameObj){
        this.r = r;
        this.w = w;
        this.o = o;
        this.indexObj = iObj;
        this.indexSub = iSub;
        this.nameSub = nameSub;
        this.nameObj = nameObj;
    }
}

