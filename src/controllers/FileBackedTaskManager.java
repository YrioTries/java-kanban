package controllers;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import classes.*;
import classes.enums.Class;
import classes.enums.Status;
import exeptions.ManagerSaveException;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final String filePath;

    public FileBackedTaskManager(String filePath) {
        super();
        this.filePath = filePath; //Пока временный вариант, нужно через параметр
        loadFromFile(filePath);
    }

    @Override
    public int pushSub(Epic epic, Subtask subtask) throws ManagerSaveException {
        int id = super.pushSub(epic, subtask);
        save();
        return id;
    }

    @Override
    public int pushEpic(Epic epic) throws ManagerSaveException {
        int id = super.pushEpic(epic);
        save();
        return id;
    }

    @Override
    public int pushTask(Task task) throws ManagerSaveException {
        int id = super.pushTask(task);
        save();
        return id;
    }

    public void save() {
        if (taskMaster.isEmpty()) {
            throw new ManagerSaveException("Нет задач для сохранения");
        }

        Epic currentEpic = null;
        ArrayList<Task> savedTasks = new ArrayList<>();

        // Чтение существующих задач из файла
        try (BufferedReader br = new BufferedReader(new FileReader(filePath, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    Task task = fromString(line);
                    if (task != null) {
                        if (task.getTaskClass() == Class.EPIC) {
                            currentEpic = (Epic) task;
                        } else if (task.getTaskClass() == Class.SUBTASK) {
                            if (currentEpic != null) {
                                currentEpic.getSubMap().put(task.getId(), (Subtask) task);
                                ((Subtask) task).setMotherId(currentEpic);
                            }
                        }
                        savedTasks.add(task);
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла " + filePath);
        }

        // Запись новых задач в файл
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(filePath, StandardCharsets.UTF_8, true))) {
            for (Task task : taskMaster.values()) {
                if (!savedTasks.contains(task)) {
                    fileWriter.write(task.toString() + "\n");
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи в файл " + filePath);
        }
    }

    private void loadFromFile(String filePath) {
        Epic currentEpic = null; // Сброс текущего эпика перед началом чтения файла
        try (BufferedReader br = new BufferedReader(new FileReader(filePath, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) { // Проверка на пустоту строки
                    Task task = fromString(line);
                    if (task != null) {
                        if (!taskMaster.containsKey(task.getId())) {
                            taskMaster.put(task.getId(), task);

                            if (taskMaster.isEmpty()) {
                                throw new ManagerSaveException("Задачи не сохранились");
                            }

                            if (task.getTaskClass() == Class.EPIC) {
                                currentEpic = (Epic) task;

                            } else if (task.getTaskClass() == Class.SUBTASK) {
                                if (currentEpic != null) {
                                    currentEpic.getSubMap().put(task.getId(), (Subtask) task);
                                    ((Subtask) task).setMotherId(currentEpic);
                                }
                            }
                            setManagerId(task.getId());
                            task = null;
                        }
                    }
                }
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла " + filePath);
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

    public static boolean isCsvFileEmpty(String filePath) {
        File csvFile = new File(filePath);

        // Проверка, существует ли файл
        if (!csvFile.exists() || !csvFile.isFile()) {
            System.out.println("Файл не найден или это не файл.");
            return true; // Считаем файл пустым, если он не существует
        }

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            // Проверяем первую строку файла
            String firstLine = br.readLine();
            return firstLine == null || firstLine.trim().isEmpty(); // Если первая строка отсутствует или пуста
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла " + filePath);
        }
    }
}
