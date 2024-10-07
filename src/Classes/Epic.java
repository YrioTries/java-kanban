package Classes;

import java.util.HashMap;

public class Epic extends Task{
    public HashMap<Integer, Subtask> epicSubtasks;
     public Epic(final int id, String title, String description){
         super(id, title, description);
         epicSubtasks =  new HashMap();
     }

     public void setEpicTasks(final Integer id, Subtask sup){
         epicSubtasks.put(id, sup);
     }

     public Subtask getSubTask(int id){
         return epicSubtasks.get(id);
     }
}
