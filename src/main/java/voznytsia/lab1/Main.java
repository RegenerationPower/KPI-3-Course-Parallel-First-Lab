package voznytsia.lab1;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

public class Main {
    private static final int ROWS_MM = 2;
    private static final int COLS_MM = 2;
    private static final int ROWS_ME = 2;
    private static final int COLS_ME = 2;
    private static final int ROWS_MX = 2;
    private static final int COLS_MX = 2;
    private static final int SIZE_B = 3;
    private static final int SIZE_D = 4;
    private static final double Q = 5.0;

    public static void main(String[] args) {
        Operations operations = new Operations();
        double[][] MM = operations.generateRandomMatrix(ROWS_MM, COLS_MM);
        double[][] ME = operations.generateRandomMatrix(ROWS_ME, COLS_ME);
        double[][] MX = operations.generateRandomMatrix(ROWS_MX, COLS_MX);
        double[] B = operations.generateRandomArray(SIZE_B);
        double[] D = operations.generateRandomArray(SIZE_D);

        AtomicReference<double[][]> result1 = new AtomicReference<>(new double[][]{{0.0}});
        AtomicReference<Double> result2 = new AtomicReference<>(0.0);
        AtomicReference<Double> result3 = new AtomicReference<>(0.0);

        Thread t1 = new Thread(() -> {
            double[][] r = operations.multiplyMatrix(MM, operations.subtractMatrix(ME, MX));
            result1.set(r);
            System.out.println("Result 1: " + Arrays.deepToString(r));
        });

        Thread t2 = new Thread(() -> {
            double r = operations.multiplyMatrix(ME, MX)[0][0] * Q;
            result2.set(r);
            System.out.println("Result 2: " + r);
        });

        Thread t3 = new Thread(() -> {
            double r = D[0] * operations.findMinValue(B);
            result3.set(r);
            System.out.println("Result 3: " + r);
        });

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        double[][] MA = new double[][]{{result1.get()[0][0] + result2.get()}};
        double[] Y = new double[]{(B[0] * ME[0][0]) * result3.get()};
        System.out.println("Final Result: MA=" + Arrays.deepToString(MA) + ", Y=" + Arrays.toString(Y));
    }

}