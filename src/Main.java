import Classes.TaskManager;

import java.util.Scanner;

public class Main {
    static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        TaskManager trecker = new TaskManager(scan);
        int command;
        while (true){
            command = scan.nextInt();
            switch (command){
                case 1:
                    trecker.getList();
                    break;

                case 2:
                    trecker.delete();
                    break;

                case 3:
                    System.out.println("Введите id элемента");
                    int id = scan.nextInt();
                    trecker.serchTask(id);
                    break;

                case 4:
                    System.out.println("До свидания");
                    return;

                default:
                    System.out.println("Неизвестная комманда");
            }
        }

    }

    public static void menu(){
        System.out.println("1.Получение списка всех задач");
        System.out.println("2.Удаление задач");
        System.out.println("3.Поиск задачи по id");
        System.out.println("4.Выход");

    }
}
