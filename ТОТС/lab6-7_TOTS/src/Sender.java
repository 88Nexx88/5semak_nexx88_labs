import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class Sender {
    private PriorityQueue<Package> senderQ;
    private Queue<Package> checkQ;
    private Package wait;
    private Instant start;
    private Channel channel;

    private Recipient recipient;

    public Sender() {
        senderQ = new PriorityQueue<>(new Comparator<Package>() {
            @Override
            public int compare(Package o1, Package o2) {
                return (o1.getName() > o2.getName()) ? 1 : -1;
            }
        });
        checkQ = new LinkedList<>();
    }

    public void proccesing() {

        Runnable task = () -> {
            Package pac = senderQ.poll();
            System.out.println("sent -> " + pac.getName());
            channel.channelQ.add(pac);
            wait = pac;
            start = Instant.now();
            while (recipient.finishQ.size() != 100) {
                Instant finish = Instant.now();
                long elapsed = Duration.between(start, finish).toMillis();
                //System.out.println(elapsed);
                if (elapsed > 3000) {
                    senderQ.add(wait);
                }
                if (!checkQ.isEmpty()) {
                    Package p = checkQ.peek();
                    if (p.getName().equals(wait.getName())) {
                        p = checkQ.poll();

                        System.out.println("Check -> " + p.getName() + " " + p.getType());
                        if (p.getType().equals("ACK")) {

                        } else {
                            p.setType("ACK");
                            p.setTo("rec");
                            p.setFrom("sender");
                            senderQ.add(p);
                        }

                        try {
                            Thread.currentThread().sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if(recipient.finishQ.size() == 100) break;
                        pac = senderQ.poll();
                        if(pac == null) break;
                        wait = pac;
                        start = Instant.now();
                        System.out.println("sent -> " + pac.getName());
                        channel.channelQ.add(pac);
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


    int sizeWindow = 3;
    Queue<Package> window;
    int S = 0;
    int SF = 0;
    int SL = SF + sizeWindow - 1;
    List<Package> list;
    public void proccesingBACKN() {
        list = new ArrayList<>();
        while(!senderQ.isEmpty()){
            list.add(senderQ.poll());
        }
        Runnable task = () -> {

            while (recipient.finishQ.size() != 100) {
                window = new LinkedList<>();
                if (!checkQ.isEmpty()) {
                    Package p = checkQ.poll();
                    System.out.println(p.getName() +" "+p.getType()+" !!!!!!");
                    Integer name = p.getName();
                    if (p.getType().equals("ACK")) {
                        SF = name;
                        SL = SF + sizeWindow - 1;
                    }

                }
                System.out.println("SF = "+ SF +" "+SL);
                if(SL > list.size()-1) SL = list.size()-1;
                for (int i = SF; i <= SL; i++) {
                    window.add(list.get(i));
                }
                //System.out.println(" "+ window.size());
                while (!window.isEmpty()) {
                    Package p = window.poll();
                    if(p == null) break;
                    System.out.println("sent -> " + p.getName());
                    channel.channelQ.add(p);

                    try {
                        Thread.currentThread().sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
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







    int sizeWindowSR = 3;
    Queue<Package> windowSR;
    int SSR = 0;
    int SFSR = 0;
    int SLSR = SFSR + sizeWindowSR - 1;
    List<Package> listSR;
    public void proccesingSELREP() {
        listSR = new ArrayList<>();
        while(!senderQ.isEmpty()){
            listSR.add(senderQ.poll());
        }
        Runnable task = () -> {

            while (recipient.finishQ.size() != 100) {
                windowSR = new LinkedList<>();
                if (!checkQ.isEmpty()) {
                    Package p = checkQ.poll();
                    System.out.println(p.getName() +" "+p.getType()+" check");
                    Integer name = p.getName();
                    if (p.getType().equals("ACK")) {
                        SFSR = name;
                        SLSR = SFSR + sizeWindowSR - 1;
                    }
                    else {
                        Package pac = listSR.get(listSR.indexOf(p));
                        System.out.println("sent again-> " + pac.getName());
                        channel.channelQ.add(pac);

                        try {
                            Thread.currentThread().sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        continue;
                    }

                }
                //System.out.println("SF = "+ SF +" "+SL);
                if(SLSR > listSR.size()-1) SLSR = listSR.size()-1;
                for (int i = SFSR; i <= SLSR; i++) {
                    windowSR.add(listSR.get(i));
                }
                //System.out.println(" "+ window.size());
                while (!windowSR.isEmpty()) {
                    Package p = windowSR.poll();
                    if(p == null) break;
                    System.out.println("sent -> " + p.getName());
                    channel.channelQ.add(p);

                    try {
                        Thread.currentThread().sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
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


    public void setOnePackage(Package p) {
        senderQ.add(p);
    }

    public void setOnePackageCheck(Package p) {
        checkQ.add(p);
    }

    public Queue<Package> getSenderQ() {
        return senderQ;
    }

    public void setSenderQ(PriorityQueue<Package> senderQ) {
        this.senderQ = senderQ;
    }

    public Queue<Package> getCheckQ() {
        return checkQ;
    }

    public void setCheckQ(PriorityQueue<Package> checkQ) {
        this.checkQ = checkQ;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Recipient getRecipient() {
        return recipient;
    }

    public void setRecipient(Recipient recipient) {
        this.recipient = recipient;
    }
}
