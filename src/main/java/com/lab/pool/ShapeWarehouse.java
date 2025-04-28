package com.lab.pool;

import com.lab.entity.Oval;
import com.lab.service.OvalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

@SuppressWarnings("deprecation")
public class ShapeWarehouse implements java.util.Observer{
    private static final Logger logger = LoggerFactory.getLogger(ShapeWarehouse.class);

    // Используем внутренний record для хранения метрик (более читаемо)
    private record OvalMetrics(double perimeter, double area) {}

    private static final ShapeWarehouse INSTANCE = new ShapeWarehouse();
    private final OvalService service = new OvalService(); // Один экземпляр сервиса
    // Карта для хранения метрик: ID овала -> Метрики
    private final Map<Long, OvalMetrics> data = new HashMap<>();

    private ShapeWarehouse() {
        logger.info("ShapeWarehouse Singleton объект создан.");
    }

    public static ShapeWarehouse getInstance() {
        return INSTANCE;
    }

    // Метод, вызываемый при изменении Oval (через notifyObservers)
    @Override
    public void update(Observable obj, Object arg) {
        // Проверяем, что объект является Oval и был помечен как измененный
        if (obj instanceof Oval oval) { // Используем instanceof с pattern variable
            long ovalId = oval.getId();
            logger.debug("Получено уведомление об обновлении овала id={}", ovalId);
            try {
                // Пересчитываем метрики
                double perimeter = service.calculatePerimeter(oval);
                double area = service.calculateArea(oval);
                OvalMetrics metrics = new OvalMetrics(perimeter, area);
                // Обновляем данные в хранилище
                data.put(ovalId, metrics);
                logger.info("Warehouse для овала обновлен id={}: Периметр={}, Площадь={}", ovalId, perimeter, area);
            } catch (Exception e) {
                // Ловим возможные ошибки при расчетах (хотя их быть не должно с валидацией)
                logger.error("Ошибка в расчетах метрик для овала id={} в процессе обновления данных: {}", ovalId, e.getMessage(), e);
            }
        } else {
            logger.warn("Получено обновление от неожиданного observable типа: {}", obj != null ? obj.getClass().getName() : "null");
        }
    }

    // Метод для удаления данных об овале (вызывается из Repository.delete)
    public void remove(long id) {
        logger.debug("Попытка удалить данные овала id={}", id);
        OvalMetrics removedMetrics = data.remove(id);
        if (removedMetrics != null) {
            logger.info("Удалены данные из Warehouse для овала id={}", id);
        } else {
            logger.warn("Попытка удалить не существующие данные овала id={}", id);
        }
    }

    // Геттеры для получения метрик (с проверкой на null)
    public Double getPerimeter(long id) {
        OvalMetrics metrics = data.get(id);
        Double perimeter = (metrics != null) ? metrics.perimeter() : null;
        logger.trace("Получен периметр овала id={}: {}", id, perimeter);
        return perimeter;
    }

    public Double getArea(long id) {
        OvalMetrics metrics = data.get(id);
        Double area = (metrics != null) ? metrics.area() : null;
        logger.trace("Получена площадь овала id={}: {}", id, area);
        return area;
    }

    // Метод для получения всех данных (может быть полезен для отладки)
    public Map<Long, OvalMetrics> getAllData() {
        return new HashMap<>(data); // Возвращаем копию
    }

}
