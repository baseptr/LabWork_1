package lt.esdc.shapes.service;

import lt.esdc.shapes.entity.Oval;
import lt.esdc.shapes.entity.Point;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class OvalServiceTest {

    private OvalService ovalService;
    private static final double DELTA = 1e-9;


    private static final Oval RECTANGLE_OVAL = new Oval(1L, new Point(0, 0), new Point(10, 6));
    private static final Oval SQUARE_OVAL_CIRCLE = new Oval(2L, new Point(0, 0), new Point(10, 10));
    private static final Oval ANOTHER_OVAL = new Oval(3L, new Point(-5, -5), new Point(5, 5));
    private static final Oval FLAT_OVAL = new Oval(4L, new Point(0,0), new Point(20, 2));

    @BeforeMethod
    public void setUp() {
        ovalService = new OvalService();
    }

    @Test
    public void testGetSemiAxes_rectangleOval_shouldReturnCorrectSemiAxes() {
        double[] semiAxes = ovalService.getSemiAxes(RECTANGLE_OVAL);
        assertEquals(semiAxes.length, 2);
        assertEquals(semiAxes[0], 5.0, DELTA);
        assertEquals(semiAxes[1], 3.0, DELTA);
    }

    @Test
    public void testGetSemiAxes_circleOval_shouldReturnEqualSemiAxes() {
        double[] semiAxes = ovalService.getSemiAxes(SQUARE_OVAL_CIRCLE);
        assertEquals(semiAxes.length, 2);
        assertEquals(semiAxes[0], 5.0, DELTA);
        assertEquals(semiAxes[1], 5.0, DELTA);
    }

    @Test
    public void testCalculateArea_rectangleOval_shouldReturnCorrectArea() {
        double expectedArea = Math.PI * 5.0 * 3.0;
        double actualArea = ovalService.calculateArea(RECTANGLE_OVAL);
        assertEquals(actualArea, expectedArea, DELTA);
    }

    @Test
    public void testCalculateArea_circleOval_shouldReturnCorrectArea() {
        double expectedArea = Math.PI * 5.0 * 5.0;
        double actualArea = ovalService.calculateArea(SQUARE_OVAL_CIRCLE);
        assertEquals(actualArea, expectedArea, DELTA);
    }

    @Test
    public void testCalculatePerimeter_circleOval_shouldReturnCorrectPerimeter() {
        double expectedPerimeter = 2.0 * Math.PI * 5.0;
        double actualPerimeter = ovalService.calculatePerimeter(SQUARE_OVAL_CIRCLE);
        assertEquals(actualPerimeter, expectedPerimeter, DELTA);
    }

    @Test
    public void testCalculatePerimeter_flatOval_shouldUseRamanujanApproximation() {
        double a = 10.0, b = 1.0;
        double expectedPerimeter = Math.PI * (3 * (a + b) - Math.sqrt((3 * a + b) * (a + 3 * b)));
        double actualPerimeter = ovalService.calculatePerimeter(FLAT_OVAL);
        assertEquals(actualPerimeter, expectedPerimeter, 0.001);
    }


    @Test
    public void testIsCircle_givenCircle_shouldReturnTrue() {
        boolean isCircle = ovalService.isCircle(SQUARE_OVAL_CIRCLE);
        assertTrue(isCircle);
    }

    @Test
    public void testIsCircle_givenNonCircle_shouldReturnFalse() {
        boolean isCircle = ovalService.isCircle(RECTANGLE_OVAL);
        assertFalse(isCircle);
    }



}