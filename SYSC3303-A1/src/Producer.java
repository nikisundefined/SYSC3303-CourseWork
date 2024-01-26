/**
 * Producer is the class for the producer thread.
 */
class Producer implements Runnable
{
    private BoundedBuffer buffer;

    public Producer(BoundedBuffer buf)
    {
        buffer = buf;
    }

    public void run()
    {
        for(int i = 0; i < 10; i++)
        {
            Integer item = new Integer(i);
            buffer.addLast(item);

            System.out.println(Thread.currentThread().getName() + " produced " + item);

            try {Thread.sleep(500);}
            catch (InterruptedException e) {}
        }
    }
}