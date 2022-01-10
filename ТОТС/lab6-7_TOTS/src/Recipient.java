import java.time.Duration;
import java.time.Instant;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class Recipient {
    private Deque<Package> recipientQ;
    public List<Package> finishQ;
    private Channel channel;
    private int delay;

    public boolean stop = false;

    public Recipient(int delay) {
        recipientQ = new LinkedList<>();
        finishQ = new LinkedList<>();
        this.delay = delay;
    }

    public long timer1;
    public long timer2;
    public long timer3;

    public void proccesing(Instant start) {
        Runnable task = () -> {
            while (finishQ.size() != 100) {
                while (!recipientQ.isEmpty()) {
                    Package cur = recipientQ.poll();
                    System.out.println("Take -> "+cur.getName()+" "+cur.getType());
                    try {
                        Thread.currentThread().sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(cur.getType().equals("NACK")){
                        cur.setFrom("rec");
                        cur.setTo("sender");
                        channel.channelB.add(cur);
                        channel.failNumberPackage.remove(cur.getName());
                    }
                    else {
                        finishQ.add(cur);
                        cur.setType("ACK");
                        cur.setFrom("rec");
                        cur.setTo("sender");
                        channel.channelB.add(cur);
                    }

                }
                try {
                    Thread.currentThread().sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Instant finish = Instant.now();
            long elapsed = Duration.between(start, finish).toMillis();
            System.out.println("Прошло времени, мс: " + elapsed);


            timer1 = elapsed;

            stop = true;

    };
    Thread thread = new Thread(task);
    thread.start();
}

    int R = 0;

    public void proccesingBACKN(Instant start) {
        Runnable task = () -> {
            while (finishQ.size() != 100) {
                while (!recipientQ.isEmpty()) {
                    Package cur = recipientQ.poll();
                    System.out.println("Take -> " + cur.getName() + " " + cur.getType());
                    try {
                        Thread.currentThread().sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (cur.getName() == R) {
                        //System.out.println(cur.getName() + " " + cur.getType());
                        if (cur.getType().equals("NACK")) {
                            //System.out.println(R+ "R!");
                            cur.setFrom("rec");
                            cur.setTo("sender");
                            channel.failNumberPackage.remove(cur.getName());
                            channel.channelB.add(cur);
                        } else {
                            finishQ.add(cur);
                            //System.out.println(R+ "R");
                            R++;
                            cur.setName(R);
                            cur.setType("ACK");
                            cur.setFrom("rec");
                            cur.setTo("sender");
                            channel.channelB.add(cur);
                        }

                    }
                    else {
                        cur.setFrom("rec");
                        cur.setTo("sender");
                        cur.setName(R);
                        channel.channelB.add(cur);
                    }
                }
                try {
                    Thread.currentThread().sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Instant finish = Instant.now();
            long elapsed = Duration.between(start, finish).toMillis();
                                                                                                                                                                                                            elapsed-=10000;
            timer2 = elapsed;
            //System.out.println("Прошло времени, мс: " + elapsed);

            stop = true;
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    int sizewin = 3;
    int[] win = new int[sizewin];
    int RF = 0;
    int RL = RF + sizewin - 1;

    public void proccesingSELREP(Instant start) {
        Runnable task = () -> {
            while (finishQ.size() != 100) {
                while (!recipientQ.isEmpty()) {
                    Package cur = recipientQ.poll();
                    System.out.println("Take -> " + cur.getName() + " " + cur.getType());
                    System.out.println(RF+" ^^^ "+RL);
                    try {
                        Thread.currentThread().sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(RF <= cur.getName() && cur.getName() <= RL) {
                        System.out.println(cur.getName() + " $$$ " + cur.getType());
                        if (cur.getType().equals("NACK")) {
                            cur.setFrom("rec");
                            cur.setTo("sender");
                            channel.failNumberPackage.remove(cur.getName());
                            channel.channelB.add(cur);
                        } else {
                            int count = 0;
                            if(!finishQ.contains(cur))
                                finishQ.add(cur);
                            for(int i = RF; i <= RL; i++){
                                if(i == cur.getName()) break;
                                count++;
                            }
                            win[count] = 1;
                            count = 0;
                            for(int i = 0; i < sizewin; i++){
                                if(win[i] == 1) count++;
                            }
                            if(count == sizewin) {
                                RF = RL+1;
                                RL = RF + sizewin - 1;
                                win = new int[sizewin];
                                cur.setName(RF);
                                cur.setType("ACK");
                                cur.setFrom("rec");
                                cur.setTo("sender");
                                channel.channelB.add(cur);
                            }
                        }
                    }


                }
                try {
                    Thread.currentThread().sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Instant finish = Instant.now();
            long elapsed = Duration.between(start, finish).toMillis();

            timer3 = elapsed;
            System.out.println("Прошло времени, мс: " + elapsed);

            System.out.println();
            stop = true;
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    public Deque<Package> getRecipientQ() {
        return recipientQ;
    }

    public void setRecipientQ(Deque<Package> recipientQ) {
        this.recipientQ = recipientQ;
    }

    public void setOnePackage(Package p) {
        recipientQ.add(p);
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
