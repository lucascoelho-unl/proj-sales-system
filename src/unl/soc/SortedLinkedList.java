package unl.soc;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

@SuppressWarnings("unchecked")
public class SortedLinkedList<T> implements Iterable<T> {
    private int size;
    private Node<T> head;
    private Node<T> tail;
    private final Comparator<T> comparator;

    public SortedLinkedList(Comparator<T> comparator) {
        this.head = null;
        this.tail = null;
        this.size = 0;
        this.comparator = comparator;
    }

    public SortedLinkedList(){
        this(null);
    }

    public int size(){
        return this.size;
    }

    public void removeAll(){
        this.size = 0;
        this.head = null;
        this.tail = null;
    }

    public boolean isEmpty(){
        return this.size == 0;
    }

    private Node<T> getNode(int index){
        boundCheck(index);
        Node<T> current;
        if (index <= size / 2 ){
            current = this.head;
            for (int i = 0; i < index; i++) {
                current = current.getNext();
            }
        } else {
            current = this.tail;
            for (int i = size - 1; i > index ; i--) {
                current = current.getPrev();
            }
        }

        return current;
    }

    public void add(T item){
        if (item == null){
            throw new IllegalArgumentException("Cannot add a null element");
        }

        if (!hasComparable(item) && comparator == null){
            throw new IllegalArgumentException("Missing a compareTo, or a comparator in the constructor");
        }

        if(isEmpty()){
            this.head = new Node<>(item);
            this.tail = this.head;
            this.size++;
            return;
        }

        if (comparator == null){
            addWithComparator(item, (a, b) -> ((Comparable<T>) a).compareTo(b));
        }
        else {
            addWithComparator(item, comparator);
        }
    }

    private void addWithComparator(T item, Comparator<T> comparator) {
        Node<T> newNode = new Node<>(item);

        if (comparator.compare(item, this.head.getItem()) <= 0){
            this.head.setPrev(newNode);
            newNode.setNext(this.head);
            this.head = newNode;
            size++;
            return;
        }

        if (comparator.compare(item, this.tail.getItem()) >= 0){
            this.tail.setNext(newNode);
            newNode.setPrev(this.tail);
            this.tail = newNode;
            size++;
            return;
        }

        Node<T> current = this.head;

        while (comparator.compare(item, current.getItem()) >= 0) {
            current = current.getNext();
        }

        insertBefore(current, newNode);
    }

    private void insertBefore(Node<T> current, Node<T> newNode){
        Node<T> previous = current.getPrev();
        previous.setNext(newNode);
        newNode.setPrev(previous);
        newNode.setNext(current);
        current.setPrev(newNode);
        this.size++;
    }

    private boolean hasComparable(T item) {
        try {
            item.getClass().getDeclaredMethod("compareTo", item.getClass());
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    public T pop(){
        if (isEmpty()){
            throw new IllegalStateException("Cannot pop an empty list");
        }
        Node<T> toDelete = getNode(this.size - 1);

        this.tail = this.tail.getPrev();
        this.tail.setNext(null);
        size--;
        return toDelete.getItem();
    }

    public T get(int index){
        return this.getNode(index).item;
    }

    public T remove(int index){
        boundCheck(index);

        if(index == 0){
            Node<T> toDelete = this.head;
            this.head = this.head.getNext();
            size--;
            return toDelete.getItem();
        }
        if (index == this.size - 1){
            return pop();
        }

        Node<T> toBeDeleted = this.getNode(index);
        Node<T> after = toBeDeleted.getNext();
        Node<T> before = toBeDeleted.getPrev();

        after.setPrev(before);
        before.setNext(after);
        size--;
        return toBeDeleted.getItem();
    }


    public void addFromArray(T [] array){
        for(T element : array){
            add(element);
        }
    }

    private void boundCheck (int index){
        if (index < 0){
            throw new IllegalArgumentException("Index less than 0");
        }
        if (index >= this.size){
            throw new IndexOutOfBoundsException();
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Node<T> current = this.head;
        while (current != null) {
            sb.append(current.getItem());
            if (current.getNext() != null) {
                sb.append(", ");
            }
            current = current.getNext();
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private Node<T> currentNode = head; // Start from the head

            @Override
            public boolean hasNext() {
                return currentNode != null; // Check if there's a next node
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException(); // Throw exception if no more elements
                }
                T item = currentNode.getItem(); // Get the item from the current node
                currentNode = currentNode.getNext(); // Move to the next node
                return item;
            }
        };
    }

    private static class Node <T> {

        private Node<T> next = null;
        private Node<T> prev = null;
        private final T item;

        public Node(T item){
            this.item = item;
        }

        public T getItem(){
            return item;
        }

        public Node<T> getNext(){
            return this.next;
        }

        public Node<T> getPrev(){
            return this.prev;
        }

        public void setNext(Node<T> next){
            this.next = next;
        }

        public void setPrev(Node<T> prev){
            this.prev = prev;
        }
    }

}
