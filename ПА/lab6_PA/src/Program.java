import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Program {
    public static void main(String[] args) throws IOException {
        Avtomat avtomat = new Avtomat();
        File file = new File("struct");
        FileReader fr = new FileReader(file);
        BufferedReader reader = new BufferedReader(fr);
        String line = reader.readLine();
        while (line != null) {
            String[] rules = line.split(";");
            String[] element_rul = rules[0].split(",");
            String[] element_perehod = rules[1].split(",");
            if (element_rul.length == 1 && element_perehod.length == 1) {
                avtomat.startsost = element_rul[0];
                avtomat.dopsost = element_perehod[0];
            } else {
                List<String> rul = new ArrayList<>();
                List<String> perehod = new ArrayList<>();
                for (String s : element_rul) {
                    rul.add(s);
                }
                for (String s : element_perehod) {
                    perehod.add(s);
                }
                avtomat.setOnePerehod(rul, perehod);
            }
            line = reader.readLine();
        }
        System.out.println("struct: ");
        System.out.println(avtomat.perehod.toString());
        System.out.printf("start %s; dop %s\n", avtomat.startsost, avtomat.dopsost);
        File file_test = new File("test");
        FileReader fr_test = new FileReader(file_test);
        BufferedReader reader_test = new BufferedReader(fr_test);
        String line_test = reader_test.readLine();
        System.out.println("chain: " + line_test);
        boolean check = true;
        avtomat.currentsost = avtomat.startsost;
        char[] chain = line_test.toCharArray();
        List<String> data;
        for (int i = 0; i < chain.length; i++) {
            data = new ArrayList<>();
            data.add(avtomat.currentsost);
            data.add(chain[i] + "");
            if (avtomat.magazin.isEmpty()) {
                data.add("eps");
            } else data.add(avtomat.magazin.pop());
            //System.out.println("symbol: " + chain[i] + " data: " + data.toString());
            if (avtomat.perehod.get(data) != null) {
                List<String> next_perehod = avtomat.perehod.get(data);
                avtomat.currentsost = next_perehod.get(0);
                if (!next_perehod.get(1).equals("eps")) {
                    for (Character ch : next_perehod.get(1).toCharArray())
                        avtomat.magazin.add(ch + "");
                }
                System.out.println("current sost: " + avtomat.currentsost + " mag:" + avtomat.magazin.toString());
            } else {
                System.out.println("Не допускающая");
                check = false;
                break;
            }
        }
        if (check) {
            data = new ArrayList<>();
            data.add(avtomat.currentsost);
            data.add("eps");
            if (avtomat.magazin.isEmpty()) {
                data.add("eps");
            } else data.add(avtomat.magazin.pop());
            //System.out.println("symbol: eps" + " data: " + data.toString());
            if (avtomat.perehod.get(data) != null) {
                List<String> next_perehod = avtomat.perehod.get(data);
                avtomat.currentsost = next_perehod.get(0);
                if (!next_perehod.get(1).equals("eps")) {
                    for (Character ch : next_perehod.get(1).toCharArray())
                        avtomat.magazin.add(ch + "");
                }
                //System.out.println("current sost: " + avtomat.currentsost + " mag:" + avtomat.magazin.toString());
            } else {
                System.out.println("Не допускающая");
                check = false;
            }
        }


        if (avtomat.currentsost.equals(avtomat.dopsost) && avtomat.magazin.isEmpty() && check) {
            System.out.println("Допускающая");
        }
        else if (check) System.out.println("Не допускающая");
    }
}
