package lt.esdc.shapes.repository;

import lt.esdc.shapes.entity.Oval;
import lt.esdc.shapes.entity.Point;
import lt.esdc.shapes.service.OvalService;
import lt.esdc.shapes.specifications.IsCircle;
import lt.esdc.shapes.specifications.Specification;
import lt.esdc.shapes.warehouse.ShapeWarehouse;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.*;

import static org.testng.Assert.*;

public class ShapeRepositoryTest {

    private ShapeRepository shapeRepository;
    private OvalService ovalService;
    private ShapeWarehouse warehouseInstance;


    private static final Oval OVAL_1 = new Oval(1L, new Point(0, 0), new Point(10, 5));
    private static final Oval OVAL_2_CIRCLE = new Oval(2L, new Point(0, 0), new Point(8, 8));
    private static final Oval OVAL_3 = new Oval(3L, new Point(-1, -1), new Point(1, 1));

    @BeforeMethod
    public void setUp() {
        ovalService = new OvalService();
        shapeRepository = ShapeRepository.getInstance(ovalService);
        warehouseInstance = ShapeWarehouse.getInstance();
        clearRepositoryAndWarehouseState();
    }

    @AfterMethod
    public void tearDown() {
        clearRepositoryAndWarehouseState();
    }

    private void clearRepositoryAndWarehouseState() {
        if (shapeRepository != null) {
            List<Oval> currentOvals = new ArrayList<>(shapeRepository.getAll());
            for (Oval oval : currentOvals) {
                shapeRepository.remove(oval.getId());
            }
        }
        if (warehouseInstance != null) {
            Map<Long, ShapeWarehouse.ShapeParameters> currentParams = new HashMap<>(warehouseInstance.getAllParameters());
            for (Long id : currentParams.keySet()) {
                warehouseInstance.removeParameters(id);
            }
        }
        if (shapeRepository != null) {
            assertTrue(shapeRepository.getAll().isEmpty(), "Repository should be empty after clearing state.");
        }
        if (warehouseInstance != null) {
            assertTrue(warehouseInstance.getAllParameters().isEmpty(), "Warehouse should be empty after clearing state.");
        }
    }


    @Test
    public void testAdd_singleOval_shouldContainOval() {
        shapeRepository.add(OVAL_1);
        Optional<Oval> retrievedOval = shapeRepository.getById(OVAL_1.getId());
        assertTrue(retrievedOval.isPresent());
        assertEquals(retrievedOval.get(), OVAL_1);
    }

    @Test
    public void testAdd_singleOval_shouldUpdateWarehouse() {
        shapeRepository.add(OVAL_1);
        assertTrue(warehouseInstance.getParameters(OVAL_1.getId()).isPresent());
    }


    @Test
    public void testRemove_existingOval_shouldNotContainOval() {
        shapeRepository.add(OVAL_1);
        boolean removed = shapeRepository.remove(OVAL_1.getId());
        assertTrue(removed);
        assertFalse(shapeRepository.getById(OVAL_1.getId()).isPresent());
    }

    @Test
    public void testRemove_existingOval_shouldRemoveFromWarehouse() {
        shapeRepository.add(OVAL_1);
        shapeRepository.remove(OVAL_1.getId());
        assertFalse(warehouseInstance.getParameters(OVAL_1.getId()).isPresent());
    }

    @Test
    public void testRemove_nonExistingOval_shouldReturnFalse() {
        boolean removed = shapeRepository.remove(999L);
        assertFalse(removed);
    }

    @Test
    public void testGetById_existingOval_shouldReturnOval() {
        shapeRepository.add(OVAL_1);
        Optional<Oval> result = shapeRepository.getById(OVAL_1.getId());
        assertTrue(result.isPresent());
        assertEquals(result.get(), OVAL_1);
    }

    @Test
    public void testGetById_nonExistingOval_shouldReturnEmpty() {
        Optional<Oval> result = shapeRepository.getById(999L);
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetAll_afterAddingOvals_shouldReturnAllOvals() {
        shapeRepository.add(OVAL_1);
        shapeRepository.add(OVAL_2_CIRCLE);
        List<Oval> allOvals = shapeRepository.getAll();
        assertEquals(allOvals.size(), 2);
        assertTrue(allOvals.contains(OVAL_1));
        assertTrue(allOvals.contains(OVAL_2_CIRCLE));
    }

    @Test
    public void testQuery_withSpecification_shouldReturnMatchingOvals() {
        shapeRepository.add(OVAL_1);
        shapeRepository.add(OVAL_2_CIRCLE);
        shapeRepository.add(OVAL_3);
        Specification<Oval> isCircleSpec = new IsCircle(ovalService);
        List<Oval> circles = shapeRepository.query(isCircleSpec);
        assertEquals(circles.size(), 2);
        assertTrue(circles.contains(OVAL_2_CIRCLE));
        assertTrue(circles.contains(OVAL_3));
    }

    @Test
    public void testSortById_ascending_shouldReturnSortedList() {
        shapeRepository.add(OVAL_3); // id 3
        shapeRepository.add(OVAL_1); // id 1
        shapeRepository.add(OVAL_2_CIRCLE); // id 2
        List<Oval> sortedOvals = shapeRepository.sortById(true);
        assertEquals(sortedOvals.get(0).getId(), OVAL_1.getId());
        assertEquals(sortedOvals.get(1).getId(), OVAL_2_CIRCLE.getId());
        assertEquals(sortedOvals.get(2).getId(), OVAL_3.getId());
    }
    @Test
    public void testAdd_duplicateIdOval_shouldNotAdd() {
        // Given
        shapeRepository.add(OVAL_1);
        int initialSize = shapeRepository.getAll().size();
        Oval ovalWithSameId = new Oval(OVAL_1.getId(), new Point(100,100), new Point(200,200));
        shapeRepository.add(ovalWithSameId);
        assertEquals(shapeRepository.getAll().size(), initialSize, "Repository size should not change when adding duplicate ID");
        Optional<Oval> retrieved = shapeRepository.getById(OVAL_1.getId());
        assertTrue(retrieved.isPresent());
        assertEquals(retrieved.get().getPointX(), OVAL_1.getPointX());
    }
}