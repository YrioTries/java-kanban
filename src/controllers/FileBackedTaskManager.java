package controllers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import classes.*;
import classes.enums.Class;
import exeptions.ManagerSaveException;


public class FileBackedTaskManager extends InMemoryTaskManager{

    private final String filePath;

    FileBackedTaskManager(){
        super();
        filePath = "base.csv";
    }
    @Override
    public int pushSub(Epic epic, Subtask subtask) throws IOException, ManagerSaveException{
        super.pushSub(epic, subtask);
        save();
        return subtask.getId();
    }

    private void save() throws IOException, ManagerSaveException {
        try (FileWriter fileWriter = new FileWriter(filePath, StandardCharsets.UTF_8, true); ){

            for (Task task : taskMaster.values()) {
                if (!isContainsInFile(task.toString())){
                    fileWriter.write(task + "\n");

                    if (task.getTaskClass() == Class.EPIC){
                        Epic epic = (Epic) task;
                        for (Subtask sub : epic.getSubMap().values()) {
                            fileWriter.write(sub + "\n");
                        }
                    }
                }

            }

            if (isCsvFileEmpty(filePath)) throw new ManagerSaveException("Файл пуст после сохранения");

        } catch (IOException e) {
            System.out.println("Ошибка записи в файл\n" + e.getStackTrace());

        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    private static boolean isCsvFileEmpty(String filePath) throws IOException {

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isContainsInFile(String newTask) {

        try (FileReader reader = new FileReader(filePath, StandardCharsets.UTF_8);
             BufferedReader br = new BufferedReader(reader)) {

            while (br.ready()) {
                String line = br.readLine();
                if (line.equals(newTask)) return true;
            }

        } catch (IOException e) {
            System.out.println("Произошла ошибка во время чтения файла.");
            e.printStackTrace();
        }

        return false;
    }
}
