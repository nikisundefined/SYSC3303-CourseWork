public class ProducerConsumer
{
    public static void main(String[] args)
    {
        Thread producer, consumer;

        BoundedBuffer buffer;
        buffer = new BoundedBuffer();

        // Create the producer and consumer threads,
        // passing each thread a reference to the
        // shared BoundedBuffer object.
        producer = new Thread(new Producer(buffer),"Producer");
        consumer = new Thread(new Consumer(buffer),"Consumer");

        producer.start();
        consumer.start();
    }
}