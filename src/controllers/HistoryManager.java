package controllers;
import Classes.*;

import java.util.ArrayList;

public interface HistoryManager<T>{
    void add (T task);
    ArrayList<T> getHistory();
}
