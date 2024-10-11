package Classes;

import java.util.HashMap;

public class Epic extends Task{
    private HashMap<Integer, Subtask> epicSubtasks;
     public Epic(String title, String description){
         super(title, description);
         epicSubtasks =  new HashMap();
     }
     public HashMap<Integer, Subtask> getSubMap(){
         return epicSubtasks;
     }

     public void setEpicTasks(final Integer id, Subtask sup){
         epicSubtasks.put(id, sup);
     }

     public Subtask getSubTask(int id){
         return epicSubtasks.get(id);
     }
}
