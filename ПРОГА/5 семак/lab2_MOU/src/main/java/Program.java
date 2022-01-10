import java.util.ArrayList;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Program {

    int m = 4;
    int n = 11;


    static int[] results_N;
    static String[] result_N;
    static int[] results_N_1;
    static String[] result_N_1;
    static int[] results_N_2;
    static String[] result_N_2;

    public static void max_f(int Y, int stage, int[][] variant){
        int[] chain = new int[stage];
        int result = 0;
        if(stage == 1) {
            for (int i = 0; i < (Y) / variant[stage - 1][2] + 1; i++) {
                if (i * variant[stage - 1][2] <= Y) {
                    if (result < i * variant[stage - 1][1]) {
                        chain[0] = i;
                        result = i * variant[stage - 1][1];
                    }
                }
            }
            results_N[stage-1] = result;
            String str = "";

            for (int i = 0; i < chain.length; i++) {
                if(i == chain.length-1){
                    str+=chain[i];
                }
                else
                    str+=chain[i]+",";
            }
            result_N[stage-1] = str;
        }
        else if(stage == 2){
            for (int i = 0; i < (Y) / variant[stage-1][2] + 1; i++) {
                for (int j = 0; j < (Y) / variant[stage - 2][2] + 1; j++) {
                    if (i * variant[stage - 1][2] + j * variant[stage - 2][2] <= Y) {
                        if (result < (i * variant[stage - 1][1] + j * variant[stage - 2][1])) {
                            chain[0] = j;
                            chain[1] = i;
                            result = (i * variant[stage - 1][1] + j * variant[stage - 2][1]);
                        }
                    }
                }
            }
            results_N[stage-1] = result;
            String str = "";

            for (int i = 0; i < chain.length; i++) {
                if(i == chain.length-1){
                    str+=chain[i];
                }
                else
                    str+=chain[i]+",";
            }
            result_N[stage-1] = str;
        }
        else if(stage == 3){
            for (int i = 0; i < (Y) / variant[stage-1][2] + 1; i++) {
                for (int j = 0; j < (Y) / variant[stage - 2][2] + 1; j++) {
                    for (int k = 0; k < (Y) / variant[stage - 3][2] + 1; k++) {
                        if (i * variant[stage - 1][2] + j * variant[stage - 2][2] +
                                k*variant[stage - 3][2] <= Y) {
                            if (result < (i * variant[stage - 1][1] + j * variant[stage - 2][1]+
                                    k*variant[stage - 3][1])) {
                                chain[0] = k;
                                chain[1] = j;
                                chain[2] = i;
                                result = (i * variant[stage - 1][1] + j * variant[stage - 2][1]+
                                        k*variant[stage - 3][1]);
                            }
                        }
                    }
                }
            }
            results_N[stage-1] = result;
            String str = "";

            for (int i = 0; i < chain.length; i++) {
                if(i == chain.length-1){
                    str+=chain[i];
                }
                else
                    str+=chain[i]+",";
            }
            result_N[stage-1] = str;
        }
        else if(stage == 4){
            for (int i = 0; i < (Y) / variant[stage-1][2] + 1; i++) {
                for (int j = 0; j < (Y) / variant[stage - 2][2] + 1; j++) {
                    for(int k = 0; k < (Y) / variant[stage - 3][2] + 1; k++) {
                        for(int d = 0; d < (Y) / variant[stage - 3][2]+1; d++) {
                            if ((i * variant[stage - 1][2] + j * variant[stage - 2][2] +
                                    k * variant[stage - 3][2] + d * variant[stage - 4][2]) <= Y) {
                                if (result < (i * variant[stage - 1][1] + j * variant[stage - 2][1] +
                                        k * variant[stage - 3][1] + d * variant[stage - 4][1])) {
                                    chain[0] = d;
                                    chain[1] = k;
                                    chain[2] = j;
                                    chain[3] = i;
                                    result = (i * variant[stage - 1][1] + j * variant[stage - 2][1] +
                                            k * variant[stage - 3][1] + d * variant[stage - 4][1]);
                                }
                            }
                        }
                    }
                }
            }

            results_N[stage-1] = result;
            String str = "";

            for (int i = 0; i < chain.length; i++) {
                if(i == chain.length-1){
                    str+=chain[i];
                }
                else
                    str+=chain[i]+",";
            }
            result_N[stage-1] = str;
        }

    }



    public static void main(String[] args){
        int[][] variant1 = {
                {1, 21, 3},
                {2, 25, 4},
                {3, 28, 6},
                {4, 31, 5}
        };
        results_N = new int[variant1.length];
        result_N = new String[variant1.length];
        int[][] variant1_1 = {
                {1, 21, 2},
                {2, 25, 3},
                {3, 28, 5},
                {4, 31, 4}
        };
        results_N_1 = new int[variant1_1.length];
        result_N_1 = new String[variant1_1.length];
        int[][] variant1_2 = {
                {1, 21, 1},
                {2, 25, 2},
                {3, 28, 4},
                {4, 31, 3}
        };
        results_N_2 = new int[variant1_2.length];
        result_N_2 = new String[variant1_2.length];
        int[] Y = new int[11];
        for(int i = 10; i <= 20; i++){
            Y[i-10] = i;
        }
        ArrayList<int[][]> zxc = new ArrayList<>();
        zxc.add(variant1);
        zxc.add(variant1_1);
        zxc.add(variant1_2);
        for(int k =0; k < 3; k++) {
            for (int j = 0; j < Y.length; j++) {
                System.out.printf("Y = %d\n", Y[j]);
                for (int i = 1; i <= 4; i++) {
                    max_f(Y[j], i, zxc.get(k));
                }
                for (int i = 0; i < results_N.length; i++) {
                    System.out.printf("f%d -> %d, {%s}\n", i, results_N[i], result_N[i]);
                }
                System.out.println();
            }
        }


    }
}

