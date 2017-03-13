package black_jack;

import java.util.*;

public class deckOfCards<Card> {

    private Card[] data;
    private static final int INITIAL_CAPACITY = 52;
    private int size = 0;
    
    public deckOfCards() {
        data = (Card[])new Object[INITIAL_CAPACITY];
    }

    public deckOfCards(int initialCapacity) {

	}

	// Methods
    public Card push(Card e) {
        data[size] = e;
        size++;
        return e;
    } 
    
    public Card peek() {
        if(isEmpty()) {
            throw new EmptyStackException();
        }
        return data[size - 1];
    } 
    
    public Card pop() {
        Card result = peek();
        data[size - 1] = null;
        size--;
        return result;
    } 

    public boolean isEmpty() {
        return size == 0;
    } 

    public String toString() {
        return Arrays.toString(data);
    } 

    public int size() {
        return size;
    } 
}