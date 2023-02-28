package voznytsia.lab1;
/*
    IO-04 Voznytsia Dmytro
    variant - 27
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;

public class Main {
    private static final String INPUT_FILENAME = "src\\main\\java\\voznytsia\\lab1\\input.txt";
    private static final String OUTPUT_FILENAME = "src\\main\\java\\voznytsia\\lab1\\output.txt";

    public static void main(String[] args) {
        long startTime = System.nanoTime();
        Operations operations = new Operations();
        try {
            String inputFilePath = new File(INPUT_FILENAME).getAbsolutePath();
            Scanner scanner = new Scanner(new File(inputFilePath));
            int sizeMM = scanner.nextInt();
            int sizeME = scanner.nextInt();
            int sizeMX = scanner.nextInt();
            int sizeB = scanner.nextInt();
            int sizeD = scanner.nextInt();
            double q = scanner.nextDouble();
            scanner.close();

            double[][] MM = operations.generateRandomMatrix(sizeMM, sizeMM);
            double[][] ME = operations.generateRandomMatrix(sizeME, sizeME);
            double[][] MX = operations.generateRandomMatrix(sizeMX, sizeMX);
            double[] B = operations.generateRandomArray(sizeB);
            double[] D = operations.generateRandomArray(sizeD);

            String outputFilePath = new File(OUTPUT_FILENAME).getAbsolutePath();
            PrintWriter writer = new PrintWriter(outputFilePath);

            AtomicReference<double[][]> result1 = new AtomicReference<>(new double[][]{{0.0}});
            AtomicReference<double[][]> result2 = new AtomicReference<>(new double[][]{{0.0}});
            AtomicReference<double[]> result3 = new AtomicReference<>(new double[]{0.0});

            Object lock1 = new Object();
            Object lock2 = new Object();
            Object lock3 = new Object();

            Thread t1 = new Thread(() -> {
                double[][] r = operations.multiplyMatrix(MM, operations.subtractMatrix(ME, MX));
                synchronized (lock1) {
                    result1.set(r);
                    System.out.println("\nResult 1: " + Arrays.deepToString(r));
                    writer.println("\nResult 1: " + Arrays.deepToString(result1.get()));
                }
            });

            Thread t2 = new Thread(() -> {
                double[][] r = operations.multiplyMatrixByScalar(operations.multiplyMatrix(ME, MX), q);
                synchronized (lock2) {
                    result2.set(r);
                    System.out.println("\nResult 2: " + Arrays.deepToString(r));
                    writer.println("\nResult 2: " + Arrays.deepToString(result2.get()));
                }
            });

            Thread t3 = new Thread(() -> {
                double[] r = operations.multiplyVectorByScalar(D, operations.findMinValue(B));
                synchronized (lock3) {
                    result3.set(r);
                    System.out.println("\nResult 3: " + Arrays.toString(r));
                    writer.println("\nResult 3: " + Arrays.toString(result3.get()));
                }
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

            double[][] MA = new double[result1.get().length][result1.get()[0].length];
            for (int i = 0; i < MA.length; i++) {
                for (int j = 0; j < MA[0].length; j++) {
                    MA[i][j] = result1.get()[i][j] + result2.get()[i][j];
                }
            }

            double[] Y = operations.multiplyVectorByMatrix(B, ME);
            System.out.println("\n\nY=" + Arrays.toString(Y));
            for (int i = 0; i < Y.length; i++) {
                Y[i] = Y[i] + result3.get()[i];
            }

            System.out.println("\nFinal Result: \nMA=" + Arrays.deepToString(MA) + "\n\nY=" + Arrays.toString(Y));
            writer.println("\nFinal Result: \nMA=" + Arrays.deepToString(MA) + "\n\nY=" + Arrays.toString(Y));

            long endTime = System.nanoTime();
            long resultTime = (endTime - startTime);
            System.out.println("\nDuration: " + resultTime + " ns");
            writer.println("\nDuration: " + resultTime + " ns");
            writer.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}