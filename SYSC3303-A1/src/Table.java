public class Table
{
    // a simple 1 integer buffer is used to hold the combination of ingredients on the table
    private int buffer;
    // If true, there is room for at least one object
    // in the buffer.
    private boolean writeable = true;

    // If true, there is at least one object stored
    // in the buffer.
    private boolean readable = false;

    public synchronized void addLast(int item)
    {
        while (!writeable)
        {
            try {wait();}
            catch (InterruptedException e) {System.err.println(e);}
        }
        buffer = item;
        readable = true;
        writeable = false;
        notifyAll();
    }

    public synchronized int removeFirst()
    {
        int item;
        while (!readable)//if the buffer isn't full
        {
            try {wait();}
            catch (InterruptedException e) {System.err.println(e);}
        }
        item = buffer;//save the value of the buffer
        buffer = 0;//remove the item stored in the buffer
        writeable = true;//flag that the buffer has room
        readable = false;//flag that the buffer cannot be read
        notifyAll();//notify all the observers(chefs)
        return item;//return the value of the item taken from the buffer
    }

    public synchronized int CheckTable(){//method to see what's on the table to decide what chef will take it
        while (!readable)//while there's an item in the buffer check what it is
        {
            try {wait();}
            catch (InterruptedException e) {System.err.println(e);}
        }
        notifyAll();
        return buffer;
    }
}