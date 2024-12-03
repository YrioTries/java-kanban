package classes;

import java.util.*;

public class HandleLinkedHashMap {

    static class Node {

        public Task data;
        public Node next;
        public Node prev;

        public Node(Node prev, Task data, Node next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return data.equals(node.data);
        }

        @Override
        public int hashCode() {
            return 31 * Objects.hashCode(data);
        }
    }

    private Map<Integer, Node> handleLinkedMap;

    private Node head;
    private Node tail;

    public HandleLinkedHashMap() {
        handleLinkedMap = new HashMap<>();
    }

    public void addLast(Task task) {
        if (handleLinkedMap.containsKey(task.getId())) {
            Node nodeNeedToMove = handleLinkedMap.get(task.getId());

            Node prevMove = nodeNeedToMove.prev;
            Node nextMove = nodeNeedToMove.next;

            if (nodeNeedToMove == head) {
                head = nextMove;
            }

            if (prevMove != null && nextMove != null) {
                prevMove.next = nextMove;
                nextMove.prev = prevMove;
            }

            nodeNeedToMove.prev = tail;
            nodeNeedToMove.next = null;

            tail.next = nodeNeedToMove;
            tail = nodeNeedToMove;

            return;
        }

        Node newNode = new Node(tail, task, null);

        if (tail != null) {
            tail.next = newNode;
        }

        if (size() == 0) {
            head = tail = newNode;
        } else {
            tail = newNode;
        }
        handleLinkedMap.put(task.getId(), newNode);
    }

    public Task getFirst() {
        final Node curHead = head;
        if (curHead == null) {
            throw new NoSuchElementException();
        }
        return head.data;
    }

    public Task getLast() {
        final Node curTail = tail;

        if (curTail == null) {
            throw new NoSuchElementException();
        }

        return tail.data;
    }

    public void removeNodeId(int id) {

        if (handleLinkedMap.containsKey(id)) {
            Node needToDel = handleLinkedMap.get(id);

            Node prevDel = needToDel.prev;
            Node afterDel = needToDel.next;

            if (needToDel == head && afterDel != null) {
                head = afterDel;

            } else if (needToDel == tail && prevDel != null) {
                tail = prevDel;

            } else if (prevDel != null && afterDel != null) {
                prevDel.next = afterDel;
            }

            handleLinkedMap.remove(needToDel.data.getId());

            needToDel.prev = null;
            needToDel.next = null;
        }
    }

    public int size() {
        return handleLinkedMap.size();
    }

    public ArrayList<Task> getTasks() {
        if (head == null) {
            return null;
        }
        ArrayList<Task> tasks = new ArrayList<>();
        Node current = head;

        while (current != null) {
            tasks.add(current.data);
            current = current.next;
        }
        return tasks;
    }

    @Override
    public String toString() {
        String res = "HandleLinkedList[size:" + size() + "] [";
        Node pr = head;

        while (pr != null) {
            if (pr.next == null) {
                res += pr.data + ". ";
            } else {
                res += pr.data + ", ";
            }
            pr = pr.next;
        }
        return res + "]";
    }
}
