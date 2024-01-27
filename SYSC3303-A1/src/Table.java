/**
 * TABLE is the class for the table thread managing sandwich ingredients.
 */
public class Table
{
    // index of ingredient combination on the table
    private String table_items;
    // table status
    private boolean empty = true;

    //  method to put ingredients on the table
    public synchronized void put(String supply)
    {
        while (!empty) // wait while table is full
        {
            try {wait();}
            catch (InterruptedException e) {System.err.println(e);}
        }
        table_items = supply; // put ingredients on the table
        empty = false; // table is full
        notifyAll(); // notify all threads
    }

    //  method to remove ingredients from the table
    public synchronized String get()
    {
        String items; // temporary storage for table items

        while (empty) // wait while table is empty
        {
            try {wait();}
            catch (InterruptedException e) {System.err.println(e);}
        }
        items = table_items;
        table_items = "";
        empty = true; // remove ingredients from the table
        notifyAll(); // notify all threads
        return items; // return ingredient combo removed from the table
    }

    //  method to show what ingredients are on the table
    public synchronized String check()
    {
        while (empty) // wait while table is empty
        {
            try {wait();}
            catch (InterruptedException e) {System.err.println(e);}
        }
        notifyAll(); // notify all threads
        return table_items; // return id for the ingredient combo currently on the table
    }
}
