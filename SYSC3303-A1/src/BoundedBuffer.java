public class BoundedBuffer
{
    // a simple ring buffer is used to hold the data
    // buffer capacity
    private static final int SIZE = 5;
    private Object[] buffer = new Object[SIZE];
    private int head = 0, tail = 0, count = 0;

    // If true, there is room for at least one object
    // in the buffer.
    private boolean writeable = true;

    // If true, there is at least one object stored
    // in the buffer.
    private boolean readable = false;

    public synchronized void addLast(Object item)
    {
        while (!writeable)
        {
            try {wait();}
            catch (InterruptedException e) {System.err.println(e);}
        }

        buffer[tail] = item;
        tail = (tail + 1) % SIZE;
        count++;
        readable = true;
        if (count == SIZE) writeable = false;
        notifyAll();
    }

    public synchronized Object removeFirst()
    {
        Object item;

        while (!readable)
        {
            try {wait();}
            catch (InterruptedException e) {System.err.println(e);}
        }

        item = buffer[head];
        head = (head + 1) % SIZE;
        count--;
        writeable = true;
        if (count == 0) readable = false;
        notifyAll();
        return item;
    }
}