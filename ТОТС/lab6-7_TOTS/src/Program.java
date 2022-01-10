import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Program {
    public static void main(String[] args){
        //parametrs
        int number = 100;
        int intens = 1;
        int delay = 1;
        List<Integer> failList = generatedFailList(100);
        //

        Sender sender = new Sender();
        Recipient recipient = new Recipient(delay);

        Channel channel = new Channel(failList, sender, recipient);
        sender.setChannel(channel);
        sender.setRecipient(recipient);
        recipient.setChannel(channel);



        for(int i = 0; i < number; i++){
            Package p = new Package(i);
            p.setTo("rec");
            p.setFrom("sender");
            p.setType("ACK");
            sender.setOnePackage(p);
        }
        Instant start1 = Instant.now();
        sender.proccesing();
        channel.proccesing();
        channel.proccesingBack();
        recipient.proccesing(start1);



        while (!recipient.stop){
            try {
                Thread.currentThread().sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("!!!!!!!!");
        Sender sender1 = new Sender();
        Recipient recipient1 = new Recipient(delay);

        Channel channel1 = new Channel(failList, sender1, recipient1);
        sender1.setChannel(channel1);
        sender1.setRecipient(recipient1);
        recipient1.setChannel(channel1);



        for(int i = 0; i < number; i++){
            Package p = new Package(i);
            p.setTo("rec");
            p.setFrom("sender");
            p.setType("ACK");
            sender1.setOnePackage(p);
        }
        start1 = Instant.now();
        sender1.proccesingBACKN();
        channel1.proccesing();
        channel1.proccesingBack();
        recipient1.proccesingBACKN(start1);

        while (!recipient1.stop){
            try {
                Thread.currentThread().sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("!!!!!!!!");
        Sender sender2 = new Sender();
        Recipient recipient2 = new Recipient(delay);

        Channel channel2 = new Channel(failList, sender2, recipient2);
        sender2.setChannel(channel2);
        sender2.setRecipient(recipient2);
        recipient2.setChannel(channel2);



        for(int i = 0; i < number; i++){
            Package p = new Package(i);
            p.setTo("rec");
            p.setFrom("sender");
            p.setType("ACK");
            sender2.setOnePackage(p);
        }
        start1 = Instant.now();
        sender2.proccesingSELREP();
        channel2.proccesing();
        channel2.proccesingBack();
        recipient2.proccesingSELREP(start1);

        while (!recipient2.stop){
            try {
                Thread.currentThread().sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println(recipient.timer1);
        System.out.println(recipient1.timer2);
        System.out.println(recipient2.timer3);

    }
    private static List<Integer> generatedFailList(int n){
        List<Integer> failNumberPackage = new ArrayList<>();
        int N = StdRandom.uniform(0, n);
        for(int i = 0; i < N; i++)
            failNumberPackage.add(StdRandom.uniform(n));
        Collections.shuffle(failNumberPackage);
        System.out.printf("Fail coefficient -> %.2f\n", ((float)N/100));
        System.out.println(failNumberPackage.toString());
        return failNumberPackage;
    }

}
