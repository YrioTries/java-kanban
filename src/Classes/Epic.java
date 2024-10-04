package Classes;

import java.util.HashMap;

public class Epic extends Task{
    private HashMap<Integer, SupTask> EpicTasks;
     public Epic(final int id, String title, String description){
         super(id, title, description);
         EpicTasks =  new HashMap();
     }

     public void setEpicTasks(final Integer id, SupTask sup){
         EpicTasks.put(id, sup);
     }

}
