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
            String outputFilePath = new File(OUTPUT_FILENAME).getAbsolutePath();
            PrintWriter writer = new PrintWriter(outputFilePath);
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

            AtomicReference<double[][]> result1 = new AtomicReference<>(new double[][]{{0.0}});
            AtomicReference<Double> result2 = new AtomicReference<>(0.0);
            AtomicReference<Double> result3 = new AtomicReference<>(0.0);

            Thread t1 = new Thread(() -> {
                double[][] r = operations.multiplyMatrix(MM, operations.subtractMatrix(ME, MX));
                result1.set(r);
                System.out.println("\nResult 1: " + Arrays.deepToString(r));
                writer.println("\nResult 1: " + Arrays.deepToString(result1.get()));
            });

            Thread t2 = new Thread(() -> {
                double r = operations.multiplyMatrix(ME, MX)[0][0] * q;
                result2.set(r);
                System.out.println("\nResult 2: " + r);
                writer.println("\nResult 2: " + result2.get());
            });

            Thread t3 = new Thread(() -> {
                double r = D[0] * operations.findMinValue(B);
                result3.set(r);
                System.out.println("\nResult 3: " + r);
                writer.println("\nResult 3: " + result3.get());
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
            System.out.println("\nFinal Result: MA=" + Arrays.deepToString(MA) + ", Y=" + Arrays.toString(Y));
            writer.println("\nFinal Result: MA=" + Arrays.deepToString(MA) + ", Y=" + Arrays.toString(Y));

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