package com.richdyang.fundus.base.datastruct;

import java.util.NoSuchElementException;
import java.util.Queue;

import static java.lang.System.arraycopy;

/**
 * 字符队列
 *
 * @author <a href="mailto:richd.yang@gmail.com">Richard Yang</a>
 * @version $Revision: 1.0 $Date:2010-2-25 下午03:24:25 $
 * @since fundus
 */
public class CharQueue implements Cloneable {

    private final int INITIAL_CAPACITY = 10;
    // Invariant of the CharQueue class:
    // 1. The number of items in the queue is in the instance variable
    // manyItems.
    // 2. For a non-empty queue, the items are stored in a circular array
    // beginning at data[front] and continuing through data[rear].
    // 3. For an empty queue, manyItems is zero and data is a reference to an
    // array, but we don't care about front and rear.
    private char[] data;
    private int size;
    private int head;
    private int tail;

    /**
     * Initialize an empty queue with an initial capacity of 10. Note that the
     * <CODE>insert</CODE> method works efficiently (without needing more
     * memory) until this capacity is reached.
     *
     * @param - none <dt><b>Postcondition:</b>
     *          <dd>
     *          This queue is empty and has an initial capacity of 10.
     * @throws OutOfMemoryError Indicates insufficient memory for: <CODE>new char[10]
     *                          </CODE>.
     */
    public CharQueue() {
        size = 0;
        data = new char[INITIAL_CAPACITY];
        // We don't care about front and rear for an empty queue.
    }

    /**
     * Initialize an empty queue with a specified initial capacity. Note that
     * the <CODE>insert</CODE> method works efficiently (without needing more
     * memory) until this capacity is reached.
     *
     * @param <CODE>initialCapacity</CODE> the initial capacity of this queue
     *                                     <dt><b>Precondition:</b>
     *                                     <dd>
     *                                     <CODE>initialCapacity</CODE> is non-negative.
     *                                     <dt><b>Postcondition:</b>
     *                                     <dd>
     *                                     This queue is empty and has the given initial capacity.
     * @throws IllegalArgumentException Indicates that initialCapacity is negative.
     * @throws OutOfMemoryError         Indicates insufficient memory for: <CODE>new
     *                                  char[initialCapacity]</CODE>.
     */
    public CharQueue(int initialCapacity) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException("initialCapacity is negative: " + initialCapacity);
        size = 0;
        data = new char[initialCapacity];
        // We don't care about front and rear for an empty queue.
    }

    /**
     * Generate a copy of this queue.
     *
     * @param - none
     * @return The return value is a copy of this queue. Subsequent changes to
     * the copy will not affect the original, nor vice versa. Note that
     * the return value must be type cast to a <CODE>CharQueue</CODE>
     * before it can be used.
     * @throws OutOfMemoryError Indicates insufficient memory for creating the clone.
     */
    public Object clone() { // Clone a CharQueue.
        CharQueue answer;

        try {
            answer = (CharQueue) super.clone();
        } catch (CloneNotSupportedException e) {
            // This exception should not occur. But if it does, it would
            // probably indicate a
            // programming error that made super.clone unavailable. The most
            // comon error
            // The most common error would be forgetting the
            // "Implements Cloneable"
            // clause at the start of this class.
            throw new RuntimeException(
                    "This class does not implement Cloneable");
        }

        answer.data = (char[]) data.clone();

        return answer;
    }

    /**
     * Change the current capacity of this queue.
     *
     * @param <CODE>minimumCapacity</CODE> the new capacity for this queue <dt>
     *                                     <b>Postcondition:</b>
     *                                     <dd>
     *                                     This queue's capacity has been changed to at least
     *                                     <CODE>minimumCapacity</CODE>. If the capacity was already at or
     *                                     greater than <CODE>minimumCapacity</CODE>, then the capacity is
     *                                     left unchanged.
     * @throws OutOfMemoryError Indicates insufficient memory for: <CODE>new
     *                          char[minimumCapacity]</CODE>.
     */
    public void expandCapacity(int minimumCapacity) {
        char biggerArray[];
        int n1, n2;

        if (data.length >= minimumCapacity)
            // No change needed.
            return;
        else if (size == 0)
            // Just increase the size of the array because the queue is empty.
            data = new char[minimumCapacity];
        else if (head <= tail) { // Create larger array and copy
            // data[front]...data[rear] into it.
            biggerArray = new char[minimumCapacity];
            arraycopy(data, head, biggerArray, head, size);
            data = biggerArray;
        } else { // Create a bigger array, but be careful about copying items
            // into it. The queue items
            // occur in two segments. The first segment goes from data[front] to
            // the end of the
            // array, and the second segment goes from data[0] to data[rear].
            // The variables n1
            // and n2 will be set to the number of items in these two segments.
            // We will copy
            // these segments to biggerArray[0...manyItems-1].
            biggerArray = new char[minimumCapacity];
            n1 = data.length - head;
            n2 = tail + 1;
            arraycopy(data, head, biggerArray, 0, n1);
            arraycopy(data, 0, biggerArray, n1, n2);
            head = 0;
            tail = size - 1;
            data = biggerArray;
        }
    }

    /**
     * Accessor method to get the current capacity of this queue. The
     * <CODE>insert</CODE> method works efficiently (without needing more
     * memory) until this capacity is reached.
     *
     * @param - none
     * @return the current capacity of this queue
     */
    public int getCapacity() {
        return data.length;
    }

    /**
     * Get the front item, removing it from this queue.
     *
     * @param - none <dt><b>Precondition:</b>
     *          <dd>
     *          This queue is not empty.
     *          <dt><b>Postcondition:</b>
     *          <dd>
     *          The return value is the front item of this queue, and the item has
     *          been removed.
     * @throws NoSuchElementException Indicates that this queue is empty.
     * @see Queue#remove()
     */
    public char remove() {
        char answer;

        if (size == 0)
            throw new NoSuchElementException("Queue underflow");
        answer = data[head];
        head = nextIndex(head);
        size--;
        return answer;
    }

    /**
     * Because char cannot be return as null, so in my implementation, this method is same as {@link #remove()}
     *
     * @see Queue#poll()
     */
    public char poll() {
        return remove();
    }

    /**
     * @see Queue#peek()
     */
    public char peek() {
        char answer;

        if (size == 0)
            throw new NoSuchElementException("Queue underflow");
        answer = data[head];
        return answer;
    }

    /**
     * @see Queue#element()
     */
    public char element() {
        char answer;

        if (size == 0)
            throw new NoSuchElementException("Queue underflow");
        answer = data[head];
        return answer;
    }

    /**
     * Insert a new item in this queue. If the addition would take this queue
     * beyond its current capacity, then the capacity is increased before adding
     * the new item. The new item may be the null reference.
     *
     * @param <CODE>item</CODE> the item to be pushed onto this queue <dt>
     *                          <b>Postcondition:</b>
     *                          <dd>
     *                          The item has been pushed onto this queue.
     * @throws OutOfMemoryError Indicates insufficient memory for increasing the queue's
     *                          capacity. <dt><b>Note:</b><dd> An attempt to increase the
     *                          capacity beyond <CODE>Integer.MAX_VALUE</CODE> will cause
     *                          the queue to fail with an arithmetic overflow.
     */
    public void offer(char item) {
        offer(item, false);
    }

    public void offer(char item, boolean fixedCapacity) {
        if (size == data.length) {
            // Double the capacity and add 1; this works even if manyItems is 0.
            // However, in
            // case that manyItems*2 + 1 is beyond Integer.MAX_VALUE, there will
            // be an
            // arithmetic overflow and the bag will fail.
            if (!fixedCapacity) {
                expandCapacity(size * 2 + 1);
            } else {
                remove();
            }
        }

        if (size == 0) {
            head = 0;
            tail = 0;
        } else
            tail = nextIndex(tail);

        data[tail] = item;
        size++;
    }

    /**
     * Determine whether this queue is empty.
     *
     * @param - none
     * @return <CODE>true</CODE> if this queue is empty; <CODE>false</CODE>
     * otherwise.
     */
    public boolean empty() {
        return (size == 0);
    }

    private int nextIndex(int i)
    // Precondition: 0 <= i and i < data.length
    // Postcondition: If i+1 is data.length,
    // then the return value is zero; otherwise
    // the return value is i+1.
    {
        if (++i == data.length)
            return 0;
        else
            return i;
    }

    /**
     * Accessor method to determine the number of items in this queue.
     *
     * @param - none
     * @return the number of items in this queue
     */
    public int size() {
        return size;
    }

    /**
     * Reduce the current capacity of this queue to its actual size (i.e., the
     * number of items it contains).
     *
     * @param - none <dt><b>Postcondition:</b>
     *          <dd>
     *          This queue's capacity has been changed to its current size.
     * @throws OutOfMemoryError Indicates insufficient memory for altering the capacity.
     */
    public void trimToSize() {
        char trimmedArray[];
        int n1, n2;

        if (data.length == size)
            // No change needed.
            return;
        else if (size == 0)
            // Just change the size of the array to 0 because the queue is
            // empty.
            data = new char[0];
        else if (head <= tail) { // Create trimmed array and copy
            // data[front]...data[rear] into it.
            trimmedArray = new char[size];
            arraycopy(data, head, trimmedArray, head, size);
            data = trimmedArray;
        } else { // Create a trimmed array, but be careful about copying items
            // into it. The queue items
            // occur in two segments. The first segment goes from data[front] to
            // the end of the
            // array, and the second segment goes from data[0] to data[rear].
            // The variables n1
            // and n2 will be set to the number of items in these two segments.
            // We will copy
            // these segments to trimmedArray[0...manyItems-1].
            trimmedArray = new char[size];
            n1 = data.length - head;
            n2 = tail + 1;
            arraycopy(data, head, trimmedArray, 0, n1);
            arraycopy(data, 0, trimmedArray, n1, n2);
            head = 0;
            tail = size - 1;
            data = trimmedArray;
        }
    }
}
