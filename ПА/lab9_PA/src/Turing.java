import java.io.FileWriter;
import java.io.IOException;

public class Turing {
    private FileWriter fw;
    private int countWhiteSpaces;

    public Turing(String outputFile) {
        try {
            fw = new FileWriter(outputFile, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run(Table table, String str) throws IOException {
        char[] example = str.toCharArray();
        String state = "q1";
        String[] step;
        do {
            fw.write(example);
            step = table.getStep(String.valueOf(example[countWhiteSpaces]), state);
            //System.out.println(step[0].length() +" " + step[0]);
            example[countWhiteSpaces] = step[0].charAt(0);
            printWhiteSpaces(step[1]);
            state = step[2];
        } while (!state.equals("stop") && countWhiteSpaces != example.length);

    }

    private void printWhiteSpaces(String pattern) throws IOException {
        if (pattern.equals(">")) {
            fw.write("\n");
            for (int i = 0; i < countWhiteSpaces; i++) {
                fw.write(" ");
            }
            fw.write("^\n");
            countWhiteSpaces++;
        } else if (pattern.equals("<")) {
            fw.write("\n");
            for (int i = 0; i < countWhiteSpaces; i++) {
                fw.write(" ");
            }
            fw.write("^\n");
            countWhiteSpaces--;
        }
        fw.flush();
    }
}