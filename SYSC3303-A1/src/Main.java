/**
 * MAIN is the class that configures and starts all threads.
 */
public class Main
{
    public static void main(String[] args)
    {
        Thread agent, chef1, chef2, chef3;
        
        Table table_ingredients;
        table_ingredients = new Table();

        // configure agent and chef threads
        agent = new Thread(new Agent(table_ingredients),"Agent");
        chef1 = new Thread(new Chef(table_ingredients, 1),"Chef with jam");
        chef2 = new Thread(new Chef(table_ingredients, 2),"Chef with peanut butter");
        chef3 = new Thread(new Chef(table_ingredients, 4),"Chef with bread");

        // start all threads
        agent.start();
        chef1.start();
        chef2.start();
        chef3.start();
    }
}
