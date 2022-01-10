import java.io.*;
import java.util.*;

public class Program {

    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[32m";

    public static void main(String[] args) throws IOException {
        Avtomat avtomat = new Avtomat();
        File file = new File("struct");
        FileReader fr = new FileReader(file);
        BufferedReader reader = new BufferedReader(fr);
        String line = reader.readLine();
        while (line != null) {
            String[] arg = line.split("->");
            char[] pereh = arg[1].toCharArray();
            avtomat.setOneRul(arg[0].toCharArray()[0], pereh);
            line = reader.readLine();
        }
        System.out.println("struct: ");
        for(Avtomat.Rules r : avtomat.rul){
            System.out.print(r.name+" -> ");
            for(char s : r.perehod){
                System.out.print(s+" ");
            }
            System.out.println();
        }
        File file_test = new File("test");
        FileReader fr_test = new FileReader(file_test);
        BufferedReader reader_test = new BufferedReader(fr_test);
        String line_test = reader_test.readLine();
        for(int i = 0; i < line_test.length()-1;i++){
            for(int j = i+1; j < line_test.length(); j++){
                source = "";
                Queue<Character> q = new LinkedList<>();
                q.add('S');
                String res = "";
                source = line_test.substring(i, j);
                findDopSost(avtomat, q, res);
            }
        }

        for(String s : answer){
            System.out.println(RED+s+RESET);
        }
    }
    public static String source;
    public static List<String> answer= new ArrayList<>();

    public static void findDopSost(Avtomat avtomat, Queue<Character> q, String res) {
        System.out.println(res+" "+q.toString()/*.replace(", ", " ").replace("[", " ").replace("]", " ")*/);
        if(q.isEmpty()){
            if(res.equals(source)) {
                answer.add(res);
            }
        }
        else if((q.size()+res.length()) > source.length()){

        } else {
            char cur = q.poll();
            for(Avtomat.Rules r : avtomat.rul){
                if(r.name == cur){
                    String newRes = res;
                    Queue<Character> newQ = new LinkedList<>();
                    int size = q.size();
                    for(int i = 0; i < size; i++){
                        char c = q.poll();
                        newQ.add(c);
                        q.add(c);
                    }
                    for(char ch : r.perehod){
                        if(Character.isLowerCase(ch)){
                            newRes+=ch+"";
                        }
                        else newQ.add(ch);
                    }
                    findDopSost(avtomat, newQ, newRes);
                }
            }
        }



    }

}


class Avtomat {
    public List<Rules> rul;

    public Avtomat() {
        rul = new ArrayList<>();
    }

    public void setOneRul(char name, char[] perehod) {
        rul.add(new Rules(name, perehod));
    }

    class Rules {
        char name;
        char[] perehod;

        public Rules(char name, char[] perehod) {
            this.name = name;
            this.perehod = perehod;
        }
    }
}

