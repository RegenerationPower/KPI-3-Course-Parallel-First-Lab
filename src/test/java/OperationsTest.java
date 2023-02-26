import org.junit.jupiter.api.Test;
import voznytsia.lab1.Operations;

import static org.junit.jupiter.api.Assertions.*;

public class OperationsTest {
    private final Operations operations = new Operations();

    @Test
    public void testMultiplyMatrix() {
        double[][] a = {{1, 2}, {3, 4}};
        double[][] b = {{5, 6}, {7, 8}};
        double[][] expected = {{19, 22}, {43, 50}};
        double[][] result = operations.multiplyMatrix(a, b);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testInvalidDimensionsForMultiplyMatrix() {
        double[][] a = {{1, 2}, {3, 4}};
        double[][] b = {{5, 6}};
        assertThrows(IllegalArgumentException.class, () -> operations.multiplyMatrix(a, b));
    }

    @Test
    public void testFindMinValue() {
        double[] arr = {1, 2, 3, 4, 5};
        double expected = 1;
        double result = operations.findMinValue(arr);
        assertEquals(expected, result, 0.0);
    }

    @Test
    public void testSubtractMatrix() {
        double[][] a = {{1, 2}, {3, 4}};
        double[][] b = {{5, 6}, {7, 8}};
        double[][] expected = {{-4, -4}, {-4, -4}};
        double[][] result = operations.subtractMatrix(a, b);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testInvalidDimensionsForSubtractMatrix() {
        double[][] a = {{1, 2}, {3, 4}};
        double[][] b = {{5, 6}};
        assertThrows(IllegalArgumentException.class, () -> operations.subtractMatrix(a, b));
    }

    @Test
    public void testGenerateRandomMatrix() {
        double[][] matrix = operations.generateRandomMatrix(2, 3);
        assertEquals(2, matrix.length);
        assertEquals(3, matrix[0].length);
    }

    @Test
    public void testGenerateRandomArray() {
        double[] array = operations.generateRandomArray(5);
        assertEquals(5, array.length);
    }
}
