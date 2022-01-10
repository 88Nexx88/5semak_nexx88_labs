
public abstract class PowerAlgorithm {
    public static long allTime = 0;
    public abstract long power(int x, int y);

    static class NaivePower extends PowerAlgorithm {
        @Override
        public long power(int x, int y) {
            long result = 1;
            for (int i = 0; i < y; i++) {
                result *= x;
            }
            return result;
        }
    }

    static class FastPower extends PowerAlgorithm {
        @Override
        public long power(int x, int y) {
            long result = (y & 1) != 0 ? x : 1;
            while (y > 1) {
                x *= x;
                y >>= 1;
                if ((y & 1) != 0) result *= x;
            }
            return result;
        }
    }

    private static void benchmark(PowerAlgorithm algorithm, int[] bases, int[] exponents) {
        long total = 0;
        long start = System.currentTimeMillis();
        total += trial(algorithm, bases, exponents);
        long end = System.currentTimeMillis();
        System.out.printf("%15s: %d ms, total = %d%n", algorithm.getClass().getSimpleName(), end - start, total);
        allTime+=(end - start);
    }

    private static long trial(PowerAlgorithm algorithm, int[] bases, int[] exponents) {
        long total = 0;
        for (int i = 0; i < bases.length; i++) {
            total += algorithm.power(bases[i], exponents[i]);
        }
        return total;
    }


    public static void main(String[] args) {
        System.out.println();
        int randCount = 100000000;
        int[] bases = new int[randCount];
        int[] exponents = new int[randCount];
        for (int i = 0; i < randCount; i++) {
            bases[i] = 2;
            exponents[i] = 31;
        }

        //PowerAlgorithm algorithm = new NaivePower();
        PowerAlgorithm algorithm = new FastPower();
        for(int i = 0; i < 10; i++) {
            benchmark(algorithm, bases, exponents);
        }
        System.out.println("\n\n"+allTime/10);




    }
}
