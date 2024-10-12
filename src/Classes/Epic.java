package Classes;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class Epic extends Task{
    private HashMap<Integer, Subtask> epicSubtasks;

     public Epic(String title, String description){
         super(title, description);
         epicSubtasks =  new HashMap<>();
     }
     public HashMap<Integer, Subtask> getSubMap(){
         return epicSubtasks;
     }
     public Subtask getSubTask(int id){
         return epicSubtasks.get(id);
     }
}
