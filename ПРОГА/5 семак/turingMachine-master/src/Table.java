import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Table {
    private Map<String, Integer> rows;
    private Map<String, Integer> columns;
    private String[][][] table;

    public Table(List<String> rules) {
        this.rows = new HashMap<>();
        this.columns = new HashMap<>();
        String[] alphabet = rules.remove(0).split(" ");
        String[] states = rules.remove(0).split(" ");
        table = new String[alphabet.length][states.length][3];
        for (int j = 0; j < alphabet.length; j++)
            rows.put(alphabet[j], j);
        for (int j = 0; j < states.length; j++)
            columns.put(states[j], j);
        String[] s;
        for (String rule : rules) {
            s = rule.split(" ");
            table[rows.get(s[0])][columns.get(s[1])][0] = s[2];
            table[rows.get(s[0])][columns.get(s[1])][1] = s[3];
            table[rows.get(s[0])][columns.get(s[1])][2] = s[4];
        }
    }

    public String[] getCeil(String row, String column){
        return table[rows.get(row)][columns.get(column)];
    }
}
