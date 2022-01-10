import java.util.ArrayList;

public class Program {
    public static void main(String[] args) {
        String string_data = "01001";
        String[] input_data = {
                "0,0,1,0",
                "1,2,0,0",
                "2,3,0,0",
                "3,3,0,1"
        };
        ArrayList<ArrayList<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < input_data.length; i++) {
            graph.add(new ArrayList<>());
            String[] s = input_data[i].split(",");
            for (int j = 0; j < s.length; j++) {
                graph.get(i).add(Integer.parseInt(s[j]));
            }
        }
        System.out.println("Chain: " + string_data);
        System.out.println("Structure: ");
        for (int i = 0; i < input_data.length; i++) {
            for (int j = 0; j < graph.get(i).size(); j++) {
                System.out.print(graph.get(i).get(j) + " ");
            }
            System.out.println();
        }


        int status = graph.get(0).get(0);
        int[] path = new int[string_data.length() + 1];
        char[] chain = string_data.toCharArray();
        for (int i = 0; i < chain.length; i++) {
            for (ArrayList<Integer> o : graph) {
                if (o.get(0) == status) {
                    status = o.get(Character.digit(chain[i], 10) + 1);
                    System.out.println(i + ") symbol " + chain[i] + " -> " + status);
                    path[i] = status;
                    break;
                }
            }
        }
        for (ArrayList<Integer> o : graph) {
            if (o.get(0) == status) {
                if (o.get(3) == 1) System.out.println("chain is adm");
                else System.out.println("chain is not adm");
                break;
            }
        }
            System.out.println("Path: ");
            for (int i = 0; i < chain.length; i++) {
                System.out.print(path[i] + " ");
            }

    }
}
