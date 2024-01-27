public class ProducerConsumer
{
    public static void main(String[] args)
    {
        Thread agent, chef1, chef2, chef3;

        BoundedBuffer buffer;
        buffer = new BoundedBuffer();

        // Create the producer and consumer threads,
        // passing each thread a reference to the
        // shared BoundedBuffer object.
        agent = new Thread(new Producer(buffer),"Agent");
        chef1 = new Thread(new Consumer(buffer, 1),"Chef with Infinite Jam");
        chef2 = new Thread(new Consumer(buffer, 2),"Chef with Infinite Peanut Butter");
        chef3 = new Thread(new Consumer(buffer, 4),"Chef with Infinite Bread");
        agent.start();
        chef1.start();
        chef2.start();
        chef3.start();
    }
}