import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Program {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("rules"), StandardCharsets.UTF_8);
        String example = lines.remove(0);
        char[] chars = example.toCharArray();
        Set<String> set = new HashSet<>();
        for (int i = 0; i < chars.length; i++) {
            set.add(String.valueOf(chars[i]));
        }
        String[] split = lines.get(0).split(" ");
        HashSet<String> collect = Arrays.stream(split).collect(Collectors.toCollection(HashSet::new));
        set.removeAll(collect);
        if(set.size() != 0){
            System.err.println("Ошибка. В ленте присутствуют символы вне алфавита!");
        }

        Table table = new Table(lines);
        Turing trace = new Turing("trace");
        trace.run(table, example);
    }
}
