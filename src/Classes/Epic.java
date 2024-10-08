package Classes;

import java.util.HashMap;

public class Epic extends Task{
    public HashMap<Integer, Subtask> epicSubtasks;

     public Epic(String title, String description){
         super(title, description);
         epicSubtasks =  new HashMap();
     }
    @Override
    public boolean correctClass() { return this.getClass() == Epic.class; }
     public void setEpicTasks(final Integer id, Subtask sup){
         epicSubtasks.put(id, sup);
     }

     public Subtask getSubTask(int id){
         return epicSubtasks.get(id);
     }
}
