package controllers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import classes.*;
import classes.enums.Class;
import classes.enums.Status;
import exeptions.ManagerSaveException;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final String filePath;

    public FileBackedTaskManager() {
        super();
        this.filePath = "base.csv"; //Пока временный вариант, нужно через параметр
        loadFromFile(filePath);
    }

    @Override
    public int pushSub(Epic epic, Subtask subtask) throws IOException, ManagerSaveException {
        int id = super.pushSub(epic, subtask);
        save();
        return id;
    }

    public void save() throws IOException, ManagerSaveException {
        if (taskMaster.isEmpty()) {
            throw new ManagerSaveException("Нет задач для сохранения");
        }
        try (FileWriter fileWriter = new FileWriter(filePath, StandardCharsets.UTF_8, true)) {
            for (Task task : taskMaster.values()) {
                fileWriter.write(task.toString() + "\n");

                if (task.getTaskClass() == Class.EPIC) {
                    Epic epic = (Epic) task;
                    for (Subtask sub : epic.getSubMap().values()) {
                        fileWriter.write(sub.toString() + "\n");
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка записи в файл\n" + e.getStackTrace());
        }
    }

    private void loadFromFile(String path) {
        Epic currentEpic = null; // Сброс текущего эпика перед началом чтения файла
        try (BufferedReader br = new BufferedReader(new FileReader(path, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) { // Проверка на пустоту строки
                    Task task = fromString(line);
                    if (task != null) {
                        if (!taskMaster.containsKey(task.getId())) {
                            taskMaster.put(task.getId(), task);

                            if (task.getTaskClass() == Class.EPIC) {
                                currentEpic = (Epic) task;
                            } else if (task.getTaskClass() == Class.SUBTASK) {
                                if (currentEpic != null) {
                                    currentEpic.getSubMap().put(task.getId(), (Subtask) task);
                                    ((Subtask) task).setMotherId(currentEpic);
                                }
                            }
                        }
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Ошибка чтения файла: " + e.getMessage());
        }
    }

    public Task fromString(String line) {
        String[] param = line.split(",");

        if (param.length != 5) {
            throw new IllegalArgumentException("Неверный формат строки: " + line);
        }

        int id = Integer.parseInt(param[0]);
        String title = param[1];
        Class taskClass = Class.valueOf(param[2]);
        Status status = Status.valueOf(param[3]);
        String description = param[4];

        Task task;
        switch (taskClass) {
            case TASK:
                task = new Task(title, description);
                break;
            case EPIC:
                task = new Epic(title, description);
                break;
            case SUBTASK:
                task = new Subtask(title, description);
                break;
            default:
                throw new IllegalArgumentException("Неизвестный тип задачи: " + taskClass);
        }

        task.setId(id);
        task.setStatus(status);

        return task;
    }
}