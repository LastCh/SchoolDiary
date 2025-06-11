package ru.bmstu.diary.util;

import org.springframework.core.io.Resource;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CsvReaderWriter {
    private final Path filePath;
    private final Resource resource;

    public CsvReaderWriter(String filePath, Resource resource) {
        this.filePath = Paths.get(filePath).toAbsolutePath().normalize();
        this.resource = resource;
        createFileIfNotExists();
    }

    private void createFileIfNotExists() {
        try {
            if (!Files.exists(filePath)) {
                if (filePath.getParent() != null) {
                    Files.createDirectories(filePath.getParent());
                }
                try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
                    writer.write("id;firstName;lastName;tokens");
                    writer.newLine();
                    if (resource != null && resource.exists()) {
                        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
                            String line;
                            boolean firstLine = true;
                            while ((line = reader.readLine()) != null) {
                                if (firstLine) {
                                    firstLine = false;
                                    continue; // Пропускаем заголовок из ресурса
                                }
                                writer.write(line);
                                writer.newLine();
                            }
                        }
                    } else {
                        System.out.println("Resource not found or null, initializing empty file: " + filePath);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to create a data file: " + filePath + ". Check access rights.", e);
        }
        if (resource == null || !resource.exists()) {
            throw new IllegalStateException("Data resource unavailable: " + filePath);
        }
    }

    public List<String[]> readAllLines() {
        List<String[]> data = new ArrayList<>();
        try {
            if (!Files.exists(filePath)) {
                createFileIfNotExists(); // Создаём файл, если его нет
            }
            List<String> lines = Files.readAllLines(filePath);
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (i == 0 || line.isEmpty()) continue; // Пропускаем заголовок и пустые строки
                String[] parts = line.split(";");
                if (parts.length >= 4) {
                    try {
                        Integer.parseInt(parts[0]); // Проверка валидности ID
                        Integer.parseInt(parts[3]); // Проверка валидности токенов
                        data.add(parts);
                    } catch (NumberFormatException e) {
                        System.err.println("Incorrect data in the string: " + line + ". Missing.");
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Data resource unavailable: " + filePath, e);
        }
        return data;
    }

    public void writeAllLines(List<String[]> data) {
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            writer.write("id;firstName;lastName;tokens");
            writer.newLine();
            for (String[] row : data) {
                if (row.length < 4) {
                    System.err.println("Incorrect string to record: " + String.join(";", row));
                    continue;
                }
                try {
                    int tokens = Integer.parseInt(row[3]);
                    if (tokens < 0 || tokens > 100) {
                        System.err.println("Tokens (" + tokens + ") outside the valid range (0-100) for : " + String.join(";", row));
                        continue; // Пропускаем строку с некорректными токенами
                    }
                    writer.write(String.join(";", row));
                    writer.newLine();
                } catch (NumberFormatException e) {
                    System.err.println("Incorrect value of tokens in the string: " + String.join(";", row) + ". Missing.");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing to CSV file: " + filePath, e);
        }
    }

    public Path getFilePath() {
        return filePath;
    }
}