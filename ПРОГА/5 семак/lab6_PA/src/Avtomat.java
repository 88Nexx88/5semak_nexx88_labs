import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class Avtomat {
    public Stack<String> magazin;
    public HashMap<List<String>, List<String>> perehod;
    public String startsost;
    public String currentsost;
    public String dopsost;
    public Avtomat(){
        this.magazin = new Stack<>();
        this.perehod = new HashMap<>();
    }

    public void setOnePerehod(List<String> rul, List<String> perehod){
        this.perehod.put(rul, perehod);
    }

}
