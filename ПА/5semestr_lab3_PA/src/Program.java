import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class Program {
    static int[][] matrix;
    static int start;
    static List<Integer> final_old;
    public static String[] createFile(String path) throws IOException {
        FileWriter writer = new FileWriter(path);
        StringBuffer sb = new StringBuffer();
        int N = 100;
        String[] strings = new String[N];
        int count = 0;
        for(int j = 0; j < N; j++) {
            int size_str = StdRandom.uniform(1, 20);
            String str = "";
            for (int i = 0; i < size_str; i++) {
                str += StdRandom.uniform(0, 2);
            }
            sb.append(str);
            strings[count++] = str;
            sb.append("\n");
        }
        writer.append(sb);
        writer.flush();
        return strings;
    }

    public static void main(String[] args) throws IOException {
        String[] strings = createFile("test_lab3");
        File file = new File("lab3");//////////////////////////////////////////////


        FileReader csv = new FileReader(file);
        BufferedReader reader = new BufferedReader(csv);

        String line = reader.readLine();
        matrix = new int[Integer.parseInt(line)][2];
        line = reader.readLine();
        start = Integer.parseInt(line);
        int count = 0;
        int c = 0;
        List<Integer> final_node = new ArrayList<>();
        while (true) {
            line = reader.readLine();
            while (line != null && count != matrix.length) {
                String[] str = line.split(",");
                matrix[c][0] = Integer.parseInt(str[0]);
                matrix[c][1] = Integer.parseInt(str[1]);
                c++;
                if (count == matrix.length - 1) ;
                else
                    line = reader.readLine();
                count++;
            }
            line = reader.readLine();
            String[] str = line.split("-");
            for (int i = 0; i < str.length; i++) {
                final_node.add(Integer.parseInt(str[i]));
            }
            break;
        }
        ArrayList<Integer> l = new ArrayList<>();
        System.out.println("File -> ");
        for (int i = 0; i < matrix.length; i++) {
            System.out.println(i + " - " + matrix[i][0] + ", " + matrix[i][1]);
            if (final_node.indexOf(i) == -1)
                l.add(i);
        }
        for (int i = 0; i < final_node.size(); i++) {
            System.out.print("finish - > " + final_node.get(i) + " ");
        }
        final_old = List.copyOf(final_node);
        System.out.println();
        List<List<Integer>> list = new ArrayList<>();
        list.add(l);
        list.add(final_node);
        System.out.print(list.toString());
        System.out.println(" - 0 эквивалент");

        List<List<Integer>> oldList = List.copyOf(list);
        int index = 0;
        while (true) {
            index++;
            List<List<Integer>> newList = new ArrayList<>();
            for (List<Integer> group : oldList) {
                List<Integer> list_eq = new ArrayList<>();
                for (Integer i : group) {
                    for (Integer check : group) {
                        if (check == i) continue;
                        for (List<Integer> g : oldList) {
                            if ((g.contains(matrix[i][0]) && g.contains(matrix[check][0])))
                                for (List<Integer> d : oldList) {
                                    if ((d.contains(matrix[i][1]) && d.contains(matrix[check][1]))) {
                                        if (!list_eq.contains(i)) list_eq.add(i);
                                        if (!list_eq.contains(check)) list_eq.add(check);
                                    }
                                }
                        }

                    }
                }
                Collections.sort(list_eq);
                if (!list_eq.isEmpty())
                    newList.add(list_eq);
                for (Integer i : group) {
                    if (!list_eq.contains(i)) {
                        List<Integer> l_one = new ArrayList<>();
                        l_one.add(i);
                        newList.add(l_one);
                    }
                }
            }
            if (newList.equals(oldList))
                break;
            else {
                System.out.print(newList.toString());
                System.out.printf(" - %d эквивалент\n", index);
                oldList = List.copyOf(newList);

            }
        }
        reverceDFA(oldList, start, final_node);
        test(strings);

    }

    static int[][] newMatrix;
    static int start_new;
    static int final_new;
    public static int[][] reverceDFA(List<List<Integer>> list, Integer start, List<Integer> finish) {
        newMatrix = new int[list.size()][2];
        start_new = start;
        final_new = list.indexOf(finish);
        int count = 0;
        System.out.println("\nstart - " + start);
        for (List<Integer> o : list) {
            int i = o.get(0);
                for(List<Integer> check : list){
                    if(check.contains(matrix[i][0])) newMatrix[count][0] = list.indexOf(check);
                    if(check.contains(matrix[i][1])) newMatrix[count][1] = list.indexOf(check);
                }
            System.out.println(/*o.toString() + " -> "+ */count+" - " + newMatrix[count][0] + ", " + newMatrix[count][1]);
            count++;
        }
        System.out.println("finish - " + list.indexOf(finish));
        return newMatrix;
    }

    public static void test(String... str){
        /*
        for(int i = 0; i < matrix.length; i++){
            for (int j = 0; j < matrix[0].length;j++){
                System.out.print(matrix[i][j]+" ");
            }
            System.out.println();
        }
        System.out.println("final - "+final_old.toString());
        for(int i = 0; i < newMatrix.length; i++){
            for (int j = 0; j < newMatrix[0].length;j++){
                System.out.print(newMatrix[i][j]+" ");
            }
            System.out.println();
        }
        System.out.println("final - "+final_new);

         */
        int status1 = start;
        int status2 = start_new;
        for(int i = 0; i < str.length; i++){
            char[] chain = str[i].toCharArray();
            for(int j = 0; j < chain.length; j++){
                status1 = matrix[status1][Character.getNumericValue(chain[j])];
                //System.out.println(status1);
                //System.out.println(matrix[0][Character.getNumericValue(chain[j])]);
                status2 = newMatrix[status2][Character.getNumericValue(chain[j])];
                //System.out.println(status2);
            }
            System.out.print(str[i]+" ");
            if(final_old.contains(status1)) System.out.print("True ");
            else System.out.print("False ");
            if(final_new == status2) System.out.print("True");
            else System.out.print("False");
            System.out.println();
            status1 = start;
            status2 = start_new;
        }
    }


}