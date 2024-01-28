/**
 * CHEF is the class for the chef thread.
 * Written by Nicholas Nemec - 101211060
 */
class Chef implements Runnable
{
    private Table table_items; // table items
    private String chef_item; // chef's item

    public Chef(Table a_table_items, String a_chef_item)
    {
        table_items = a_table_items;
        chef_item = a_chef_item;
    }

    public void run()
    {
        String items;

        while (true) // infinite loop to make sandwich until agent quits
        {
            // check table items
            items = table_items.check();

            // terminate thread when agent quits
            if (items == "quit") break;

            // retrieve table items only if chef's item not included
            if (!items.contains(chef_item))
            {
                // retrieve items from table, make sandwich, and eat it
                items = table_items.get();

                // simulate delay to make the sandwich
                try {Thread.sleep(1000);}
                catch (InterruptedException e) {}

                // describe activity
                System.out.println(Thread.currentThread().getName() + " used " + items);
            }
        }
    }
}
