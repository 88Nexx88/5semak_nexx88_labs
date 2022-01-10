import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


public class Program extends JComponent {

    static Queue<Double> all_result1 = new LinkedList();
    static Queue<Double> all_result2 = new LinkedList();
    static Queue<Double> all_result3 = new LinkedList();

    static int[] results_N;
    static String[] result_N;

    static int[] results2_N;
    static String[] result2_N;

    static int[] results3_N;
    static String[] result3_N;


    public static void max_f(int Y, int stage, int[][] variant) {
        int[] chain = new int[stage];
        int result = 0;
        if (stage == 1) {
            for (int i = 0; i < (Y) / variant[stage - 1][2] + 1; i++) {
                if (i * variant[stage - 1][2] <= Y) {
                    if (result < i * variant[stage - 1][1]) {
                        chain[0] = i;
                        result = i * variant[stage - 1][1];
                    }
                }
            }
            results_N[stage - 1] = result;
            String str = "";

            for (int i = 0; i < chain.length; i++) {
                if (i == chain.length - 1) {
                    str += chain[i];
                } else
                    str += chain[i] + ",";
            }
            result_N[stage - 1] = str;
        } else if (stage == 2) {
            for (int i = 0; i < (Y) / variant[stage - 1][2] + 1; i++) {
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
            results_N[stage - 1] = result;
            String str = "";

            for (int i = 0; i < chain.length; i++) {
                if (i == chain.length - 1) {
                    str += chain[i];
                } else
                    str += chain[i] + ",";
            }
            result_N[stage - 1] = str;
        } else if (stage == 3) {
            for (int i = 0; i < (Y) / variant[stage - 1][2] + 1; i++) {
                for (int j = 0; j < (Y) / variant[stage - 2][2] + 1; j++) {
                    for (int k = 0; k < (Y) / variant[stage - 3][2] + 1; k++) {
                        if (i * variant[stage - 1][2] + j * variant[stage - 2][2] +
                                k * variant[stage - 3][2] <= Y) {
                            if (result < (i * variant[stage - 1][1] + j * variant[stage - 2][1] +
                                    k * variant[stage - 3][1])) {
                                chain[0] = k;
                                chain[1] = j;
                                chain[2] = i;
                                result = (i * variant[stage - 1][1] + j * variant[stage - 2][1] +
                                        k * variant[stage - 3][1]);
                            }
                        }
                    }
                }
            }
            results_N[stage - 1] = result;
            String str = "";

            for (int i = 0; i < chain.length; i++) {
                if (i == chain.length - 1) {
                    str += chain[i];
                } else
                    str += chain[i] + ",";
            }
            result_N[stage - 1] = str;
        } else if (stage == 4) {
            for (int i = 0; i < (Y) / variant[stage - 1][2] + 1; i++) {
                for (int j = 0; j < (Y) / variant[stage - 2][2] + 1; j++) {
                    for (int k = 0; k < (Y) / variant[stage - 3][2] + 1; k++) {
                        for (int d = 0; d < (Y) / variant[stage - 4][2] + 1; d++) {
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

            results_N[stage - 1] = result;
            String str = "";

            for (int i = 0; i < chain.length; i++) {
                if (i == chain.length - 1) {
                    str += chain[i];
                } else
                    str += chain[i] + ",";
            }
            result_N[stage - 1] = str;
        } else if (stage == 5) {
            for (int i = 0; i < (Y) / variant[stage - 1][2] + 1; i++) {
                for (int j = 0; j < (Y) / variant[stage - 2][2] + 1; j++) {
                    for (int k = 0; k < (Y) / variant[stage - 3][2] + 1; k++) {
                        for (int d = 0; d < (Y) / variant[stage - 4][2] + 1; d++) {
                            for (int h = 0; h < (Y) / variant[stage - 4][2] + 1; h++) {
                                if ((i * variant[stage - 1][2] + j * variant[stage - 2][2] +
                                        k * variant[stage - 3][2] + d * variant[stage - 4][2]) <= Y) {
                                    if (result < (i * variant[stage - 1][1] + j * variant[stage - 2][1] +
                                            k * variant[stage - 3][1] + d * variant[stage - 4][1])) {
                                        chain[0] = h;
                                        chain[1] = d;
                                        chain[2] = k;
                                        chain[3] = j;
                                        chain[4] = i;
                                        result = (i * variant[stage - 1][1] + j * variant[stage - 2][1] +
                                                k * variant[stage - 3][1] + d * variant[stage - 4][1] + h * variant[stage - 5][1]);

                                    }
                                }
                            }
                        }
                    }
                }
            }

            results_N[stage - 1] = result;
            String str = "";

            for (int i = 0; i < chain.length; i++) {
                if (i == chain.length - 1) {
                    str += chain[i];
                } else
                    str += chain[i] + ",";
            }
            result_N[stage - 1] = str;
        }

    }


    public static void max_f2(int Y, int stage, int[][] variant) {
        int[] chain = new int[stage];
        int result = 0;
        if (stage == 1) {
            int c;
            if (((Y) / variant[stage - 1][2]) > variant[stage - 1][3])
                c = variant[stage - 1][3] + 1;
            else c = (Y) / variant[stage - 1][2] + 1;

            for (int i = 0; i < c; i++) {
                if (i * variant[stage - 1][2] <= Y) {
                    if (result < i * variant[stage - 1][1]) {
                        chain[0] = i;
                        result = i * variant[stage - 1][1];
                    }
                }
            }
            results2_N[stage - 1] = result;
            String str = "";

            for (int i = 0; i < chain.length; i++) {
                if (i == chain.length - 1) {
                    str += chain[i];
                } else
                    str += chain[i] + ",";
            }
            result2_N[stage - 1] = str;
        } else if (stage == 2) {
            int c;
            int c1;

            if (((Y) / variant[stage - 2][2]) > variant[stage - 2][3])
                c = variant[stage - 2][3] + 1;
            else c = (Y) / variant[stage - 2][2] + 1;

            if ((Y) / variant[stage - 1][2] > variant[stage - 1][3])
                c1 = variant[stage - 1][3] + 1;
            else c1 = (Y) / variant[stage - 1][2] + 1;
            for (int i = 0; i < c1; i++) {
                for (int j = 0; j < c; j++) {
                    if (i * variant[stage - 1][2] + j * variant[stage - 2][2] <= Y) {
                        if (result < (i * variant[stage - 1][1] + j * variant[stage - 2][1])) {
                            chain[0] = j;
                            chain[1] = i;
                            result = (i * variant[stage - 1][1] + j * variant[stage - 2][1]);
                        }
                    }
                }
            }
            results2_N[stage - 1] = result;
            String str = "";

            for (int i = 0; i < chain.length; i++) {
                if (i == chain.length - 1) {
                    str += chain[i];
                } else
                    str += chain[i] + ",";
            }
            result2_N[stage - 1] = str;
        } else if (stage == 3) {
            int c;
            int c1;
            int c2;
            if (((Y) / variant[stage - 3][2]) > variant[stage - 3][3])
                c = variant[stage - 3][3] + 1;
            else c = (Y) / variant[stage - 3][2] + 1;

            if ((Y) / variant[stage - 2][2] > variant[stage - 2][3])
                c1 = variant[stage - 2][3] + 1;
            else c1 = (Y) / variant[stage - 2][2] + 1;

            if ((Y) / variant[stage - 1][2] > variant[stage - 1][3])
                c2 = variant[stage - 1][3] + 1;
            else c2 = (Y) / variant[stage - 1][2] + 1;


            for (int i = 0; i < c2; i++) {
                for (int j = 0; j < c1; j++) {
                    for (int k = 0; k < c; k++) {
                        if (i * variant[stage - 1][2] + j * variant[stage - 2][2] +
                                k * variant[stage - 3][2] <= Y) {
                            if (result < (i * variant[stage - 1][1] + j * variant[stage - 2][1] +
                                    k * variant[stage - 3][1])) {
                                chain[0] = k;
                                chain[1] = j;
                                chain[2] = i;
                                result = (i * variant[stage - 1][1] + j * variant[stage - 2][1] +
                                        k * variant[stage - 3][1]);
                            }
                        }
                    }
                }
            }
            results2_N[stage - 1] = result;
            String str = "";

            for (int i = 0; i < chain.length; i++) {
                if (i == chain.length - 1) {
                    str += chain[i];
                } else
                    str += chain[i] + ",";
            }
            result2_N[stage - 1] = str;
        } else if (stage == 4) {
            int c;
            int c1;
            int c2;
            int c3;
            if (((Y) / variant[stage - 4][2]) > variant[stage - 4][3])
                c = variant[stage - 4][3] + 1;
            else c = (Y) / variant[stage - 4][2] + 1;

            if ((Y) / variant[stage - 3][2] > variant[stage - 3][3])
                c1 = variant[stage - 3][3] + 1;
            else c1 = (Y) / variant[stage - 3][2] + 1;

            if ((Y) / variant[stage - 2][2] > variant[stage - 2][3])
                c2 = variant[stage - 2][3] + 1;
            else c2 = (Y) / variant[stage - 2][2] + 1;

            if ((Y) / variant[stage - 1][2] > variant[stage - 1][3])
                c3 = variant[stage - 1][3] + 1;
            else c3 = (Y) / variant[stage - 1][2] + 1;

            for (int i = 0; i < c3; i++) {
                for (int j = 0; j < c2; j++) {
                    for (int k = 0; k < c1; k++) {
                        for (int d = 0; d < c; d++) {
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

            results2_N[stage - 1] = result;
            String str = "";

            for (int i = 0; i < chain.length; i++) {
                if (i == chain.length - 1) {
                    str += chain[i];
                } else
                    str += chain[i] + ",";
            }
            result2_N[stage - 1] = str;
        }

    }

    public static void max_f3(int Y, int stage, int[][] variant) {
        int[] chain = new int[stage];
        int result = 0;
        if (stage == 1) {
            for (int i = variant[stage - 1][3]; i <= variant[stage - 1][4]; i++) {
                if (i * variant[stage - 1][2] <= Y) {
                    if (result < i * variant[stage - 1][1]) {
                        chain[0] = i;
                        result = i * variant[stage - 1][1];
                    }
                }
            }
            results3_N[stage - 1] = result;
            String str = "";

            for (int i = 0; i < chain.length; i++) {
                if (i == chain.length - 1) {
                    str += chain[i];
                } else
                    str += chain[i] + ",";
            }
            result3_N[stage - 1] = str;
        } else if (stage == 2) {
            for (int i = variant[stage - 1][3]; i <= variant[stage - 1][4]; i++) {
                for (int j = variant[stage - 2][3]; j <= variant[stage - 2][4]; j++) {
                    if (i * variant[stage - 1][2] + j * variant[stage - 2][2] <= Y) {
                        if (result < (i * variant[stage - 1][1] + j * variant[stage - 2][1])) {
                            chain[0] = j;
                            chain[1] = i;
                            result = (i * variant[stage - 1][1] + j * variant[stage - 2][1]);
                        }
                    }
                }
            }
            results3_N[stage - 1] = result;
            String str = "";

            for (int i = 0; i < chain.length; i++) {
                if (i == chain.length - 1) {
                    str += chain[i];
                } else
                    str += chain[i] + ",";
            }
            result3_N[stage - 1] = str;
        } else if (stage == 3) {
            for (int i = variant[stage - 1][3]; i <= variant[stage - 1][4]; i++) {
                for (int j = variant[stage - 2][3]; j <= variant[stage - 2][4]; j++) {
                    for (int k = variant[stage - 3][3]; k <= variant[stage - 3][4]; k++) {
                        if (i * variant[stage - 1][2] + j * variant[stage - 2][2] +
                                k * variant[stage - 3][2] <= Y) {
                            if (result < (i * variant[stage - 1][1] + j * variant[stage - 2][1] +
                                    k * variant[stage - 3][1])) {
                                chain[0] = k;
                                chain[1] = j;
                                chain[2] = i;
                                result = (i * variant[stage - 1][1] + j * variant[stage - 2][1] +
                                        k * variant[stage - 3][1]);
                            }
                        }
                    }
                }
            }
            results3_N[stage - 1] = result;
            String str = "";

            for (int i = 0; i < chain.length; i++) {
                if (i == chain.length - 1) {
                    str += chain[i];
                } else
                    str += chain[i] + ",";
            }
            result3_N[stage - 1] = str;
        } else if (stage == 4) {
            for (int i = variant[stage - 1][3]; i <= variant[stage - 1][4]; i++) {
                for (int j = variant[stage - 2][3]; j <= variant[stage - 2][4]; j++) {
                    for (int k = variant[stage - 3][3]; k <= variant[stage - 3][4]; k++) {
                        for (int d = variant[stage - 4][3]; d <= variant[stage - 4][4]; d++) {
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

            results3_N[stage - 1] = result;
            String str = "";

            for (int i = 0; i < chain.length; i++) {
                if (i == chain.length - 1) {
                    str += chain[i];
                } else
                    str += chain[i] + ",";
            }
            result3_N[stage - 1] = str;
        }

    }


    public static void main(String[] args) throws IOException {

        int[][] variant1 = {
                {1, 50, 9},
                {2, 59, 10},
                {3, 70, 11},
                {4, 71, 13},
                {5, 69, 15}
        };
        results_N = new int[variant1.length];
        result_N = new String[variant1.length];
        int[][] variant1_1 = {
                {1, 50, 8},
                {2, 59, 9},
                {3, 70, 10},
                {4, 71, 12},
                {5, 69, 14}
        };
        int[][] variant1_2 = {
                {1, 50, 7},
                {2, 59, 8},
                {3, 70, 9},
                {4, 71, 11},
                {5, 69, 13}
        };

        int[] Y = new int[26];
        for (int i = 25; i <= 50; i++) {
            Y[i - 25] = i;
        }
        ArrayList<int[][]> zxc = new ArrayList<>();
        zxc.add(variant1);
        zxc.add(variant1_1);
        zxc.add(variant1_2);
        for (int k = 0; k < 3; k++) {
            for (int j = 0; j < Y.length; j++) {
                System.out.printf("Y = %d\n", Y[j]);
                for (int i = 1; i <= 5; i++) {
                    max_f(Y[j], i, zxc.get(k));
                }
                for (int i = 0; i < results_N.length; i++) {
                    System.out.printf("f%d -> %d, {%s}\n", i, results_N[i], result_N[i]);
                    all_result1.add((double) results_N[i]);
                }
                System.out.println();
            }
        }

        DefaultXYDataset xyDataset = new DefaultXYDataset();
        for (int i = 0; i < 3; i++) {
            int number = 10;
            double[][] series = new double[2][132 / 3];
            for (int j = 0; j < 132 / 3; j++) {
                series[1][j] = number;
                series[0][j] = all_result1.poll();
                if ((j + 1) % 4 == 0) number++;
            }
            String str = "Y-" + i;
            xyDataset.addSeries(str, series);
        }


        JFreeChart chart = ChartFactory.createXYLineChart("MOK", "C", "Y",
                xyDataset,
                PlotOrientation.HORIZONTAL,
                true, true, true);
        Plot plot = chart.getPlot();
        plot.setDrawingSupplier(new ChartDrawingSupplier());

        JFrame frame =
                new JFrame("MOK_lab2");
        frame.setLayout(new FlowLayout());
        frame.getContentPane().add(new ChartPanel(chart));

        frame.pack();
        frame.setSize(700, 500);
        frame.show();


        //pt. 5

        int[][] variant_2 = {
                {1, 10, 4, 3},
                {2, 15, 6, 2},
                {3, 13, 7, 2},
                {4, 14, 9, 3}
        };


        int[][] variant_2_1 = {
                {1, 10, 4, 4},
                {2, 15, 6, 3},
                {3, 13, 7, 3},
                {4, 14, 9, 4}
        };


        int[][] variant_2__1 = {
                {1, 10, 4, 5},
                {2, 15, 6, 4},
                {3, 13, 7, 4},
                {4, 14, 9, 5}
        };
        Y = new int[28];
        results2_N = new int[variant_2.length];
        result2_N = new String[variant_2.length];
        for (int i = 27; i <= 54; i++) {
            Y[i - 27] = i;
        }

        /*
        int[][] variant_2 = {
                {1, 50, 5, 2},
                {2, 70, 6, 2},
                {3, 60, 8, 2},
                {4, 79, 9, 3}
        };

         */
         /*
        int[][] variant_2_1 = {
                {1, 50, 5, 3},
                {2, 70, 6, 3},
                {3, 60, 8, 3},
                {4, 79, 9, 4}
        };

         */
        /*
        int[][] variant_2__1 = {
                {1, 50, 5, 1},
                {2, 70, 6, 1},
                {3, 60, 8, 1},
                {4, 79, 9, 2}
        };

         */

        zxc = new ArrayList<>();
        zxc.add(variant_2);
        zxc.add(variant_2_1);
        zxc.add(variant_2__1);
        System.out.println(Arrays.toString(Y));
        for (int k = 0; k < 3; k++) {
            for (int j = 0; j < Y.length; j++) {
                System.out.printf("Y = %d\n", Y[j]);
                for (int i = 1; i <= 4; i++) {
                    max_f2(Y[j], i, zxc.get(k));
                }
                for (int i = 0; i < results2_N.length; i++) {
                    System.out.printf("f%d -> %d, {%s}\n", i, results2_N[i], result2_N[i]);
                    all_result2.add((double) results2_N[i]);
                }
                System.out.println();
            }
        }

        System.out.println();
        xyDataset = new DefaultXYDataset();
        for (int i = 0; i < 3; i++) {
            int number = Y.length - 1;
            double[][] series = new double[2][Y.length * 4];
            for (int j = 0; j < Y.length * 4; j++) {
                series[1][j] = number;
                series[0][j] = all_result2.poll();
                if ((j + 1) % 4 == 0) number++;
            }

            String str = "Y-" + i;
            xyDataset.addSeries(str, series);
        }


        chart = ChartFactory.createXYLineChart("MOK", "C", "Y",
                xyDataset,
                PlotOrientation.HORIZONTAL,
                true, true, true);
        plot = chart.getPlot();
        plot.setDrawingSupplier(new ChartDrawingSupplier());

        frame =
                new JFrame("MOK_lab2");
        frame.setLayout(new FlowLayout());
        frame.getContentPane().add(new ChartPanel(chart));

        frame.pack();
        frame.setSize(700, 500);
        frame.show();


        //pt 9
        /*
        int[][] variant_3 = {
                {1, 50, 5, 1, 2},
                {2, 70, 6, 1, 2},
                {3, 60, 8, 1, 2},
                {4, 79, 9, 2, 4}
        };

        int[][] variant_3_1 = {
                {1, 50, 5, 2, 2},
                {2, 70, 6, 2, 2},
                {3, 60, 8, 2, 2},
                {4, 79, 9, 3, 4}
        };
        int[][] variant_3_2 = {
                {1, 50, 5, 1, 3},
                {2, 70, 6, 1, 3},
                {3, 60, 8, 1, 3},
                {4, 79, 9, 2, 5}
        };

         */
        int[][] variant_3 = {
                {1, 10, 4, 2, 3},
                {2, 15, 6, 1, 2},
                {3, 13, 7, 1, 2},
                {4, 24, 9, 1, 3}
        };

        int[][] variant_3_1 = {
                {1, 10, 4, 3, 3},
                {2, 15, 6, 2, 2},
                {3, 13, 7, 2, 2},
                {4, 24, 9, 2, 3}
        };

        Y = new int[13];
        results3_N = new int[variant_3.length];
        result3_N = new String[variant_3.length];
        for (int i = 62; i <= 74; i++) {
            Y[i - 62] = i;
        }
        zxc = new ArrayList<>();
        zxc.add(variant_3);
        zxc.add(variant_3_1);
        //zxc.add(variant_3_2);
        System.out.println(Arrays.toString(Y));

        for (int k = 0; k < 2; k++) {
            for (int j = 0; j < Y.length; j++) {
                System.out.printf("Y = %d\n", Y[j]);
                for (int i = 1; i <= 4; i++) {
                    max_f3(Y[j], i, zxc.get(k));
                }
                for (int i = 0; i < results3_N.length; i++) {
                    System.out.printf("f%d -> %d, {%s}\n", i, results3_N[i], result3_N[i]);
                    all_result3.add((double) results3_N[results3_N.length - 1]);
                }
                System.out.println();
            }
        }

        System.out.println();
        xyDataset = new DefaultXYDataset();
        for (int i = 0; i < 2; i++) {
            int number = Y.length - 1;
            double[][] series = new double[2][Y.length * 4];
            for (int j = 0; j < Y.length * 4; j++) {
                series[1][j] = number;
                series[0][j] = all_result3.poll();
                if ((j + 1) % 4 == 0) number++;
            }

            String str = "Y-" + i;
            xyDataset.addSeries(str, series);
        }


        chart = ChartFactory.createXYLineChart("MOK", "C", "Y",
                xyDataset,
                PlotOrientation.HORIZONTAL,
                true, true, true);
        plot = chart.getPlot();
        plot.setDrawingSupplier(new ChartDrawingSupplier());

        frame =
                new JFrame("MOK_lab2");
        frame.setLayout(new FlowLayout());
        frame.getContentPane().add(new ChartPanel(chart));

        frame.pack();
        frame.setSize(700, 500);
        frame.show();


    }


    public static class ChartDrawingSupplier extends DefaultDrawingSupplier {

        public Paint[] paintSequence;
        public int paintIndex;
        public int fillPaintIndex;

        {
            paintSequence = new Paint[]{
                    new Color(227, 26, 28),
                    new Color(000, 102, 204),
                    new Color(000, 000, 000),
                    new Color(102, 51, 0),
                    new Color(156, 136, 48),
                    new Color(153, 204, 102),
            };
        }

        @Override
        public Paint getNextPaint() {
            Paint result
                    = paintSequence[paintIndex % paintSequence.length];
            paintIndex++;
            return result;
        }


        @Override
        public Paint getNextFillPaint() {
            Paint result
                    = paintSequence[fillPaintIndex % paintSequence.length];
            fillPaintIndex++;
            return result;
        }


    }
}
