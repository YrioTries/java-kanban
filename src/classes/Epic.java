package classes;

import classes.enums.Class;

import java.util.HashMap;

public class Epic extends Task{

    private HashMap<Integer, Subtask> epicSubtasks;

     public Epic(String title, String description){
         super(title, description);
         epicSubtasks =  new HashMap<>();
     }

     @Override
     public classes.enums.Class getTaskClass(){
         return Class.EPIC;
     }

     public HashMap<Integer, Subtask> getSubMap(){
         return epicSubtasks;
     }

     public Subtask getSubTask(int id){
         return epicSubtasks.get(id);
     }
}
