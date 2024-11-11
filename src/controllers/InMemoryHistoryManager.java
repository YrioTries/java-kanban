package controllers;

import Classes.Task;
import controllers.interfaces.HistoryManager;

import java.util.*;

import Classes.Node;

public class InMemoryHistoryManager implements HistoryManager {
    private HandleLinkedHashMap handleMap;


    public InMemoryHistoryManager(){
        handleMap = new HandleLinkedHashMap();
    }

    @Override
    public void add(Task task) {
        handleMap.addLast(task);
    }

    @Override
    public void remove(int id) {
        handleMap.removeNodeId(id);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return handleMap.getTasks();
    }
}

class HandleLinkedHashMap {

    Map<Integer, Node> handleLinkedMap;
    private Node head;
    private Node tail;
    private int size;

    public HandleLinkedHashMap(){
        handleLinkedMap = new HashMap<>();
        size = 0;
    }

    public void addFirst(Task task) {
        if (handleLinkedMap.containsKey(task.getId())){
            removeNodeId(task.getId());
        }
        final Node oldHead = head;
        final Node newNode = new Node(null, task, oldHead);
        handleLinkedMap.put(task.getId(), newNode);
        head = newNode;
        if (oldHead == null)
            tail = newNode;
        else
            oldHead.prev = newNode;
        size++;
    }

    public void addLast(Task task) {
        if (handleLinkedMap.containsKey(task.getId())){
            Node nodeNeedToMove = handleLinkedMap.get(task.getId());

            Node prevMove = nodeNeedToMove.prev;
            Node nextMove = nodeNeedToMove.next;
            if (prevMove != null && nextMove != null && size != 0){
                prevMove.next = nextMove;
                nextMove.prev = prevMove;
            }

            if (nodeNeedToMove == head) {
                head = nextMove;
            }

            if (tail != null && size != 1) tail.next = nodeNeedToMove;
            nodeNeedToMove.prev = tail;
            nodeNeedToMove.next = null;

            if (size == 0) {
                head = tail = nodeNeedToMove;
            } else {
                tail = nodeNeedToMove;
            }

            return;
        }
        //final Node oldTail = tail;
        Node newNode = new Node(tail, task, null);
        handleLinkedMap.put(task.getId(), newNode);

        if (tail != null) tail.next = newNode;

        if (size == 0) {
            head = tail = newNode;
        } else {
            tail = newNode;
        }
        size++;
    }

    public Task getFirst() {
        final Node curHead = head;
        if (curHead == null)
            throw new NoSuchElementException();
        return head.data;
    }

    public Task getLast() {
        final Node curTail = tail;
        if (curTail == null)
            throw new NoSuchElementException();
        return tail.data;
    }

    public void removeNode(int position){
        int w = 0;
        Node needToDel = head;

        while (w < position)
        {
            needToDel = needToDel.next;
            w++;
        }

        Node prevDel = needToDel.prev;
        Node AfterDel = needToDel.next;

        if (prevDel != null && size != 0) {
            prevDel.next = AfterDel;
        }

        if (AfterDel != null && size != 0) {
            AfterDel.prev = prevDel;
        }

        if (position == 0) {
            head = AfterDel;
        }

        if (position == size) {
            tail = prevDel;
        }
        size--;
    }

    public void removeNodeId(int id) {

        Node needToDel = handleLinkedMap.get(id);

        Node prevDel = needToDel.prev;
        Node AfterDel = needToDel.next;

        handleLinkedMap.remove(id);

        if (prevDel != null && AfterDel != null && size != 0) {
            prevDel.next = AfterDel;
            AfterDel.prev = prevDel;
        }

        if (needToDel == head) {
            head = AfterDel;
        }

        if (needToDel == tail) {
            tail = prevDel;
        }

        size--;
    }

    public int size() {
        return this.size;
    }

    public ArrayList<Task> getTasks(){
        if (head == null) return null;
        ArrayList<Task> tasks = new ArrayList<>();
        Node current = head;

        while (current != null){
            tasks.add(current.data);
            current = current.next;
        }
        return tasks;
    }

    @Override
    public String toString(){
        String res = "HandleLinkedList[size:" + size + "] [";
        Node pr = head;

        for (int i = 0; i <= size; i++){
            if (i == size){
                res = pr.data + ". ";
            } else {
                res = pr.data + ", ";
            }
            pr = pr.next;
        }
        return res + "]";
    }
}


