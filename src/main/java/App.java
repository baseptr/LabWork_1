import com.lab.entity.Oval;
import com.lab.exception.InvalidShapeException;
import com.lab.factory.OvalFactory;
import com.lab.repository.ShapeRepository;
import com.lab.service.FileReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;


public class App {

    //private static final Logger logger = LogManager.getLogger(App.class);

    public static void main(String[] args) {
        ShapeRepository repository = new ShapeRepository(); // Создаем репозиторий
        String filePath = "data/ovals.txt"; // Путь к файлу (относительный, Rule 12)

        List<String> lines = null;
        try {
            // logger.info("Чтение файла данных: {}", filePath);
            lines = FileReader.readLines(filePath);
            // logger.info("Прочитано строк: {}", lines.size());
        } catch (IOException e) {
            // logger.error("Ошибка чтения файла {}: {}", filePath, e.getMessage());
            System.err.println("Не удалось прочитать файл: " + filePath); // Или логирование
            return; // Завершаем работу, если файл не прочитан
        }

        int successfulCreations = 0;
        int failedCreations = 0;
        for (String line : lines) {
            if (line == null || line.trim().isEmpty()) {
                // logger.warn("Пропущена пустая строка");
                continue; // Пропускаем пустые строки
            }
            try {
                Oval oval = OvalFactory.createOval(line.trim());
                repository.add(oval); // Добавляем валидный овал в репозиторий
                successfulCreations++;
                // logger.debug("Успешно создан и добавлен овал: {}", oval);
            } catch (InvalidShapeException e) {
                failedCreations++;
                // logger.warn("Некорректные данные в строке '{}': {}. Строка пропущена.", line, e.getMessage());
                // Пропускаем строку и переходим к следующей (Требование 7)
            }
        }

        // logger.info("Обработка файла завершена. Успешно создано: {}, Ошибок: {}", successfulCreations, failedCreations);
        System.out.println("Создано овалов: " + repository.getAll().size()); // Вывод для проверки


    }
}
