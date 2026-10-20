package com.carbon.quiz.script;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import tools.jackson.core.type.TypeReference;

import com.carbon.quiz.QuizApplication;
import com.carbon.quiz.model.Question;
import com.carbon.quiz.repository.QuestionRepository;

import tools.jackson.databind.ObjectMapper;

// A cheap script to load the questions if available in JSON form

public class ImportQuestions {
    public static void main(String[] args) {
        System.out.println("Loading questions from JSON files, make sure your questions are in the /question folder.");
        ConfigurableApplicationContext context = SpringApplication.run(QuizApplication.class, args);
        QuestionRepository questionRepository = context.getBean(QuestionRepository.class);

        ObjectMapper mapper = new ObjectMapper();

        String folder = "questions";
        Path folderPath = Paths.get(folder);

        if(!Files.exists(folderPath) || !Files.isDirectory(folderPath)) {
            System.out.println("Directory not found.");
            context.close();
            return;
        }

        System.out.println("Scanning the directory...");

        try (Stream<Path> paths = Files.walk(folderPath, 1)) {
            paths.filter(Files::isRegularFile)
            .filter(path -> path.toString().endsWith(".json"))
            .forEach(filePath -> processSingleFile(filePath, questionRepository, mapper));
        } catch (Exception exception) {
            System.out.println("Error occured while reading files from the directory: " + exception.getMessage());
        }

        context.close();
        System.out.println("Completed importing the questions.");
    }

    private static void processSingleFile(Path filePath, QuestionRepository repository, ObjectMapper mapper) {
        String fileName = filePath.getFileName().toString();

        try {
            System.out.println("Processing " + fileName + "...");

            List<Question> questions = mapper.readValue(
                filePath.toFile(),
                new TypeReference<List<Question>>() {}
            );

            int importedCount = 0;
            int skippedCount = 0;

            for (Question question : questions) {
                boolean exists = repository.existsByQuestionText(question.getQuestionText());
                if(!exists) {
                    repository.save(question);
                    importedCount++;
                } else {
                    skippedCount++;
                }
            }

            System.out.println("Imported " + importedCount + " (skipped: " + skippedCount + ")" + " questions from " + fileName);
        } catch (Exception exception) {
            System.out.println("Failed to process " + fileName);
            System.out.println("Error: " + exception.getMessage());
        }
    }
}
