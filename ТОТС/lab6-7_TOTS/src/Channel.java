import java.util.*;

public class Channel {
    public List<Integer> failNumberPackage;
    public Queue<Package> channelQ;
    public Queue<Package> channelB;

    private Sender sender;
    private Recipient recipient;


    public Channel(List<Integer> list, Sender sender, Recipient recipient) {
        failNumberPackage = new ArrayList<>();
        this.sender = sender;
        this.recipient = recipient;

        for (Integer i : list) {
            failNumberPackage.add(i);
        }
        channelQ = new LinkedList<>();
        channelB = new LinkedList<>();
    }

    public void proccesing() {
        Runnable task = () -> {
            while (true) {
                while (!channelQ.isEmpty()) {
                    Package cur = channelQ.poll();
                    System.out.println("!"+cur.getFrom()+" -"+cur.getName()+" "+cur.getType()+"->"+cur.getTo()+"!");
                    if (cur != null) {
                        if (failNumberPackage.contains(cur.getName())) {
                            cur.setType("NACK");
                        } else cur.setType("ACK");
                        recipient.setOnePackage(cur);


                    }
                }
                try {
                    Thread.currentThread().sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }



    public void proccesingBack() {
        Runnable task = () -> {
            while (true) {
                while (!channelB.isEmpty()) {
                    Package cur = channelB.poll();
                    //System.out.println("!"+cur.getFrom()+" -"+cur.getName()+" "+cur.getType()+"->"+cur.getTo()+"!");

                    sender.setOnePackageCheck(cur);


                }
                try {
                    Thread.currentThread().sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

}
