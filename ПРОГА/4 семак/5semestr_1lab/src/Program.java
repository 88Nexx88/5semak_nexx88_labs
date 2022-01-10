import java.io.*;
import java.util.Vector;

public class Program {

    public static void createFile(String path) throws IOException {
        FileWriter writer = new FileWriter(path);
        StringBuffer sb = new StringBuffer();
        int N = StdRandom.uniform(10, 20);
        for(int j = 0; j < N; j++) {
            int size_str = StdRandom.uniform(5, 100);
            String str = "";
            for (int i = 0; i < size_str; i++) {
                str += StdRandom.uniform(0, 2);
            }
            sb.append(str);
            sb.append("\n");
        }
        writer.append(sb);
        writer.flush();
    }

    public static void main(String[] args) throws IOException {
        File file = new File("lab1-str.csv");
        FileReader csv = new FileReader(file);
        BufferedReader reader = new BufferedReader(csv);
        String line = reader.readLine();
        int v = 0;
        while (true) {
            while (line != null) {
                v++;
                line = reader.readLine();
            }
            break;
        }
        Node[] graph = new Node[v];
        for (int i = 0; i < graph.length; i++) {
            graph[i] = new Node(i);
        }
        FileReader csv1 = new FileReader(file);
        BufferedReader reader1 = new BufferedReader(csv1);
        String line1 = reader1.readLine();
        int number = 0;
        while (true) {
            while (line1 != null) {
                String[] link_ = line1.split(",");
                boolean adm;
                if (Integer.parseInt(link_[2]) == 0) adm = false;
                else adm = true;
                graph[number].createNode(Integer.parseInt(link_[0]), Integer.parseInt(link_[1]), adm);
                number++;
                line1 = reader1.readLine();
            }
            break;
        }
        System.out.println("Структура: ");
        for (int i = 0; i < graph.length; i++) {
            System.out.println(graph[i].name + " " + graph[i].edge0 + " " + graph[i].edge1 + " " + graph[i].admitting);
        }

        File file1 = new File("strings.csv");
        FileReader csv2 = new FileReader(file1);
        BufferedReader reader2 = new BufferedReader(csv2);
        String line2 = reader2.readLine();
        v = 0;
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
        number = 0;
        while (true) {
            while (line3 != null) {
                str[number] = line3;
                number++;
                line3 = reader3.readLine();
            }
            break;
        }
        System.out.println("Строки: ");
        for (int i = 0; i < str.length; i++) {
            System.out.println(str[i]);
        }
        System.out.println();







        for (int i = 0; i < str.length; i++) {
            Status status = new Status();
            status.setStatus(graph[0]);
            char[] sequence = str[i].toCharArray();
            for (int k = 0; k < sequence.length; k++) {
                Node current = status.getStatus();
                status.setStatus(graph[current.getEdge(Character.digit(sequence[k], 10))]);
            }
            if (status.getStatus().admitting == true) System.out.print("Строка [" + str[i] + "] допустимая, " +
                    "состояние -> " + status.getStatus().name + " Переходы из состояний -> ");
            else System.out.print("Строка [" + str[i] + "] недопустимая, " +
                    "состояние -> " + status.getStatus().name + " Переходы из состояний -> ");
            System.out.println(status.getPath());


        }

    }


}

class Status {
    private Node currentNode;
    private Vector names = new Vector();

    public void setStatus(Node newNode) {
        this.currentNode = newNode;
        names.add(newNode.name);
    }

    public Node getStatus() {
        return currentNode;
    }

    public Vector getPath() {
        return names;
    }
}

class Node {
    boolean admitting;
    int name;
    int edge0;
    int edge1;

    public Node(int name) {
        this.name = name;
    }

    public void createNode(int edge0, int edge1, boolean admitting) {

        this.edge0 = edge0;
        this.edge1 = edge1;
        this.admitting = admitting;
    }

    public int getEdge(int bit) {
        if (bit == 0) return edge0;
        else return edge1;
    }

}



