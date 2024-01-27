/**
 * Producer is the class for the producer thread. AGENT
 */
import java.util.concurrent.ThreadLocalRandom;

class Agent implements Runnable
{
    String[] ingname = {"Peanut Butter and Bread","Bread and Jam","Peanut Butter and Jam"};

    private Table buffer;

    public Agent(Table buf)
    {
        buffer = buf;
    }

    public void run()
    {
        for(int i = 0; i < 20; i++)
        {
            int item = ThreadLocalRandom.current().nextInt(3);
            buffer.addLast(item);

            System.out.println(i+ " " +Thread.currentThread().getName() + " produced " + ingname[item]);

            try {Thread.sleep(500);}
            catch (InterruptedException e) {}
        }
        buffer.addLast(-1);
    }
}