package It.esdc.shapes.repostory;

import It.esdc.shapes.entity.Oval;
import It.esdc.shapes.entity.Point;
import It.esdc.shapes.pool.ShapeWarehouse; // Нужен для проверки взаимодействия
import It.esdc.shapes.repository.ShapeRepository;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

public class ShapeRepositoryTest {

    private ShapeRepository repository;
    private Oval oval1;
    private Oval oval2;
    private Oval oval3;

    @BeforeMethod // Выполняется перед каждым тестовым методом
    public void setUp() {
        repository = new ShapeRepository();
        // Создаем овалы с разными характеристиками для сортировки
        // ID генерируются случайно, полагаемся на разницу в area/perimeter
        // oval1: p1(0,0), p2(2,2) -> a=1, b=1 (Круг) -> Area=PI, P=2*PI
        oval1 = new Oval(new Point(0, 0), new Point(2, 2));
        // oval2: p1(0,0), p2(4,2) -> a=2, b=1 -> Area=2*PI, P ~= 2*PI*sqrt((4+1)/2) = 2*PI*sqrt(2.5)
        oval2 = new Oval(new Point(0, 0), new Point(4, 2));
        // oval3: p1(0,0), p2(2,4) -> a=1, b=2 -> Area=2*PI, P ~= 2*PI*sqrt((1+4)/2) = 2*PI*sqrt(2.5)
        oval3 = new Oval(new Point(0, 0), new Point(2, 4)); // Area как у oval2, Perimeter как у oval2

        // Добавляем в репозиторий
        repository.add(oval1);
        repository.add(oval2);
        repository.add(oval3);
    }

    @Test
    public void testAdd() {
        int initialSize = repository.getAll().size(); // Должно быть 3
        Oval newOval = new Oval(new Point(10, 10), new Point(12, 12));
        repository.add(newOval);
        Assert.assertEquals(repository.getAll().size(), initialSize + 1, "Размер должен увеличиться после добавления");
        Assert.assertTrue(repository.getAll().contains(newOval), "Репозиторий должен содержать добавленный овал");
    }

    @Test
    public void testAddDuplicate() {
        int initialSize = repository.getAll().size(); // 3
        repository.add(oval1); // Пытаемся добавить существующий
        Assert.assertEquals(repository.getAll().size(), initialSize, "Размер не должен меняться при добавлении дубликата");
    }

    @Test
    public void testAddNull() {
        int initialSize = repository.getAll().size(); // 3
        repository.add(null); // Пытаемся добавить null
        Assert.assertEquals(repository.getAll().size(), initialSize, "Размер не должен меняться при добавлении null");
    }


    @Test
    public void testDelete() {
        int initialSize = repository.getAll().size(); // 3
        long idToDelete = oval2.getId();
        repository.delete(oval2);
        Assert.assertEquals(repository.getAll().size(), initialSize - 1, "Размер должен уменьшиться после удаления");
        Assert.assertFalse(repository.getAll().contains(oval2), "Репозиторий не должен содержать удаленный овал");
        //Проверяем, что данные удалены с дубликата
        Assert.assertNull(ShapeWarehouse.getInstance().getArea(idToDelete), "Площадь должна быть null в warehouse после удаления");
        Assert.assertNull(ShapeWarehouse.getInstance().getPerimeter(idToDelete), "Периметр должен быть null в warehouse после удаления");
    }

    @Test
    public void testDeleteNonExistent() {
        int initialSize = repository.getAll().size(); // 3
        Oval nonExistent = new Oval(new Point(100,100), new Point(101,101));
        repository.delete(nonExistent);
        Assert.assertEquals(repository.getAll().size(), initialSize, "Размер не должен меняться при удалении несуществующего овала");
    }

    @Test
    public void testDeleteNull() {
        int initialSize = repository.getAll().size(); // 3
        repository.delete(null);
        Assert.assertEquals(repository.getAll().size(), initialSize, "Размер не должен меняться при удалении null");
    }

    @Test
    public void testGetAll() {
        List<Oval> allOvals = repository.getAll();
        Assert.assertEquals(allOvals.size(), 3);
        Assert.assertTrue(allOvals.contains(oval1));
        Assert.assertTrue(allOvals.contains(oval2));
        Assert.assertTrue(allOvals.contains(oval3));
        // Проверка, что возвращается копия
        allOvals.clear();
        Assert.assertEquals(repository.getAll().size(), 3, "Внутренний список не должен изменяться при очистке возвращаемого списка");
    }

    @Test
    public void testGetById() {
        Oval foundOval = repository.getById(oval1.getId());
        Assert.assertNotNull(foundOval);
        Assert.assertEquals(foundOval.getId(), oval1.getId());

        Oval notFoundOval = repository.getById(-999L); // Несуществующий ID
        Assert.assertNull(notFoundOval);
    }


    // Тесты сортировки - проверяем порядок ID, т.к. area/perimeter могут совпадать
    @Test
    public void testSortById() {
        List<Oval> sorted = repository.sortById();
        Assert.assertEquals(sorted.size(), 3);
        // Проверяем, что ID идут по возрастанию
        Assert.assertTrue(sorted.get(0).getId() <= sorted.get(1).getId(), "Сортировка по ID неверна [0] vs [1]");
        Assert.assertTrue(sorted.get(1).getId() <= sorted.get(2).getId(), "Сортировка по ID неверна [1] vs [2]");
    }

    @Test
    public void testSortByArea() {
        ShapeWarehouse warehouse = ShapeWarehouse.getInstance();
        List<Oval> sorted = repository.sortByArea();
        Assert.assertEquals(sorted.size(), 3);
        // Получаем площади из Warehouse для проверки порядка
        double area0 = warehouse.getArea(sorted.get(0).getId());
        double area1 = warehouse.getArea(sorted.get(1).getId());
        double area2 = warehouse.getArea(sorted.get(2).getId());
        Assert.assertTrue(area0 <= area1, "Сортировка по площади неверна [0] vs [1]");
        Assert.assertTrue(area1 <= area2, "Сортировка по площади неверна [1] vs [2]");
    }

    @Test
    public void testSortByPerimeter() {
        ShapeWarehouse warehouse = ShapeWarehouse.getInstance();
        List<Oval> sorted = repository.sortByPerimeter();
        Assert.assertEquals(sorted.size(), 3);
        // Получаем периметры из Warehouse для проверки порядка
        double p0 = warehouse.getPerimeter(sorted.get(0).getId());
        double p1 = warehouse.getPerimeter(sorted.get(1).getId());
        double p2 = warehouse.getPerimeter(sorted.get(2).getId());
        Assert.assertTrue(p0 <= p1, "Сортировка по периметру неверна [0] vs [1]");
        Assert.assertTrue(p1 <= p2, "Сортировка по периметру неверна [1] vs [2]");
    }

    // Тест взаимодействия с Warehouse при изменении Oval
    @Test
    public void testWarehouseUpdateOnOvalChange() {
        ShapeWarehouse warehouse = ShapeWarehouse.getInstance();
        long ovalId = oval1.getId(); // Берем круг
        double initialArea = warehouse.getArea(ovalId);
        double initialPerimeter = warehouse.getPerimeter(ovalId);

        //Assert.assertNotNull(initialArea);
        //Assert.assertNotNull(initialPerimeter);

        // Изменяем овал так, чтобы он перестал быть кругом
        Point currentP2 = oval1.getPointY(); // (2,2)
        oval1.setPointY(new Point(currentP2.x() + 2, currentP2.y())); // Стало (4,2)

        // Проверяем, что метрики в Warehouse изменились
        double updatedArea = warehouse.getArea(ovalId);
        double updatedPerimeter = warehouse.getPerimeter(ovalId);

        //Assert.assertNotNull(updatedArea);
        //Assert.assertNotNull(updatedPerimeter);
        Assert.assertNotEquals(updatedArea, initialArea, 1E-9, "Площадь в warehouse должна измениться");
        Assert.assertNotEquals(updatedPerimeter, initialPerimeter, 1E-9, "Периметр в warehouse должен измениться");
    }
}