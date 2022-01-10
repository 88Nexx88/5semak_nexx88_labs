import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;

public class Test {
    public static void main(String[] args){
        PriorityBlockingQueue<Package> test = new PriorityBlockingQueue<Package>(1, new Comparator<Package>() {
            @Override
            public int compare(Package o1, Package o2) {
                return (o1.priority > o2.priority) ? 1 : -1;
            }
        });
        for(int i = 0; i < 10; i++){
            Package newP = new Package(i, i+1);
            newP.priority = i;
            test.add(newP);
        }
        List<Package> ls = new ArrayList<>();
        while (!test.isEmpty()){

        }
    }
}
