/**
 * TABLE is the class for the table thread managing sandwich ingredients.
 */
public class Table
{
    // index of ingredient combination on the table
    private int ingredients;
    // table status
    private boolean empty = true;

    //  method to put ingredients on the table
    public synchronized void add(int supply)
    {
        while (!empty) // wait while table is full
        {
            try {wait();}
            catch (InterruptedException e) {System.err.println(e);}
        }
        ingredients = supply; // put ingredients on the table
        empty = false; // table is full
        notifyAll(); // notify all threads
    }

    //  method to remove ingredients from the table
    public synchronized int take()
    {
        while (empty) // wait while table is empty
        {
            try {wait();}
            catch (InterruptedException e) {System.err.println(e);}
        }
        empty = true; // remove ingredients from the table
        notifyAll(); // notify all threads
        return ingredients; // return id for the ingredient combo removed from the table
    }

    //  method to show what ingredients are on the table
    public synchronized int check()
    {
        while (empty) // wait while table is empty
        {
            try {wait();}
            catch (InterruptedException e) {System.err.println(e);}
        }
        notifyAll(); // notify all threads
        return ingredients; // return id for the ingredient combo currently on the table
    }
}
