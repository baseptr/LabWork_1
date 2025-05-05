package com.esde.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileReader {
    private static final Logger logger = LoggerFactory.getLogger(FileReader.class);

    public static List<String> readLines(String filePath) throws IOException {
        if (filePath == null || filePath.trim().isEmpty()) {
            logger.error("Путь к файлу null или пуст.");
            // Можно бросить IllegalArgumentException или вернуть пустой список
            throw new IllegalArgumentException("Путь к файлу не может быть null или пуст");
        }

        logger.info("Попытка прочитать файл: {}", filePath);
        Path path;
        try {
            // Paths.get может бросить InvalidPathException
            path = Paths.get(filePath);
        } catch (InvalidPathException e) {
            logger.error("Некорректный синтаксис пути к файлу: {}", filePath, e);
            throw new IOException("Некорректный путь к файлу: " + filePath, e);
        }

        // Проверка существования и доступности файла
        if (!Files.exists(path)) {
            logger.error("Файл не найден: {}", path);
            throw new IOException("Файл не найден: " + path.toAbsolutePath());
        }
        if (!Files.isReadable(path)) {
            logger.error("Файл не читаемый: {}", path);
            throw new IOException("Файл не для чтения: " + path.toAbsolutePath());
        }


        try {
            List<String> lines = Files.readAllLines(path); // Использует UTF-8 по умолчанию
            logger.info("Успешно прочтено {} строк из файла: {}", lines.size(), filePath);
            return lines;
        } catch (IOException e) {
            logger.error("IOException возникло в процессе чтения файла {}: {}", filePath, e.getMessage(), e);
            throw e; // Перебрасываем исключение дальше
        } catch (SecurityException e) {
            logger.error("SecurityException: Нет прав на чтение файла {}: {}", filePath, e.getMessage(), e);
            throw new IOException("Нет прав на чтение файла: " + filePath, e);
        }
    }
}
