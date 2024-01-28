/**
 * AGENT is the class for the agent thread.
 * Written by Nicholas Nemec - 101211060
 */
import java.util.concurrent.ThreadLocalRandom;

class Agent implements Runnable
{
    // array containing unique ingredient combos
    String[] item_combo = {"peanut butter and bread","bread and jam","peanut butter and jam"};
    // declare variable for table items
    private Table table_items;
    // number of sandwiches
    private int sandwich_no;

    public Agent (Table a_table_items, int a_sandwich_no)
    {
        table_items = a_table_items;
        sandwich_no = a_sandwich_no;
    }

    public void run()
    {
        for (int i = 0; i < sandwich_no; i++)
        {
            // generate random index of ingredient combo
            //int index = ThreadLocalRandom.current().nextInt(item_combo.length);
            int index = ThreadLocalRandom.current().nextInt(3);
            // put ingredient combo on the table
            table_items.put(item_combo[index]);

            // simulate delay
            try {Thread.sleep(500);}
            catch (InterruptedException e) {}

            // describe activity
            System.out.println(i+1 + ". " + Thread.currentThread().getName() + " supplied " + item_combo[index]);
        }
        table_items.put("quit"); // signal table closed
    }
}
