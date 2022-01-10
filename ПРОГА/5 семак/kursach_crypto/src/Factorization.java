import java.time.Duration;
import java.time.Instant;

public class Factorization {
    public static void primeFactors(long n)
    {
        // Print the number of 2s that divide n
        while (n%2==0)
        {
            System.out.print(2 + " ");
            n /= 2;
        }

        // n must be odd at this point.  So we can
        // skip one element (Note i = i +2)
        for (long i = 3; i <= Math.sqrt(n); i+= 2)
        {
            // While i divides n, print i and divide n
            while (n%i == 0)
            {
                System.out.print(i + " ");
                n /= i;
            }
        }

        // This condition is to handle the case when
        // n is a prime number greater than 2
        if (n > 2)
            System.out.print(n);
    }

    public static void main (String[] args)
    {
        long n = 1024;
        long[] list = {11, 17, 18, 19, 22, 12, 13, 14};
        for(int i = 6; i < 10; i++){
            int rand = StdRandom.uniform(list.length-1);
            n = n * list[rand];
            long start = System.nanoTime();
            primeFactors(n);
            long end = System.nanoTime();
            long elapsed = end - start;
            System.out.println("Прошло времени, мс: " + elapsed);
        }
        //int n = 1024121241;
        Instant start = Instant.now();
        primeFactors(90415945);
        Instant finish = Instant.now();
        long elapsed = Duration.between(start, finish).toMillis();
        System.out.println("Прошло времени, мс: " + elapsed);
    }
}



