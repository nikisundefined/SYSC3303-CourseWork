/**
 * Consumer is the class for the consumer thread.
 */
class Consumer implements Runnable
{
    private BoundedBuffer buffer;

    public Consumer(BoundedBuffer buf)
    {
        buffer = buf;
    }

    public void run()
    {
        for(int i = 0; i < 10; i++)
        {
            Object item = buffer.removeFirst();

            System.out.println(Thread.currentThread().getName() + " consumed " + item);

            try {Thread.sleep(1000);}
            catch (InterruptedException e) {}
        }
    }
}