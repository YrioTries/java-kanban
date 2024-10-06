import Classes.TaskManager;

import java.util.Scanner;

public class Main {
    static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        TaskManager Trecker = new TaskManager(scan);
        int command;
        while (true){
            command = scan.nextInt();
            switch (command){
                case 1:

                    break;
            }
        }

    }

    public static void menu(){
        System.out.println("1.Получение списка всех задач");
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
    }
}
