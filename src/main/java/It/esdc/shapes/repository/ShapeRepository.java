package It.esdc.shapes.repository;

import It.esdc.shapes.entity.Oval;
import It.esdc.shapes.pool.ShapeWarehouse;
import It.esdc.shapes.service.OvalService;
import It.esdc.shapes.specifications.Specification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

public class ShapeRepository {
    private static final Logger logger = LoggerFactory.getLogger(ShapeRepository.class);

    private final List<Oval> ovals = new ArrayList<>();
    private final OvalService service = new OvalService(); // Один экземпляр сервиса
    private final ShapeWarehouse warehouse = ShapeWarehouse.getInstance(); // Получаем экземпляр Warehouse

    public void add(Oval oval) {
        if (oval == null) {
            logger.warn("Попытка добавить null овал в репозиторий.");
            return;
        }
        // Проверка на дубликаты по ID
        if (!ovals.contains(oval)) {
            ovals.add(oval);
            logger.info("Овал id={} добавлен в репозиторий.", oval.getId());

            // Добавляем Warehouse как наблюдателя
            oval.addObserver(warehouse);
            // Вызываем update вручную для инициализации данных в Warehouse
            warehouse.update(oval, null); // 'null' в качестве аргумента, т.к. он не используется
        } else {
            logger.warn("Попытка добавить дубликат овала id={} в репозиторий.", oval.getId());
        }
    }

    public void delete(Oval oval) {
        if (oval == null) {
            logger.warn("Попытка удалить null овал из репозитория.");
            return;
        }
        boolean removed = ovals.remove(oval); // Использует Oval.equals() (сравнение по ID)
        if (removed) {
            logger.info("Овал id={} удален из репозитория.", oval.getId());
            // Удаляем Warehouse из наблюдателей
            oval.deleteObserver(warehouse);
            // Удаляем данные из Warehouse
            warehouse.remove(oval.getId());
        } else {
            logger.warn("Попытка удалить не существующий овал id={} из репозитория.", oval.getId());
        }
    }

    public Oval getById(long id) {
        for (Oval oval : ovals) {
            if (oval.getId() == id) {
                return oval;
            }
        }
        logger.trace("Овал с id={} не найден в репозитории.", id);
        return null; // Или Optional<Oval>
    }


    public List<Oval> getAll() {
        logger.debug("Получение всех овалов из репозитория. Счет: {}", ovals.size());
        return new ArrayList<>(ovals); // Возвращаем копию для защиты внутреннего состояния
    }

    // --- Методы сортировки (используют один экземпляр service) ---
    public List<Oval> sortById() {
        logger.debug("Сортировка овалов по ID.");
        List<Oval> sorted = new ArrayList<>(ovals);
        // Comparator.comparingLong(oval -> oval.getId())
        sorted.sort(Comparator.comparingLong(Oval::getId));
        return sorted;
    }

    public List<Oval> sortByArea() {
        logger.debug("Сортировка овалов по площади.");
        List<Oval> sorted = new ArrayList<>(ovals);
        // Используем ссылку на метод существующего сервиса
        sorted.sort(Comparator.comparingDouble(service::calculateArea));
        return sorted;
    }

    public List<Oval> sortByPerimeter() {
        logger.debug("Сортировка овалов по периметру.");
        List<Oval> sorted = new ArrayList<>(ovals);
        sorted.sort(Comparator.comparingDouble(service::calculatePerimeter));
        return sorted;
    }

    // --- Метод выборки по спецификации ---
    public List<Oval> query(Specification<Oval> specification) {
        logger.debug("Выбор овалов используя спецификацию: {}", specification.getClass().getSimpleName());
        List<Oval> result = new ArrayList<>();
        for (Oval oval : ovals) {
            if (specification.isSatisfiedBy(oval)) {
                result.add(oval);
            }
        }
        logger.debug("Выборка завершена. Найдено {} овала(ов) удовлетворяющих условию.", result.size());
        return result;
    }

}
