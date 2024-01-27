public class BoundedBuffer
{
    // a simple ring buffer is used to hold the data
    // buffer capacity
    ///private static final int SIZE = 1;
    ///private Object[] buffer = new Object[SIZE];
    private int buffer;
    ///private int head = 0, tail = 0, count = 0;

    // If true, there is room for at least one object
    // in the buffer.
    private boolean writeable = true;

    // If true, there is at least one object stored
    // in the buffer.
    private boolean readable = false;

    ///public synchronized void addLast(Object item)
    public synchronized void addLast(int item)
    {
        while (!writeable)
        {
            try {wait();}
            catch (InterruptedException e) {System.err.println(e);}
        }

        ///buffer[tail] = item;
        buffer = item;
        ///tail = 1;
        ///count++;
        readable = true;
        ///if (count == SIZE) writeable = false;
        writeable = false;
        notifyAll();
    }

    ///public synchronized Object removeFirst()
    public synchronized int removeFirst()
    {
        ///Object item;
        int item;

        while (!readable)
        {
            try {wait();}
            catch (InterruptedException e) {System.err.println(e);}
        }

        ///item = buffer[head];
        item = buffer;
        buffer = 0;
        ///head = 1;
        ///count--;
        writeable = true;
        ///if (count == 0) readable = false;
        readable = false;
        notifyAll();
        return item;
    }

    public synchronized int CheckTable(){

        while (!readable)
        {
            try {wait();}
            catch (InterruptedException e) {System.err.println(e);}
        }
        notifyAll();
        return buffer;
    }
}