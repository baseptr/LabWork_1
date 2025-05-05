import com.lab.pool.ShapeWarehouse;
import com.lab.entity.Oval;
import com.lab.entity.Point;
import com.lab.exception.InvalidShapeException;
import com.lab.factory.OvalFactory;
import com.lab.repository.ShapeRepository;
import com.lab.service.FileReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    // Путь к файлу относительно корня проекта (например, если файл лежит в папке data)
    private static final String OVAL_DATA_FILE = "src/main/resources/ovals.txt";
    public static void main(String[] args) {
        logger.info("Запуск приложения...");

        ShapeRepository repository = new ShapeRepository(); // Создаем репозиторий

        // --- Чтение данных из файла ---
        List<String> lines;
        try {
            lines = FileReader.readLines(OVAL_DATA_FILE);
        } catch (IOException | IllegalArgumentException e) { // Ловим ошибки чтения и невалидного пути
            logger.error("Ошибка чтения файла данных '{}': {}", OVAL_DATA_FILE, e.getMessage(), e);
            logger.error("Приложение не может продолжить работу без данных. Завершение.");
            return; // Завершаем работу
        }

        // --- Обработка строк и создание овалов ---
        logger.info("Обработка {} строк из файла...", lines.size());
        int successfulCreations = 0;
        int failedCreations = 0;
        int skippedLines = 0;

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            int lineNumber = i + 1; // Номер строки для логов

            if (line == null || line.trim().isEmpty()) {
                logger.trace("Пропуск пустой строки #{}", lineNumber);
                skippedLines++;
                continue; // Пропускаем пустые строки
            }

            // Можно добавить пропуск строк с комментариями (например, начинающихся с '#')
            if (line.trim().startsWith("#")) {
                logger.trace("Пропуск строки с комментарием #{}", lineNumber);
                skippedLines++;
                continue;
            }

            try {
                // Пытаемся создать и добавить овал
                Oval oval = OvalFactory.createOval(line);
                repository.add(oval); // Репозиторий сам обработает дубликаты и добавит Observer
                successfulCreations++;
                // logger.debug("Successfully processed line #{}: {}", lineNumber, line); // Можно убрать для чистоты логов
            } catch (InvalidShapeException e) {
                // Ловим исключение от фабрики (неверный формат, невалидная геометрия)
                failedCreations++;
                logger.warn("Ошибка обработки строки #{}: \"{}\". Причина: {}", lineNumber, line, e.getMessage());
                // Просто переходим к следующей строке (Требование 7)
            } catch (Exception e) {
                // Ловим любые другие неожиданные ошибки при обработке строки
                failedCreations++;
                logger.error("Неожиданная ошибка при обработке строки #{}: \"{}\"", lineNumber, line, e);
            }
        }

        logger.info("--------------------------------------------------");
        logger.info("Обработка файла завершена.");
        logger.info("Всего строк прочитано: {}", lines.size());
        logger.info("Успешно создано овалов: {}", successfulCreations);
        logger.info("Ошибочных строк (невалидные/ошибки): {}", failedCreations);
        logger.info("Пропущено строк (пустые/комментарии): {}", skippedLines);
        logger.info("Текущее количество овалов в репозитории: {}", repository.getAll().size());
        logger.info("--------------------------------------------------");


        // --- Пример использования репозитория и склада ---
        if (!repository.getAll().isEmpty()) {
            logger.info("Пример операций:");
            Oval firstOval = repository.getAll().getFirst();
            long firstOvalId = firstOval.getId();
            logger.info("Получение метрик для овала id={}: Периметр={}, Площадь={}",
                    firstOvalId,
                    ShapeWarehouse.getInstance().getPerimeter(firstOvalId),
                    ShapeWarehouse.getInstance().getArea(firstOvalId));

            // Пример изменения овала (если он изменяемый)
            logger.info("Изменение точки X для овала id={}", firstOvalId);
            Point currentP1 = firstOval.getPointX();
            firstOval.setPointX(new Point(currentP1.x() + 1, currentP1.y() + 1)); // Сдвигаем точку

            // Проверяем, обновились ли данные в Warehouse
            logger.info("Получение обновлённых метрик для овала id={}: Периметр={}, Площадь={}",
                    firstOvalId,
                    ShapeWarehouse.getInstance().getPerimeter(firstOvalId),
                    ShapeWarehouse.getInstance().getArea(firstOvalId));

            // Пример сортировки
            List<Oval> sortedByArea = repository.sortByArea();
            logger.info("Овалы, отсортированные по площади (первые 3):");
            sortedByArea.stream().limit(3).forEach(o -> logger.info(" - ID: {}, Площадь: {}", o.getId(), ShapeWarehouse.getInstance().getArea(o.getId())));
        }

        logger.info("Работа приложения завершена.");
    }
}
