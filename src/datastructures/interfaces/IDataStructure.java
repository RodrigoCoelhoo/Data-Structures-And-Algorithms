package datastructures.interfaces;

public interface IDataStructure<T> {

    // Add elements
    default void add(T value) {}
    default void add(T value, int index) {}
    default void insert(T value) {}

    // Access elements
    default T get(int index) { return null; }         
    default INode<T> get(T value) { return null; }    
    default INode<T> search(T value) { return null; }
    
    // Update elements
    default void set(int index, T value) {}

    // Remove elements
    default void remove(int index) {}
    default void remove(T value) {}
    default void delete(T value) {}
	
    // Stack / Queue operations
    default void push(T value) {}
    default T pop() { return null; }
    default void enqueue(T value) {}
    default T dequeue() { return null; }
    default T peek() { return null; }
	
    // Utility
    default void clear() {}
    default boolean contains(T value) { return false; }
    default int size() { return 0; }
    default boolean isEmpty() { return true; }
}
