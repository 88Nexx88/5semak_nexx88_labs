import java.io.*;
import java.util.*;

public class Program {

    static Queue<Status> result = new LinkedList<>();
    static Queue<Status> statuses = new LinkedList<>();
    public static void transitions(Node[] graph, Queue<Character> string){

        if(string.isEmpty()){
            result = statuses;
        }
        /*
        ch 00

        st 0 1 2


         */

        else {
            char bit = string.poll();
            int num = statuses.size();
                for(int i = 0; i < num; i++) {
                    //System.out.println(statuses.size()+"!");
                    Status status = statuses.poll();
                    Queue names;
                    Node currentNode = status.getStatus().poll();
                    for (int j = 0; j < currentNode.edgeTo.size(); j++) {
                        char[] st = currentNode.edgeTo.get(j);
                        if (st[0] == bit) {
                            Status s = new Status();
                            names = status.clonePath(status.getPath());
                            int n = 0;
                            for(int k = 0; k < graph.length; k++){
                                if(graph[k].name == st[1]) n = k;
                            }
                            s.setStatus(graph[n], names);
                            statuses.add(s);
                        }
                    }
                }


            transitions(graph, string);

        }
    }

    public static void main(String[] args) throws IOException {
        File file = new File("lab2-str00.csv");//////////////////////////////////////////////

        FileReader csv = new FileReader(file);
        BufferedReader reader = new BufferedReader(csv);
        String line = reader.readLine();
        Vector<Character> names = new Vector<>();
        while (true) {
            while (line != null) {
                String[] str = line.split("-");
                names.add(str[0].toCharArray()[0]);
                line = reader.readLine();
            }
            break;
        }
        Node[] graph = new Node[names.size()];
        for (int i = 0; i < graph.length; i++) {
            graph[i] = new Node(names.get(i));
        }
        FileReader csv1 = new FileReader(file);
        BufferedReader reader1 = new BufferedReader(csv1);
        String line1 = reader1.readLine();
        while (true) {
            while (line1 != null) {
                String[] link_ = line1.split("-");
                Vector<String[]> nodes = new Vector();
                int checkNull = 0;
                for (int i = 1; i < link_.length-1; i++) {
                    if(link_[i] == "") checkNull = 1;
                    else nodes.add(link_[i].split(","));
                }
                /*
                for(int i = 0; i < nodes.size();i++){
                    String[] s = nodes.get(i);
                    System.out.print(s[0]+" "+s[1]+" ");
                }
                System.out.println();

                 */

                boolean adm;
                //System.out.println(nodes.size()+" !");

                Vector<char[]> edges = new Vector<>();
                if(checkNull == 1 ) {
                    edges.add(new char[]{ '`', '`'});
                    if (Integer.parseInt(link_[link_.length - 1]) == 0) adm = false;
                    else adm = true;
                    graph[Integer.parseInt(link_[0])].createNode(edges, adm);
                }
                else {
                    for (int i = 0; i < nodes.size(); i++) {
                        edges.add(new char[]{nodes.get(i)[0].toCharArray()[0], nodes.get(i)[1].toCharArray()[0]});
                    }
                    if (Integer.parseInt(link_[link_.length - 1]) == 0) adm = false;
                    else adm = true;
                    graph[Integer.parseInt(link_[0])].createNode(edges, adm);

                }
                line1 = reader1.readLine();
            }
            break;


        }
        System.out.println("Структура: ");
        for (int i = 0; i < graph.length; i++) {
            String str ="";
            for(int j = 0; j < graph[i].edgeTo.size();j++){
                if(graph[i].edgeTo.get(j)[0] == '`') {
                    break;
                }
                str+="symbol ";
                for(int k = 0; k < 2;k++){
                    if(k==1) str+=" to ";
                    str+=graph[i].edgeTo.get(j)[k];
                }
            }
            System.out.println("Name-> "+graph[i].name + " paths -> " + str+" adm-> " + graph[i].admitting);
        }
        File file1 = new File("strings.csv");
        FileReader csv2 = new FileReader(file1);
        BufferedReader reader2 = new BufferedReader(csv2);
        String line2 = reader2.readLine();
        int v = 0;
        while (true) {
            while (line2 != null) {
                v++;
                line2 = reader2.readLine();
            }
            break;
        }
        String[] str = new String[v];
        FileReader csv3 = new FileReader(file1);
        BufferedReader reader3 = new BufferedReader(csv3);
        String line3 = reader3.readLine();

        int number = 0;
        while (true) {
            while (line3 != null) {
                str[number] = line3;
                number++;
                line3 = reader3.readLine();
            }
            break;
        }

        System.out.println("Цепочки: ");
        for (int i = 0; i < str.length; i++) {
            System.out.println(str[i]);
        }
        System.out.println();

        for(int i = 0; i < number; i++) {
            result.clear();
            statuses.clear();
            char[] strChar = str[i].toCharArray();
            String currentstr = "";
            Queue string = new LinkedList();
            for(int j = 0; j < strChar.length;j++){
                string.add(strChar[j]);
                currentstr+=strChar[j];
            }
            System.out.println("Chain -> " + currentstr);
            Status st = new Status();
            st.setStatus_(graph[0]);
            statuses.add(st);
            transitions(graph, string);
            while (!result.isEmpty()) {
                Status s = result.poll();
                while (!s.getStatus().isEmpty()) {
                    Node n = s.getStatus().poll();
                    System.out.print("Path -> ");
                    while (!s.getPath().isEmpty()) {
                        System.out.print(s.getPath().poll() + " ");
                    }
                    System.out.print("LastStatus -> " + n.name + ", status is admitting " + n.admitting);
                    if(n.admitting == true) {
                        System.out.println();
                        System.out.print("Chain is valid");
                    }
                    System.out.println();
                }
            }
            System.out.println();
        }
         /*
        if (status.getStatus().admitting == true) System.out.print("Строка [" + str[i] + "] допустимая, " +
                "состояние -> " + status.getStatus().name + " Переходы из состояний -> ");
        else System.out.print("Строка [" + str[i] + "] недопустимая, " +
                "состояние -> " + status.getStatus().name + " Переходы из состояний -> ");
        System.out.println(status.getPath());



        */


    }


}


class Status {
    private Queue<Node> currentsNode = new LinkedList<>();
    private Queue names = new LinkedList();

    public void setStatus(Node newNode, Queue<Character> lastnames) {
        this.currentsNode.add(newNode);
        while(!lastnames.isEmpty()){
            char l = lastnames.poll();
            //System.out.print(l+" ");
            names.add(l);
        }
        names.add(newNode.name);
        //System.out.print(newNode.name+" ");
        //System.out.println();

    }
    public void setStatus_(Node newNode) {
        this.currentsNode.add(newNode);
        names.add(newNode.name);
    }

    public Queue<Node> getStatus() {
        return currentsNode;
    }

    public Queue clonePath(Queue paths){
        Queue result = new LinkedList();
        Iterator<Integer> it = paths.iterator();
        while(it.hasNext())  {
            result.add(it.next());
        }
        return result;
    }

    public Queue getPath() {
        return names;
    }
}

class Node {
    boolean admitting;
    char name;
    Vector<char[]> edgeTo;

    public Node(Character name) {
        this.name = name;
    }

    public void createNode(Vector edge, boolean admitting) {

        this.edgeTo = edge;
        this.admitting = admitting;
    }


}
