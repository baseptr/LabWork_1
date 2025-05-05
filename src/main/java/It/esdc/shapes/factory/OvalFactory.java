package It.esdc.shapes.factory;


import It.esdc.shapes.entity.Oval;
import It.esdc.shapes.entity.Point;
import It.esdc.shapes.exception.InvalidShapeException;
import It.esdc.shapes.validator.OvalValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OvalFactory {

    private static final Logger logger = LoggerFactory.getLogger(OvalFactory.class);
    private static final String POINT_SEPARATOR = ";";
    private static final String COORD_SEPARATOR = ",";
    public static Oval createOval(String data) throws InvalidShapeException {
        logger.debug("Попытка создать овал из данных: \"{}\"", data);
        if (data == null || data.trim().isEmpty()) {
            throw new InvalidShapeException("Входные данные для овала null или пусты");
        }

        String trimmedData = data.trim();
        String[] parts = trimmedData.split(POINT_SEPARATOR);
        if (parts.length != 2) {
            throw new InvalidShapeException("Некорректный формат: ожидалось 2 точки разделенные  '" + POINT_SEPARATOR + "', найдено " + parts.length + " части в  \"" + trimmedData + "\"");
        }

        // Парсим точки
        Point p1 = parsePoint(parts[0].trim());
        Point p2 = parsePoint(parts[1].trim());

        // Создаем объект
        Oval oval = new Oval(p1, p2);
        logger.trace("Объект овал создан: {}", oval);

        // Валидируем геометрию овала
        try {
            OvalValidator.validate(oval);
            logger.debug("Овал с id={} прошел валидацию", oval.getId());
        } catch (InvalidShapeException e) {
            logger.warn("Ошибка валидации овала с данными  \"{}\": {}", trimmedData, e.getMessage());
            // Перебрасываем исключение, т.к. фабрика не должна возвращать невалидный объект
            throw e;
        }

        logger.info("Удачное создание и валидация овала с id={}", oval.getId());
        return oval;
    }

    private static Point parsePoint(String str) throws InvalidShapeException {
        logger.trace("Парсинг точек из строки: \"{}\"", str);
        String[] coords = str.split(COORD_SEPARATOR);
        if (coords.length != 2) {
            throw new InvalidShapeException("Неверный формат точке: ожидалось 2 координаты разделенные с '" + COORD_SEPARATOR + "', найдено " + coords.length + " части в \"" + str + "\"");
        }

        try {
            // Используем trim() для каждой координаты на случай лишних пробелов
            double x = Double.parseDouble(coords[0].trim());
            double y = Double.parseDouble(coords[1].trim());
            Point point = new Point(x, y);
            logger.trace("Парсинг точки: {}", point);
            return point;
        } catch (NumberFormatException e) {
            logger.warn("Ошибка парсинга координат в \"{}\": {}", str, e.getMessage());
            throw new InvalidShapeException("Некорректное числовое значение в точке строки \"" + str + "\"", e);
        }
    }
}