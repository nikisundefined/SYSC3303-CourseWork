/**
 * CHEF is the class for the chef thread.
 */
class Chef implements Runnable
{
    private Table ingredients;
    private int chef_id; // chef's ingredient id
    // codes for ingredient combinations
    int[] combo_id = {6,5,3};
    // descriptions of ingredient combinations
    String[] combo_name = {"peanut butter and bread","bread and jam","peanut butter and jam"};
<<<<<<< HEAD
=======
    
>>>>>>> 6809644fdc5260bea7c662525b77445c77f0a251
    // pass the table and the ingredient that the chef has infinite of as parameters when initializing the objects
    public Chef(Table table_ingredients, int chef_ingredient)
    {
        ingredients = table_ingredients;
        chef_id = chef_ingredient;
    }

    public void run()
    {
        int combo_index = 0;

        while (true)
        {
<<<<<<< HEAD
            if (ingredients.check() == -1) break;
            // If the ingredient the chef has infinite of matches the combination on the table, make the sandwich and eat it
=======
            // terminate thread when table closed
            if (ingredients.check() == -1) break;
            // if chef's ingredient complements ingredient combo on the table, retrieve them, make sandwich, and eat it
>>>>>>> 6809644fdc5260bea7c662525b77445c77f0a251
            if (chef_id + combo_id[ingredients.check()] == 7)
            {
                combo_index = ingredients.take();
                // announce ingredients taken from the table
                System.out.println(Thread.currentThread().getName() + " retrieved " + combo_name[combo_index]);
            }
            try {Thread.sleep(1500);}
            catch (InterruptedException e) {}
        }
    }
}