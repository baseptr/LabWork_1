package lt.esdc.shapes.factory;

import lt.esdc.shapes.entity.Oval;
import lt.esdc.shapes.entity.Point;
import lt.esdc.shapes.exception.InvalidShapeException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertEquals;

public class OvalFactoryTest {

    private OvalFactory ovalFactory;

    private static final Point VALID_P1 = new Point(0, 0);
    private static final Point VALID_P2 = new Point(10, 5);

    @BeforeMethod
    public void setUp() {
        ovalFactory = new OvalFactory();
    }

    @Test
    public void testCreateShape_validPoints_shouldReturnOval() throws InvalidShapeException {

        Oval oval = ovalFactory.createShape(VALID_P1, VALID_P2);
        assertNotNull(oval);
        assertEquals(oval.getPointX(), VALID_P1);
        assertEquals(oval.getPointY(), VALID_P2);
    }

    @DataProvider(name = "degeneratePointsProvider")
    public Object[][] degeneratePointsProvider() {
        return new Object[][]{
                {new Point(1.0, 5.0), new Point(10.0, 5.0)},
                {new Point(2.0, 1.0), new Point(2.0, 10.0)},
                {new Point(3.0, 4.0), new Point(3.0, 4.0)}
        };
    }

    @Test(dataProvider = "degeneratePointsProvider", expectedExceptions = InvalidShapeException.class)
    public void testCreateShape_degeneratePoints_shouldThrowInvalidShapeException(Point p1, Point p2) throws InvalidShapeException {
        ovalFactory.createShape(p1, p2);
    }

    @Test(expectedExceptions = InvalidShapeException.class)
    public void testCreateShape_nullPointOne_shouldThrowExceptionOrHandle() throws InvalidShapeException {
        throw new InvalidShapeException("Placeholder: Test for null points if factory handles it specifically");
    }
}
