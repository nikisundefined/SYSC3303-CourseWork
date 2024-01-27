/**
 * AGENT is the class for the agent thread.
 */
import java.util.concurrent.ThreadLocalRandom;

class Agent implements Runnable
{
    // list of unique ingredient combos
    String[] combo_name = {"peanut butter and bread","bread and jam","peanut butter and jam"};
    private Table ingredients;

    public Agent(Table table_ingredients)
    {
        ingredients = table_ingredients;
    }

    public void run()
    {
        for(int i = 0; i < 20; i++)
        {
            int index = ThreadLocalRandom.current().nextInt(3); // random index of ingredient combo
            ingredients.add(index); // place ingredient combo on the table

            // announce ingredient combo placed on the table
            System.out.println(i+ " " +Thread.currentThread().getName() + " supplied " + combo_name[index]);

            try {Thread.sleep(250);}
            catch (InterruptedException e) {}
        }
        ingredients.add(-1); // signal table closed
    }
}
