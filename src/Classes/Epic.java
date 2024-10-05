package Classes;

import java.util.HashMap;

public class Epic extends Task{
    protected HashMap<Integer, SubTask> EpicTasks;
     public Epic(final int id, String title, String description){
         super(title, description);
         EpicTasks =  new HashMap();
     }

     public void setEpicTasks(final Integer id, SubTask sup){
         EpicTasks.put(id, sup);
     }

}
