package model;

public class Node<T> { // отдельный класс Node для узла списка

    public Task data;
    public Node<T> next;
    public Node<T> prev;


    public Node(Node<T> prev, Task data, Node<T> next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }


}