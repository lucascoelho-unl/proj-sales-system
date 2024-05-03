package unl.soc;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A sorted linked list implementation.
 *
 * @param <T> the type of elements stored in the list
 */
@SuppressWarnings("unchecked")
public class SortedLinkedList<T> implements Iterable<T> {
    private int size;
    private Node<T> head;
    private Node<T> tail;
    private final Comparator<T> comparator;

    /**
     * Constructs a new sorted linked list with the specified comparator.
     *
     * @param comparator the comparator to use for sorting elements
     */
    public SortedLinkedList(Comparator<T> comparator) {
        this.head = null;
        this.tail = null;
        this.size = 0;
        this.comparator = comparator;
    }

    /**
     * Constructs a new sorted linked list with natural ordering.
     */
    public SortedLinkedList(){
        this((Comparator<T>) null);
    }

    /**
     * Constructs a new sorted linked list containing the elements of the specified collection,
     * sorted according to the specified comparator.
     *
     * @param c          the collection whose elements are to be placed into this list
     * @param comparator the comparator to use for sorting elements
     */
    public SortedLinkedList(Collection<? extends T> c, Comparator<T> comparator) {
        this(comparator);
        T[] a = (T[]) c.toArray();
        addFromArray(a);
    }

    /**
     * Constructs a new sorted linked list containing the elements of the specified collection,
     * sorted according to their natural ordering.
     *
     * @param c the collection whose elements are to be placed into this list
     */
    public SortedLinkedList(Collection<? extends T> c) {
        this(c, null);
        T[] a = (T[]) c.toArray();
        addFromArray(a);
    }

    /**
     * Returns the number of elements in this list.
     *
     * @return the number of elements in this list
     */
    public int size(){
        return this.size;
    }

    /**
     * Removes all elements from this list.
     */
    public void removeAll(){
        this.size = 0;
        this.head = null;
        this.tail = null;
    }

    /**
     * Returns true if this list contains no elements.
     *
     * @return true if this list contains no elements
     */
    public boolean isEmpty(){
        return this.size == 0;
    }

    /**
     * Gets a node based on its index.
     *
     * @param index
     * @return Tree node
     */
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

    /**
     * Adds the specified element to this list.
     *
     * @param item the element to add
     */
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

    /**
     * If a comparator is used in the construction of the class instance, we call
     * a different add method with logic to use a comparator.
     *
     * @param item
     * @param comparator
     */
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

    /**
     * Inserts given node before the current node that was passed as an argument
     *
     * @param current
     * @param newNode
     */
    private void insertBefore(Node<T> current, Node<T> newNode){
        Node<T> previous = current.getPrev();
        previous.setNext(newNode);
        newNode.setPrev(previous);
        newNode.setNext(current);
        current.setPrev(newNode);
        this.size++;
    }

    /**
     * Checks weather the class of the item passed as an argument has a declared method. If not, return false.
     *
     * @param item
     * @return {@code true} if has compareTo, {@code false} otherwise.
     */
    private boolean hasComparable(T item) {
        try {
            item.getClass().getDeclaredMethod("compareTo", item.getClass());
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    /**
     * Removes and returns the last element from this list.
     *
     * @return the last element from this list
     * @throws IllegalStateException if this list is empty
     */
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

    /**
     * Removes and returns the first element from this list.
     *
     * @return the first element from this list
     * @throws IllegalStateException if this list is empty
     */
    public T poll(){
        if (isEmpty()){
            throw new IllegalStateException("Cannot poll an empty list");
        }
        Node<T> toDelete = getNode(0);

        this.head = this.head.getNext();
        this.head.setPrev(null);
        size--;
        return toDelete.getItem();
    }

    /**
     * Returns the element at the specified position in this list.
     *
     * @param index the index of the element to return
     * @return the element at the specified position in this list
     */
    public T get(int index){
        return this.getNode(index).item;
    }

    /**
     * Removes the element at the specified position in this list.
     *
     * @param index the index of the element to be removed
     * @return the element previously at the specified position
     */
    public T remove(int index){
        boundCheck(index);

        if(index == 0){
            return poll();
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

    /**
     * Adds all elements from the array to this list.
     *
     * @param array the array containing elements to be added
     */
    public void addFromArray(T [] array){
        for(T element : array){
            add(element);
        }
    }

    /**
     * Check if the index given is in bounds
     *
     * @param index {@code true} if it is between 0 and the size - 1, {@code false} otherwise.
     */
    private void boundCheck (int index){
        if (index < 0){
            throw new IllegalArgumentException("Index less than 0");
        }
        if (index >= this.size){
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
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
            private Node<T> currentNode = head;

            @Override
            public boolean hasNext() {
                return currentNode != null;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                T item = currentNode.getItem();
                currentNode = currentNode.getNext();
                return item;
            }
        };
    }


    /**
     * Private static class that represents the Node. We chose to create this class
     * as a private class inside the sortedLinkedList for better data protection and
     * code organization
     *
     * @param <T>
     */
    private static class Node <T> {
        private Node<T> next = null;
        private Node<T> prev = null;
        private T item;

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
