/**
 * MAIN is the class that configures and starts all threads.
 * Written by Nicholas Nemec - 101211060
 */
public class Main
{
    public static void main(String[] args)
    {
        Thread agent, chef1, chef2, chef3;

        Table table_items;
        table_items = new Table();

        // configure agent and chef threads
        agent = new Thread(new Agent(table_items, 20),"Agent");
        chef1 = new Thread(new Chef(table_items, "jam"),"Chef with jam");
        chef2 = new Thread(new Chef(table_items, "peanut butter"),"Chef with peanut butter");
        chef3 = new Thread(new Chef(table_items, "bread"),"Chef with bread");

        // start all threads
        agent.start();
        chef1.start();
        chef2.start();
        chef3.start();
    }
}
